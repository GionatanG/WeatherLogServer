/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.ejb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 *
 * @author GionatanG
 * @author ChiaraC
 */
public class OWMBean {
    private final String baseURL = "http://api.openweathermap.org/data/2.5/";
    private final String appIDsFile = "appIDs.config";
    private LinkedBlockingQueue<String> appIDQueue;
    
    public OWMBean()
    {
        appIDQueue = initializeAppIDQueue();
    }

    Document getData(String cityName) 
    {
        try
        {
            String myAppID = appIDQueue.take();
            appIDQueue.put(myAppID);
            String subURL = "weather?q=" + cityName 
                          + "&units=metric&mode=xml"
                          + "&appid=" + myAppID;
            
            URL url = new URL(this.baseURL + subURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document d=builder.parse( connection.getInputStream());
            return d;
        } 
        
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (MalformedURLException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex) 
        {
            Logger.getLogger(SchedulerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private LinkedBlockingQueue<String> initializeAppIDQueue() 
    {
        LinkedBlockingQueue<String> result=new LinkedBlockingQueue<String>();
        //the appIDs are stored in a configuration file
         try
        {
            //Open AppIDs.config
            BufferedReader reader = new BufferedReader(new FileReader(appIDsFile));
            String singleAppID;
            while ( (singleAppID = reader.readLine()) != null)
            {
                //I add a singleAppID to the queue result
                result.put(singleAppID);
            }
        }
         
        catch (IOException e) 
        {
            System.out.println("Error in read/write operation");
        }
        catch (InterruptedException e)
        {
            System.out.println("Error in create the list of app ID");
        }
        System.out.println(result.size());
        return result;
    }
}
