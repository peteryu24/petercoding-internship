<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <base href="http://localhost:8080/egov30/">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <title>Posts</title>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f2f2f2;
            display: flex;
            flex-direction: column;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        #postList {
            width: 70%;
            border-radius: 10px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
            background-color: #ffffff;
            padding: 15px;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .buttonGroup {
            display: flex;
            justify-content: space-between;
            width: 70%;
            margin-top: 20px;
        }

        .buttonGroup button {
            width: 23%;
            padding: 10px;
            margin-right: 10px; /* 버튼 사이 간격 조절 */
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            background-color: #007BFF;
            color: #fff;
        }

        .writePost:hover, .changePassword:hover, .logOut:hover {
            background-color: #0056b3;
        }
        
        .unRegister:hover {
            background-color: #c9302c;
        }
        .detailPost , #sortButton, #searchButton{
        width: auto; /* 너비 자동 조정 */
        padding: 5px 10px; /* 내부 여백 조정 */
        margin-right: 10px; /* 오른쪽 여백 추가 */
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
        background-color: #888; /* 어두운 회색 배경색 */
        color: #fff;
	    }
	

	    .detailPost:hover, sortButton:hover , searchButton:hover{
	        background-color: #ccc; /* 얕은 회색 */
	    }
	    
	    .pageNumber:hover {
	        color: blue;
	        cursor: pointer;
	    }
	    
    </style>
</head>
<body>
    <div id="postList">
        <table border="1">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Content</th>
                    <th>File</th>
                    <th>View</th>
                    <th>Writer</th>
                    <th>Create Time</th>
                </tr>
            </thead>
            <tbody id="postsTbody">
                <!-- 동적으로 추가됨 
                	게시글 작성 순으로 오름차순 -->
            </tbody>
        </table>
    </div>
    
    <div id="pageNumbers"></div>
    
    <div>
        <select id="sortSelect">
            <option value="최신순">최신순</option>
            <option value="조회순">조회순</option>
        </select>
        <button id="sortButton">정렬</button>
    </div>
    <div>
    	<input type ="text" name = "Keyword">
    	<button id="searchButton">검색</button>
    </div>
    
    <button id="resetButton" type="button" onclick="resetAndClearSession()">Reset</button>

    <div class="buttonGroup">
        <button class="writePost" type="button" onclick="location.href='post/showWritePost.do'">Write Post</button>
        <button class="logOut" type="button" onclick="logoutAndClearSession()">Log Out</button>
        <button class="unRegister" type="button" onclick="confirmUnregister()">Unregister</button>
        <button class="changePassword" type="button" onclick="location.href='user/showChangePassword.do'">Change Password</button>
        <button class="changePassword" type="button" onclick="location.href='map/showViewMap.do'">View Map</button>
    </div>
    
    
    <script type="text/javascript">
	    $(document).ready(function() {
	        showAllPosts();  
	        showPageNum();
	        
	        // session에 저장된 정렬방식이나 검색어 유무 확인
	        let currentSort = sessionStorage.getItem('orderBy');
	        let currentSearch = sessionStorage.getItem('searchedKeyword');
	        // 존재한다면 기존 값을 바탕으로 게시판 표출
	        if(currentSort) {
	            $('#sortSelect').val(currentSort);
	        }
	        if(currentSearch) {
	            $('input[name="Keyword"]').val(currentSearch);
	        }
	    });
	    
	    // setter 느낌의 sessionStorage 
	    var pageController = (function() {
	        return {
	            setCurrentPage: function(page) {
	                currentPage = page;
	                sessionStorage.setItem('currentPage', currentPage);
	                console.log("currentPage session setting됨!")
	            },
	            setTotalPage: function(page) {
	                totalPage = page;
	                sessionStorage.setItem('totalPage', totalPage);
	                console.log("setTotalPage session setting됨!")
	            },
	            setOrderBy: function(order) {
	                orderBy = order;
	                sessionStorage.setItem('orderBy', orderBy);
	                console.log("orderBy session setting됨!")
	            },
	            setSearchedKeyword: function(Keyword) {
	                searchedKeyword = Keyword;
	                sessionStorage.setItem('searchedKeyword', searchedKeyword);
	                console.log("searchedKeyword session setting됨!")
	            },
	        };
	    })();
     	
	    // session에 저장된 값들을 바탕으로 게시판 표출, 없다면 default 값으로
        function showAllPosts() {
            $.ajax({
                url: "post/postPerPage.do", // 5개씩 보여주는 컨트롤러
                type: "GET",
                dataType: "json",
                data: {
                    sortType: sessionStorage.getItem('orderBy'),
                    currentPage: sessionStorage.getItem('currentPage'),  
                    searchedKeyword: sessionStorage.getItem('searchedKeyword')
                },
                success: Posts, 
                error: function(xhr, status, error) {
                    alert("error");
                    console.log("에러: " + error);
                    console.log(xhr.responseText);
                }
            });
        }
        
	    // 페이징 관련하여 개시글 갯수 식별 후 필요한 번호 계산해서 보여주기
        function showPageNum(totalPage) {
            if (totalPage) {
                generatePageNumbers(totalPage);
            } else {
                $.ajax({
                    url: "post/totalPageNum.do",
                    type: "GET",
                    dataType: "json",
                    success: function(response) {
                        var fetchedTotalPage = response.totalPage;
                        pageController.setTotalPage(fetchedTotalPage);
                        generatePageNumbers(fetchedTotalPage);
                    },
                    error: function(xhr, status, error) {
                        alert("Error");
                        console.log("Error: " + error);
                        console.log(xhr.responseText);
                    }
                });
            }
        }
		
        function generatePageNumbers(totalPage) {
            var pageNumbersClick = '';
            for (var i = 1; i <= totalPage; i++) {
                pageNumbersClick += '<span class="pageNumber">' + i + '</span>';
            }
            $("#pageNumbers").html(pageNumbersClick);

            $(".pageNumber").on('click', function() {
                var pageNumber = $(this).text();
                pageController.setCurrentPage(pageNumber);
                
                var selectedSort = $('#sortSelect').val();
                pageController.setOrderBy(selectedSort);
                console.log(selectedSort," selectedSort");           

                $.ajax({
                    url: "post/postPerPage.do",
                    type: "GET",
                    dataType: "json",
                    data: {
                        currentPage: sessionStorage.getItem('currentPage'),
                        sortType: sessionStorage.getItem('orderBy')
                    },
                    success: function(data) {
                        Posts(data);
                    },
                    error: function(xhr, status, error) {
                        alert("Error");
                        console.log("Error: " + error);
                        console.log(xhr.responseText);
                    }
                });
            });
        }

		// 동적으로 받은 게시글들 보여주기
        function Posts(data) { 
        	console.log(data)
            var tbody = $("#postsTbody");
            tbody.empty();
            data.forEach(function(post) {
                var createTime = new Date(post.createTime).toLocaleString(); 
                var $tr = $('<tr>');
                var $td = $('<td>');
                
                $tr.append($td.clone().html('<button class="detailPost" data-postid="' 
                    + post.postId + '">' + post.title + '</button>'));
                $tr.append($td.clone().text(post.content));
                
                var fileStatus = post.hasFile ? 'O' : 'X';
                $tr.append($td.clone().text(fileStatus));
                
                $tr.append($td.clone().text(post.view));
                $tr.append($td.clone().text(post.email));
                $tr.append($td.clone().text(createTime));
                tbody.append($tr);
                
            });
        }
    	// 게시글의 title 누르면 게시글 상세보기 페이지로
        $(document).on('click', '.detailPost', function() {
            var postId = $(this).data('postid'); 
            window.location.href = 'post/detailPost.do?postId=' + postId + '&viewSet=yes'; 
        });
		
    	// 사용자가 선택한 정렬 방식 넘겨줌
		$('#sortButton').on('click', function() {
			var selectedSort = $('#sortSelect').val();
			pageController.setOrderBy(selectedSort);
			$.ajax({
				url : "post/postPerPage.do",
				type : "GET",
				data : {
					sortType : sessionStorage.getItem('orderBy')
				},
				dataType : "json",
				success : Posts,
				error : function(xhr, status, error) {
					alert("error");
					console.log("에러: " + error);
					console.log(xhr.responseText);
				}
			});
		});
		
		$('#searchButton').on('click', function() {
			var keyword = $('input[name="Keyword"]').val();
			pageController.setSearchedKeyword(keyword);
			$.ajax({
				url : "post/searchPost.do",
				type : "GET",
				data : {
					keyword : sessionStorage.getItem('searchedKeyword') 
				},
				dataType : "json",
				success : Posts,
				error : function(xhr, status, error) {
					alert("error");
					console.log("에러: " + error);
					console.log(xhr.responseText);
				}
			});
			
			$.ajax({
		        url: "post/getSearchedPostCount.do",
		        type: "GET",
		        data: { keyword: keyword },
		        dataType: "json",
		        success: function(response) {
		            var totalPage = response.totalPage;
		            pageController.setTotalPage(totalPage);
		            showPageNum(totalPage); 
		        },
		        error: function(xhr, status, error) {
		            alert("error");
		            console.log("에러: " + error);
		            console.log(xhr.responseText);
		        }
		    });
		});
		
		function confirmUnregister() {
		    var confirmFlag = window.confirm("탈퇴하시겠습니까?");
		    if (confirmFlag) {
		    	sessionStorage.clear();
		        location.href = 'user/unregister.do';
		    }
		}
		
		function logoutAndClearSession() {
	        sessionStorage.clear(); 
	        location.href = 'user/logout.do'; 
	    }
	    function resetAndClearSession() {
	        sessionStorage.clear(); 
	        location.href = 'post/goShowAllPosts.do'; 
	    }
	
    </script>
</body>
</html>

