package influxDB;
import java.io.IOException;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

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
	
	public InfluxDB setUp() throws InterruptedException, IOException {	//copy from https://github.com/influxdata/influxdb-java
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
		System.out.println("#  Connected to InfluxDB Version: " + this.influxDB.version() + " #");
		
		return this.influxDB;
	}	
	public void InfluxDBClose() {
		influxDB = null;
	}
}