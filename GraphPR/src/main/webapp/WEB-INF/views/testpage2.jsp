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
	function selectPerson(person, div){
		console.log(person);
		var datas;
		$.ajax({
			type : "GET",
			url :"personData.do",
			data : {"person":person},
			//dataType : "json",
			error : function(){alert('faild');},
			success : function(response){
				//var test = JSON.stringify(response);
				console.log(typeof response);
				console.log(response);
				response = eval(response);
				console.log(typeof response);
				gc(person,response,div);
				/* alert(response);
				google.charts.load('current', {
					packages : [ 'corechart', 'line' ]
				});
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
							title : 'Number'
						},
						pointSize: 5,
						width:500,
						height:300
					};
					var chart = new google.visualization.LineChart(document
						.getElementById(div));
					chart.draw(data,options);
				} */
			}
		});
		
		function gc(person,response,div){
			console.log(response);
			google.charts.load('current', {
				packages : [ 'corechart', 'line' ]
			});
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
						title : 'Number'
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
		
		/* google.charts.load('current', {
			packages : [ 'corechart', 'line' ]
		});
		google.charts.setOnLoadCallback(drawBasic(person,div));
		
		function drawBasic(person,div){
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'X');
			data.addColumn('number', person);
			//data.addRows(datas);
			
			var options = {
					
				hAxis : {
					title : 'Time'
				},
				vAxis : {
					title : 'Number'
				},
				pointSize: 5,
				width:500,
				height:300
			};
			var chart = new google.visualization.LineChart(document
				.getElementById(div));
			chart.draw(data,options);
		} */
	}
	
</script>

<script type="text/javascript">
	google.charts.load('current', {
		packages : [ 'corechart', 'line' ]
	});
	google.charts.setOnLoadCallback(drawBasic);
	
	 function drawBasic() {

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'X');
		data.addColumn('number', 'PC or Mobile');
		data.addRows(${datas});
		
		console.log(typeof ${datas})
		
		var mobileData = new google.visualization.DataTable();
		mobileData.addColumn('string', 'X');
		mobileData.addColumn('number', 'Mobile');
		mobileData.addRows(${mobileDatas});
		
		var pcData = new google.visualization.DataTable();
		pcData.addColumn('string', 'X');
		pcData.addColumn('number', 'PC');
		pcData.addRows(${pcDatas});
			
		var options = {
				
			hAxis : {
				title : 'Time'
			},
			vAxis : {
				title : 'Number'
			},
			pointSize: 5,
			width:500,
			height:300
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('chart_div'));
		var mobileChart = new google.visualization.LineChart(document
				.getElementById('mobileChart_div'));
		var pcChart = new google.visualization.LineChart(document
				.getElementById('pcChart_div'));

		chart.draw(data, options);
		mobileChart.draw(mobileData,options);
		pcChart.draw(pcData,options);
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