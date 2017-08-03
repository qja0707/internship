package com.timegraph.vo;

import java.util.List;

public class InfluxdbVO {
	List<List<Object>> datas;

	public List<List<Object>> getDatas() {
		return datas;
	}

	public void setDatas(List<List<Object>> datas) {
		this.datas = datas;
	}
	
}
