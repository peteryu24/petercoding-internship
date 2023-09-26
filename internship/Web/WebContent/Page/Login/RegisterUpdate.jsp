<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.user.UserTable"%>
<%
	String email = request.getParameter("email");
	String nickname = request.getParameter("nickname");
	String password = request.getParameter("password");

	if ((nickname == "") || (email == "") || (password == "")) {
		out.println("<script>alert('모든 정보를 입력하세요.');</script>");
		out.println("<script>location.href='Register.jsp'</script>");
	}
	UserTable ut = new UserTable();

	String isCheck = ut.insertValue(email, nickname, password);

	if (isCheck == "proceed") {
		out.println("<script>alert('회원가입 성공하셨습니다.');</script>");
		out.println("<script>location.href='Login.jsp'</script>");
	} else if (isCheck == "duplicate") {
		out.println("<script>alert('아이디가 중복됩니다.');</script>");
		out.println("<script>location.href='Register.jsp'</script>");
	}
%>

