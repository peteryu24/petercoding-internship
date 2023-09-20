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
<title>User Post Comment</title>
<style>
h2 {
	text-align: center;
	color: #8f784b;
}

#user, #post, #comment {
	margin: auto;
	background-color: #eee6c4;
	border: 10px solid #fbceb1;
}

body {
	background-color: #f5f5dc;
}
</style>
</head>
<body>

	<h2>User</h2>
	<table id="user">
		<thead>
			<tr>
				<th>User ID</th>
				<th>Nickname</th>
				<th>Email</th>
				<th>Password</th>
				<th>Create Time</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < userList.size(); i++) {
					UserVo user = userList.get(i);
			%>
			<tr>
				<td><%=user.getUser_id()%></td>
				<td><%=user.getNickname()%></td>
				<td><%=user.getEmail()%></td>
				<td><%=user.getPassword()%></td>
				<td><%=user.getCreate_time()%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>

	<h2>Post</h2>
	<table id="post">
		<thead>
			<tr>
				<th>Post ID</th>
				<th>User ID</th>
				<th>Title</th>
				<th>Content</th>
				<th>View</th>
				<th>Create Time</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < postList.size(); i++) {
					PostVo post = postList.get(i);
			%>
			<tr>
				<td><%=post.getPost_id()%></td>
				<td><%=post.getUser_id()%></td>
				<td><%=post.getTitle()%></td>
				<td><%=post.getContent()%></td>
				<td><%=post.getView()%></td>
				<td><%=post.getCreate_time()%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>

	<h2>Comments</h2>
	<table id="comment">
		<thead>
			<tr>
				<th>Comment ID</th>
				<th>User ID</th>
				<th>Post ID</th>
				<th>Comment</th>
				<th>Create Time</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < commentList.size(); i++) {
					CommentVo comment = commentList.get(i);
			%>
			<tr>
				<td><%=comment.getComment_id()%></td>
				<td><%=comment.getUser_id()%></td>
				<td><%=comment.getPost_id()%></td>
				<td><%=comment.getComment()%></td>
				<td><%=comment.getCreate_time()%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>

</body>
</html>
