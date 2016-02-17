/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.utility;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author GionatanG
 * @author ChiaraC
 */
public class HandlerXML {
    public void updateHourly(Document data, NodeList measurementList) 
    {
        /**
         * Take values from data for each measurement into measurementList
         * ask a class that handle the DB to insert measurements
         */
        System.out.println("provaaaaa");
        System.out.println("sono nella updateHourly()");
    }

    public void updateDaily(Document data, NodeList measurementList) 
    {
        /**
         * Take values from data for each measurement into measurementList
         * ask a class that handle the DB to insert measurements
         */
        System.out.println("sono nella updateDaily()");
    }

    public ArrayList<String> getCityList(NodeList cities) 
    {
        ArrayList<String> result= new ArrayList<String>();
        System.out.println("ho creato result");
        for(int i=0;i<cities.getLength();i++)
        {
            result.add(cities.item(i).getTextContent());
            System.out.println("added "+cities.item(i).getTextContent()+" in result");
        }
        return result;
    }

    public void fromXMLToData(ArrayList<String> attr, Document responseFromOWM)
    {
        //cities is a list of cities
        
        NodeList cities = responseFromOWM.getElementsByTagName("item");
        for(int i = 0; i < cities.getLength(); i++)
        {
            
            //tutti i figli di item
            Element city = (Element) cities.item(i);
            String updateTime = city.getElementsByTagName("lastupdate")
                                    .item(0).getAttributes()
                                    .item(1).getNodeValue();
            String cityID = city.getElementsByTagName("city")
                                    .item(0).getAttributes()
                                    .item(1).getNodeValue();
            //System.out.println(updateTime);
            //System.out.println(cityID);
            for (int j = 0;j < attr.size(); j++)
            {
                String attribute = attr.get(j);
                Node attributeNode= city.getElementsByTagName(attribute).item(0);
                {
                    NamedNodeMap temp2=attributeNode.getAttributes();
                    for(int k=0; k<temp2.getLength(); k++)
                    {
                        
                    }
                }
            }
        }
    }
    
    public void fromDataToXML()
    {
        
        
        
    }
}
