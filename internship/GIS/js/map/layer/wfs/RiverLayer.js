function createRiverLayer() {
	var map = window.myApp.maps.daumMap;

	var riverSource = new ol.source.Vector({
		// bbox strategy
		strategy : ol.loadingstrategy.bbox,
		loader : function(extent, resolution, projection) {
			var _format = new ol.format.GeoJSON();
			$.ajax({
				url : "http://127.0.0.1:8000/xeus/wfs",
				data : {
					service : 'WFS',
					version : '1.1.0',
					request : 'GetFeature',
					typename : 'gmx:kais_river_as',
					outputFormat : 'json',
					srsname : 'EPSG:5179',
					// 필요한 feature 만 불러오기(성능 저하 방지)
					bbox : extent.join(',') + ',' + 'EPSG:5179'
				},
				dataType : 'json',
				success : function(data) {
					alert("성공")
					var features = _format.readFeatures(data);
					// 좌표계 변환
					// features.forEach((feature) =>
					// feature.getGeometry().transfrom('EPSG:5179',
					// 'EPSG:5186'));
					riverSource.addFeatures(features);
				},
			});
		}
	});

	var riverLayer = new ol.layer.Vector({
		name : '강뷰',
		visible : true,
		updateWhileAnimating : false,
		updateWhileInteracting : false,
		type : "POLYGON",
		zIndex : 3,
		fullName : "",
		minResolution : 0,
		maxResolution : Infinity,
		group : "지적 기반",
		source : riverSource
		//style : getStyleForLayer('kais_river_as')
	});

	map.addLayer(riverLayer);
}
