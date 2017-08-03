package com.timegraph.dao;

import java.io.IOException;
import java.util.List;

import org.influxdb.InfluxDB;

import com.timegraph.bo.InfluxdbQueryGenerator;
import com.timegraph.dataProcess.ProcessForGooglechart;
import com.timegraph.dto.InfluxdbDTO;

import influxDB.InfluxDBConn;

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
	public InfluxdbDTO readDatas(InfluxdbDTO dto){
		if(dto.getField2()!=null)
			queryGenerator.select(dto.getField(), dto.getField2());
		else
			queryGenerator.select(dto.getField());
		dto.setDatas(transform.dateToString(queryGenerator.execute()));
		return dto;
	}
	/*public List<List<Object>> readDatas(InfluxdbVO dto){
		queryGenerator.select(dto.getField(), dto.getField2());
		return transform.dateToString(queryGenerator.execute());
	}*/
	public InfluxdbDTO readDatasPerPerson(InfluxdbDTO dto){
		queryGenerator.selectPerson(dto.getField(), dto.getPerson());
		dto.setDatas(transform.dateToString(queryGenerator.execute()));
		return dto;
	}
	public InfluxdbDTO readDatasPerCategory(InfluxdbDTO dto){
		queryGenerator.selectCategory(dto.getField(), dto.getCategoryName());
		dto.setDatas(transform.dateToString(queryGenerator.execute()));
		return dto;
	}
	public InfluxdbDTO readDatasPerPersonAndCategory(InfluxdbDTO dto){
		queryGenerator.selectPersonWithCategory(dto.getField(), dto.getPerson(), dto.getCategoryName());
		dto.setDatas(transform.dateToString(queryGenerator.execute()));
		return dto;
	}
	public List<List<Object>> getTagValues(String key){
		queryGenerator.tagValues(key);
		return queryGenerator.execute();
	}
}
