package com.wlspa.weatherlogserver.servlet;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.persistence.ManagerDB;
import com.wlspa.weatherlogserver.utility.ServerInfo;
import java.io.StringWriter;
import java.util.List;
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

    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/city")
    public List<City> getAllCities()
    {
        ManagerDB manager = new ManagerDB();
        return manager.findAllCities();
    }
    
    
    @GET
    @Produces(MediaType.TEXT_XML)
    @Path("/measurement")
    public String getAllMeasurements()
    {
        ServerInfo serverinfo = new ServerInfo();
        Node measurements =  serverinfo.getNodeMeasurements();
        return convertDOMToString(measurements);
    }
 
    
    private String convertDOMToString(Node root)
    {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(root), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            
            return output;
            
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
} 