<%@ page import="java.sql.*" %>
<%@ page import="gmx.upc.DBInfo" %>
<%
    String userid = request.getParameter("userid");
    String email = request.getParameter("email");
    String nickname = request.getParameter("nickname");
    String password = request.getParameter("password");

    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
        DBInfo dbInfo = DBInfo.getInstance();
        conn = dbInfo.getConnection();

        String sql = "INSERT INTO exam.users (user_id, email, nickname, password) VALUES (?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userid);
        pstmt.setString(2, email);
        pstmt.setString(3, nickname);
        pstmt.setString(4, password);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) {} }
        if (conn != null) { try { conn.close(); } catch (SQLException e) {} }
    }
%>
