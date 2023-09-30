<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String strId = request.getParameter("postId");
	int postId = Integer.parseInt(strId);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Content</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- jquery 기본  양식 -->
<script>
	$(document).ready(function() { // 웹 페이지가 모두 로드되면 내부의 함수 실행
		$("#apply").click(function(event) { // id가 register에서 클릭 이벤트가 발생하면 지정된 함수 실행

			$.ajax({ // 서버에 비동기적으로 데이터를 전송하거나 요청
				type : "POST", // http 요청 메서드 // 서블릿 doPost
				url : "../../EditPostServelt", // 요청을 보낼 서버 URL
				data : { // 서버에 전송될 데이터 지정
					content : $("#content").val(),
					 postId: <%= postId %>
				},
				dataType : "json", // 받을 데이터 타입 지정
				success : function(response) { // 요청이 성공적으로 처리되면 실행될 콜백 함수 response는 서버로 부터 받은 응답 데이터
					alert(response.message); // 응답 메시지를 팝업으로 표시
					if (response.message === "변경이 완료되었습니다.") { // javascript는 equals 없음
						location.href = 'ShowPost.jsp'; // ShowPost.jsp로 리다이렉트
					} else {
						location.href = 'EditPost.jsp';
					}

				},
			});
		});
	});
</script>

<style>
#content {
	width: 150px;
	height: 20px;
}

#editContentBlock {
	background-color: #eee6c4;
	padding: 9px;
	width: 240px;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}
</style>
</head>
<body
style="background-image: url('https://img.freepik.com/free-photo/design-space-paper-textured-background_53876-41739.jpg?w=900&t=st=1695099733~exp=1695100333~hmac=97017b46f44363931f386356ef4a473acfa31d53893c1288020443c23cb617fa')">
	<div id="editContentBlock">
		<h2>
			<i>Edit content</i>
		</h2>
		<!-- <form> -->
		<!-- 만약 from 태그 ajax랑 같이 사용시 event.preventDefault(); 클릭 이벤트 아랫줄에 사용-->
		<label for="content">EDIT Content</label>
		<br> 
		<input type="text" id="content" name="content" value="" required="required">
		<br>
		<button class="button" id="goBack" type="button" onclick="location='ShowPost.jsp'" style='width: 60pt; height: 60pt;'>
			Go Back
		</button>
		<button class="button" id="apply" type="button" style='width: 60pt; height: 60pt;'>
			Apply
		</button>

		<!-- </form> -->
	</div>
</body>
</html>
