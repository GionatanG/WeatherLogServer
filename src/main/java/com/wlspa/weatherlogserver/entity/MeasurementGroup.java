package com.wlspa.weatherlogserver.entity;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gionatanG
 * @author chiaraC
 * 
 */

@XmlRootElement(name = "cityLog")
@XmlAccessorType(XmlAccessType.NONE)
public class MeasurementGroup {
    
    @XmlAttribute(name="update")
    private Date updateTime;
    
    @XmlElement(name = "measurement")
    private List<Measurement> measurements;

    public MeasurementGroup(Date updateTime, List<Measurement> measurements) 
    {
        this.updateTime = updateTime;
        this.measurements = measurements;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
    
    
}
