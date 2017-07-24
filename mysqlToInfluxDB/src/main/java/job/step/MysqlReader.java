package job.step;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import vo.DatasetVO;

@Component
public class MysqlReader implements ItemReader<DatasetVO>{
	private final String URL = "capp23.ext.nhncorp.com";
	private final String PORT = "33326";
	private final String DATABASE = "sirius";
	
	private final String USER = "sirius";
	private final String PASSWORD = "sirius";
	
	private final String sql = "SELECT 	A.object_id ," + 
			"			   	A.object_name, " + 
			"			   	A.service_status, " + 
			"				A.mobile_service_status, " + 
			"				B.person_in_charge, " + 
			"				B.category_id, " + 
			"				(SELECT value1 FROM sr_category WHERE id = B.category_id) as category_name  " + 
			"		FROM sr_object as A , cpeditor as B  " + 
			"		WHERE A.object_id = B.object_id  " + 
			"			AND A.service_status='Y'  " + 
			"			AND A.startdate <= now() AND A.enddate >=now()";
	
	/*public DataSource siriusDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl();
		dataSource.setUsername(USER);
		dataSource.setPassword(PASSWORD);
		
		return dataSource;
	}*/
	@Override
	public DatasetVO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("read");
		Connection conn = DriverManager.getConnection("jdbc:mysql://"+URL+":"+PORT+"/"+DATABASE,USER,PASSWORD);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			DatasetVO dataset = new DatasetVO();
			dataset.setObjectId(rs.getInt("A.object_id"));
			dataset.setObjectName(rs.getString("A.object_name"));
			dataset.setPcServiceYn(rs.getString("A.service_status"));
			dataset.setMobileServiceYn(rs.getString("A.mobile_service_status"));
			dataset.setPerson(rs.getString("B.person_in_charge"));
			dataset.setCategoryId(rs.getLong("B.category_id"));
			dataset.setCategoryName(rs.getString("category_name"));
			return dataset;
		}
		return null;
	}
/*
	public JdbcCursorItemReader<DatasetVO> read(){
		System.out.println("reading@@@@@@@@@@@@@@@@@@@@@@@@@@");
		String sql = "SELECT 	A.object_id ," + 
				"			   	A.object_name, " + 
				"			   	A.service_status, " + 
				"				A.mobile_service_status, " + 
				"				B.person_in_charge, " + 
				"				B.category_id, " + 
				"				(SELECT value1 FROM sr_category WHERE id = B.category_id) as category_name  " + 
				"		FROM sr_object as A , cpeditor as B  " + 
				"		WHERE A.object_id = B.object_id  " + 
				"			AND A.service_status='Y'  " + 
				"			AND A.startdate <= now() AND A.enddate >=now()";
		JdbcCursorItemReader<DatasetVO> mysqlReader = new JdbcCursorItemReader<DatasetVO>();
		mysqlReader.setDataSource(siriusDataSource());
		mysqlReader.setSql(sql);
		mysqlReader.setRowMapper(new MysqlRowMapper());
		
		return mysqlReader;
	}*/
	
	/*public class MysqlRowMapper implements RowMapper<DatasetVO>{

		public DatasetVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DatasetVO dataset = new DatasetVO();
			dataset.setObjectId(rs.getInt("A.object_id"));
			dataset.setObjectName(rs.getString("A.object_name"));
			dataset.setPcServiceYn(rs.getString("A.service_status"));
			dataset.setMobileServiceYn(rs.getString("A.mobile_service_status"));
			dataset.setPerson(rs.getString("B.person_in_charge"));
			dataset.setCategoryId(rs.getLong("B.category_id"));
			dataset.setCategoryName(rs.getString("category_name"));
			return dataset;
		}
	}*/

	
}
