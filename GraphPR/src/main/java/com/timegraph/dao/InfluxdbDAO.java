package com.timegraph.dao;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxdbDAO {

	public static final String DATABASE = "Statistics";
	public static final String MEASUREMENT = "test16";
	
	InfluxDB influxDB;
	Query query;
	QueryResult result;
	
	public InfluxdbDAO(InfluxDB influxdb) {
		this.influxDB = influxdb;
	}
	public QueryResult read(String sql){
		query = new Query(sql,DATABASE);
		result = influxDB.query(query);
		return result;
	}
	
}
