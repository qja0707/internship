package job.step;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import vo.DatasetVO;

public class MysqlReader implements ItemStreamReader<DatasetVO>{
	private final String READ = "Read----------";
	
	private final String URL = "capp23.ext.nhncorp.com";
	private final String PORT = "3326";
	private final String DATABASE = "sius";
	
	private final String USER = "sius";
	private final String PASSWORD = "sius";
	
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
			//"			AND A.startdate <= (now() - interval 8 day) AND A.enddate >=(now() - interval 8 day)";					특정 날짜의 데이터를 넣을 때
	
	Connection conn; 
	Statement stmt;
	ResultSet rs;
	int num=0;
	
	@Override
	public DatasetVO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		DatasetVO dataset=null;
		if(rs.next()) {
			dataset = new DatasetVO();
			dataset.setObjectId(rs.getInt("A.object_id"));
			dataset.setObjectName(rs.getString("A.object_name"));
			dataset.setPcServiceYn(rs.getString("A.service_status"));
			dataset.setMobileServiceYn(rs.getString("A.mobile_service_status"));
			dataset.setPerson(rs.getString("B.person_in_charge"));
			dataset.setCategoryId(rs.getLong("B.category_id"));
			dataset.setCategoryName(rs.getString("category_name"));
			num++;
		}
		System.out.println(READ+num+" is read");
		
		return dataset;
	}
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		System.out.println("DB = "+"jdbc:mysql://"+URL+":"+PORT+"/"+DATABASE);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://"+URL+":"+PORT+"/"+DATABASE,USER,PASSWORD);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(READ+"error is occured while reading MySQL");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(READ+"com.mysql.jdbc.Driver is not founded");
			e.printStackTrace();
		}
		System.out.println(READ+"Connected to MySQL");
	}
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
	}
	@Override
	public void close() throws ItemStreamException {
		try {
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
			System.out.println(READ+"Error is occured while closing MySQL");
			e.printStackTrace();
		}
	}
}
