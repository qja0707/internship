<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>

<script type="text/javascript">

	window.onload=function(){
		googleChart('total','total','PC or Mobile',${datas},'chart_div');
	}

	google.charts.load('current', {
		packages : [ 'corechart' ]
	});

	function selectOption(person,category,service,div){
		console.log(person);
		console.log(category);
		console.log(service);
		
		var selected = new Object();
		selected.person = person;
		selected.category = category;
		selected.service = service;
		
		var jsonData = JSON.stringify(selected)
		$.ajax({
			type : 'GET',
			url :'/srObject/personData',
			contentType : 'application/json;charset=UTF-8',
			data : {'jsonData':jsonData},
			dataType:'json',
			error : function(response){alert("No Data");},
			success : function(response){
				console.log(typeof response);
				console.log(response);
				googleChart(person,category,service,response,div);
			}
		});
	}
	function googleChart(person,category,service,response,div){
		
		google.charts.setOnLoadCallback(drawBasic(person,category,service,response,div));
		
		function drawBasic(person,category,service,response,div){
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Time');
			data.addColumn('number', person+"\n"+category+"\n"+service);
			data.addRows(response);
			
			var options = {
					
				hAxis : {
					title : 'Time'
				},
				vAxis : {
					title : 'Number of srObject', format : '0'
				},
				pointSize: 5,
				width:'100%',
				height:600
			};
			var chart = new google.visualization.ColumnChart(document
				.getElementById(div));
			chart.draw(data,options);
		}
	}
	
	
	//not used
	function changeCategory(person){
		console.log("this is the test of new function");
		var selected = new Object();
		selected.person = person;
		
		var jsonData = JSON.stringify(selected)
		$.ajax({
			type : 'GET',
			url :'/srObject/optionData',
			contentType : 'application/json;charset=UTF-8',
			data : {'jsonData':jsonData},
			dataType:'json',
			error : function(response){alert("No Data");},
			success : function(response){
				console.log(typeof response);
				console.log(response);
				
			}
		});
	}
	
</script>
</head>
<body>
	<%
		List<List<Object>> persons = (List<List<Object>>) request.getAttribute("persons");
		List<List<Object>> categories = (List<List<Object>>) request.getAttribute("categories");
	%>
	
	<div id = "chart_div"></div>
	<select id="person0" class="select"
		size="1" onchange="selectOption(person0.value,
										category0.value,
										service.value,
										'chart_div')">
		<option value=total>person : total</option>
		<%
			for (List<Object> list : persons) {
		%>
		<option value=<%=list.get(1)%>><%=list.get(1)%></option>
		<%
			}
		%>
	</select>
	<select id="category0" size="1" onchange="selectOption(person0.value,
												category0.value,
												service.value,
												'chart_div')">
		<option value=total>category : total</option>
		<%
			for (List<Object> list : categories) {
		%>
		<option value=<%=list.get(1)%>><%=list.get(1)%></option>
		<%
			}
		%>
	</select>
	<select id="service" size = "1" onchange="selectOption(person0.value,
												category0.value,
												service.value,
												'chart_div')">
		<option value="PC or Mobile">PC or Mobile</option>
		<option value=pcServiceYn>PC</option>
		<option value=mobileServiceYn>Mobile</option>
	</select>
</body>
</html>