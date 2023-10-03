<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="gmx.upc.post.PostTable" %>
<%@ page import="gmx.upc.post.PostVo" %>
<%@ page import="gmx.upc.comment.CommentTable" %>
<%@ page import="gmx.upc.comment.CommentVo" %>
<%@ page import="gmx.upc.file.FileTable" %>
<%@ page import="gmx.upc.file.FileVo" %>
<%@ page import="gmx.session.SessionFilter"%>
<%
if (!SessionFilter.isUserLoggedIn(request)) {
    response.sendRedirect("../Login/Login.jsp?error=unauthorized");
    return;
}
%>

<%
    String strId = request.getParameter("postId");
    int postId = Integer.parseInt(strId);

    PostTable pt = new PostTable();
    PostVo pv = pt.getPostById(postId);

    CommentTable ct = new CommentTable();
    ArrayList<CommentVo> commentList = ct.getCommentsByPostId(postId);

    FileTable ft = new FileTable();
    ArrayList<FileVo> files = ft.getFilesByPostId(postId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Content</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        var sessionEmail = '<%= session.getAttribute("userEmail") %>';
        var postEmail = '<%= pv.getEmail() %>';

        $(document).ready(function() {
            $("#apply").click(function(event) {
                $.ajax({
                    type : "POST",
                    url : "../../EditPostServlet",
                    data : {
                        content : $("#content").val(),
                        postId: <%= postId %>
                    },
                    dataType : "json",
                    success : function(response) {
                        alert(response.message);
                        if (response.message === "변경이 완료되었습니다.") {
                            location.href = 'ShowPost.jsp';
                        } else {
                            location.href = 'EditPost.jsp';
                        }
                    },
                });
            });
        });

    </script>
</head>
<body style="background-image: url('https://img.freepik.com/free-photo/design-space-paper-textured-background_53876-41739.jpg?w=900&t=st=1695099733~exp=1695100333~hmac=97017b46f44363931f386356ef4a473acfa31d53893c1288020443c23cb617fa')">
    <div id="editContentBlock">
        <h2>
            <i>Edit content</i>
        </h2>
        <label for="content">EDIT Content</label>
        <br> 
        <textarea id="content" name="content" rows="4" cols="50"><%=pv.getContent()%></textarea>
        <br>
        <button class="button" id="goBack" type="button" onclick="location='ShowPost.jsp'" style='width: 60pt; height: 60pt;'>
            Go Back
        </button>
        <button class="button" id="apply" type="button" style='width: 60pt; height: 60pt;'>
            Apply
        </button>
        <!-- 파일 목록을 여기에 표시 -->
        <div class="detailInfo">
            <span class="detailLabel">[File] </span>
            <ul>
                <%
                if (files != null && !files.isEmpty()) {
                    for (FileVo file : files) {
                %>
                        <li>
                            <a href="../../DownloadFileServlet?fileId=<%=file.getFileId()%>&postId=<%=postId%>"><%=file.getFileName()%></a>
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
</body>
</html>
