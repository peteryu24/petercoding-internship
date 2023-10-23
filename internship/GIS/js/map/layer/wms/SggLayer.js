function createSggLayer() {
	var map = window.myApp.maps.daumMap;

	/*
	 * WMS Web Map Service
	 * 
	 * OGC에서 개발된 지도 이미지를 제공하는 웹기반 GIS 서비스 지도 이미지를 제공하는 서비스
	 * 
	 * WFS에서 제공하는 feature 정보와 유사하게 간단한 요약 정보는 제공 가능
	 */
	var sggWMSLayer = new ol.layer.Tile({
		name : '시군구WMS',
		visible : true,
		zIndex : 0,
		updateWhileAnimating : false,
		updateWhileInteracting : false,
		minResolution : 2,
		maxResolution : Infinity,
		source : new ol.source.TileWMS({
			url : "http://127.0.0.1:8000/xeus/wfs", // wms 요청 url
			params : {
				'LAYERS' : 'kais_sig_as',
				'FORMAT' : 'image/png',
				'STYLES' : '',
				'TRANSPARENT' : 'TRUE'
			}
		})
	});

	map.addLayer(sggWMSLayer);
}
