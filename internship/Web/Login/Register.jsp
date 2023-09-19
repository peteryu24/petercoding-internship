<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--html5를 사용-->
<html>
<!--html 문서의 시작-->
<style>
/*css코드(css용 주석)*/
input {
	width: 150px;
	height: 20px;
}

div {
	background-color: #eee6c4;
	padding: 9px;
	width: 240px;
	position: absolute;
	/* 절대 위치 */
	top: 50%;
	left: 50%;
	/* 상단으로부터 50% 떨어져 위치 */
	transform: translate(-50%, -50%);
	/*미사용시 div의 왼쪽 상단 꼭지점이 중앙으로 오게됨*/
	width: 240px;
}
</style>
<head>
<!--문서의 메타정보-->
<meta charset="UTF-8">
<title>로그인 페이지 구현 과제</title>
</head>
<body
	style="background-image: url('https://img.freepik.com/free-photo/design-space-paper-textured-background_53876-41739.jpg?w=900&t=st=1695099733~exp=1695100333~hmac=97017b46f44363931f386356ef4a473acfa31d53893c1288020443c23cb617fa')">
	<!--실제로 화면에 보여지는-->



	<div>
		<h2>
			<i>Hello</i>
			<!--이탈릭한(i) 제목(h2)-->
		</h2>

		<label for="email">SET e-mail</label><br> <input type="text"
			id="email" name="email" value=""><br>
		<!--이메일 상자-->
		<label for="nickname">SET nickname</label><br> <input type="text"
			id="nickname" name="nickname" value=""><br>
		<!--닉네임 상자-->
		<label for="password">SET PW</label><br> <input type="text"
			id="password" name="password" value=""><br>
		<!--비밀번호 상자-->

		<button class="button" id="go back" type="button"
			onclick="location='Login.jsp'" style='width: 60pt; height: 60pt;'>Go
			Back</button>
		<button class="button" id="register" type="button"
			style='width: 60pt; height: 60pt;'>
			REGISTER <br>NOW
		</button>
		<!--버튼-->
	</div>
</body>
</html>
