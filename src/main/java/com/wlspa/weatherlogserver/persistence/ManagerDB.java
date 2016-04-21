/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wlspa.weatherlogserver.persistence;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.Measurement;
import java.util.ArrayList;
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

public class ManagerDB {
    private static final Logger LOGGER = Logger.getLogger("JPA");

    private EntityManager em;
    
    public ManagerDB()
    {
        EntityManagerFactory factory;
        factory = Persistence.createEntityManagerFactory("weatherLogUnit");
        em = factory.createEntityManager();
    }

    public boolean checkCityExists(int id)
    {
        Query findQuery = em.createNamedQuery("City.findById");
        findQuery.setParameter("id", id);
        
        List res = findQuery.getResultList();
        return (!res.isEmpty());
    }
    
    public void addCity(int id, String name, String country, 
                            double longitude, double latitude) 
    {
        EntityTransaction transaction = em.getTransaction();
        try 
        {
            transaction.begin();

            City city = new City(id, name, country, longitude, latitude);

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
            
            Measurement mes;
            mes = new Measurement(idCity, updateTime, name, value, unit);
            
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
        City firstResult = result.get(0);
        firstResult.setMeasurementCollection(null);
        return firstResult;
    }
    
    public List<City> findAllCities()
    {
        Query findQuery = em.createQuery("SELECT c FROM City c");
        
        List<City> result= findQuery.getResultList() ;
        
        for(int i = 0; i < result.size(); i++)
        {
            result.get(i).setMeasurementCollection(null);
        }
        
        return result;
    }

    public List<Measurement> findPastMeasurementsByCityID(int id, int interval, int step) 
    {
        Query findQuery = em.createQuery(
                "SELECT m.measurementPK.name, AVG(m.value), m.unit "
              + "FROM Measurement m "
              + "WHERE m.measurementPK.city = :id AND "
              + "((unix_timestamp() - unix_timestamp(m.measurementPK.updateTime)) "
              + "BETWEEN 3600*(:step)*(:interval) AND 3600*(:step)*(:interval + 1)) "
              + "GROUP BY m.measurementPK.name, m.unit");
        
        findQuery.setParameter("id", id);
        findQuery.setParameter("step", step);
        findQuery.setParameter("interval", interval);
        
        List resultList = findQuery.getResultList();
        List<Measurement> measurements = new ArrayList<Measurement>();
        for(int i = 0; i < resultList.size(); i++)
        {
            Object[] result = (Object[]) resultList.get(i);
            Measurement m = new Measurement(0, null, (String) result[0]);
            Double value = (Double) result[1];
            String s = String.format("%.2f", value);
            m.setValue(s);
            m.setUnit((String) result[2]);
            measurements.add(m);
        }
        return measurements;
    }
    
}
