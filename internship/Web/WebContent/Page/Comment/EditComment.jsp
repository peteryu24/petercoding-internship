<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="gmx.upc.comment.CommentTable"%>
<%@page import="gmx.upc.comment.CommentVo"%>
<%
if(request.getSession().getAttribute("userEmail") == null) {
    response.sendRedirect("../Login/Login.jsp?error=unauthorized");
    return;
	}
%>  

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
    <title>댓글 수정</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $("#editButton").click(function() {
                var updatedComment = $("#commentTextArea").val();

                $.ajax({
                    type: "POST",
                    url: "../../EditCommentServlet",
                    data: {
                        commentId: <%=commentId%>,
                        comment: updatedComment
                    },
                    dataType: "json",
                    success: function(response) {
                        if(response.success) {
                            alert("댓글이 성공적으로 수정되었습니다.");
                            location.href = 'DetailComment.jsp?commentId=<%=commentId%>';
                        } else {
                            alert("댓글 수정 실패: " + response.message);
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>
    <h2>댓글 수정</h2>
    <textarea id="commentTextArea" rows="4" cols="50"><%=comment.getComment()%></textarea><br>
    <button id="editButton">수정</button>
    <button onclick="location.href='DetailComment.jsp?commentId=<%=commentId%>'">뒤로 가기</button>
</body>
</html>
