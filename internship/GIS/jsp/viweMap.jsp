<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<!-- 해당 jsp파일을 불러주는 @Controller 기준으로 경로 설정하기 -->
<script type="text/javascript" src="../js/lib/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="../js/lib/proj4js-2.3.15/proj4.js"></script>
<!-- 오픈레이어스 6 -->
<link rel="stylesheet" type="text/css" href="../js/lib/ol-v6.4.3/ol.css">           
<script type="text/javascript" src="../js/lib/ol-v6.4.3/ol.js"></script>

<script type="text/javascript" src="../js/lib/proj4/proj4.js"></script>

<script src="../js/map/creator/baseMapCreator.js"></script>

<script src="../js/map/creator/mapSourceCreator.js"></script>
<script src="../js/map/creator/mapLayerCreator.js"></script>

<script src="../js/map/controller/layerController.js"></script>


<script>
	ol.proj.proj4.register(proj4);

	let console = window.console || {
		log : function() {
		}
	};
	/*
	 * centroid 함수로 center 구하기
	 *
	 * BUT!
	 * MultiPolygon의 경우, 빈 공간이 center가 될 수도 있음.
	 */
	//춘천
	let center = [ 263846.4536899561, 586688.9485874075 ];
	/* 좌표계 5186   proj4.defs('EPSG:5186', '+proj=tmerc +lat_0=38 +lon_0=127 
	 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs'); */
	let targetCRS = 5186;
	let map = null;

	$(document).ready(function() {

		baseMapCreator.createDaumMap();

		mapSourceCreator.createSource();
		mapLayerCreator.createLayer();
		
		// #toggle 내부의 모든 button 요소를 대상으로 클릭 이벤트
		$("#toggle > button").on("click", function() {
			const layerName = $(this).attr("id"); // 클릭한 버튼의 id 가져오기
			layerController.onOffLayer(layerName);
		});

	});
</script>

<style>
#map {
	width: 100%;
	height: 100vh;
	position: relative;
	margin: 0;
	padding: 0;
}

#toggle {
	opacity: 0.5; /* 투명도 */
	width: 100px;
	position: absolute;
	top: 10px;
	left: 10px;
	background-color: rgba(255, 255, 255, 0); /* 투명 */
	border-radius: 5px;
	z-index: 1000;
}

#toggle ul {
	padding: 0;
	margin: 0;
}

#toggle li {
	font-size: 12px;
	margin-bottom: 5px;
	padding: 5px;
}
</style>

<meta charset="UTF-8">
<base href="http://localhost:8080/egov40/">
<title>Map</title>
</head>
<!-- favicon.ico 404 오류  임시 해결 -->
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<body>
	<div id="map">
		<div id="toggle">
			<button type="button" id="koreaLayer">한반도</button>
			<button type="button" id="emdLayer">춘천시</button>
			<button type="button" id="cctvLayer">CCTV</button>
		</div>
	</div>
</body>
</html>
