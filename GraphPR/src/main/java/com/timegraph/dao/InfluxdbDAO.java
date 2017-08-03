package com.timegraph.dao;

import java.io.IOException;
import java.util.List;

import org.influxdb.InfluxDB;

import com.timegraph.dataProcess.ProcessForGooglechart;

import influxDB.InfluxDBConn;
import influxDB.InfluxdbQueryGenerator;

public class InfluxdbDAO {

	private final String IP_ADDR = "10.110.248.58";
	private final String PORT = "8086";
	private final String USER = "root";
	private final String PASSWORD = "root";

	public static final String DATABASE = "Statistics";
	public static final String MEASUREMENT = "test16";
	
	InfluxDB influxDB;
	InfluxdbQueryGenerator queryGenerator;
	
	ProcessForGooglechart transform;
	
	public InfluxdbDAO() {
		try {
			influxDB = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
			queryGenerator = new InfluxdbQueryGenerator(influxDB);
			transform = new ProcessForGooglechart();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<List<Object>> readDatas(String field){
		queryGenerator.select(field);
		return transform.dateToString(queryGenerator.execute());
	}
	public List<List<Object>> readDatas(String field, String field2){
		queryGenerator.select(field, field2);
		return transform.dateToString(queryGenerator.execute());
	}
	public List<List<Object>> readDatasPerPerson(String field, String person){
		queryGenerator.selectPerson(field, person);
		return transform.dateToString(queryGenerator.execute());
	}
	public List<List<Object>> readDatasPerCategory(String field, String category){
		queryGenerator.selectCategory(field, category);
		return transform.dateToString(queryGenerator.execute());
	}
	public List<List<Object>> readDatasPerPersonAndCategory(String field, String person, String category){
		queryGenerator.selectPersonWithCategory(field, person, category);
		return transform.dateToString(queryGenerator.execute());
	}
	public List<List<Object>> getTagValues(String key){
		queryGenerator.tagValues(key);
		return queryGenerator.execute();
	}
}
