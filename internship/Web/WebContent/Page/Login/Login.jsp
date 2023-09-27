<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>로그인 페이지 구현 과제</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function() {
		$(".loginButton").click(function(event) {

			$.ajax({
				type : "POST",
				url : "../../LoginServlet",
				data : {
					email : $("#email").val(),
					password : $("#password").val()
				},
				dataType : "json",
				success : function(response) {
					alert(response.message);
					if (response.message === "로그인 성공하셨습니다.") {
						location.href = '../Post/ShowPost.jsp';
					} else {
						location.href = 'Login.jsp';
					}
				},
			});
		});
	});
</script>
<style>
#email, #password {
	width: 150px;
	height: 20px;
}

#loginBlock {
	background-color: #f2f2f2;
	padding: 9px;
	width: 240px;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}
</style>
</head>

<body style="background-color: #eee6c4">
	<div id="loginBlock">
		<h2>
			<i><u>Welcome</u></i>
		</h2>
		<!-- <form> -->
		<label for="email"> email</label>
		<br> 
		<input type="text" id="email" name="email" required="required">
		<br> 
		<label for="password">PW</label><br> <input type="password" id="password" name="password" required="required">
		<br> 
		<button class="loginButton" type="button" style='width: 70pt; height: 70pt;'>
			LOGIN
		</button>
		<button class="registerButton" type="button" onclick="location='Register.jsp'" style='width: 70pt; height: 70pt;'>
			REGISTER
		</button>
		<!-- </form> -->
	</div>
</body>

</html>
