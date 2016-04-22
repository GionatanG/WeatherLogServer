package com.wlspa.weatherlogserver.servlet;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.CityLog;
import com.wlspa.weatherlogserver.entity.Measurement;
import com.wlspa.weatherlogserver.entity.MeasurementGroup;
import com.wlspa.weatherlogserver.persistence.ManagerDB;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/******************************************************************************
 * 
 * send the weather measurements
 * 
 *****************************************************************************/
@Path("/weather")
public class Weather 
{
 
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/analysis/")
    public List<CityLog> getAnalysisWeather(
        @QueryParam("name") String names,
        @QueryParam("type") String type)
    {
        
        if(names == null || names.length() == 0 || type == null)
        {
            return null;
        }
        
        int count, step;
        
        if(type.equals("h"))
        {
            count = 1;
            step = 1;
        } else 
            if(type.equals("d"))
            {
                count = 24;
                step = 1;               
            }
            else 
                if(type.equals("w"))
                {
                    step = 6;
                    count = 28;    
                }
                else 
                    if(type.equals("m"))
                    {
                        step = 24;
                        count = 30;
                    }
                    else
                    {
                        System.out.println("Type not supported");
                        return null;   
                    }
        String[] cities = names.split(",");
        
        return getCityLogs(cities, count, step);   
    } 
    
    private List<MeasurementGroup> getMeasurementGroupsByCityID(
            ManagerDB hdb, int id, int howmany, int step) 
    {
        List<MeasurementGroup> result = new ArrayList<MeasurementGroup>();
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTime(new Date());
        updateTime.set(Calendar.MINUTE, 0);  
        updateTime.set(Calendar.SECOND, 0);  
        updateTime.set(Calendar.MILLISECOND, 0);
        
        for(int i = 0; i < howmany; i++)
        {
            MeasurementGroup group = new MeasurementGroup();
            List<Measurement> measurements = hdb.findPastMeasurementsByCityID(id, i, step);
            group.setMeasurements(measurements);
            group.setUpdateTime(updateTime.getTime());
            updateTime.add(Calendar.HOUR, -step);
            result.add(group);
        }
        return result;
    }

    private ArrayList<CityLog> getCityLogs(String[] cities, int count, int step) 
    {
   
        ArrayList<CityLog> response = new ArrayList<CityLog>();
        ManagerDB managerDB = new ManagerDB();
        for(int i = 0; i < cities.length; i++)
        {
            response.add(getCityLogByCityName(managerDB, cities[i], count, step));
            
        }
        
        return response;
    }

    private CityLog getCityLogByCityName(ManagerDB hdb, String cityName, int count, int step) 
    {
        City city;
        city = hdb.findCityByName(cityName);
        if(city == null)
        {
            return null;
        }
        List<MeasurementGroup> measurements;
        measurements = getMeasurementGroupsByCityID(hdb, city.getId(), count, step);
        
        CityLog log = new CityLog();
        log.setCity(city);
        log.setMeasurements(measurements);
        
        return log;
    }

    
} 