package com.wlspa.weatherlogserver.ejb;

import com.wlspa.weatherlogserver.entity.City;
import com.wlspa.weatherlogserver.entity.Measurement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {

  // This method is called if TEXT_PLAIN is request
  /*@GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello WeatherLog Jersey Server";
  }
*/
  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    //return "<?xml version=\"1.0\"?>" + "<hello> Hello WeatherLog Jersey Server" + "</hello>";
      City c = new City();
      c.setCountry("IT");
      c.setId(123);
      c.setLatitude(30.3);
      c.setLongitude(23.3);
      c.setName("Lucca");
      
      City c2 = new City();
      c2.setCountry("UK");
      c2.setId(133);
      c2.setLatitude(30.33);
      c2.setLongitude(23.33);
      c2.setName("Ucca");
      
      Measurement m = new Measurement(123,new Date(),"temperature");
      m.setValue("23.5");
      m.setUnit("°C");
      
      //single actual city
      return "<city id=\"3170647\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\"> 1</measurement><measurement name=\"humidity\" unit=\"%\">1</measurement><measurement name=\"pressure\" unit=\"hPa\">1</measurement><measurement name=\"speed\" unit=\"mps\">1</measurement><measurement name=\"direction\" unit=\"mps\">1</measurement></city>";
        
    //past multiple 
      //return "<root><city id=\"3170647\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">11</measurement><measurement name=\"humidity\" unit=\"%\">12</measurement><measurement name=\"pressure\" unit=\"hPa\">13</measurement><measurement name=\"speed\" unit=\"mps\">14</measurement><measurement name=\"direction\" unit=\"mps\">15</measurement></city><city id=\"3169921\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">21</measurement><measurement name=\"humidity\" unit=\"%\">22</measurement><measurement name=\"pressure\" unit=\"hPa\">23</measurement><measurement name=\"speed\" unit=\"mps\">24</measurement><measurement name=\"direction\" unit=\"mps\">25</measurement></city><city id=\"3166548\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">31</measurement><measurement name=\"humidity\" unit=\"%\">32</measurement><measurement name=\"pressure\" unit=\"hPa\">33</measurement><measurement name=\"speed\" unit=\"mps\">34</measurement><measurement name=\"direction\" unit=\"mps\">35</measurement></city><city id=\"3176959\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">41</measurement><measurement name=\"humidity\" unit=\"%\">42</measurement><measurement name=\"pressure\" unit=\"hPa\">43</measurement><measurement name=\"speed\" unit=\"mps\">44</measurement><measurement name=\"direction\" unit=\"mps\">45</measurement></city><city id=\"6541868\" date=\"2016-02-19T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">51</measurement><measurement name=\"humidity\" unit=\"%\">52</measurement><measurement name=\"pressure\" unit=\"hPa\">53</measurement><measurement name=\"speed\" unit=\"mps\">54</measurement><measurement name=\"direction\" unit=\"mps\">55</measurement></city><city id=\"3170647\" date=\"2016-02-20T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">111</measurement><measurement name=\"humidity\" unit=\"%\">112</measurement><measurement name=\"pressure\" unit=\"hPa\">113</measurement><measurement name=\"speed\" unit=\"mps\">114</measurement><measurement name=\"direction\" unit=\"mps\">115</measurement></city><city id=\"3169921\" date=\"2016-02-20T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">121</measurement><measurement name=\"humidity\" unit=\"%\">122</measurement><measurement name=\"pressure\" unit=\"hPa\">123</measurement><measurement name=\"speed\" unit=\"mps\">124</measurement><measurement name=\"direction\" unit=\"mps\">125</measurement></city><city id=\"3166548\" date=\"2016-02-20T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">131</measurement><measurement name=\"humidity\" unit=\"%\">132</measurement><measurement name=\"pressure\" unit=\"hPa\">133</measurement><measurement name=\"speed\" unit=\"mps\">134</measurement><measurement name=\"direction\" unit=\"mps\">135</measurement></city><city id=\"3176959\" date=\"2016-02-20T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">141</measurement><measurement name=\"humidity\" unit=\"%\">142</measurement><measurement name=\"pressure\" unit=\"hPa\">143</measurement><measurement name=\"speed\" unit=\"mps\">144</measurement><measurement name=\"direction\" unit=\"mps\">145</measurement></city><city id=\"6541868\" date=\"2016-02-20T15:30:02\"><measurement name=\"temperature\" unit=\"Kelvin\">151</measurement><measurement name=\"humidity\" unit=\"%\">152</measurement><measurement name=\"pressure\" unit=\"hPa\">153</measurement><measurement name=\"speed\" unit=\"mps\">154</measurement><measurement name=\"direction\" unit=\"mps\">155</measurement></city></root>";
  } 
/*
  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
        + "<body><h1>" + "Hello WeatherLog Jersey Server" + "</h1></body>" + "</html> ";
  }
*/
} 