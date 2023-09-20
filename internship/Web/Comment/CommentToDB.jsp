<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.comment.CommentTable"%>
<%
	String comment = request.getParameter("comment");

	CommentTable ct = new CommentTable();
	Boolean isCheck = ct.insertValue(comment);
%>
