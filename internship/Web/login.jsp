<!DOCTYPE html>
<!--html5를 사용-->
<html>
<!--html 문서의 시작-->
<style>
<!--css코드-->  
	label, input{
    	width: 150px;
        height:20px;
    }
</style>
<head>
<meta charset="UTF-8">
<title>로그인 페이지 구현 과제</title>
</head>
<body style="background-image: url('https://e0.pxfuel.com/wallpapers/934/955/desktop-wallpaper-blue-two-tone-mural.jpg')">
    <!--실제로 화면에 보여지는-->

    <div>
        <label for="fname"> ID</label><br> 
        <input type="text" id="fname"name="fname" value=""><br>
        <!--텍스트를 입력할 수 있는 상자-->
        <label for="lname">PW</label><br>
        <input type="password" id="lname" name="lname" value="">
        <!--입력한 텍스트는 별표로 표시-->
        <button class="login button" type="button" style='width:50pt; height:50pt;'>LOGIN</button>
        <!--버튼-->
    </div>
</body>
</html>
