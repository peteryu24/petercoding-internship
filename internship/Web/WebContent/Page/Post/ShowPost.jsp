<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="gmx.upc.user.UserTable"%>
<%@page import="gmx.upc.user.UserVo"%>
<%@page import="gmx.upc.post.PostTable"%>
<%@page import="gmx.upc.post.PostVo"%>
<%@page import="gmx.upc.comment.CommentTable"%>
<%@page import="gmx.upc.comment.CommentVo"%>

<%
	UserTable userTable = new UserTable();
	PostTable postTable = new PostTable();
	CommentTable commentTable = new CommentTable();

	ArrayList<UserVo> userList = userTable.input();
	ArrayList<PostVo> postList = postTable.input();
	ArrayList<CommentVo> commentList = commentTable.input();
%>

<!DOCTYPE html>
<html>
<head>
<title>Post</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function() { // 웹 페이지가 모두 로드되면 내부의 함수 실행
		$("#unregister").click(function(event) { // id가 unregister에서 클릭 이벤트가 발생하면 지정된 함수 실행
			$.ajax({ // 서버에 비동기적으로 데이터를 전송하거나 요청
				type : "POST", // http 요청 메서드 // 서블릿 doPost
				url : "../../UnregisterServlet", // 요청을 보낼 서버 URL
				data : {}, // 서버에 전송될 데이터 지정
				dataType : "json", // 받을 데이터 타입 지정
				success : function(response) { // 요청이 성공적으로 처리되면 실행될 콜백 함수 response는 서버로 부터 받은 응답 데이터
					alert(response.message); // 응답 메시지를 팝업으로 표시
					if (response.message === "탈퇴 완료되었습니다.") { // javascript는 equals 없음
						location.href = '../Login/Login.jsp'; // Login.jsp로 리다이렉트
					} else {
						location.href = 'ShowPost.jsp';
					}
				},
			});
		});
	});

	function viewPost(postId) { // 제목을 버튼화 하여 전송
		location.href = 'DetailPost.jsp?postId=' + postId;
	}
</script>
<style>
h2 {
	text-align: center;
	color: #8f784b;
}

#post {
	margin: auto;
	background-color: #eee6c4;
	border: 10px solid #fbceb1;
}

body {
	background-color: #f5f5dc;
}

.postButton {
	background-color: transparent;
	border: none;
	text-align: left;
	display: block;
	width: 100%;
	text-align: left;
	cursor: pointer;
	color: inherit;
}

.postButton:hover {
	background-color: #f0f0f0;
}
</style>
</head>
<body>
	<h2>Post</h2>
	<table id="post">
		<thead>
			<tr>
				<th>Post ID</th>
				<th>Email</th>
				<th>Title</th>
				<th>View</th>
				<th>Create Time</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (PostVo post : postList) {
			%>
			<tr>
				<td><%=post.getPostId()%></td>
				<td><%=post.getEmail()%></td>
				<td>
					<button class="postButton"
						onclick="viewPost('<%=post.getPostId()%>')">
						<%=post.getTitle()%>
					</button>
				</td>
				<td><%=post.getView()%></td>
				<td><%=post.getCreateTime()%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	<div>
		<button class="write button" type="button"
			onclick="location='CreatePost.jsp'"
			style='width: 70pt; height: 70pt;'>write</button>
		<button class="logout" type="button"
			onclick="location='../Login/Login.jsp'"
			style='width: 70pt; height: 70pt;'>logout</button>
		<button class="editInfo" type="button"
			onclick="location='../Login/EditInfo.jsp'"
			style='width: 70pt; height: 70pt;'>editInfo</button>
		<button class="unregister" id="unregister" type="button"
			style='width: 70pt; height: 70pt;'>unregister</button>
	</div>
</body>
</html>
