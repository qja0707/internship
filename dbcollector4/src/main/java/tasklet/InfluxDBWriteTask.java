package tasklet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import influxDB.InfluxDBConn;
import vo.DatasetVO;

@Component
public class InfluxDBWriteTask implements Tasklet{
	
	private final static String IP_ADDR = "localhost";
	private final static String PORT = "8086";
	private final static String USER = "root";
	private final static String PASSWORD = "root";
	
	private final static String DATABASE = "Statistics";
	private final static String MEASUREMENT = "test11"; 
	
	
	ArrayList<DatasetVO> dataArray;
	String date;
	long unixtime;

	@SuppressWarnings("unchecked")
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		dataArray = (ArrayList<DatasetVO>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("DATA_ARRAY");
		date = (String) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("DATE");
		unixtime = timeTransform(date);
		
		InfluxDBConn db = new InfluxDBConn(IP_ADDR,PORT,DATABASE,USER,PASSWORD);	
		
		db.setUp();

		db.customWrite(MEASUREMENT,dataArray,unixtime);		//measurement, VO, time
		
		db.closeDB();
		
		return null;
	}
	
	public long timeTransform(String rowDate) {
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
		long unixtime = 0;

		dfm.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));

		try {
			unixtime = dfm.parse(rowDate).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return unixtime;

	}

}
