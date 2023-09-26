<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.user.UserTable"%>

<%
	String email = request.getParameter("email");
	String nickname = request.getParameter("nickname");
	String password = request.getParameter("password");

	if ((nickname == null || nickname.trim().isEmpty()) || (email == null || email.trim().isEmpty())
			|| (password == null || password.trim().isEmpty())) {
		out.print("모든 정보를 입력하세요.");
		return;
	}

	UserTable ut = new UserTable();
	String isCheck = ut.insertValue(email, nickname, password);

	if (isCheck == "proceed") {
		out.print("회원가입 성공하셨습니다.");
	} else if (isCheck == "duplicate") {
		out.print("아이디가 중복됩니다.");
	}
%>
