<%@ page import="java.sql.*" %>
<%@ page import="gmx.upc.DBInfo" %>
<%
  String email = request.getParameter("userid");
  String password = request.getParameter("pw");

  Connection conn = null;
  PreparedStatement pstmt = null;
  ResultSet rs = null;

  try {
      DBInfo dbInfo = DBInfo.getInstance();
      conn = dbInfo.getConnection();

      String sql = "SELECT * FROM exam.users WHERE email = ? AND password = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, email);
      pstmt.setString(2, password);
      rs = pstmt.executeQuery();

      if(rs.next()) {
          // 로그인 성공, aaa.jsp로 리다이렉트
          response.sendRedirect("aaa.jsp");
      } else {
          // 로그인 실패, 다시 로그인 페이지로
          response.sendRedirect("Login.jsp");
      }
  } catch(SQLException e) {
      e.printStackTrace();
  } finally {
      if(rs != null) try { rs.close(); } catch(SQLException e) {}
      if(pstmt != null) try { pstmt.close(); } catch(SQLException e) {}
      if(conn != null) try { conn.close(); } catch(SQLException e) {}
  }
%>
