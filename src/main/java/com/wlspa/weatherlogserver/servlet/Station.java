package com.wlspa.weatherlogserver.servlet;

import com.wlspa.weatherlogserver.utility.ServerInfo;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
 * send to the client the list of parameter:
 *     -list of cities
 *     -list of measurement
 * 
 *****************************************************************************/
@Path("/list")
public class Station 
{
/******************************************************************************
 * 
 * return the whole list of cities
 * 
 *****************************************************************************/
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/city")
    public String getAllCities()
    {
        ServerInfo serverinfo = new ServerInfo();
        Node cities =  serverinfo.getNodeCities();
        String ret =  convertDomToString(cities);
        System.out.println(ret);
        return ret;
    }
    
    
 /******************************************************************************
 * 
 * return the whole list of measurements
 * 
 *****************************************************************************/
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/measurement")
    public String getAllMeasurements()
    {
        ServerInfo serverinfo = new ServerInfo();
        Node measurements =  serverinfo.getNodeMeasurements();
        return convertDomToString(measurements);
    }
    
    
/******************************************************************************
 * 
 * DA CANCELLARE!!!!!!!!!!!!!
 * 
 
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/hello")
    public List<CityLog> sayXMLHello() {
        City c = new City();
        c.setCountry("IT");
        c.setId(123);
        c.setLatitude(30.3);
        c.setLongitude(23.3);
        c.setName("Lucca");

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

    } 
*****************************************************************************/
    
    
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
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
} 