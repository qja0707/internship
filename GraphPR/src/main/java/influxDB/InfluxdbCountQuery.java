package influxDB;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxdbCountQuery {

	final String dbName = "Statistics";
	
	private InfluxDB influxDB;

	Query query;
	QueryResult result;
	List<List<Object>> lists;

	public InfluxdbCountQuery(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}

	public List<List<Object>> select(String sql) {
		query = new Query(sql, dbName);
		result = influxDB.query(query);

		lists = result.getResults().get(0).getSeries().get(0).getValues();
		return lists;
	}

	public List<List<Object>> countByPerson(String sql) {
		query = new Query(sql, dbName);
		result = influxDB.query(query);
		System.out.println(sql);
		lists = result.getResults().get(0).getSeries().get(0).getValues();
		System.out.println(lists);
		return null;
	}
}
