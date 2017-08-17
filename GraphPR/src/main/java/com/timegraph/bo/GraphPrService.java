package com.timegraph.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.timegraph.dao.InfluxdbDAO;
import com.timegraph.dto.InfluxdbDTO;

public class GraphPrService {

	GenericObjectPool<InfluxDB> pool;

	AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	public InfluxdbDTO getDatas(InfluxdbDTO dto) {
		// IncfluxDB connection pool
		pool = (GenericObjectPool<InfluxDB>) context.getBean("pool");		//influxDB connection pool
		try {
			InfluxDB influxDB = (InfluxDB) pool.borrowObject();				//connection borrow
			try {
				InfluxdbDAO influxdbDAO = new InfluxdbDAO(influxDB);

				QueryGenerator queryGenerator = new QueryGenerator(dto);	
				queryGenerator.select();									//dto 의 정보를 통해 쿼리 생성
				String sql = queryGenerator.getQuery();
				QueryResult result = influxdbDAO.read(sql);					//influxDB 에서 쿼리 수행

				
				//결과 값 중 차트에 필요한 데이터 추출
				//QueryResult [results=[Result [series=[Series [name=test16, tags=null, columns=[time, count], values=[[2017-07-23T00:00:00Z, 1.0], [2017-07-24T00:00:00Z, 1.0], ...
				//	->[[2017-07-23, 1.0], [2017-07-24, 1.0], [2017-07-25, 1.0], ...
				List<List<Object>> resultForChart = transformDate(
						result.getResults().get(0).getSeries().get(0).getValues());		
				
				
				//pc service 와 mobile service 를 동시에 그래프에 나타내기 위해 pc 데이터 List 에 mobile 데이터 삽입
				//[[2017-07-23, 1.0], [2017-07-24, 1.0], [2017-07-25, 1.0], ...
				//	->[[2017-07-23, 1.0, 1.0], [2017-07-24, 1.0, 1.0], [2017-07-25, 1.0, 1.0], ...
				if (dto.getField2() != null) {		

					
					//현재 queryGenerator 가 dto.field 값을 이용해 쿼리를 생성하기 때문에
					//잠시 field2 값을 field 로 옮겨 쿼리 생성
					String temp = dto.getField();
					dto.setField(dto.getField2());		 
					
					queryGenerator.setDTO(dto);
					queryGenerator.select();
					sql = queryGenerator.getQuery();
					QueryResult result2 = influxdbDAO.read(sql);
					System.out.println(result2);
					List<List<Object>> resultForChart2;
					try {
						resultForChart2 = result2.getResults().get(0).getSeries().get(0).getValues();

						System.out.println(resultForChart);
						for (int i = 0; i < resultForChart.size(); i++) {
							resultForChart.get(i)
									.add(resultForChart2.get(i).get(resultForChart2.get(0).size() - 1));
						}
					} catch (NullPointerException e) {
						for (int i = 0; i < resultForChart.size(); i++)
							resultForChart.get(i).add(0);
					}
					dto.setField(temp);		//field 값 복원
				}
				dto.setDatas(putHeader(dto, resultForChart));		//구글 차트에 넣기 위해 칼럼을 데이터 앞에 붙임

				System.out.println(dto.getDatas());

				System.out.println("pool: " + pool);
				System.out.println("created:" + pool.getCreatedCount());
				System.out.println("destroyed:" + pool.getDestroyedCount());
			} catch (Exception e) {
				e.printStackTrace();
				// invalidate the object
				pool.invalidateObject(influxDB);
				// do not return the object to the pool twice
				influxDB = null;
			} finally {
				if (influxDB != null)
					pool.returnObject(influxDB);
			}
		} catch (Exception e) {
			System.out.println("Failed to borrow a connection");
			e.printStackTrace();
		}
		return dto;
	}

	//콤보박스의 동적 option 생성을 위해 influxDB의 person 혹은 category 태그 value 가져옴. 
	public InfluxdbDTO getTagValues(InfluxdbDTO dto) {					
		pool = (GenericObjectPool<InfluxDB>) context.getBean("pool");	//influxDB connection pool
		try {
			InfluxDB influxDB = (InfluxDB) pool.borrowObject();			//connection borrow
			try {
				InfluxdbDAO influxdbDAO = new InfluxdbDAO(influxDB);

				QueryGenerator queryGenerator = new QueryGenerator(dto);		
				queryGenerator.showTag();								//query generation
				String sql = queryGenerator.getQuery();

				QueryResult result = influxdbDAO.read(sql);				//쿼리 수행

				List<List<Object>> resultForChart = result.getResults().get(0).getSeries().get(0).getValues();
				dto.setDatas(resultForChart);

			} catch (Exception e) {
				// invalidate the object
				pool.invalidateObject(influxDB);
				// do not return the object to the pool twice
				influxDB = null;
			} finally {
				if (influxDB != null)
					pool.returnObject(influxDB);
			}
		} catch (Exception e) {
			System.out.println("Failed to borrow a connection");
			e.printStackTrace();
		}
		return dto;
	}

	// 2017-07-23T00:00:00 -> 2017-07-23
	protected List<List<Object>> transformDate(List<List<Object>> items) {
		for (List<Object> list : items) {
			String newItem = String.valueOf(list.get(0));
			newItem = newItem.substring(0, 10);
			list.set(0, newItem);
		}
		return items;
	}

	
	//구글 차트에 넣기 위해 칼럼을 데이터 앞에 붙임
	//[[2017-07-23, 1.0], [2017-07-24, 1.0], [2017-07-25, 1.0], ...
	//	->[[Date, 박옥화 total pcServiceYn], [2017-07-23, 1.0], [2017-07-24, 1.0], ...
	protected List<List<Object>> putHeader(InfluxdbDTO dto, List<List<Object>> items) {
		List<Object> t = new ArrayList<Object>();
		t.add("Date");
		t.add(dto.getPerson() + "\n" + dto.getCategoryName() + "\n" + dto.getField());
		
		
		//하나의 그래프에 pc,mobile service를 같이 그리기 위해 칼럼 하나 더 추가
		//		->[[Date, 박옥화 total pcServiceYn, mobileServiceYn], [2017-07-23, 1.0, 1.0], [2017-07-24, 1.0, 1.0],
		if (dto.getField2() != null) {		
			t.add(dto.getField2());
		}
		items.add(0, t);
		return items;
	}
	
	
	// pc or mobile 원래 함수 - (${dto.getField()} = 'Y' OR ${dto.getField2()} = 'Y') in com.timegraph.bo.QueryGenerator.groovy
	//not used
	public InfluxdbDTO getORDatas(InfluxdbDTO dto) {
		// IncfluxDB connection pool
		pool = (GenericObjectPool<InfluxDB>) context.getBean("pool");
		try {
			InfluxDB influxDB = (InfluxDB) pool.borrowObject();
			try {
				InfluxdbDAO influxdbDAO = new InfluxdbDAO(influxDB);
				QueryGenerator queryGenerator = new QueryGenerator(dto);
				queryGenerator.select();
				String sql = queryGenerator.getQuery();

				QueryResult result = influxdbDAO.read(sql);
				List<List<Object>> resultForChart = transformDate(
						result.getResults().get(0).getSeries().get(0).getValues());
				dto.setDatas(putHeader(dto, resultForChart));
				System.out.println(result.getResults().get(0).getSeries().get(0).getValues());

				System.out.println("pool: " + pool);
				System.out.println("created:" + pool.getCreatedCount());
				System.out.println("destroyed:" + pool.getDestroyedCount());
			} catch (Exception e) {
				// invalidate the object
				pool.invalidateObject(influxDB);
				// do not return the object to the pool twice
				influxDB = null;
			} finally {
				if (influxDB != null)
					pool.returnObject(influxDB);
			}
		} catch (Exception e) {
			System.out.println("Failed to borrow a connection");
			e.printStackTrace();
		}
		return dto;
	}
}
