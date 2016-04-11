package com.wlspa.weatherlogserver.ejb;

import com.wlspa.weatherlogserver.persistence.HandlerDB;
import com.wlspa.weatherlogserver.utility.OWMResponse;
import com.wlspa.weatherlogserver.utility.ServerInfo;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
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

    
    
    @Schedule(hour="*", minute="*/1", persistent=false)
    public void hourlySchedule() 
    {
        ServerInfo info = new ServerInfo();
        
        ArrayList<String> cityList = info.getCities();
        ArrayList<String> measurementNames = info.getMeasurementsProperty("name");
        ArrayList<String> measurementUnits = info.getMeasurementsProperty("unit");
        
        OWMBean owmBean = new OWMBean();
        for(int i = 0; i < cityList.size(); i++)
        {
            String cityName = cityList.get(i);
            Document data = owmBean.getData(cityName);
            OWMResponse response = new OWMResponse(data);
            int cityID = response.getCityID();
            if(!dbHandler.findCityById(cityID))
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
    
    
    @Schedule(hour = "2", minute = "59", second= "59", persistent = false)
    public void dailySchedule() 
    {
        ServerInfo info = new ServerInfo();
        
        ArrayList<String> cityList = info.getCities();
        OWMBean owmBean = new OWMBean();
        for(int i = 0; i < cityList.size(); i++)
        {
            String cityName = cityList.get(i);
            Document data = owmBean.getData(cityName);
            OWMResponse response = new OWMResponse(data);
            int cityID = response.getCityID();
            if(!dbHandler.findCityById(cityID))
            {
                addCityToDB(response);
            }
            addSunToDB("rise",response);
            addSunToDB("set",response);
        }
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
        Calendar cal = Calendar.getInstance();  
        cal.setTime(updateTime);   
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        updateTime = cal.getTime();
        
        String value = response.getMeasurement(name);
        
        dbHandler.addMeasurement(idCity, updateTime, name, value, unit);
    }

    private void addSunToDB(String type, OWMResponse response) 
    {
        String name = "sun" + type;
        int idCity = response.getCityID();
        Date updateTime = response.getUpdateTime();
        String value;
        if(type.equals("rise"))
        {
            value = response.getSunrise();
        }
        else
        {
            value = response.getSunset();
        }
        String unit = "";
        
        dbHandler.addMeasurement(idCity, updateTime, name, value, unit);
    }
}
