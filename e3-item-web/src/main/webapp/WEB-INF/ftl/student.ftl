<html>


<head>
	<title>自我介绍</title>
</head>

<body>
	学生信息<br>
	你好，我是${student.name},我${student.age}岁了，学号是${student.id}
	
	学生列表<br>
	<table border=1>
		<tr>
			<td>序号</td>
			<td>学号</td>
			<td>姓名</td>
			<td>年龄</td>
		</tr>
		<#list stuList as student>
			<#if student_index % 2 == 0>
				<tr bgcolor="red">
			<#else>
				<tr bgcolor="green">
			
			</#if>
				<td>${student_index+1}</td>
				<td>${student.id}</td>
				<td>${student.name}</td>
				<td>${student.age}</td>
			</tr>
			
			
		</#list>
	
	
	<table><br>
	日期：${date?date}<br>  <!-- 使用?date    ?time    ?datetime -->
	<!-- 使用?string("pattern")自定义输入时间的类型 -->
	日期：${date?string("yyyy/MM/dd HH:mm:ss")}
	
	<br>
	<!--空值：${null!} -->  <!-- 对于空值的处理，!空面加的是默认值 -->
	
	判断null的值是否位空:<br>
	<#if null??>    <!-- ??表示不为空 -->
		null不为空
	<#else>
		null为空
	</#if>
	
	引用模板测试：<br>
	<#include "hello.ftl">
	
	
</body>

</html>
