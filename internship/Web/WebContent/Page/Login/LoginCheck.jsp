<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.user.UserTable"%>

<%
	String email = request.getParameter("email");
	String password = request.getParameter("password");

	if ((email == null || email.trim().isEmpty()) || (password == null || password.trim().isEmpty())) {
		out.print("모든 정보를 입력하세요.");
		return;
	}

	UserTable ut = new UserTable();
	Boolean isCheck = ut.loginCheck(email, password);

	if (isCheck) {
		session.setAttribute("userEmail", email);
		out.print("로그인 성공하셨습니다.");
	} else {
		out.print("로그인 실패하셨습니다.");
	}
%>
