<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.post.PostTable"%>

<%
	String title = request.getParameter("title");
	String content = request.getParameter("content");

	PostTable pt = new PostTable();
	Boolean isCheck = pt.insertValue(title, content);

	if (isCheck) {
		out.println("<script>alert('게시글 작성 성공.');</script>");
		out.println("<script>location.href='ShowPost.jsp'</script>");
	} else {
		out.println("<script>alert('제목을 입력하세요.');</script>");
		out.println("<script>location.href='CreatePost.jsp'</script>");
	}
%>
