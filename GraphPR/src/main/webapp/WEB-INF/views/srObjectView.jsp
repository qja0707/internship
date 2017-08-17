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
		googleChart(${datas},'chart_div');
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
			url :'/srObject/selectedData',
			contentType : 'application/json;charset=UTF-8',
			data : {'jsonData':jsonData},
			dataType:'json',
			error : function(response){alert("No Data");},
			success : function(response){
				if(response==null){
					alert("No Data");
				}
				console.log(typeof response);
				console.log(response);
				googleChart(response,div);
			}
		});
	}
	function googleChart(response,div){
		console.log(typeof response);
		var maxNum=0;
		var minNum=10000;
		for(var i in response){				//그래프 y축의 최댓값과 최솟값을 지정할 때 사용
			if(maxNum<response[i][1]){
				maxNum=response[i][1];
			}
			if(minNum>response[i][1]){
				minNum=response[i][1];
			}
		}
		console.log(maxNum);
		
		google.charts.setOnLoadCallback(drawBasic(response,div));
		
		function drawBasic(response,div){
			console.log(response);
			var data = new google.visualization.arrayToDataTable(response,false);
			
			var options = {
					
				hAxis : {
					title : 'Time'
				},
				vAxis : {
					title : 'Number of srObject', 
					format : '0',
					//maxValue: maxNum + maxNum/10,
				},
				pointSize: 5,
				width:'100%',
				height:600,
			};
			var chart = new google.visualization.AreaChart(document
				.getElementById(div));
			chart.draw(data,options);
		}
	}
	
	function changeOption(caller, callee){		//콤보 박스 간 연동을 위한 함수
		var preSelected = callee.value;
		var selected = new Object();
		if(caller.getAttribute('id')=='person'){
			selected.person = caller.value;
		}
		else if(caller.getAttribute('id')=='category'){
			selected.category = caller.value;
		} 
		
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
				
				$(callee).find("option").remove();
				$(callee).append("<option value='total'>"+callee.getAttribute('id')+" : total</option>");
				
				for(i in response){
					$(callee).append("<option value='"+response[i][1]+"'>"+response[i][1]+"</option>");
					console.log(response[i][1]);
				}
				$(callee).val(preSelected).attr("selected","selected");
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
	<select id="person" class="select"
		size="1" onchange="selectOption(person.value,
										category.value,
										service.value,
										'chart_div'),
							changeOption(person,category)">
		<option value=total>person : total</option>
		<%
			for (List<Object> list : persons) {
		%>
		<option value=<%=list.get(1)%>><%=list.get(1)%></option>
		<%
			}
		%>
	</select>
	<select id="category" size="1" onchange="selectOption(person.value,
												category.value,
												service.value,
												'chart_div'),
											changeOption(category,person)">
		<option value=total>category : total</option>
		<%
			for (List<Object> list : categories) {
		%>
		<option value=<%=list.get(1)%>><%=list.get(1)%></option>
		<%
			}
		%>
	</select>
	<select id="service" size = "1" onchange="selectOption(person.value,
												category.value,
												service.value,
												'chart_div')">
		<option value="PC or Mobile">PC or Mobile</option>
		<option value=pcServiceYn>PC</option>
		<option value=mobileServiceYn>Mobile</option>
	</select>
</body>
</html>