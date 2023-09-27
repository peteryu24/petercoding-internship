<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Post</title>
<script>
	function submitData() {
		var xhr = new XMLHttpRequest();
		xhr.open('POST', '../../PostServlet', true);
		xhr.setRequestHeader('Content-type',
				'application/x-www-form-urlencoded');

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var response = xhr.responseText;
				if (response === "success") {
					alert('게시글 작성 성공.');
					location.href = 'ShowPost.jsp';
				} else if (response === "error") {
					alert('제목을 입력하세요.');
					location.href = 'CreatePost.jsp';
				}
			}
		};

		var title = document.getElementById('title').value;
		var content = document.getElementById('content').value;
		var params = 'title=' + title + '&content=' + content;
		xhr.send(params);
	}
</script>
</head>
<body>
	<div>
		<label for="title">Title</label>
		<br> 
		<input type="text" id="title" name="title" value="">
		<br>
		<label for="content">Content</label>
		<br> 
		<input type="text" id="content" name="content" value="">
		
		<button class="write button" onclick="submitData()" style='width: 70pt; height: 70pt;'>
			Write
		</button>
	</div>
</body>
</html>
