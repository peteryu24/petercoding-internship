<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="gmx.upc.user.UserTable"%>

<%
	String email = request.getParameter("email");
	String password = request.getParameter("password");
	UserTable ut = new UserTable();
	Boolean isCheck = ut.loginCheck(email, password);

	if (isCheck) {
		//session.setAttribute("userEmail", email);
		out.println("<script>alert('로그인 성공하셨습니다.');</script>");
		out.println("<script>location.href='../Post/ShowPost.jsp'</script>");

	} else {
		out.println("<script>alert('로그인 실패하셨습니다.');</script>");
		out.println("<script>location.href='Login.jsp'</script>");

	}
%>

