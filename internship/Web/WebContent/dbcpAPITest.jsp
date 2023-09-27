<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>

<%
 	Connection conn = null; 
	
	try {
  		Context init = new InitialContext();
        //dpcpAPITest.jsp 페이지가 실행되는 Tomcat 자체의 Context를 얻어오는 부분
        //Context란 각종 환경 설정 정보를 의미함
        
  		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/postgres");
        //Tomcat 서버에 공유되어 있는 DataSource 객체 리소스를 얻어오는 부분
        //java:comp/env JNDI는 정해져있는 이름이며 Resource 정의 Context까지 접근하는 JNDI임
        //즉, context.xml에 정의된 리소스에 접근하려면 java.comp/env라는 이름의 Context까지는
        //반드시 접근해야 함
        //jdbc/postgres JNDI 이름은 context.xml에서 리소스명으로 정의한 이름
        
  		conn = ds.getConnection();
        //DataSource에서 제공하는 getConnection() 메소드를 사용하면
        //Tomcat 서버의 CP(Connection Pool)에서 제공하는 Connection 객체를 얻어올 수 있음
  		
  		out.println("<h3>연결되었습니다.</h3>");
	}catch(Exception e){
		out.println("<h3>연결에 실패하였습니다.</h3>");
		e.printStackTrace();
 	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
