package com.wlspa.weatherlogserver.servlet;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.CityLog;
import com.wlspa.weatherlogserver.entity.Measurement;
import com.wlspa.weatherlogserver.entity.MeasurementGroup;
import com.wlspa.weatherlogserver.persistence.HandlerDB;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;


/******************************************************************************
 * 
 * send the weather measurements
 * 
 *****************************************************************************/
@Path("/weather")
public class Weather 
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("weatherLogUnit");
 
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/actual")
    public List<CityLog> getActualWeather(
        @QueryParam("name") String names) 
    {
        City city = null;
        List<MeasurementGroup> measurements = null;
        ArrayList<CityLog> response = new ArrayList<CityLog>();
        
        String[] cities = names.split(",");
      
        HandlerDB managerDB = new HandlerDB();
        for(int i = 0; i < cities.length; i++)
        {
            CityLog log = new CityLog();
            city = managerDB.findCityByName(cities[i]);
            if(city == null)
            {
                measurements = null;
            }
            else
            {
                //measurements = findMeasurements(city.getId(), 1, 1);
            }
            log.setCity(city);
            log.setMeasurements(measurements);
            response.add(log);
        }
        
        return response;
    } 
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
    
    private String convertDomToString(Node root)
    {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(root), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            System.out.println(output);
            return output;
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void persist(Object object) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    private List<MeasurementGroup> findMeasurements(HandlerDB hdb, int id, int howmany, int step) 
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
        HandlerDB managerDB = new HandlerDB();
        for(int i = 0; i < cities.length; i++)
        {
            response.add(getCityLog(managerDB, cities[i], count, step));
            
        }
        
        return response;
    }

    private CityLog getCityLog(HandlerDB hdb, String cityName, int count, int step) 
    {
        City city = null;
        List<MeasurementGroup> measurements = null;
        CityLog log = new CityLog();
        city = hdb.findCityByName(cityName);
        if(city == null)
        {
            return null;
        }
        measurements = findMeasurements(hdb, city.getId(), count, step);
        log.setCity(city);
        log.setMeasurements(measurements);
        return log;
    }

    
} 