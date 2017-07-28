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
	google.charts.setOnLoadCallback(drawBasic);
	
	 function drawBasic() {

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'X');
		data.addColumn('number', 'PC or Mobile');
		data.addRows(${datas});
		
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
			/* width:800, */
			height:200
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
	<div id="curve_chart" style="width: 30; height: 500px"></div>
	${datas}
</body>
</html>