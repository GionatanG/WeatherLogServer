/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.persistence;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.Measurement;
import java.util.Date;
import java.util.List;
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

    public boolean findCityById(int id)
    {
        Query findQuery = em.createNamedQuery("City.findById");
        findQuery.setParameter("id", id);
        
        List<City> result=findQuery.getResultList() ;
        if(!result.isEmpty())
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
    
    public City findCityByName(String name)
    {
        Query findQuery = em.createQuery("SELECT c FROM City c WHERE c.name = :name");
        findQuery.setParameter("name", name);
        
        List<City> result= findQuery.getResultList() ;
        System.out.println("The list size is " + result.size());
        City firstResult = result.get(0);
        firstResult.setMeasurementCollection(null);
        return firstResult;
    }

    public List<Measurement> findActualMeasurementByCityID(Integer id)
    {
        Query findQuery = em.createQuery("SELECT m FROM Measurement m "
                       + "WHERE m.measurementPK.city = :id AND "
                + "m.measurementPK.updateTime = (SELECT MAX(m2.measurementPK.updateTime) "
                + "FROM Measurement m2 WHERE m2.measurementPK.city = :id AND "
                + "m.measurementPK.name = m2.measurementPK.name)");
        findQuery.setParameter("id", id);
        
        List<Measurement> result= findQuery.getResultList() ;
        System.out.println("The list size is " + result.size());
        for(int i = 0; i < result.size(); i++)
        {
           result.get(0).setCity1(null);
        }
        
        return result;
    }
}
