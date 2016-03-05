/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.utility;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GionatanG
 * @author ChiaraC
 */
public class OWMResponse {
    private final Document doc;

    public OWMResponse(Document data) 
    {
        doc = data;
    }
    
    public int getCityID()
    {
        Node city = doc.getElementsByTagName("city").item(0);
        if(city == null) 
            return -1;
        NamedNodeMap attributes = city.getAttributes();
        String id = attributes.getNamedItem("id").getNodeValue();
        return Integer.parseInt(id);
    }
    
    public String getCityName()
    {
        Node city = doc.getElementsByTagName("city").item(0);
        if(city == null) 
            return null;
        NamedNodeMap attributes = city.getAttributes();
        String name = attributes.getNamedItem("name").getNodeValue();
        return name;
    }
    
    public String getCountry()
    {
        Node countryNode = doc.getElementsByTagName("country").item(0);
        if(countryNode == null) 
            return null;
        String country = countryNode.getTextContent();
        return country;
    }
    
    public double getLatitude()
    {
        Node coord = doc.getElementsByTagName("coord").item(0);
        if(coord == null) 
            return Double.NaN;
        NamedNodeMap attributes = coord.getAttributes();
        String latitude = attributes.getNamedItem("lat").getNodeValue();
        return Double.valueOf(latitude);
    }
    
    public double getLongitude()
    {
        Node coord = doc.getElementsByTagName("coord").item(0);
        if(coord == null) 
            return Double.NaN;
        NamedNodeMap attributes = coord.getAttributes();
        String longitude = attributes.getNamedItem("lon").getNodeValue();
        return Double.valueOf(longitude);
    }
    
    public String getSunrise()
    {
        Node sun = doc.getElementsByTagName("sun").item(0);
        if(sun == null)
            return null;
        NamedNodeMap attributes = sun.getAttributes();
        String sunrise = attributes.getNamedItem("rise").getNodeValue();
        return sunrise;
    }
    
    public String getSunset()
    {
        Node sun = doc.getElementsByTagName("sun").item(0);
        if(sun == null)
            return null;
        NamedNodeMap attributes = sun.getAttributes();
        String sunset = attributes.getNamedItem("set").getNodeValue();
        return sunset;
    }
    
    public String getMeasurement(String name)
    {
        Node measurement = doc.getElementsByTagName(name).item(0);
        if(measurement == null)
            return null;
        NamedNodeMap attributes = measurement.getAttributes();
        String value = attributes.getNamedItem("value").getNodeValue();
        return value;
    }
    
    public Date getUpdateTime()
    {
        Node lastupdate = doc.getElementsByTagName("lastupdate").item(0);
        if(lastupdate == null)
            return null;
        NamedNodeMap attributes = lastupdate.getAttributes();
        String valueString = attributes.getNamedItem("value").getNodeValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date value = null;
        try 
        {
            value = sdf.parse(valueString);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(OWMResponse.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return value;
    }
    
}
