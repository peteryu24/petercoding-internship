<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="gmx.upc.comment.CommentTable"%>
<%@page import="gmx.upc.comment.CommentVo"%>
<%@page import="org.json.simple.JSONObject"%> <!-- json-simple 사용 시 추가 -->

<%
String commentIdStr = request.getParameter("commentId");
int commentId = Integer.parseInt(commentIdStr);

CommentTable ct = new CommentTable();
CommentVo comment = ct.getByCommentId(commentId);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>댓글 상세보기</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- jQuery 라이브러리 추가 -->
<%
String loggedInUserEmail = (String) request.getSession().getAttribute("userEmail");
%>

<script>
    function verifyAndDeleteComment() {
        // 현재 로그인한 사용자의 이메일 주소
        var loggedInUserEmail = '<%= loggedInUserEmail %>';

        // 댓글 작성자의 이메일 주소
        var commentAuthorEmail = '<%= comment.getEmail() %>';

        if(loggedInUserEmail !== commentAuthorEmail) {
            alert('권한이 없습니다');
            return;
        }

        // 위의 권한 검사를 통과한 경우, 댓글 삭제 함수 호출
        deleteComment();
    }

    function verifyAndEditComment() {
        var loggedInUserEmail = '<%= loggedInUserEmail %>';
        var commentAuthorEmail = '<%= comment.getEmail() %>';

        if(loggedInUserEmail !== commentAuthorEmail) {
            alert('권한이 없습니다');
            return;
        }

        location.href='EditComment.jsp?commentId=<%=commentId%>';
    }
</script>

</head>
<body>
    <h2>댓글 상세보기</h2>
    작성자: <%=comment.getEmail()%><br>
    댓글: <%=comment.getComment()%><br>

    <!-- 수정 및 삭제 버튼 -->
    <button onclick="verifyAndEditComment()">수정</button>
	<button onclick="verifyAndDeleteComment()">삭제</button>


    <button onclick="location.href='../Post/DetailPost.jsp?postId=<%=comment.getPostId()%>'">뒤로 가기</button>
</body>
</html>
