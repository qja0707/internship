package job.step;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import influxDB.InfluxDBConn;
import vo.DatasetVO;

public class InfluxdbWriter implements ItemWriter<DatasetVO> {
	private final String WRITE = "+++++Write+++++";
	private final String IP_ADDR = "10.110.248.58";
	private final String PORT = "8086";
	private final String USER = "root";
	private final String PASSWORD = "root";
	
	private final String DATABASE = "Statistics";		
	private final String MEASUREMENT = "test16"; 
	
	InfluxDBConn db;
	int num;
	
	public InfluxdbWriter() {
		num=0;
		db = new InfluxDBConn(IP_ADDR,PORT,DATABASE,USER,PASSWORD);
		try {
			db.setUp();
		} catch (InterruptedException | IOException e) {
			System.out.println(WRITE+"incluxDB connecting error");
		}
	}
	
	public void write(List<? extends DatasetVO> items) throws Exception {
		
		for (DatasetVO item : items) {
			DatasetVO datasetVO = new DatasetVO();

			datasetVO.setCategoryId(item.getCategoryId());
			datasetVO.setCategoryName(item.getCategoryName());
			datasetVO.setMobileServiceYn(item.getMobileServiceYn());
			datasetVO.setObjectId(item.getObjectId());
			datasetVO.setObjectName(item.getObjectName());
			datasetVO.setPcServiceYn(item.getPcServiceYn());
			datasetVO.setPerson(item.getPerson());

			db.customWrite(MEASUREMENT,datasetVO);		//measurement, VO, time
			num++;
		}
		System.out.println(WRITE+num+" is written");
	}
	public void finalize() {
		if(db!=null) {
			db.closeDB();
		}
		System.out.println(WRITE+"InfluxDB close");
	}

}
