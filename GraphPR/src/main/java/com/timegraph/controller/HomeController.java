package com.timegraph.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.influxdb.InfluxDB;
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

import com.timegraph.dataProcess.ProcessForGooglechart;

import influxDB.InfluxDBConn;
import influxDB.InfluxdbCountQuery;
import influxDB.InfluxdbTagQuery;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private final String IP_ADDR = "10.110.248.58";
	private final String PORT = "8086";
	private final String USER = "root";
	private final String PASSWORD = "root";

	public static final String DATABASE = "Statistics";
	public static final String MEASUREMENT = "test16";
	
	private final String pcServiceYn = "pcServiceYn";
	private final String mobileServiceYn = "mobileServiceYn";
	private final String person = "person";
	private final String categoryName = "categoryName";
	
	private final String stringNull = "null";

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	InfluxDB influxDBConn;
	InfluxdbCountQuery influxQuery;
	
	@RequestMapping(value = "/srObject", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		try {
			influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
			influxQuery = new InfluxdbCountQuery(influxDBConn);

			influxQuery.select(pcServiceYn,mobileServiceYn);
			List<List<Object>> pcOrMobileDatas = influxQuery.execute();
			
			influxQuery.select(pcServiceYn);
			List<List<Object>> pcDatas = influxQuery.execute();
					
			influxQuery.select(mobileServiceYn);
			List<List<Object>> mobileDatas = influxQuery.execute();

			ProcessForGooglechart change = new ProcessForGooglechart();
			pcOrMobileDatas = change.dateToString(pcOrMobileDatas);
			mobileDatas = change.dateToString(mobileDatas);
			pcDatas = change.dateToString(pcDatas);

			model.addAttribute("datas", pcOrMobileDatas);
			model.addAttribute("mobileDatas", mobileDatas);
			model.addAttribute("pcDatas",pcDatas);
			
			InfluxdbTagQuery tagKeyQuery = new InfluxdbTagQuery(influxDBConn);
			List<List<Object>> persons = tagKeyQuery.tagKeys(person);
			List<List<Object>> categories = tagKeyQuery.tagKeys(categoryName);
			model.addAttribute("persons",persons);
			model.addAttribute("categories",categories);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			influxQuery=null;
			influxDBConn=null;
		}
		return "testpage2";
		
	}
	
	@RequestMapping(value = "/srObject/personData",method = RequestMethod.GET)
	public String personData(HttpServletRequest request) throws ParseException {
		
		System.out.println(request.getParameter("jsonData"));
		
		JSONParser parser = new JSONParser();
		JSONObject jsonData = (JSONObject) parser.parse(request.getParameter("jsonData"));
		String selectedPerson=(String) jsonData.get("person");
		String selectedCategory = (String) jsonData.get("category");
		
		try {
			influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
			influxQuery = new InfluxdbCountQuery(influxDBConn);

			if(!selectedPerson.equals(stringNull) && !selectedCategory.equals(stringNull)) {
				influxQuery.selectPersonWithCategory(pcServiceYn, selectedPerson, selectedCategory);
			}else if(!selectedPerson.equals(stringNull)) {
				influxQuery.selectPerson(pcServiceYn, selectedPerson);
			}else if(!selectedCategory.equals(stringNull)) {
				influxQuery.selectCategory(pcServiceYn, selectedCategory);
			}
			List<List<Object>> countedData = influxQuery.execute();
			ProcessForGooglechart change = new ProcessForGooglechart();
			countedData = change.dateToString(countedData);
			System.out.println(countedData);
			
			JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(countedData));
			System.out.println(jsonObj);
			request.setAttribute("countedData",jsonObj);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			influxQuery=null;
			influxDBConn=null;
		}
		
		return "personData";
	}

}
