package com.wlspa.weatherlogserver.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MeasurementPK.class)
public abstract class MeasurementPK_ {

	public static volatile SingularAttribute<MeasurementPK, Integer> city;
	public static volatile SingularAttribute<MeasurementPK, String> name;
	public static volatile SingularAttribute<MeasurementPK, Date> updateTime;

}

