<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.comment.CommentTable"%>
<%
	String comment = request.getParameter("comment");

	CommentTable ct = new CommentTable();
	Boolean isCheck = ct.insertValue(comment);
	
	if(isCheck){
		out.println("<script>alert('댓글 작성 완료.');</script>");
		out.println("<script>location.href='../Post/ShowPost.jsp'</script>");
	}else{
		out.println("<script>alert('작성할 댓글이 없음.');</script>");
		out.println("<script>location.href='Comment.jsp'</script>");
	}
%>
