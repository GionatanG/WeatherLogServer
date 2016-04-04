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
    public City getActualWeather(
        @QueryParam("name") String names) 
    {
        /**/
        System.out.println("dcdhgcc");
        String[] cities = names.split(",");
        for(int i = 0; i < cities.length; i++)
        {
            System.out.println(cities[i]);
        }
      
        HandlerDB managerDB = new HandlerDB();
        String s = cities[0];
        City city = managerDB.findCityByName(s);
        
        System.out.println("id :" + city.getId());
        System.out.println("name :" + city.getName());
        System.out.println("country :" + city.getCountry());
        City c=new City();
        c.setCountry(city.getCountry());
        c.setId(city.getId());
        c.setLatitude(city.getLatitude());
        c.setLongitude(city.getLongitude());
        c.setName(city.getName());
        return city;
        /*
        City c = new City();
        c.setCountry("IT");
        c.setId(123);
        c.setLatitude(30.3);
        c.setLongitude(23.3);
        c.setName("Lucca");
        
        return c;

        City c2 = new City();
        c2.setCountry("UK");
        c2.setId(133);
        c2.setLatitude(30.33);
        c2.setLongitude(23.33);
        c2.setName("Ucca");

        Measurement m = new Measurement(123,new Date(),"temperature");
        m.setValue("23.5");
        m.setUnit("°C");

        Measurement m2 = new Measurement(123,new Date(),"pressure");
        m2.setValue("1080");
        m2.setUnit("Pa");

        //single actual city
        //return "<city id=\"3170647\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\"> 1</measurement><measurement name=\"humidity\" unit=\"%\">1</measurement><measurement name=\"pressure\" unit=\"hPa\">1</measurement><measurement name=\"speed\" unit=\"mps\">1</measurement><measurement name=\"direction\" unit=\"mps\">1</measurement></city>";
        List<Measurement> measurements = new ArrayList<Measurement>();
        measurements.add(m);
        measurements.add(m2);
        System.out.println("Il ritorno e' " + measurements.toString());

        CityLog ret = new CityLog();
        ret.setCity(c);
        ret.setMeasurements(measurements);

        List<CityLog> retList = new ArrayList<CityLog>();
        retList.add(ret);
        retList.add(ret);
        return retList;
*/
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