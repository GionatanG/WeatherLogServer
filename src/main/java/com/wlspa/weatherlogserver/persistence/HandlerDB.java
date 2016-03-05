/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.persistence;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.Measurement;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author GionatanG
 * @author ChiaraC
 */

public class HandlerDB {
    private static final Logger LOGGER = Logger.getLogger("JPA");

    private EntityManager em;
    
    public HandlerDB()
    {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("weatherLogUnit");
        em = factory.createEntityManager();
    }

    public boolean findCity(int id)
    {
        Query findQuery;
        findQuery = em.createQuery("SELECT c FROM City c "
                                + "WHERE c.id = " + id);
        if(findQuery.getResultList() != null)
        {
           return true;
        }
        return false;
    }
    
    public void addCity(int id, String name, String country, 
                            double longitude, double latitude) 
    {
        EntityTransaction transaction = em.getTransaction();
        try 
        {
            transaction.begin();

            City city = new City();
            city.setId(id);
            city.setName(name); 
            city.setCountry(country);
            city.setLongitude(longitude);
            city.setLatitude(latitude);

            em.merge(city);
            transaction.commit();

        } 
        catch (Exception e) 
        {
            if (transaction.isActive()) 
            {
                transaction.rollback();
            }

        }
    }
    
    public void addMeasurement(int idCity, Date updateTime,String name, 
                                String value, String unit)
    {
        EntityTransaction transaction = em.getTransaction();
        try
        {
            transaction.begin();
            
            Measurement mes = new Measurement(idCity, updateTime, name);
            mes.setValue(value);
            mes.setUnit(unit);
            
            em.merge(mes);
            transaction.commit();
            
        }catch(Exception e)
        {
            if (transaction.isActive()) 
            {
                transaction.rollback();
            }
        }
    }
}
