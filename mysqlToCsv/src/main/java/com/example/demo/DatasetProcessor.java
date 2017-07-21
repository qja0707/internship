package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

import vo.DatasetVO;

public class DatasetProcessor implements ItemProcessor<DatasetVO, DatasetVO>{
	
	int num=0;

	@Override
	public DatasetVO process(DatasetVO datasetVO) throws Exception {
		System.out.println(++num);
		// TODO Auto-generated method stub
		return datasetVO;
	}

}
