function createCctvLayer() {
	var map = window.myApp.maps.daumMap;

	var cctvSource = new ol.source.Vector({
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
					typename : 'gmx:asset_cctv',
					outputFormat : 'json',
					srsname : 'EPSG:5186',
					// 필요한 feature 만 불러오기(성능 저하 방지)
					bbox : extent.join(',') + ',' + 'EPSG:5186'
				},
				dataType : 'json',
				success : function(data) {
					var features = _format.readFeatures(data);
					cctvSource.addFeatures(features);
				},
			});
		}
	});

	var cctvWFSLayer = new ol.layer.Vector({
		name : 'CCTV',
		visible : true,
		updateWhileAnimating : false,
		updateWhileInteracting : false,
		type : "POINT",
		zIndex : 2,
		fullName : "",
		minResolution : 0,
		maxResolution : Infinity,
		group : "지적 기반",
		source : cctvSource
		//style : getStyleForLayer('asset_cctv')
	});

	map.addLayer(cctvWFSLayer);

}
