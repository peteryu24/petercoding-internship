	<%--
		var xhr = new XMLHttpRequest();

		xhr.open('POST', '../../PostServlet', true);
		xhr.setRequestHeader('Content-type', 'application/json;charset=UTF-8');

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var response = JSON.parse(xhr.responseText);
				if (response.status === "success") {
					alert('게시글 작성 성공.');
					

                    // postId를 받아와 파일 데이터와 함께 전송
                    var formData = new FormData($("#uploadForm")[0]);
                    formData.append("postId", response.postId);  // 응답에서 받아온 postId를 formData에 추가

                    $.ajax({
                        url: '../../FileServlet',
                        type: 'POST',
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(fileResponse) {
                            // 파일 업로드에 대한 응답 처리
                            if (fileResponse.status === "success") {
                                alert('파일 업로드 성공.');
                                location.href = 'ShowPost.jsp';
                            } else {
                                alert('파일 업로드 실패: ' + fileResponse.message);
                            }
                        }
                    });

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
	
	--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	if (request.getSession().getAttribute("userEmail") == null) {
		response.sendRedirect("../Login/Login.jsp?error=unauthorized");
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Post</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

function submitData() {
    
    var title = $("#title").val();
    var content = $("#content").val();


    var formData = new FormData($("#uploadForm")[0]);

    $.ajax({
        url: '../../PostFileServlet',  
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if (response.status === "success") {
                alert('게시글 및 파일 업로드 성공.');
                location.href = 'ShowPost.jsp';
            } else {
                alert('업로드 실패: ' + response.message);
                location.href = 'CreatePost.jsp';
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('AJAX error: ' + textStatus + ', ' + errorThrown);
        }
    });
}

function goBack() {
    location.href = 'ShowPost.jsp';
}

</script>
</head>
<body>
	<div>
		<form id="uploadForm" enctype="multipart/form-data">
			<label for="title">Title</label> <br> 
			<input type="text" id="title" name="title"> <br> 
			<label for="content">Content</label> <br>
			<textarea id="content" name="content"></textarea> <br> 
			<input type="file" name="files" multiple> <br>
			<button type="button" onclick="submitData()">Write</button>
			<button type="button" onclick="goBack()">Go Back</button>
		</form>
	</div>
</body>
</html>
    	
