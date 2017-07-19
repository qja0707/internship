package influxDB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;

import vo.DatasetVO;


public class InfluxDBConn {
	
	private InfluxDB influxDB;
	private String ip_addr;
	private String port;
	private String dbName;
	private String user;
	private String password;
	
	public InfluxDBConn(String ip_addr, String port, String dbName, String user, String password) {
		this.ip_addr = ip_addr;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.password = password;
	}
	
	public void setUp() throws InterruptedException, IOException {	//copy from https://github.com/influxdata/influxdb-java
		this.influxDB = InfluxDBFactory.connect("http:////" + ip_addr + ":" + port, user , password);
		
		boolean influxDBstarted = false;
		do {
			Pong response;
			try {
				response = this.influxDB.ping();
				if (!response.getVersion().equalsIgnoreCase("unknown")) {
					influxDBstarted = true;
				}
			} catch (Exception e) {
				// NOOP intentional
				e.printStackTrace();
			}
			Thread.sleep(100L);
		} while (!influxDBstarted);
		
		this.influxDB.setLogLevel(LogLevel.NONE);
		this.influxDB.createDatabase(dbName);
        System.out.println("################################################################################## ");
		System.out.println("#  Connected to InfluxDB Version: " + this.influxDB.version() + " #");
		System.out.println("##################################################################################");
	}		
	
	public void customWrite(String measurementName, ArrayList<DatasetVO> dataArray, long unixTime) {
		
		BatchPoints batchPoints = BatchPoints.database(dbName).tag("async", "true").retentionPolicy("").build();
		
		System.out.println("Database : "+dbName);
		System.out.println("Mesurement : "+measurementName);
		System.out.println("dataArray size : "+dataArray.size());
		
		for (DatasetVO dataset : dataArray) {	
			batchPoints.point(Point
					.measurement(measurementName)
					.tag("objectId", String.valueOf(dataset.getObjectId()))
					.tag("objectName",dataset.getObjectName())
					.tag("categoryId",String.valueOf(dataset.getCategoryId()))
					.tag("categoryName",dataset.getCategoryName())
					.addField("categoryType",dataset.getCategoryType())
					.addField("pcServiceYn",dataset.getPcServiceYn())
					.addField("mobileServiceYn",dataset.getMobileServiceYn())
					.time(unixTime, TimeUnit.MILLISECONDS)				
					.build());
		}
		this.influxDB.write(batchPoints);
		System.out.println("Writing batchPoints is done");
	}
	
	public void closeDB() {
		this.influxDB.close();
		System.out.println("Close InfluxDB");
	}
	
}