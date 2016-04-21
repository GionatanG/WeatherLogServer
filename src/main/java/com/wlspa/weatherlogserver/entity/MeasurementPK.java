package com.wlspa.weatherlogserver.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gionatanG
 * @author chiaraC
 */

@Embeddable
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "info")
public class MeasurementPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "City")
    private int city;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "UpdateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "Name")
    @XmlAttribute(name="name")
    private String name;

    public MeasurementPK() {
    }

    public MeasurementPK(int city, Date updateTime, String name) {
        this.city = city;
        this.updateTime = updateTime;
        this.name = name;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) city;
        hash += (updateTime != null ? updateTime.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MeasurementPK)) {
            return false;
        }
        MeasurementPK other = (MeasurementPK) object;
        if (this.city != other.city) {
            return false;
        }
        if ((this.updateTime == null && other.updateTime != null) || (this.updateTime != null && !this.updateTime.equals(other.updateTime))) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entity.MeasurementPK[ city=" + city + ", updateTime=" + updateTime + ", name=" + name + " ]";
    }
    
}
