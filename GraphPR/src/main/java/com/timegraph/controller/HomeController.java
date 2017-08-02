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
import influxDB.InfluxdbTagKeys;


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
			
			InfluxdbTagKeys tagKeyQuery = new InfluxdbTagKeys(influxDBConn);
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
		String person=(String) jsonData.get("person");
		
		try {
			influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
			influxQuery = new InfluxdbCountQuery(influxDBConn);

			influxQuery.selectPerson(pcServiceYn, person);
			List<List<Object>> countByPersonData = influxQuery.execute();
			ProcessForGooglechart change = new ProcessForGooglechart();
			countByPersonData = change.dateToString(countByPersonData);
			System.out.println(countByPersonData);
			
			JSONArray jsonObj = (JSONArray) parser.parse(String.valueOf(countByPersonData));
			System.out.println(jsonObj);
			request.setAttribute("countByPersonData",jsonObj);
			
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
