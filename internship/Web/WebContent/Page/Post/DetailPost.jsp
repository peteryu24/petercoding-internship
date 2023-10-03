<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="gmx.upc.post.PostTable" %>
<%@ page import="gmx.upc.post.PostVo" %>
<%@ page import="gmx.upc.comment.CommentTable" %>
<%@ page import="gmx.upc.comment.CommentVo" %>
<%@ page import="gmx.upc.file.FileTable" %>
<%@ page import="gmx.upc.file.FileVo" %>

<%
    String strId = request.getParameter("postId");
    int postId = Integer.parseInt(strId);

    PostTable pt = new PostTable();
    PostVo pv = pt.getPostById(postId);

    CommentTable ct = new CommentTable();
    ArrayList<CommentVo> commentList = ct.getCommentsByPostId(postId);

    FileTable ft = new FileTable();
    ArrayList<FileVo> files = ft.getFilesByPostId(postId);

    if (request.getSession().getAttribute("userEmail") == null) {
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

        $(document).ready(function() {
            $("#newComment").click(function() {
                location.href = '../Comment/WriteComment.jsp?postId=<%= postId %>';
            });
            $("#editButton").click(function() {
                if (sessionEmail !== postEmail) {
                    alert("권한이 없습니다.");
                    return false;
                } else {
                    location.href = 'EditPost.jsp?postId=<%= postId %>';
                }
            });

            $("#deletePost").click(function(event) {
                if (sessionEmail !== postEmail) {
                    alert("권한이 없습니다.");
                    return false;
                } else {
                    $.ajax({
                        type: "POST",
                        url: "../../DeletePostServlet",
                        data: {postId: <%= postId %>},
                        dataType: "json",
                        success: function(response) {
                            alert(response.message);
                            if (response.message === "삭제 완료되었습니다.") {
                                location.href = '../Post/ShowPost.jsp';
                            } else {
                                location.href = 'DetailPost.jsp';
                            }
                        },
                    });
                }
            });
        });

        // 파일 삭제 함수
        function deleteFile(fileId, postId) {
            $.ajax({
                type: "POST",
                url: "../../DeleteFileServlet",
                data: { fileId: fileId, postId: postId },
                dataType: "json",
                success: function (response) {
                    alert(response.message);
                    if (response.message === "삭제 완료되었습니다.") {
                        location.href = 'DetailPost.jsp?postId=' + postId;
                    }
                },
            });
        }
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

    <div class="detailInfo">
        <span class="detailLabel">[Attached Files] </span>
        <ul>
            <%
            if (files != null && !files.isEmpty()) {
                for (FileVo file : files) {
            %>
                <li>
                    
                    <a href="#" onclick="deleteFile(<%=file.getFileId()%>, <%=postId%>)">Delete</a>
                    <%=file.getFileName()%>
                </li>
            <%
                }
            } else {
            %>
                <li>No attached files</li>
            <%
            }
            %>
        </ul>
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
