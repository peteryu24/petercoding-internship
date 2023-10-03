<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit File</title>
</head>
<body>
    <h2>Edit File</h2>
    <form action="../../EditFileServlet" method="post" enctype="multipart/form-data">
        <input type="hidden" name="fileId" value="<%= request.getParameter("fileId") %>">
        <input type="hidden" name="postId" value="<%= request.getParameter("postId") %>">
        <input type="file" name="newFile" accept=".txt"> <br>
        <input type="submit" value="Save">
    </form>
    <br>
    <a href="../Post/DetailPost.jsp?postId=<%= request.getParameter("postId") %>">Back to Post</a>
</body>
</html>
