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
		$(".login.button").click(function(event) {
			event.preventDefault();

			$.ajax({
				type : "POST",
				url : "LoginCheck.jsp",
				data : {
					email : $("#email").val(),
					password : $("#pw").val()
				},
				//dataType : "json",
				success : function(response) {
					alert(response.trim());
					if (response.trim() === "로그인 성공하셨습니다.") {
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
input {
	width: 150px;
	height: 20px;
}

div {
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
	<div>
		<h2>
			<i><u>Welcome</u></i>
		</h2>
		<form>
			<label for="email"> email</label><br> <input type="text"
				id="email" name="email" required="required"><br> <label
				for="password">PW</label><br> <input type="password" id="pw"
				name="password" required="required"><br>
			<button class="login button" type="button"
				style='width: 70pt; height: 70pt;'>LOGIN</button>
			<button class="register button" type="button"
				onclick="location='Register.jsp'" style='width: 70pt; height: 70pt;'>REGISTER</button>
		</form>
	</div>
</body>

</html>
