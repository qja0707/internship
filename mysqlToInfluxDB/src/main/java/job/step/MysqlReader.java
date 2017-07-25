package job.step;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import vo.DatasetVO;

public class MysqlReader implements ItemReader<DatasetVO>{
	private final String READ = "Read----------";
	
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
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	int num;
	
	public MysqlReader() {
		num=0;
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
	public DatasetVO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		while(rs.next()) {
			DatasetVO dataset = new DatasetVO();
			dataset.setObjectId(rs.getInt("A.object_id"));
			dataset.setObjectName(rs.getString("A.object_name"));
			dataset.setPcServiceYn(rs.getString("A.service_status"));
			dataset.setMobileServiceYn(rs.getString("A.mobile_service_status"));
			dataset.setPerson(rs.getString("B.person_in_charge"));
			dataset.setCategoryId(rs.getLong("B.category_id"));
			dataset.setCategoryName(rs.getString("category_name"));
			num++;
			return dataset;
		}
		System.out.println(READ+num+" is read");
		
		return null;
	}
	public void finalize() {
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
