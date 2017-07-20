package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

import vo.DatasetVO;

public class DatasetProcessor implements ItemProcessor<DatasetVO, DatasetVO>{

	@Override
	public DatasetVO process(DatasetVO datasetVO) throws Exception {
		// TODO Auto-generated method stub
		return datasetVO;
	}

}
