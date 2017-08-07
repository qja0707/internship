package com.timegraph.bo

import java.text.SimpleDateFormat
import java.util.Date

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dto.InfluxdbDTO

class QueryGenerator {

	private String query;
	private InfluxdbDTO dto;
	
	private String none = "total"
	
	public QueryGenerator(InfluxdbDTO dto) {
		this.dto = dto;
	}
	
	public String getQuery() {
		println query;
		return query;
	}
	public void select() {
		query = """
				SELECT COUNT(pcServiceYn)
						FROM ${InfluxdbDAO.MEASUREMENT}	
						WHERE time<'${dateToday()}'
				"""
		
		if(dto.getField()!=null&&!dto.getField().equals(none) 
		&& dto.getField2()!=null&&!dto.getField2().equals(none)) {
			
			query += 
				"""
				AND (${dto.getField()} = 'Y' OR ${dto.getField2()} = 'Y') 
				"""
					
		}else if(dto.getField()!=null&&!dto.getField().equals(none)) {
			query += 
				"""
				AND ${dto.getField()} = 'Y' 
				"""
		}
		
		if(dto.getPerson()!=null&&!dto.getPerson().equals(none)) {
			query += 
				"""
				AND person = \'${dto.getPerson()}\' 
				"""
		}
		
		if(dto.getCategoryName()!=null&&!dto.getCategoryName().equals(none)) {
			query += 
				"""
				AND categoryName = \'${dto.getCategoryName()}\' 
				"""
		}
		
		query +="""
				GROUP BY time(1d)
				"""
	}
	
	public void showTag() {
		query = """
				SHOW TAG VALUES WITH KEY IN (${dto.getTag()})
				"""
		
		if(dto.getPerson()!=null&&!dto.getPerson().equals(none)) {
			query += 
				"""
				WHERE person = \'${dto.getPerson()}\' 
				"""
		}else if(dto.getCategoryName()!=null&&!dto.getCategoryName().equals(none)) {
			query += 
				"""
				WHERE categoryName = \'${dto.getCategoryName()}\' 
				"""
		}
	}
	
	protected String dateToday() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		
		return format.format(now);
	}
}
