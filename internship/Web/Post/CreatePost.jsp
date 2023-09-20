<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
	<form action="PostToDB.jsp" method="post">
		<label for="title"> Title</label><br> <input type="text"
			id="title" name="title" value=""><br>
		<!--텍스트를 입력할 수 있는 상자-->
		<label for="content">Content</label><br> <input type="text"
			id="content" name="content" value="">
		<button class="write button" type="submit"
			style='width: 70pt; height: 70pt;'>Write</button>
	</form>
	</div>

</body>
</html>
