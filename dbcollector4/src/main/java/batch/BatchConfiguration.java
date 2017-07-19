package batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tasklet.JsonReadTask;
import tasklet.InfluxDBWriteTask;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JsonReadTask jsonReadTask;
	
	@Autowired
	public InfluxDBWriteTask influxDBWriteTask;
	
	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
		executionContextPromotionListener.setKeys(new String[] {"DATE","DATA_ARRAY"});
		
		return executionContextPromotionListener;
	}
	
	@Bean
	public Job readJsonWriteInfluxdb(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("read json file and write to influxDB")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(jsonReadStep())
				.next(influxDBWriteStep())
				.end()
				.build();
	}

	@Bean
	public Step jsonReadStep() {
		return stepBuilderFactory.get("jsonReadStep")
				.tasklet(jsonReadTask)
				.listener(promotionListener())
				.build();
	}
	
	@Bean
	public Step influxDBWriteStep() {
		return stepBuilderFactory.get("influxDBWriteStep")
				.tasklet(influxDBWriteTask)
				.listener(promotionListener())
				.build();
	}
	// end::jobstep[]
}