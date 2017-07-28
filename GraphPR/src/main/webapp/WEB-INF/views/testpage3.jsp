<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<div id="chart_div" style="float:left;width:33%"></div>
<div id="mobileChart_div"style="float:left;width:33%"></div>
<div id="pcChart_div"style="float:left;width:33%"></div>
<div id="empty0_div"style="float:left;width:33%"></div>
<div id="empty1_div"style="float:left;width:33%"></div>
<div id="empty2_div"style="float:left;width:33%"></div>

<script type="text/javascript">
	google.charts.load('current', {
		packages : [ 'corechart', 'line' ]
	});
	google.charts.setOnLoadCallback(drawEmpty);
	
	function drawEmpty(){
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'X');
		data.addColumn('number', ' ');
		
		var options = {
				
			hAxis : {
				title : 'Time'
			},
			vAxis : {
				title : 'Number'
			},
			pointSize: 5,
			height:200
		};
		var chart = new google.visualization.LineChart(document
			.getElementById('empty0_div'));
		chart.draw(data,options);
	}
</script>
</head>
<body>
	<div id="curve_chart" style="width: 30; height: 500px"></div>
	
</body>
</html>