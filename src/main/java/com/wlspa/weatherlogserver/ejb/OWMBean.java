package com.wlspa.weatherlogserver.ejb;

import com.wlspa.weatherlogserver.utility.HandlerXML;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 *
 * @author GionatanG
 * @author ChiaraC
 */

@Startup
@Singleton
public class OWMBean 
{
    private final String baseURL = "http://api.openweathermap.org/data/2.5/";
    private final String configFile = "data.xml";
    private final String appIDsFile = "appIDs.config";
    private LinkedBlockingQueue<String> appIDQueue;
    private HandlerXML xmlManager = null;
    //private HandlerDB db = null;
    
    @PostConstruct
    public void init()
    {
        xmlManager = new HandlerXML();
        appIDQueue=initializeAppIDQueue();
        //db=new HandlerDB();
        //Manage hourly information
        //hourlySchedule();
        //Manage daily information
        //dailySchedule();
        //db.run();
    }

    
    @Schedule(minute="*/1", hour="*", persistent=false)
    public void hourlySchedule() 
    {
        Document config = getConfigFile();
        System.out.println("Sono tornata in hourly schedule");
        NodeList cities = config.getElementsByTagName("city");
        System.out.println("ho creato cities");
        NodeList measurements = config.getElementsByTagName("hourly");
        System.out.println("ho creato measuements");
        ArrayList<String> cityList = xmlManager.getCityList(cities);
        
        System.out.println("Abbiamo "+cityList.size()+" città" );
        for(int i = 0; i < cityList.size(); i++)
        {
            Document data = getData(cityList.get(i));            
            xmlManager.updateHourly(data,measurements);
        } 
    }
    
    
    @Schedule(hour = "15", minute = "36", second= "0", persistent = false)
    public void dailySchedule() 
    {
        System.out.println("Ora sono nella dailySchedule()");
        Document config = getConfigFile();
        NodeList cities = config.getElementsByTagName("city");
        NodeList measurements = config.getElementsByTagName("daily");
        ArrayList<String> cityList = xmlManager.getCityList(cities);
        for(int i = 0; i < cityList.size(); i++)
        {
            Document data = getData(cityList.get(i));
            xmlManager.updateDaily(data,measurements);
        }
    }
    
    
     public LinkedBlockingQueue<String> initializeAppIDQueue()
    {
        LinkedBlockingQueue<String> result=new LinkedBlockingQueue<String>();
        //the appIDs are stored in a configuration file
         try
        {
            //Open AppIDs.config
            BufferedReader reader = new BufferedReader(new FileReader(appIDsFile));
            String singleAppID;
            while ( (singleAppID= reader.readLine()) != null)
            {
                //I add a singleAppID to the queue result
                result.put(singleAppID);
            }
        }
         
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Error in create the list of app ID");
        }
        System.out.println(result.size());
        return result;
    }
    
     
    public Document getData(String cityName) 
    {
        System.out.println("sono nella getData()");
        try
        {
            String myAppID=appIDQueue.take();
            appIDQueue.put(myAppID);
            System.out.println("Questa è l'appID corrente: "+ myAppID);
            String subURL = "weather?q=" + cityName 
                          + "&units=metric&mode=xml"
                          + "&appid=" + myAppID;
            
            URL url = new URL(this.baseURL + subURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("creata connection");
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            System.out.println("Creato builder");
            Document d=builder.parse( connection.getInputStream());
            System.out.println("sono nella updateHourly()");
            return d;
        } 
        
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (MalformedURLException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    private Document getConfigFile() 
    {
        System.out.println("Sono entrato nella getConfigFile");
        File file = new File(this.configFile);
        Document dataXML = null;
        try 
        {
            //create the file if it not exist
            if (!file.exists())
                    if (file.createNewFile())
                        System.out.println("Il file " + this.configFile + " è stato creato");
                    else
                        System.out.println("Il file " + this.configFile + " non può essere creato");
        
            
            DocumentBuilder dataXMLBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            System.out.println("Mi preparo a fare la parse");
            System.out.println(file.toString());
            dataXML = dataXMLBuilder.parse(file);
            System.out.println("Ho fatto la parse");
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(OWMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Sto uscendo dalla getConfigFile");
        return dataXML;
    }
}
