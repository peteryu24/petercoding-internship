<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head> 
<!-- 해당 jsp파일을 불러주는 @Controller 기준으로 경로 설정하기 -->
<script type="text/javascript" src="../js/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="../js/proj4js-2.3.15/proj4.js"></script>
<!-- 오픈레이어스 6 -->
<link rel="stylesheet" type="text/css" href="../js/ol-v6.4.3/ol.css">
<script type="text/javascript" src="../js/ol-v6.4.3/ol.js"></script>
<script>
// EPSG: 전세계 좌표계 정의에 대한 고유한 명칭
// EPSG.io(webSite)를 통해 각 EPSG 코드에 대한 proj4와 wkt 문자열을 파악 가능
proj4.defs('EPSG:3857', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs');
proj4.defs('EPSG:4326', '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs');
proj4.defs('EPSG:900913', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs');


proj4.defs('EPSG:5179', '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');


proj4.defs('EPSG:5180', '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5182', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=550000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5183', '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5184', '+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');

proj4.defs('EPSG:5185', '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5186', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5187', '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5188', '+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');

//오류보정된 Bessel
proj4.defs('EPSG:5173', '+proj=tmerc +lat_0=38 +lon_0=125.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5174', '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5175', '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=550000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5176', '+proj=tmerc +lat_0=38 +lon_0=129.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5177', '+proj=tmerc +lat_0=38 +lon_0=131.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs("EPSG:5178", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=bessel +units=m +no_defs");
ol.proj.proj4.register(proj4);

var console = window.console || {
    log: function () {}
};

/*
 * centroid 함수로 center 구하기
 *
 * BUT!
 * MultiPolygon의 경우, 빈 공간이 center가 될 수도 있음.
 */
 
//춘천
var center = [263846.4536899561, 586688.9485874075]; 

// 좌표계 5186   proj4.defs('EPSG:5186', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
var targetCRS = 5186;

//맵 설정
var map = null;
$(document).ready(function () {
	// OpenLayer 맵 객체 할당
    var map = new ol.Map({
    	// ol의 기본 컨트롤러 사용
        controls: ol.control.defaults().extend([]),
    	/*
    	 * WebGL (Web Graphics Library)을 사용하여 맵을 렌더링
    	 * canvas, dom 같은 renderer도 존재하지만
    	 * WebGL이 가장 효율적인 방법
    	 */
        renderer: "webgl",
        // OpenLayers 워터마크 비활성화
        logo: false,
        // 'map'이라는 ID의 맵을 사용
        target: "map",
        // 맵에 추가될 layer 배열
        layers: [],
        // 맵의 상호작용
        interactions: ol.interaction.defaults({
        	// 드래그 기능 비활성화
            dragPan: false,
            // 휠 확대/축소 비활성화
            mouseWheelZoom: false
        }).extend([
                new ol.interaction.DragPan({ // 드래그로 맵 이동 활성화
                	// 기네시 효과 비활성화
                    kinetic: false
                }),
                new ol.interaction.MouseWheelZoom({ // 휠 확대/축소 활성화
                	// 숫자가 커질수록 부드러운 움직임(다소 느릴수도 있음)  ex) Google Earth
                    duration: 95
                })
            ]),
        // 뷰 설정
        view: new ol.View({
        	// 맵의 투명 설정
            projection: ol.proj.get("EPSG:5186"),
            // 맵의 초기 중심 좌표
            center: [263846.4536899561, 586688.9485874075],
            // 초기 확대 레벨
            zoom: 13,
            // 최소 축소 레벨
            minZoom: 7,
            // 최대 확대 레벨
            maxZoom: 19
        })
    });
	
	// 5181 좌표게를 사용하는 프로젝션 객체 가져옴
    var _daumProjection = new ol.proj.get('EPSG:5181');
	// 다음 지도 서비스의 타일 원점
    var _daumOrigin = [-30000, -60000];
	// 확대 레벨별 해상도
    var _daumResolutions = [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25, 0.125];
	// 다음 지도 타일 레이어 생성
    var _daumMapLayer = new ol.layer.Tile({
    	// 레이어 이름 설정
        name: "다음맵",
        // 활성화: 초기에 레이어가 표시됨
        visible: true,
        // 레이어 소스 설정
        source: new ol.source.XYZ({
        	// 타일의 프로젝션을 5181로 설정
            projection: _daumProjection,
            tileUrlFunction: function (coordinate) {
            	// 14에서 현재 확대 레벨을 뺀 값
                var level = 14 - coordinate[0];
            	// Y행 좌표는 OpenLayers의 좌표 체계와 반대
                var row = (coordinate[2] * -1) - 1;
            	// X 좌표
                var col = coordinate[1];
                /*
            	 *  서브 도메인 값을 계산하여 여러 서버에서 타일을 로드할 수 있도록 분산
            	 *  동시 다운로드 증가, 로딩 속도 향상
            	 */
                var subdomain = ((level + col) % 4) + 1;
                // 최종 타일 url 반환
                return "http://map" + subdomain + ".daumcdn.net/map_2d/1909dms/L" + level + "/" + row + "/" + col + ".png";
            },
            // 타일 그리드 설정
            tileGrid: new ol.tilegrid.TileGrid({
            	// 타일 원점 설정
                origin: _daumOrigin,
                // 확대 레벨별 해상도 설정
                resolutions: _daumResolutions
            })
        }),
    });

    map.addLayer(_daumMapLayer);
	
    // Vector 데이터를 표현하기 위해 Vector(point, line, polygon)
    var emdSource = new ol.source.Vector({
    	// bbox로 데이터 로딩      현재 보이는 면적에 해당하는 데이터만 서버에서 가져오기(성능향상)
        strategy: ol.loadingstrategy.bbox,
        // 특정 영역, 해상도, 투영에 대한 데이터를 로드하기 위해
        loader: function (extent, resolution, projection) {
        	// GeoJSON으로 파싱
            var _format = new ol.format.GeoJSON();
            $.ajax({
                url: "http://127.0.0.1:8000/xeus/wfs",
                data: {
                    service: 'WFS',
                    version: '1.1.0',
                    // 방식     (GetCapabilities, DescribeFeatureType, LockFeature, GetFeatureWithLock, Transaction)
                    request: 'GetFeature',
                    // 조회할 레이어 이름
                    typename: 'gmx:kais_emd_as',
                    outputFormat: 'json',
                    srsname: 'EPSG:5186',
                    bbox: extent.join(',') + ',' + 'EPSG:5186'
                },
                dataType: 'json',
                success: function (data) {
                	// 응답 데이터를 파싱하여 OpenLayers의 feature 객체 배열로 반환
                    var features = _format.readFeatures(data);
                	// Vector 소스에 추가
                    emdSource.addFeatures(features);
                },
            });
        }
    });
	
	/*
	 * WFS
	 * Web Feature Service
	 * 
	 * OGC에서 개발된 공간 데이터를 다루는 웹 기반 GIS 서비스
	 * 
	 * 지도 상에 개별 feature에 대한 정보를 제공하는 서비스
	 * client가 요청한 지리 정보를 서버에서 조회하고, feature의 상세 정보를 반환
	 * 
	 * WFS는 동적인 기능을 제공하며 지리 정보 CRUD 가능
	 * feature 속성 정보 뿐만 아니라 Geometry 정보(point, line, Polygon) 제공
	 */
    var emdWFSLayer = new ol.layer.Vector({
        name: '읍면동WFS',
        visible: true,
        // 애니메이션중에는 레이어 업데이트 비활성화
        updateWhileAnimating: false,
        // 사용자와 상호작용 중에는 레이어 업데이트 비활성화
        updateWhileInteracting: false,
        type: "MULTIPOLYGON",
        // 레이어의 z-index
        zIndex: 0,
        // 레이어 전체 이름 설정
        fullName: "",
        // 레이어가 표시되는 최소 해상도
        minResolution: 0,
        // 최대 해상도
        maxResolution: Infinity,
        // 레이어 그룹 이름
        group: "지적 기반",
        source: emdSource,
        // 각 feature마다 style 지정
        style: function (feature, resolution) {
            var txt = feature.get('emd_kor_nm');
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                	// green
                    color: 'rgba(38, 195, 70, 1)', 
                    width: 2
                }),
                fill: new ol.style.Fill({
                	//white
                    color: 'rgba(0, 0, 0, 0)'
                }),
                text: new ol.style.Text({
                    font: '15px Calibri,sans-serif',
                    text: txt,
                    fill: new ol.style.Fill({
                    	// white
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(38, 195, 70, 1)',
                        width: 3
                    })
                })
            });
        }
    });

    map.addLayer(emdWFSLayer);
    
	/*
	 * WMS
	 * Web Map Service
	 * 
	 * OGC에서 개발된 지도 이미지를 제공하는 웹기반 GIS 서비스
	 * 지도 이미지를 제공하는 서비스
	 * 
	 * WFS에서 제공하는 feature 정보와 유사하게 간단한 요약 정보는 제공 가능
	 */
    var sggWMSLayer = new ol.layer.Tile({
        name: '시군구WMS',
        visible: true,
        zIndex: 1,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        minResolution: 2,
        maxResolution: Infinity,
        source: new ol.source.TileWMS({
            url: "http://127.0.0.1:8000/xeus/wfs", //wms 요청 url
            params: {
                'LAYERS': 'kais_sig_as',
                'FORMAT': 'image/png',
                'STYLES': '',
                'TRANSPARENT': 'TRUE'
            }
        })
    });

    map.addLayer(sggWMSLayer);

    var korSource = new ol.source.Vector({
        // bbox strategy
        strategy: ol.loadingstrategy.bbox,
        loader: function (extent, resolution, projection) {
            var _format = new ol.format.GeoJSON();
            $.ajax({
                url: "http://127.0.0.1:8000/xeus/wfs",
                data: {
                    service: 'WFS',
                    version: '1.1.0',
                    request: 'GetFeature',
                    typename: 'gmx:kais_korea_as',
                    outputFormat: 'json',
                    srsname: 'EPSG:5186',
                    // 필요한 feature 만 불러오기(성능 저하 방지)
                    bbox: extent.join(',') + ',' + 'EPSG:5186'
                },
                dataType: 'json',
                success: function (data) {
                    var features = _format.readFeatures(data);
                    korSource.addFeatures(features);
                },
            });
        }
    });

    var koreaWFSLayer = new ol.layer.Vector({
        name: '대한민국',
        visible: true,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        type: "MULTIPOLYGON",
        zIndex: 2,
        fullName: "",
        minResolution: 0,
        maxResolution: Infinity,
        group: "지적 기반",
        source: korSource,
        style: function (feature, resolution) {
            var txt = feature.get('kais_korea_as');
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: 'rgba(38, 195, 70, 1)',
                    width: 2
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(0, 0, 0, 0)'
                }),
                text: new ol.style.Text({
                    font: '15px Calibri,sans-serif',
                    text: txt,
                    fill: new ol.style.Fill({
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(38, 195, 70, 1)',
                        width: 3
                    })
                })
            });
        }
    });

    map.addLayer(koreaWFSLayer);
    
    var cctvSource = new ol.source.Vector({
        // bbox strategy
        strategy: ol.loadingstrategy.bbox,
        loader: function (extent, resolution, projection) {
            var _format = new ol.format.GeoJSON();
            $.ajax({
                url: "http://127.0.0.1:8000/xeus/wfs",
                data: {
                    service: 'WFS',
                    version: '1.1.0',
                    request: 'GetFeature',
                    typename: 'gmx:asset_cctv',
                    outputFormat: 'json',
                    srsname: 'EPSG:5186',
                    // 필요한 feature 만 불러오기(성능 저하 방지)
                    bbox: extent.join(',') + ',' + 'EPSG:5186'
                },
                dataType: 'json',
                success: function (data) {
                    var features = _format.readFeatures(data);
                    cctvSource.addFeatures(features);
                },
            });
        }
    });

    var cctvWFSLayer = new ol.layer.Vector({
        name: 'CCTV',
        visible: true,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        type: "POINT",
        zIndex: 2,
        fullName: "",
        minResolution: 0,
        maxResolution: Infinity,
        group: "지적 기반",
        source: korSource,
        style: function (feature, resolution) {
            var txt = feature.get('asset_cctv');
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: 'rgba(38, 195, 70, 1)',
                    width: 2
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(0, 0, 0, 0)'
                }),
                text: new ol.style.Text({
                    font: '15px Calibri,sans-serif',
                    text: txt,
                    fill: new ol.style.Fill({
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(38, 195, 70, 1)',
                        width: 3
                    })
                })
            });
        }
    });

    map.addLayer(cctvWFSLayer);
    
    function toggleKoreaBoundary() {
		var visibility = koreaWFSLayer.getVisible();
		console.log("asdasdasdasdasdasdasdasdas")
		koreaWFSLayer.setVisible(!visibility);	
	}
	function toggleSggArea() {
		var visibility = sggWMSLayer.getVisible();
		sggWMSLayer.setVisible(!visibility);
	}
	
	$("#koreaLayer").on("click", function() {
		toggleKoreaBoundary();
	});
	$("#sggLayer").on("click", function() {
		toggleSggArea();
	});


});

</script>
<style>
	#map {
           width: 100%;
           height: 100%;
           position: absolute;
           margin:0;
           padding:0;
           background-color: #cfc2c3; /* 흰색 */
   }
   
   #toggle {
	    opacity: 0.5; /* 1은 완전 불투명, 0은 완전 투명 */
	    width: 100px; /* 또는 원하는 크기로 */
	}

	#toggle ul {
	    list-style-type: none; /* 리스트 표시기 제거 */
	    padding: 0; /* 패딩 제거 */
	    margin: 0; /* 마진 제거 */
	}
	
	#toggle li {
	    font-size: 12px; /* 작은 글씨 크기 */
	    margin-bottom: 5px; /* 항목 간의 간격 */
	    padding: 5px; /* 내부 패딩 */
	}

      
</style>


<meta charset="UTF-8">
<base href="http://localhost:8080/egov40/">
<title>Map</title>
</head>
<body>
	<div id="map">
		<div id="toggle">
			    <button type="button" id="koreaLayer">한반도</button>
			    <button type="button" id="sggLayer">춘천시</button>
		</div>
	</div>
</body>
</html>
