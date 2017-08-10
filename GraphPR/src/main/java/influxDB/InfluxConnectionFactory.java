package influxDB;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.influxdb.InfluxDB;

public class InfluxConnectionFactory extends BasePooledObjectFactory<InfluxDB>{
	private String ip;
	private String port;
	private String user;
	private String password;
	public String database;
	
	@Override
	public InfluxDB create() throws Exception {
		return new InfluxDBConn(ip, port, database, user, password).setUp();
	}

	@Override
	public PooledObject<InfluxDB> wrap(InfluxDB influxDB) {
		return new DefaultPooledObject<InfluxDB>(influxDB);
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

}
