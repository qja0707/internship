<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<select id="selectHours" size = "1" onChange = "setValues();">
	<option value="1">test_1</option>
	<option value="2">test_2</option>
</select>

<input type="text" id="textTime">



<script type="text/javascript">
	function setValues(){
		var sh = document.getElementById("selectHours");
		var tt = document.getElementById("textTime");
		//tt.text = sh.selectIndex.text;
		console.log(sh.options[sh.selectedIndex].value);
		
		tt.text = sh.options[sh.selectedIndex].value;
	}
	
	
</script>
</body>
</html>