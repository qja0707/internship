package com.timegraph.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.timegraph.bo.GraphPrService;
import com.timegraph.dto.InfluxdbDTO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private final String pcServiceYn = "pcServiceYn";
	private final String mobileServiceYn = "mobileServiceYn";
	private final String person = "person";
	private final String categoryName = "categoryName";

	GraphPrService graphPrService = new GraphPrService();
	
	@RequestMapping(value = "/srObject", method = RequestMethod.GET)
	public String home(Locale locale, HttpServletRequest request) throws Exception {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		InfluxdbDTO pcOrMobileDatas = new InfluxdbDTO();
		
		Gson gson = new Gson();
		pcOrMobileDatas.setPerson("total");
		pcOrMobileDatas.setCategoryName("total");
		pcOrMobileDatas.setField(pcServiceYn);
		pcOrMobileDatas.setField2(mobileServiceYn);
		pcOrMobileDatas = graphPrService.getDatas(pcOrMobileDatas);
		
		System.out.println(pcOrMobileDatas.getDatas());
		request.setAttribute("datas", gson.toJson(pcOrMobileDatas.getDatas()));
		
		// for select box in web page
		InfluxdbDTO persons = new InfluxdbDTO();
		InfluxdbDTO categories = new InfluxdbDTO();
		
		persons.setTag(person);
		categories.setTag(categoryName);
		persons = graphPrService.getTagValues(persons);
		categories = graphPrService.getTagValues(categories);

		System.out.println(persons.getDatas());
		JSONParser parser = new JSONParser();
		JSONArray jsonPersons = (JSONArray) parser.parse(gson.toJson(persons.getDatas()));
		JSONArray jsonCategories = (JSONArray) parser.parse(gson.toJson(categories.getDatas()));
		
		request.setAttribute("persons",jsonPersons);
		request.setAttribute("categories",jsonCategories);
		
		return "srObjectView";
		
	}
	
	@RequestMapping(value = "/srObject/selectedData",method = RequestMethod.GET)		//콤보박스의 선택된 값을 받아 데이터 select
	public String personData(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("jsonData"));
		Gson gson = new Gson();
		JSONParser parser = new JSONParser();
		JSONObject jsonData = (JSONObject) parser.parse(request.getParameter("jsonData"));

		InfluxdbDTO dto = new InfluxdbDTO();
		
		if(jsonData.get("service").equals("PC or Mobile")) {
			dto.setField(pcServiceYn);
			dto.setField2(mobileServiceYn);
		}else {
			dto.setField((String) jsonData.get("service"));
		}
		dto.setPerson((String) jsonData.get("person"));
		dto.setCategoryName((String) jsonData.get("category"));
		
		dto = graphPrService.getDatas(dto);
		
		JSONArray jsonObj = (JSONArray) parser.parse(gson.toJson(dto.getDatas()));
		System.out.println(jsonObj);
		request.setAttribute("selectedData",jsonObj);
		
		return "selectedData";
	}
	
	@RequestMapping(value = "/srObject/optionData",method = RequestMethod.GET)		//콤보박스의 선택된 값을 받아 해당 값에 유효한 tag value 가져옴
	public String optionData(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("jsonData"));
		Gson gson = new Gson();
		JSONParser parser = new JSONParser();
		JSONObject jsonData = (JSONObject) parser.parse(request.getParameter("jsonData"));

		InfluxdbDTO dto = new InfluxdbDTO();
		
		if(jsonData.get("person")==null) {
			dto.setCategoryName((String) jsonData.get("category"));
			dto.setTag(person);
		}else {
			dto.setPerson((String) jsonData.get("person"));
			dto.setTag(categoryName);
		}
		
		dto = graphPrService.getTagValues(dto);
		
		JSONArray jsonObj = (JSONArray) parser.parse(gson.toJson(dto.getDatas()));
		System.out.println(jsonObj);
		request.setAttribute("optionData",jsonObj);
		
		return "optionData";
	}

}
