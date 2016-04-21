package com.wlspa.weatherlogserver.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gionatanG
 * @author chiaraC
 */
@Entity
@Table(name = "Measurement")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
    @NamedQuery(name = "Measurement.findAll", query = "SELECT m FROM Measurement m"),
    @NamedQuery(name = "Measurement.findByCity", query = "SELECT m FROM Measurement m WHERE m.measurementPK.city = :city"),
    @NamedQuery(name = "Measurement.findByUpdateTime", query = "SELECT m FROM Measurement m WHERE m.measurementPK.updateTime = :updateTime"),
    @NamedQuery(name = "Measurement.findByName", query = "SELECT m FROM Measurement m WHERE m.measurementPK.name = :name"),
    @NamedQuery(name = "Measurement.findByValue", query = "SELECT m FROM Measurement m WHERE m.value = :value"),
    @NamedQuery(name = "Measurement.findByUnit", query = "SELECT m FROM Measurement m WHERE m.unit = :unit")})

public class Measurement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    @XmlElement(name = "main")
    public MeasurementPK measurementPK;
    
    @Size(max = 128)
    @Column(name = "Value")
    @XmlElement(name = "value")
    private String value;
    
    @Size(max = 16)
    @Column(name = "Unit")
    @XmlElement(name = "unit")
    private String unit;
    
    @JoinColumn(name = "City", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private City city1;

    public Measurement() {
    }

    public Measurement(MeasurementPK measurementPK) {
        this.measurementPK = measurementPK;
    }

    public Measurement(int city, Date updateTime, String name) {
        this.measurementPK = new MeasurementPK(city, updateTime, name);
    }

    public Measurement(int city, Date updateTime, String name, String value, String unit) {
        this.measurementPK = new MeasurementPK(city, updateTime, name);
        this.value = value;
        this.unit = unit;
    }
    
    public MeasurementPK getMeasurementPK() {
        return measurementPK;
    }

    public void setMeasurementPK(MeasurementPK measurementPK) {
        this.measurementPK = measurementPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (measurementPK != null ? measurementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Measurement)) {
            return false;
        }
        Measurement other = (Measurement) object;
        if ((this.measurementPK == null && other.measurementPK != null) || (this.measurementPK != null && !this.measurementPK.equals(other.measurementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entity.Measurement[ measurementPK=" + measurementPK + " ]";
    }
    
}
