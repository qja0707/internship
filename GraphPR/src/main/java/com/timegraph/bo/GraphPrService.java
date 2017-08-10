package com.timegraph.bo;

import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dto.InfluxdbDTO;

public class GraphPrService {
	
	QueryGenerator queryGenerator;
	InfluxdbDAO influxdbDAO;
	
	GenericObjectPool<InfluxDB> pool;
	//InfluxDB influxDB;
	
	AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	public InfluxdbDTO getDatas(InfluxdbDTO dto) throws Exception {
		pool = (GenericObjectPool<InfluxDB>) context.getBean("pool");
		InfluxDB influxDB = (InfluxDB) pool.borrowObject();
		influxdbDAO = new InfluxdbDAO(influxDB);
		
		queryGenerator = new QueryGenerator(dto);
		queryGenerator.select();
		String sql = queryGenerator.getQuery();
		
		QueryResult result = influxdbDAO.read(sql);
		List<List<Object>> resultForGoogleChart = 
				dateToString(result.getResults().get(0).getSeries().get(0).getValues());
		dto.setDatas(resultForGoogleChart);
		
		System.out.println("pool: "+pool);
		System.out.println("created:"+pool.getCreatedCount());
		System.out.println("destroyed:"+pool.getDestroyedCount());
		System.out.println("borrowed:"+pool.getBorrowedCount());
		System.out.println("returned:"+pool.getReturnedCount());
		pool.returnObject(influxDB);
		return dto;
	}
	
	public InfluxdbDTO getTagValues(InfluxdbDTO dto) throws Exception{
		pool = (GenericObjectPool<InfluxDB>) context.getBean("pool");
		InfluxDB influxDB = (InfluxDB) pool.borrowObject();
		influxdbDAO = new InfluxdbDAO(influxDB);
		
		queryGenerator = new QueryGenerator(dto);
		queryGenerator.showTag();
		String sql = queryGenerator.getQuery();
		
		QueryResult result = influxdbDAO.read(sql);
		dto.setDatas(result.getResults().get(0).getSeries().get(0).getValues());
		
		pool.returnObject(influxDB);
		return dto;
	}
	
	protected List<List<Object>> dateToString(List<List<Object>> items){
		for (List<Object> list : items) {
			String newItem = String.valueOf(list.get(0));
			newItem = newItem.substring(0, 10);
			newItem = "\""+newItem+"\"";
			list.set(0, newItem);
		}
		return items;
	}
	
	//not used
	protected List<List<Object>> forJson(List<List<Object>> items){
		for (List<Object> list : items) {
			String newItem = String.valueOf(list.get(0));
			String newItem2 = String.valueOf(list.get(1));
			newItem = "\""+newItem+"\"";
			newItem2 = "\""+newItem+"\"";
			list.set(0, newItem);
			list.set(1, newItem2);
		}
		return items;
	}
}
