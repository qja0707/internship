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
import org.json.simple.parser.ParseException;

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
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		InfluxdbDTO pcOrMobileDatas = new InfluxdbDTO();
		
		pcOrMobileDatas.setField(pcServiceYn);
		pcOrMobileDatas.setField2(mobileServiceYn);
		pcOrMobileDatas = graphPrService.read(pcOrMobileDatas);
		
		model.addAttribute("datas", pcOrMobileDatas.getDatas());
		
		InfluxdbDTO persons = new InfluxdbDTO();
		InfluxdbDTO categories = new InfluxdbDTO();
		
		persons.setTag(person);
		categories.setTag(categoryName);
		
		persons = graphPrService.getTagValues(persons);
		categories = graphPrService.getTagValues(categories);

		model.addAttribute("persons",persons.getDatas());
		model.addAttribute("categories",categories.getDatas());
		
		return "testpage2";
		
	}
	
	@RequestMapping(value = "/srObject/personData",method = RequestMethod.GET)
	public String personData(HttpServletRequest request) throws ParseException {
		
		System.out.println(request.getParameter("jsonData"));
		
		JSONParser parser = new JSONParser();
		JSONObject jsonData = (JSONObject) parser.parse(request.getParameter("jsonData"));

		InfluxdbDTO dto = new InfluxdbDTO();
		
		dto.setField(pcServiceYn);
		dto.setPerson((String) jsonData.get("person"));
		dto.setCategoryName((String) jsonData.get("category"));
		
		dto = graphPrService.read(dto);
		
		JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(dto.getDatas()));
		System.out.println(jsonObj);
		request.setAttribute("countedData",jsonObj);
		
		return "personData";
	}

}
