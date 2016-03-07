package com.wlspa.weatherlogserver.ejb;

import com.wlspa.weatherlogserver.persistence.HandlerDB;
import com.wlspa.weatherlogserver.utility.OWMResponse;
import com.wlspa.weatherlogserver.utility.ServerInfo;
import java.util.Date;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.w3c.dom.Document;
/**
 *
 * @author GionatanG
 * @author ChiaraC
 */

@Startup
@Singleton
public class SchedulerBean 
{
    
    private HandlerDB dbHandler = null;
    
    @PostConstruct
    public void init()
    {
        dbHandler=new HandlerDB();
    }

    
    @Schedule(minute="*/1", hour="*", persistent=false)
    public void hourlySchedule() 
    {
        ServerInfo info = new ServerInfo();
        info.printDoc();
        
        ArrayList<String> cityList = info.getCities();
        ArrayList<String> measurementNames = info.getMeasurementsProperty("name");
        ArrayList<String> measurementUnits = info.getMeasurementsProperty("unit");
        
        System.out.println("cities names:    "+ cityList.toString());
        System.out.println("measurement names:    "+ measurementNames.toString());
        System.out.println("measurement units:    "+ measurementUnits.toString());
                
        System.out.println("Abbiamo "+cityList.size()+" città" );
        OWMBean owmBean = new OWMBean();
        for(int i = 0; i < cityList.size(); i++)
        {
            String cityName = cityList.get(i);
            Document data = owmBean.getData(cityName);
            OWMResponse response = new OWMResponse(data);
            int cityID = response.getCityID();
            System.out.println(cityID);
            if(!dbHandler.findCity(cityID))
            {
                addCityToDB(response);
            }
            for(int j = 0; j < measurementNames.size(); j++)
            {
                addMeasurementToDB(measurementNames.get(j),
                        measurementUnits.get(j), response);
            }
            
        }
    }
    
    
    @Schedule(hour = "15", minute = "36", second= "0", persistent = false)
    public void dailySchedule() 
    {
        /*System.out.println("Ora sono nella dailySchedule()");
        Document config = getConfigFile();
        NodeList cities = config.getElementsByTagName("city");
        NodeList measurements = config.getElementsByTagName("daily");
        ArrayList<String> cityList = xmlManager.getCityList(cities);
        for(int i = 0; i < cityList.size(); i++)
        {
            Document data = getData(cityList.get(i));
            xmlManager.updateDaily(data,measurements);
        }*/
    }
    

    private void addCityToDB(OWMResponse response) {
        int id = response.getCityID();
        String name = response.getCityName();
        String country = response.getCountry();
        double lat = response.getLatitude();
        double lon = response.getLongitude();
        
        dbHandler.addCity(id, name, country, lon, lat);

    }

    private void addMeasurementToDB(String name, 
                                  String unit, OWMResponse response) {
        int idCity = response.getCityID();
        Date updateTime = response.getUpdateTime();
        String value = response.getMeasurement(name);
        
        dbHandler.addMeasurement(idCity, updateTime, name, value, unit);
    }
}
