package com.timegraph.bo

import java.text.SimpleDateFormat
import java.util.Date

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dto.InfluxdbDTO

class QueryGenerator {

	private String query;
	private InfluxdbDTO dto;
	
	private String none = "null"
	
	public QueryGenerator(InfluxdbDTO dto) {
		this.dto = dto;
	}
	
	public String getQuery() {
		println query;
		return query;
	}
	/*public String generate() {
		select();
		where();
		groupBy();
		
		return query;
	}*/
	
	public void select() {
		query	= """SELECT COUNT(pcServiceYn)
						FROM ${InfluxdbDAO.MEASUREMENT}	"""
	}
	
	public void where() {
	
		query += 	"""	WHERE time<'${dateToday()}'	"""
		
		if(dto.getField()!=null&&!dto.getField().equals("null") 
			&& dto.getField2()!=null&&!dto.getField2().equals("null")) {
			
			query += """and (${dto.getField()} = 'Y' or ${dto.getField2()} = 'Y') """
			
		}else if(dto.getField()!=null&&!dto.getField().equals("null")) {
			query += """and ${dto.getField()} = 'Y' """
		}
		
		if(dto.getPerson()!=null&&!dto.getPerson().equals("null")) {
			query += """and person = \'${dto.getPerson()}\' """
		}
		
		if(dto.getCategoryName()!=null&&!dto.getCategoryName().equals("null")) {
			query += """and categoryName = \'${dto.getCategoryName()}\' """
		}
	}
	
	public void groupBy() {
		query += "GROUP BY time(1d)";
	}
	
	public void showTag() {
		query = """show tag values with key in (${dto.getTag()})"""
	}
	
	
	protected String dateToday() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		
		return format.format(now);
	}
}
