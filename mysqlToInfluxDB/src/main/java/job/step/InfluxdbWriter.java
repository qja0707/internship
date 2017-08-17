package job.step;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import influxDB.InfluxDBConn;
import vo.DatasetVO;

public class InfluxdbWriter implements ItemStreamWriter<DatasetVO> {
	private final String WRITE = "+++++Write+++++";
	private final String IP_ADDR = "10.110.248.58";
	private final String PORT = "8086";
	private final String USER = "root";
	private final String PASSWORD = "root";
	
	private final String DATABASE = "Statistics";		
	private final String MEASUREMENT = "test16"; 
	
	private final long CUSTOM_DATE = System.currentTimeMillis();//-86400000*8;			//default : System.currentTimeMillis()
	private final long TIME_DIFFERENCE = 9*60*60*1000;	//GMT+9:00 * 1hour * 1minute * 1000(ms)
	
	InfluxDBConn db;
	int num=0;
	
	long presentTime;
	
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

			db.customWrite(MEASUREMENT,datasetVO,presentTime);		//measurement, VO, time
			num++;
		}
		System.out.println(WRITE+num+" is written");
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {		//writing step 시작할 때 influxDB 연결
		presentTime = CUSTOM_DATE + TIME_DIFFERENCE;
		db = new InfluxDBConn(IP_ADDR,PORT,DATABASE,USER,PASSWORD);
		try {
			db.setUp();
		} catch (InterruptedException | IOException e) {
			System.out.println(WRITE+"incluxDB connecting error");
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() throws ItemStreamException {			//writing step 끝난 후 influxDB 연결 끊기
		if(db!=null) {
			db.closeDB();
		}
		System.out.println(WRITE+"InfluxDB close");
	}

}
