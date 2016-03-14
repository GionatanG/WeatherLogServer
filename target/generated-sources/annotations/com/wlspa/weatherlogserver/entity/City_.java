package com.wlspa.weatherlogserver.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(City.class)
public abstract class City_ {

	public static volatile SingularAttribute<City, String> country;
	public static volatile SingularAttribute<City, Double> latitude;
	public static volatile SingularAttribute<City, String> name;
	public static volatile SingularAttribute<City, Integer> id;
	public static volatile CollectionAttribute<City, Measurement> measurementCollection;
	public static volatile SingularAttribute<City, Double> longitude;

}

