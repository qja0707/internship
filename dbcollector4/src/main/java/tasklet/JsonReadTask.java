package tasklet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import vo.DatasetVO;

@Component
public class JsonReadTask implements Tasklet {
	
	private final static String FILE_PATH = "C:\\Users\\NAVER\\Documents\\cpeditorStatistics2017-07-14.json";
	
	DatasetVO dataset;

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		JSONParser parser = new JSONParser();
				

		try {

			Object obj = parser.parse(new FileReader(FILE_PATH));

			JSONObject jsonObject = (JSONObject) obj;

			String date = (String) jsonObject.get("date");
			System.out.println(date);

			JSONArray items = (JSONArray) jsonObject.get("items");
			JSONObject innerObject;
			
			
			ArrayList<DatasetVO> dataArray = new ArrayList<DatasetVO>(items.size());

			for (Object object : items) {
				dataset = new DatasetVO();
				innerObject = (JSONObject) object;
				dataset.setCategoryType((String) innerObject.get("categoryType"));
				dataset.setPcServiceYn((String) innerObject.get("pcServiceYn"));
				dataset.setPerson((String) innerObject.get("person"));
				dataset.setMobileServiceYn((String) innerObject.get("mobileServiceYn"));
				dataset.setObjectName((String) innerObject.get("objectName"));
				dataset.setCategoryName((String) innerObject.get("categoryName"));
				dataset.setObjectId((Long) innerObject.get("objectId"));
				dataset.setCategoryId((Long) innerObject.get("categoryId"));

				dataArray.add(dataset);				
			}	
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("DATE", date);
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("DATA_ARRAY", dataArray);
			
			
			System.out.println(items.size()+" is read");
			System.out.println("Done");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
