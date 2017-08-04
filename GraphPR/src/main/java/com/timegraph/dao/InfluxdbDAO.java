package com.timegraph.dao;

import java.io.IOException;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import influxDB.InfluxDBConn;

public class InfluxdbDAO {

	private final String IP_ADDR = "10.110.248.58";
	private final String PORT = "8086";
	private final String USER = "root";
	private final String PASSWORD = "root";

	public static final String DATABASE = "Statistics";
	public static final String MEASUREMENT = "test16";
	
	InfluxDB influxDB;
	Query query;
	QueryResult result;
	
	public InfluxdbDAO() {
		try {
			influxDB = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public QueryResult read(String sql){
		query = new Query(sql,DATABASE);
		result = influxDB.query(query);
		return result;
	}
	
}
