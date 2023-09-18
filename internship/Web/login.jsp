<!DOCTYPE html>
<!--html5를 사용-->
<html>
<!--html 문서의 시작-->
<style>
/*css코드(css용 주석)*/
	input{
    	width: 150px;
        height:20px;
    }
     div{
     	background-color: #f2f2f2;
        padding: 9px;
        width: 240px;
        position: absolute;  
        /* 절대 위치 */
    	top: 50%;  
   		left: 50%;  
        /* 상단으로부터 50% 떨어져 위치 */
        transform: translate(-50%, -50%);
        /*미사용시 div의 왼쪽 상단 꼭지점이 중앙으로 오게됨*/
     }
     
</style>
<head>
<!--문서의 메타정보-->
<meta charset="UTF-8">
<title>로그인 페이지 구현 과제</title>
</head>
<body style="background-image: url('https://e0.pxfuel.com/wallpapers/934/955/desktop-wallpaper-blue-two-tone-mural.jpg')">
    <!--실제로 화면에 보여지는-->
    
    

    <div>
   		 <h2><i><u>Welcome</u></i></h2>
         <!--이탈릭한(i) 제목(h2)에 밑줄(u)-->
        <label for="userid"> ID</label><br> 
        <input type="text" id="userid"name="userid" value=""><br>
        <!--텍스트를 입력할 수 있는 상자-->
        <label for="pw">PW</label><br>
        <input type="password" id="pw" name="pw" value="">
        <!--입력한 텍스트는 별표로 표시-->
        <button class="login button" type="button" style='width:50pt; height:50pt;'>LOGIN</button>
        <!--버튼-->
    </div>
</body>
</html>
