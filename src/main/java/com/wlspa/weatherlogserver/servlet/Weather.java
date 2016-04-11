package com.wlspa.weatherlogserver.servlet;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.CityLog;
import com.wlspa.weatherlogserver.entity.Measurement;
import com.wlspa.weatherlogserver.persistence.HandlerDB;
import java.io.StringWriter;
import java.util.ArrayList;
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
        List<Measurement> measurements = null;
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
                measurements = managerDB.findActualMeasurementByCityID(city.getId());
            }
            log.setCity(city);
            log.setMeasurements(measurements);
            response.add(log);
        }
        
        return response;
    } 
        @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/analysis")
    public List<CityLog> getAnalysisWeather(
        @QueryParam("name") String names) 
    {
        City city = null;
        List<Measurement> measurements = null;
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
                measurements = managerDB.findActualMeasurementByCityID(city.getId());
            }
            log.setCity(city);
            log.setMeasurements(measurements);
            response.add(log);
        }
        
        return response;
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
} 