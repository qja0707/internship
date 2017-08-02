package influxDB;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.timegraph.controller.HomeController;
import com.timegraph.dataProcess.MakingDate;

public class InfluxdbCountQuery {
	
	private InfluxDB influxDB;

	Query query;
	QueryResult result;
	List<List<Object>> lists;
	String sql;
	final String measurement = HomeController.MEASUREMENT;
	final String timeCondition = "time<\'"+new MakingDate().dateToday()+"\'";
	final String timeInterval = "group by time(1d)";

	public InfluxdbCountQuery(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}

	public void select(String field) {
		sql = "select count("+field+") from "+measurement+" where "+timeCondition+" and "+field+" = 'Y' "+timeInterval;
	}
	public void select(String field, String field2) {
		sql = "select count("+field+") from "+measurement+" where "+timeCondition+" and ("+field+" = 'Y'or "+field2+" = 'Y') "+timeInterval;
	}
	public void selectPerson(String field,String person) {
		sql = "select count("+field+") from "+measurement+" where "+timeCondition+" and person = \'"+person+"\' "+timeInterval;
	}
	public void selectCategory(String field,String category) {
		sql = "select count("+field+") from "+measurement+" where "+timeCondition+" and categoryName = \'"+category+"\' "+timeInterval;
	}
	public void selectPersonWithCategory(String field,String person, String category) {
		sql = "select count("+field+") from "+measurement+" where "+timeCondition+" and person = \'"+person+"\' and categoryName = \'"+category+"\' "+timeInterval;
	}
	
	public List<List<Object>> execute(){
		query = new Query(sql, HomeController.DATABASE);
		result = influxDB.query(query);
		System.out.println(sql);
		System.out.println(result);
		lists = result.getResults().get(0).getSeries().get(0).getValues();
		System.out.println("in influxdbcountquery:"+lists);
		return lists;
	}
}
