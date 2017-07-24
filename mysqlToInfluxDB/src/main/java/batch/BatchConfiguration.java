package batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import vo.DatasetVO;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://10.64.65.102:3306/test01?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("!@#123");
		
		
		return dataSource;
	}
	
	public DataSource siriusDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://capp23.ext.nhncorp.com:33326/sirius");
		dataSource.setUsername("sirius");
		dataSource.setPassword("sirius");
		
		return dataSource;
	}
	
	@Bean
	public JdbcCursorItemReader<DatasetVO> mysqlReader(){
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
	}
	
	public class MysqlRowMapper implements RowMapper<DatasetVO>{

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
	}
	@Bean
	public Step mysqlToInfluxDB() {
		return stepBuilderFactory.get("step1").<DatasetVO,DatasetVO> chunk(100)
				.reader(mysqlReader())
				.writer(new InfluxdbItemWriter())
				.build();
	}
	
	
	@Bean
	public Job exportUserJob() {
		return jobBuilderFactory.get("exportUserJob")
				.incrementer(new RunIdIncrementer())
				.flow(mysqlToInfluxDB())
				.end()
				.build();
	}
}
