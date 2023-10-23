function createEmdLayer(){
	var map = window.myApp.maps.daumMap;
	
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
        zIndex: 1,
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
        style: getStyleForLayer('emd_kor_nm') // // getStyleForLayer("emdLayer")
    });
    map.addLayer(emdWFSLayer);
}
