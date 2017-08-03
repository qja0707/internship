package com.timegraph.dto;

import java.util.List;

public class InfluxdbDTO {
	private String field;
	private String field2;
	private String person;
	private String objectName;
	private String categoryName;
	private List<List<Object>> datas;

	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<List<Object>> getDatas() {
		return datas;
	}
	public void setDatas(List<List<Object>> datas) {
		this.datas = datas;
	}
}
