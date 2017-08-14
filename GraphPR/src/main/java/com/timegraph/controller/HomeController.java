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
		
		pcOrMobileDatas.setField(pcServiceYn);
		pcOrMobileDatas.setField2(mobileServiceYn);
		pcOrMobileDatas = graphPrService.getDatas(pcOrMobileDatas);
		
		request.setAttribute("datas", pcOrMobileDatas.getDatas());
		
		// for select box in web page
		InfluxdbDTO persons = new InfluxdbDTO();
		InfluxdbDTO categories = new InfluxdbDTO();
		
		persons.setTag(person);
		categories.setTag(categoryName);
		persons = graphPrService.getTagValues(persons);
		categories = graphPrService.getTagValues(categories);

		System.out.println(persons.getDatas());
		JSONParser parser = new JSONParser();
		JSONArray jsonPersons = (JSONArray) parser.parse(String.valueOf(persons.getDatas()));
		JSONArray jsonCategories = (JSONArray) parser.parse(String.valueOf(categories.getDatas()));
		
		request.setAttribute("persons",jsonPersons);
		request.setAttribute("categories",jsonCategories);
		
		return "srObjectView";
		
	}
	
	@RequestMapping(value = "/srObject/personData",method = RequestMethod.GET)
	public String personData(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("jsonData"));
		
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
		
		JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(dto.getDatas()));
		System.out.println(jsonObj);
		request.setAttribute("countedData",jsonObj);
		
		return "personData";
	}
	
	@RequestMapping(value = "/srObject/optionData",method = RequestMethod.GET)
	public String optionData(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("jsonData"));
		
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
		
		JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(dto.getDatas()));
		System.out.println(jsonObj);
		request.setAttribute("optionData",dto.getDatas());
		
		return "optionData";
	}

}
