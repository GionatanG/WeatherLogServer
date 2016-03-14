package com.wlspa.weatherlogserver.ejb;

/**
 *
 * @author gionatanG
 * 
 */


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Produces("application/xml")
public class HandleConnection
{
    
    @GET
    @Path("/{prova}/")
    public void ClasseDiProva(@PathParam("prova") String value)
    {
        System.out.println("Questa è una stringa di prova " + value);
    }

    
}





	
