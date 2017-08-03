package com.timegraph.bo;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dataProcess.MakingDate;
import com.timegraph.dto.InfluxdbDTO;

public class InfluxdbQueryGenerator {
	
	private InfluxDB influxDB;

	Query query;
	QueryResult result;
	List<List<Object>> lists;
	String sql;
	final String timeCondition = "time<\'"+new MakingDate().dateToday()+"\'";
	final String timeInterval = "group by time(1d)";

	public InfluxdbQueryGenerator(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}

	public void select(String field) {
		sql = "select count("+field+") from "+InfluxdbDAO.MEASUREMENT+" where "+timeCondition+" and "+field+" = 'Y' group by time(1d)";
	}
	public void select(String field, String field2) {
		sql = "select count("+field+") from "+InfluxdbDAO.MEASUREMENT+" where "+timeCondition+" and ("+field+" = 'Y'or "+field2+" = 'Y') group by time(1d)";
	}
	public void selectPerson(String field,String person) {
		sql = "select count("+field+") from "+InfluxdbDAO.MEASUREMENT+" where "+timeCondition+" and person = \'"+person+"\' group by time(1d)";
	}
	public void selectCategory(String field,String category) {
		sql = "select count("+field+") from "+InfluxdbDAO.MEASUREMENT+" where "+timeCondition+" and categoryName = \'"+category+"\' group by time(1d)";
	}
	public void selectPersonWithCategory(String field,String person, String category) {
		sql = "select count("+field+") from "+InfluxdbDAO.MEASUREMENT+" where "+timeCondition+" and person = \'"+person+"\' and categoryName = \'"+category+"\' group by time(1d)";
	}
	public void tagValues(String key) {
		sql = "show tag values with key in("+key+")";
	}
	public void read() {
		
	}
	
	public List<List<Object>> execute(){
		query = new Query(sql, InfluxdbDAO.DATABASE);
		result = influxDB.query(query);
		System.out.println(sql);
		System.out.println(result);
		lists = result.getResults().get(0).getSeries().get(0).getValues();
		System.out.println("in influxdbcountquery:"+lists);
		return lists;
	}
}
