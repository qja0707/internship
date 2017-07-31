package com.timegraph.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
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

	private final String pcOrMobile = "select count(pcServiceYn) from "+MEASUREMENT+" where time<now() + 9h and (pcServiceYn = 'Y' or mobileServiceYn = 'Y') group by time(1d)";
	private final String mobileService = "select count(mobileServiceYn) from "+MEASUREMENT+" where time<now() + 9h and (mobileServiceYn = 'Y') group by time(1d)";
	private final String pcService = "select count(pcServiceYn) from "+MEASUREMENT+" where time<now() + 9h and (pcServiceYn = 'Y') group by time(1d)";
	

	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		try {
			InfluxDBConn influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD);
			InfluxdbCountQuery influxQuery = new InfluxdbCountQuery(influxDBConn.setUp());

			List<List<Object>> pcOrMobileDatas = influxQuery.select(pcOrMobile);
			List<List<Object>> mobileDatas = influxQuery.select(mobileService);
			List<List<Object>> pcDatas = influxQuery.select(pcService);

			ProcessForGooglechart change = new ProcessForGooglechart();
			pcOrMobileDatas = change.dateToString(pcOrMobileDatas);
			mobileDatas = change.dateToString(mobileDatas);
			pcDatas = change.dateToString(pcDatas);

			model.addAttribute("datas", pcOrMobileDatas);
			model.addAttribute("mobileDatas", mobileDatas);
			model.addAttribute("pcDatas",pcDatas);
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "testpage";
	}
	
	@RequestMapping(value = "/test2",method = RequestMethod.GET)
	public String testpage2(Model model) {
		
		try {
			InfluxDB influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD).setUp();
			InfluxdbCountQuery influxQuery = new InfluxdbCountQuery(influxDBConn);

			List<List<Object>> pcOrMobileDatas = influxQuery.select(pcOrMobile);
			List<List<Object>> mobileDatas = influxQuery.select(mobileService);
			List<List<Object>> pcDatas = influxQuery.select(pcService);

			ProcessForGooglechart change = new ProcessForGooglechart();
			pcOrMobileDatas = change.dateToString(pcOrMobileDatas);
			mobileDatas = change.dateToString(mobileDatas);
			pcDatas = change.dateToString(pcDatas);

			model.addAttribute("datas", pcOrMobileDatas);
			model.addAttribute("mobileDatas", mobileDatas);
			model.addAttribute("pcDatas",pcDatas);
			
			InfluxdbTagKeys tagKeyQuery = new InfluxdbTagKeys(influxDBConn);
			List<List<Object>> persons = tagKeyQuery.tagKeys();
			model.addAttribute("persons",persons);
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "testpage2";
	}
	@RequestMapping(value = "/personData.do",method = RequestMethod.GET)
	public String personData(HttpServletRequest request) {
		String person = request.getParameter("person");
		String countByPerson = "select count(pcServiceYn) from "+MEASUREMENT+" where time<now() + 9h and (\"person\" = '"+person+"') group by time(1d)";
		
		try {
			InfluxDBConn influxDBConn = new InfluxDBConn(IP_ADDR, PORT, DATABASE, USER, PASSWORD);
			InfluxdbCountQuery influxQuery = new InfluxdbCountQuery(influxDBConn.setUp());

			List<List<Object>> countByPersonData = influxQuery.select(countByPerson);
			
			ProcessForGooglechart change = new ProcessForGooglechart();
			countByPersonData = change.dateToString(countByPersonData);
			System.out.println(countByPersonData);
			request.setAttribute("countByPersonData",countByPersonData);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "personData";
	}

}
