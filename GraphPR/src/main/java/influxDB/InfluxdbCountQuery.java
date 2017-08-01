package influxDB;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.timegraph.controller.HomeController;

public class InfluxdbCountQuery {
	
	private InfluxDB influxDB;

	Query query;
	QueryResult result;
	List<List<Object>> lists;

	public InfluxdbCountQuery(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}

	public List<List<Object>> select(String sql) {
		query = new Query(sql, HomeController.DATABASE);
		result = influxDB.query(query);
		System.out.println(sql);
		System.out.println(result);
		lists = result.getResults().get(0).getSeries().get(0).getValues();
		System.out.println("in influxdbcountquery:"+lists);
		return lists;
	}
}
