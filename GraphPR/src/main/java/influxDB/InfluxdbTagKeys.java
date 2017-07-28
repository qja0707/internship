package influxDB;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxdbTagKeys {
	final String dbName = "Statistics";

	private InfluxDB influxDB;

	Query query;
	QueryResult result;
	List<List<Object>> lists;
	
	public InfluxdbTagKeys(InfluxDB influxDB) {
		this.influxDB=influxDB;
	}
	
	public List<List<Object>> tagKeys() {
		query = new Query("show tag values with key in(person)", dbName);
		result = influxDB.query(query);
		
		lists = result.getResults().get(0).getSeries().get(0).getValues();
		
		return lists;
	}
}
