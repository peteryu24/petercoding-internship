<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="gmx.upc.post.PostTable"%>
<%@page import="gmx.upc.post.PostVo"%>
<%@page import="gmx.upc.comment.CommentTable"%>
<%@page import="gmx.upc.comment.CommentVo"%>
<%@page import="gmx.upc.file.FileTable"%>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    PostTable postTable = new PostTable();
    ArrayList<PostVo> postList = postTable.input();

    FileTable fileTable = new FileTable();

    if(request.getSession().getAttribute("userEmail") == null) {
        response.sendRedirect("../Login/Login.jsp?error=unauthorized");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<title>Post</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $("#unregister").click(function(event) {
            $.ajax({
                type : "POST",
                url : "../../UnregisterServlet",
                data : {},
                dataType : "json",
                success : function(response) {
                    alert(response.message);
                    if (response.message === "탈퇴 완료되었습니다.") {
                        location.href = '../Login/Login.jsp';
                    } else {
                        location.href = 'ShowPost.jsp';
                    }
                },
            });
        });

        $(".logout").click(function(event) {
            $.ajax({
                type : "POST",
                url : "../../LogoutServlet",
                dataType : "json",
                success : function(response) {
                    alert(response.message);
                    location.href = '../Login/Login.jsp';
                }
            });
        });
    });

    function viewPost(postId) {
        location.href = 'DetailPost.jsp?postId=' + postId;
    }
</script>
<style>
    h2 {
        text-align: center;
        color: #8f784b;
    }

    #post {
        margin: auto;
        background-color: #eee6c4;
        border: 10px solid #fbceb1;
    }

    body {
        background-color: #f5f5dc;
    }

    .postButton {
        background-color: transparent;
        border: none;
        text-align: left;
        display: block;
        width: 100%;
        text-align: left;
        cursor: pointer;
        color: inherit;
    }

    .postButton:hover {
        background-color: #f0f0f0;
    }
</style>
</head>
<body>
    <h2>Post</h2>
    <table id="post">
        <thead>
            <tr>
                <th>Post ID</th>
                <th>Email</th>
                <th>Title</th>
                <th>Create Time</th>
                <th>Has File?</th>
            </tr>
        </thead>
        <tbody>
            <% 
            for (PostVo post : postList) { 
                boolean hasFile = fileTable.hasFileForPost(post.getPostId());
            %>
                <tr>
                    <td><%=post.getPostId()%></td>
                    <td><%=post.getEmail()%></td>
                    <td>
                        <button class="postButton" onclick="viewPost('<%=post.getPostId()%>')">
                            <%=post.getTitle()%>
                        </button>
                    </td>
                    <td><%=post.getCreateTime()%></td>
                    <td><%= hasFile ? "Yes" : "No" %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
    <div>
        <button class="write button" type="button" onclick="location='CreatePost.jsp'" style='width: 70pt; height: 70pt;'>write</button>
        <button class="logout" type="button" onclick="location='../Login/Login.jsp'" style='width: 70pt; height: 70pt;'>logout</button>
        <button class="editInfo" type="button" style='width: 70pt; height: 70pt;'>editInfo</button>
        <button class="unregister" id="unregister" type="button" style='width: 70pt; height: 70pt;'>unregister</button>
    </div>
</body>
</html>
