<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome</title>
<style>
    body {
        text-align: center;
        font-size: 24px;
    }

    .backButton {
        width: 70pt;
        height: 30pt;
    }
</style>
</head>
<body>
    <div class="container">
        <h1>Web Project</h1>
        <br>
        <h3>추가한 라이브러리</h3> 
        <br>
        <h5>DBCP Connection Pool</h5>
        <p>
        	tomcat-juli.jar / Apache Commons DBCP / commons-pool2
        	<br> <br>
        </p>
        <h5>File Upload</h5>
        <p>
        	Apache Commons Io / Apache Commons Fileupload 	
        	<br><br>
        </p>
        <h5>Login & Register</h5>
        <p>
        	[회원가입]
        	<br>
        	이메일, 닉네임, 비밀번호 텍스트 상자 존재
        	<br>
        	입력 후 회원가입을 누르면 ajax json으로 보내진
        	<br>
        	데이터를 RegisterServlet에서 
        	<br>
        	모든 텍스트 상자가 입력된 것을 확인 후 
        	<br>
        	테이블에 성공적으로 데이터가 삽입되면
        	<br>
        	회원가입 성공 메세지를 보낸다.
        	<br>
        	테이블 초기 설정시 이메일은 unique이기 
        	<br>
        	때문에 테이블 삽입 에러시, 회원가입 불가
        	<br> <br>
        	[로그인]
        	<br>
        	이메일, 비밀번호 텍스트 상자 존재
        	<br>
        	마찬가지로 ajax json으로 데이터를 보내고 서블릿에서 nullcheck를 시행
        	<br>
        	테이블에서 해당 값이 존재하는지 비교 후 로그인
        	<br>
        	<span style="color: red;">로그인시 해당 이메일을 세션으로 세팅</span>   
        	<br><br>
        	[회원정보 수정]   
        	<br>
        	비밀번호 변경 가능
        	<br>
        	테이블에서 회원가입 했던 이메일을 기준으로 
        	<br>
        	비번 변경 요청시 테이블에서
        	<br>
        	데이터를 조작
        	<br><br>
        	[회원탈퇴]
        	<br>
        	마찬가지로 이메일을 기준으로
        	<br>
        	탈퇴 요청시 테이블에서
        	<br>
        	해당 데이터 삭제
        	<br><br>
        	[로그아웃]
        	<br>
        	초기 로그인 화면으로 돌아가며 세션 종료
        	<br>
        	<span style="color: red;">뒤로가기로 이전 페이지 접근 불가(세션종료, 캐시없앰)</span>
        	 	
        </p>
        
        <h5>Post</h5>
        <p>
        	[게시글 작성]
        	<br>
        	게시글 작성시 제목, 내용 텍스트 상자와 파일 업로드 파트 존재
        	<br>
        	데이터 통신 다양화를 위해 xhr를 사용하려고 했으나,
        	<br>
        	게시글과 파일을 동시에 전송하기 위해 multipart ajax formData로 변경
        	<br>
        	게시글 제목과 내용은 회원가입과 유사하게 
        	<br>
        	DB에 넣고 성공하면 성공 반환
        	<br>
        	파일 업로드는 Collection[Part] 사용
        	<br>
        	디렉토리는 배경화면
        	<br><br>
        	[게시글 상세페이지]
        	<br>
        	게시글에 대해 상세보기 기능
        	<br>
        	<span style="color: red;">작성자에 한해서</span>(초기 작성했던 id값을 비교해 권한 설정) 
        	<br>
        	삭제와 수정이 가능함(회원정보 수정, 탈퇴와 유사한 로직)
        	<br>
        	업로드 된 파일이 있다면, 마찬가지로 <span style="color: red;">작성자에 한해서</span> 수정과 삭제 기능   	
        </p>
        
        <h5>Comment</h5>
        <p>
        	[댓글 작성]
        	<br>
        	해당 게시물의 아이디를 외래키로 받아
        	<br>
        	게시물에 연결된 댓글을 작성
        	<br>
        	작성자에 한해서 수정 삭제 권한 부여
        </p>
        <br>
        
        <h5>전체 구조</h5>
        <p>
        	아이디와 게시물 댓글은 모두 왜래키로 연관성을 가짐
        	<br>
        	따라서 탈퇴시 해당 회원이 작성한 게시물과 댓글은 모두 삭제
            <br>
            jsp페이지와 서블릿의 통신은 전부 Ajax와 json을 함께 사용(비동기 통신)
            <br>
            서블릿에서 json으로 응답한 것을 클라이언트는 파싱하여 데이터를 추출
            <br>
            requstBody는 spring 프레임워크 추가?
            <br>
            파일 업로드 관련해서는 multipart 사용
            <br>
            (현재: form data -> jQuery Ajax로 업로드 구현 필요)
            <br>
            각 파일은 Parts 객체. 경로를 지정하여 실제 파일을 디스크에 저장
        </p>
        
        
        <button class="backButton" type="button" onclick="location.href='./page/Login/Login.jsp'">
            Start
        </button>
    </div>
</body>
</html>
