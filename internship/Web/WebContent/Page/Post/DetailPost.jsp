<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="gmx.upc.post.PostTable"%>
<%@page import="gmx.upc.post.PostVo"%>
<%@page import="gmx.upc.comment.CommentTable"%>
<%@page import="gmx.upc.comment.CommentVo"%>

<%
    String strId = request.getParameter("postId");
    int postId = Integer.parseInt(strId);

    PostTable pt = new PostTable();
    PostVo pv = pt.getPostById(postId);
    
    CommentTable ct = new CommentTable();
    ArrayList<CommentVo> commentList = ct.getCommentsByPostId(postId);
    
    if(request.getSession().getAttribute("userEmail") == null) {
        response.sendRedirect("../Login/Login.jsp?error=unauthorized");
        return;
    	}
    
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detail Post view</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

	var sessionEmail = '<%= session.getAttribute("userEmail") %>';
	var postEmail = '<%= pv.getEmail() %>';
	
	$(document).ready(function() { // 웹 페이지가 모두 로드되면 내부의 함수 실행
		$("#newComment").click(function() { 
			 location.href = '../Comment/WriteComment.jsp?postId=<%= postId %>';
    	});
		$("#editButton").click(function() {
            if (sessionEmail !== postEmail) {
                alert("권한이 없습니다.");
                return false;
            } else {
                location.href = 'EditPost.jsp?postId=<%=postId%>';
            }
        });

        $("#deletePost").click(function(event) { // id가 deletePost에서 클릭 이벤트가 발생하면 지정된 함수 실행
            if (sessionEmail !== postEmail) {
                alert("권한이 없습니다.");
                return false;
            } else {
            	$.ajax({ // 서버에 비동기적으로 데이터를 전송하거나 요청
    				type : "POST", // http 요청 메서드 // 서블릿 doPost
    				url : "../../DeletePostServlet", // 요청을 보낼 서버 URL
    				data : {postId: <%= postId %>},  // 서버에 전송될 데이터 지정
    				dataType : "json", // 받을 데이터 타입 지정
    				success : function(response) { // 요청이 성공적으로 처리되면 실행될 콜백 함수 response는 서버로 부터 받은 응답 데이터
    					alert(response.message); // 응답 메시지를 팝업으로 표시
    					if (response.message === "삭제 완료되었습니다.") { // javascript는 equals 없음
    						location.href = '../Post/ShowPost.jsp'; // ShowPost.jsp로 리다이렉트
    					} else {
    						location.href = 'DetailPost.jsp';
    					}
    				},
    			})
            }
        });
	});

</script>
    <style>
        h2 {
            text-align: center;
            color: #8f784b;
        }

        #postDetail {
            margin: auto;
            background-color: #eee6c4;
            border: 10px solid #fbceb1;
            width: 70%;
            padding: 2%;
        }

        body {
            background-color: #f5f5dc;
        }

        .detailLabel {
            font-weight: bold;
            color: #8f784b;
        }

        .detailInfo {
            padding: 1%;
        }
    </style>
</head>
<body>
    <h2>Detail Post View</h2>
    <div id="postDetail">
        <div class="detailInfo">
            <span class="detailLabel">[Title] </span>
            <span><%=pv.getTitle()%></span>
        </div>
        <div class="detailInfo">
            <span class="detailLabel">[Content] </span>
            <p><%=pv.getContent()%></p>
        </div>
        <div style="text-align: center;">
    <button class="editButton" id="editButton" type="button" style='width: 70pt; height: 30pt;'>Edit</button>
    <button class="deleteButton" id="deletePost" type="button" style='width: 70pt; height: 30pt;'>Delete</button>
    <button class="backButton" type="button" onclick="location.href='ShowPost.jsp'" style='width: 70pt; height: 30pt;'>Go<br>Back</button>
		</div>

    </div>
    <h2>Comments</h2>
    <div id="writeComment">
    <button id="newComment">댓글 작성</button>
</div>
    
<table id="commentTable">
    <thead>
        <tr>
            <th>writer</th>
            <th>Comment</th>
        </tr>
    </thead>
    <tbody>
       <% 
for (CommentVo comment : commentList) {
%>
    <tr>
        <td><%=comment.getEmail()%></td> 
        <td>
         <a href="../Comment/DetailComment.jsp?commentId=<%=comment.getCommentId()%>">
        <%=comment.getComment()%>
    	</a>

        </td> 
    </tr>
<% 
}
%>


    </tbody>
</table>
</body>
</html>
