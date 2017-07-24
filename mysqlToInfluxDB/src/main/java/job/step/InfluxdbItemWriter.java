package job.step;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import influxDB.InfluxDBConn;
import vo.DatasetVO;

public class InfluxdbItemWriter implements ItemWriter<DatasetVO> {
	private final static String IP_ADDR = "localhost";
	private final static String PORT = "8086";
	private final static String USER = "root";
	private final static String PASSWORD = "root";
	
	private final static String DATABASE = "Statistics";		
	private final static String MEASUREMENT = "test16"; 
	
	InfluxDBConn db = new InfluxDBConn(IP_ADDR,PORT,DATABASE,USER,PASSWORD);

	
	public void write(List<? extends DatasetVO> items) throws Exception {
		db.setUp();

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
		}
		
		db.closeDB();
	}

}
