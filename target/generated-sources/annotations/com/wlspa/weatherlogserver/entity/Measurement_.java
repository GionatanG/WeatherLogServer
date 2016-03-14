package com.wlspa.weatherlogserver.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Measurement.class)
public abstract class Measurement_ {

	public static volatile SingularAttribute<Measurement, String> unit;
	public static volatile SingularAttribute<Measurement, City> city1;
	public static volatile SingularAttribute<Measurement, MeasurementPK> measurementPK;
	public static volatile SingularAttribute<Measurement, String> value;

}

