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
		xhr.setRequestHeader('Content-type', 'application/json;charset=UTF-8');

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var response = JSON.parse(xhr.responseText);
				if (response.status === "success") {
					alert('게시글 작성 성공.');
					location.href = 'ShowPost.jsp';
				} else if (response.status === "error") {
					alert('제목을 입력하세요.');
					location.href = 'CreatePost.jsp';
				}
			}
		};

		var data = {
			title : document.getElementById('title').value,
			content : document.getElementById('content').value
		};

		xhr.send(JSON.stringify(data));
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

		<button class="write button" onclick="submitData()"
			style='width: 70pt; height: 70pt;'>
			Write
		</button>
	</div>
</body>
</html>
