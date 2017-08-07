package com.timegraph.bo;

import java.util.List;

import org.influxdb.dto.QueryResult;

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dto.InfluxdbDTO;

public class GraphPrService {
	
	QueryGenerator queryGenerator;
	String sql;
	InfluxdbDAO influxdbDAO;
	QueryResult result;
	
	public GraphPrService() {
		influxdbDAO = new InfluxdbDAO();
	}
	
	public InfluxdbDTO getDatas(InfluxdbDTO dto) {
		queryGenerator = new QueryGenerator(dto);
		
		queryGenerator.select();
		
		sql = queryGenerator.getQuery();
		
		result = influxdbDAO.read(sql);
		List<List<Object>> resultForGoogleChart = 
				dateToString(result.getResults().get(0).getSeries().get(0).getValues());
		dto.setDatas(resultForGoogleChart);
		return dto;
	}
	
	public InfluxdbDTO getTagValues(InfluxdbDTO dto){
		queryGenerator = new QueryGenerator(dto);
		queryGenerator.showTag();
		sql = queryGenerator.getQuery();
		
		result = influxdbDAO.read(sql);
		dto.setDatas(result.getResults().get(0).getSeries().get(0).getValues());
		
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
