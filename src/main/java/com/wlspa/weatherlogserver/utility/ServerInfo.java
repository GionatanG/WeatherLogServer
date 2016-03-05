/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.utility;

import com.wlspa.weatherlogserver.ejb.SchedulerBean;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author gionatanG
 */
public class ServerInfo {  
    private final String PATH = "data.xml";
    
    private Document info;
    
    public ServerInfo()
    {
        getFile(PATH);
    }
    
    private void getFile(String path)
    {
        File file = new File(path);
        try 
        {
            //create the file if it not exist
            if (!file.exists())
                    if (file.createNewFile())
                        System.out.println("Il file " + path + " è stato creato");
                    else
                        System.out.println("Il file " + path + " non può essere creato");
        
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            System.out.println(file.toString());
            info = builder.parse(file);
            System.out.println("il file xml è " + info);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> getCities()
    {
        NodeList cities = info.getElementsByTagName("city");
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < cities.getLength(); i++)
        {
            String value = cities.item(i).getFirstChild().getNodeValue();
            result.add(value);
        }
        return result; 
    }

    public ArrayList<String> getMeasurementsProperty(String name) 
    {
        NodeList properties = info.getElementsByTagName(name);
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i < properties.getLength(); i++)
        {
            String value = properties.item(i).getFirstChild().getNodeValue();
            result.add(value);
        }
        return result;
    }

    public void printDoc()
    {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            transformer.transform(new DOMSource(info),
                    new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
