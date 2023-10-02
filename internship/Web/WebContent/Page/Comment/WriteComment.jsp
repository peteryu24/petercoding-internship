<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
if(request.getSession().getAttribute("userEmail") == null) {
    response.sendRedirect("../Login/Login.jsp?error=unauthorized");
    return;
	}
%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 작성</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $("#submitComment").click(function() {
            let comment = $("#commentContent").val();
            if (comment) {
                $.ajax({
                    type: "POST",
                    url: "../../NewCommentServlet",
                    data: JSON.stringify({
                        postId: <%= request.getParameter("postId") %>,
                        comment: comment
                    }),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(response) {
                        if (response.success) {
                            alert("댓글이 성공적으로 등록되었습니다.");
                            location.href = '../Post/DetailPost.jsp?postId=<%= request.getParameter("postId") %>';
                        } else {
                            alert("댓글 작성 실패");
                        }
                    },
                    error: function() {
                        alert("댓글 작성 실패");
                    }
                });
            } else {
                alert("댓글을 입력해주세요.");
            }
        });
    });
</script>
</head>
<body>
    <h2>댓글 작성</h2>
    <textarea id="commentContent" rows="4" cols="50" placeholder="여기에 댓글을 작성하세요."></textarea>
    <br/>
    <button id="submitComment">댓글 제출</button>
    <button onclick="location.href='../Post/DetailPost.jsp?postId=<%= request.getParameter("postId") %>'">뒤로 가기</button>
</body>
</html>
