function createKoreaLayer() {
	var map = window.myApp.maps.daumMap;

	var korSource = new ol.source.Vector({
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
					typename : 'gmx:kais_korea_as',
					outputFormat : 'json',
					srsname : 'EPSG:5186',
					// 필요한 feature 만 불러오기(성능 저하 방지)
					bbox : extent.join(',') + ',' + 'EPSG:5186'
				},
				dataType : 'json',
				success : function(data) {
					var features = _format.readFeatures(data);
					korSource.addFeatures(features);
					
					/*$.ajax({
				        url: 'map/saveKoreaLayer.do',
				        method: 'POST',
				        data: JSON.stringify(layerData),
				        contentType: 'application/json',
				        success: function(response) {
				            console.log(response);
				        },
				        error: function(error) {
				            console.error("레이어 저장 실패:", error);
				        }
				    });*/

				},
			});
		}
	});
	
	var koreaWFSLayer = new ol.layer.Vector({
		name : '대한민국',
		visible : true,
		updateWhileAnimating : false,
		updateWhileInteracting : false,
		type : "MULTIPOLYGON",
		zIndex : 2,
		fullName : "",
		minResolution : 0,
		maxResolution : Infinity,
		group : "지적 기반",
		source : korSource,
		style : getStyleForLayer('kais_korea_as') // getStyleForLayer("koreaLayer")
	});

	map.addLayer(koreaWFSLayer);

}
