package com.timegraph.controller;

import java.util.List;
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

import com.timegraph.dao.InfluxdbDAO;

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
	
	private final String stringNull = "null";

	InfluxdbDAO influxdbDAO = new InfluxdbDAO();
	
	@RequestMapping(value = "/srObject", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		List<List<Object>> pcOrMobileDatas = influxdbDAO.readDatas(pcServiceYn,mobileServiceYn);
		List<List<Object>> mobileDatas = influxdbDAO.readDatas(mobileServiceYn);
		List<List<Object>> pcDatas = influxdbDAO.readDatas(pcServiceYn);
		model.addAttribute("datas", pcOrMobileDatas);
		model.addAttribute("mobileDatas", mobileDatas);
		model.addAttribute("pcDatas",pcDatas);
		
		List<List<Object>> persons = influxdbDAO.getTagValues(person);
		List<List<Object>> categories = influxdbDAO.getTagValues(categoryName);
		model.addAttribute("persons",persons);
		model.addAttribute("categories",categories);
		
		return "testpage2";
		
	}
	
	@RequestMapping(value = "/srObject/personData",method = RequestMethod.GET)
	public String personData(HttpServletRequest request) throws ParseException {
		
		System.out.println(request.getParameter("jsonData"));
		
		JSONParser parser = new JSONParser();
		JSONObject jsonData = (JSONObject) parser.parse(request.getParameter("jsonData"));
		String selectedPerson=(String) jsonData.get("person");
		String selectedCategory = (String) jsonData.get("category");
		
		List<List<Object>> countedData;
		
		if(!selectedPerson.equals(stringNull) && !selectedCategory.equals(stringNull)) {
			countedData = influxdbDAO.readDatasPerPersonAndCategory(pcServiceYn, selectedPerson, selectedCategory);
		}else if(!selectedPerson.equals(stringNull)) {
			countedData = influxdbDAO.readDatasPerPerson(pcServiceYn, selectedPerson);
		}else if(!selectedCategory.equals(stringNull)) {
			countedData = influxdbDAO.readDatasPerCategory(pcServiceYn, selectedCategory);
		}else {
			countedData=null;
		}
		JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(countedData));
		System.out.println(jsonObj);
		request.setAttribute("countedData",jsonObj);
		
		return "personData";
	}

}
