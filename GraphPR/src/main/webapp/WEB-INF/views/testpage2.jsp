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
		googleChart('PC or Mobile',${datas},'chart_div');
		googleChart('PC',${pcDatas},'pcChart_div');
		googleChart('Mobile',${mobileDatas},'mobileChart_div');
	}

	google.charts.load('current', {
		packages : [ 'corechart', 'line' ]
	});

	function selectPerson(person, div){
		console.log(person);
		var selected = new Object();
		selected.person = person;
		var jsonData = JSON.stringify(selected)
		$.ajax({
			type : 'GET',
			url :'/srObject/personData',
			contentType : 'application/json;charset=UTF-8',
			data : {'jsonData':jsonData},
			dataType:'json',
			error : function(response){alert(response);},
			success : function(response){
				console.log(typeof response);
				console.log(response);
				googleChart(person,response,div);
			}
		});
	}
	function googleChart(person,response,div){
		
		google.charts.setOnLoadCallback(drawBasic(person,response,div));
		
		function drawBasic(person,response,div){
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'X');
			data.addColumn('number', person);
			data.addRows(response);
			
			var options = {
					
				hAxis : {
					title : 'Time'
				},
				vAxis : {
					title : 'Number', format : '0'
				},
				pointSize: 5,
				width:500,
				height:300
			};
			var chart = new google.visualization.LineChart(document
				.getElementById(div));
			chart.draw(data,options);
		}
	}
	
</script>
</head>
<body>

	<%
		List<List<Object>> persons = (List<List<Object>>) request.getAttribute("persons");
	%>
	<table>
		<tr>
			<td>
				<div id="chart_div"></div>
			</td>
			<td>
				<div id="pcChart_div"></div>
			</td>
			<td>
				<div id="mobileChart_div"></div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="empty0_div"></div> <select id="select0" class="select"
				size="1" onchange="selectPerson(this.value,'empty0_div')"
				style="float: left">
					<option>person</option>
					<%
						for (List<Object> list : persons) {
					%>
					<option value=<%=list.get(1)%>><%=list.get(1)%></option>
					<%
						}
					%>
			</select>
			</td>
			<td><div id="empty1_div"></div> <select id="select1"
				class="select" size="1"
				onchange="selectPerson(this.value,'empty1_div')">
					<option>person</option>
					<%
						for (List<Object> list : persons) {
					%>
					<option value=<%=list.get(1)%>><%=list.get(1)%></option>
					<%
						}
					%>
			</select></td>
			<td><div id="empty2_div"></div> <select id="select2"
				class="select" size="1"
				onchange="selectPerson(this.value,'empty2_div')">
					<option>person</option>
					<%
						for (List<Object> list : persons) {
					%>
					<option value=<%=list.get(1)%>><%=list.get(1)%></option>
					<%
						}
					%>
			</select></td>
		</tr>
	</table>

	${datas}
</body>
</html>