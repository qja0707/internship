package batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import job.step.InfluxdbItemWriter;
import job.step.MysqlReader;
import vo.DatasetVO;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private final String IP_ADDR = "10.110.248.58";		//intern server
	private final String PORT = "13306";
	private final String DATABASE = "dbForBatch";
	
	private final String USER = "root";
	private final String PASSWORD = "!@#123";

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
		//dataSource.setUrl("jdbc:mysql://10.64.65.102:3306/test01?useSSL=false");		//laptop
		dataSource.setUrl("jdbc:mysql://"+IP_ADDR+":"+PORT+"/"+DATABASE+"");		
		dataSource.setUsername(USER);
		dataSource.setPassword(PASSWORD);
		
		return dataSource;
	}
	
	@Bean
	public Step mysqlToInfluxDB() {
		return stepBuilderFactory.get("mysql->influxDB").<DatasetVO,DatasetVO> chunk(1000)
				.reader(new MysqlReader())
				.writer(new InfluxdbItemWriter())
				.build();
	}
	
	@Bean
	public Job influxdbJob() {
		return jobBuilderFactory.get("influxdbJob")
				.incrementer(new RunIdIncrementer())
				.flow(mysqlToInfluxDB())
				.end()
				.build();
	}
}
