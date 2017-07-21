package com.example.demo;

import java.util.ArrayList;

import org.springframework.batch.item.ItemProcessor;

import influxDB.InfluxDBConn;
import vo.DatasetVO;

public class DatasetProcessor implements ItemProcessor<DatasetVO, ArrayList<DatasetVO>> {

	public static ArrayList<DatasetVO> dataArray = new ArrayList<>();

	@Override
	public ArrayList<DatasetVO> process(DatasetVO datasetVO) throws Exception {
		System.out.println(datasetVO.getObjectName());
		dataArray.add(datasetVO);

		return null;
	}

}
