var mapSourceCreator = {

	emdSource : null,
	koreaSource : null,
	cctvSource : null,

	createSource : function() {
		this.createEmdSource();
		this.createKoreaSource();
		this.createCctvSource();
	},

	createEmdSource : function() {
		// 함수 내부에서 선언된 var와 같은 의미인 let(재선언은 불가능!)
		let emdSource = new ol.source.Vector({
			// bbox로 데이터 로딩 현재 보이는 면적에 해당하는 데이터만 서버에서 가져오기(성능향상)
			strategy : ol.loadingstrategy.bbox,
			// 특정 영역, 해상도, 투영에 대한 데이터를 로드하기 위해
			loader : function(extent, resolution, projection) {
				// GeoJSON으로 파싱
				var _format = new ol.format.GeoJSON();
				$.ajax({
					url : "http://127.0.0.1:8000/xeus/wfs",
					data : {
						service : 'WFS',
						version : '1.1.0',
						// 방식 (GetCapabilities, DescribeFeatureType,
						// LockFeature, GetFeatureWithLock, Transaction)
						request : 'GetFeature',
						// 조회할 레이어 이름
						typename : 'gmx:kais_emd_as',
						outputFormat : 'json',
						srsname : 'EPSG:5186',
						bbox : extent.join(',') + ',' + 'EPSG:5186'
					},
					dataType : 'json',
					success : function(data) {
						// 응답 데이터를 파싱하여 OpenLayers의 feature 객체 배열로 반환
						var features = _format.readFeatures(data);
						// Vector 소스에 추가
						emdSource.addFeatures(features);
					},
				});
			}
		});
		this.emdSource = emdSource;
	},

	createKoreaSource : function() {
		let koreaSource = new ol.source.Vector({
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
						koreaSource.addFeatures(features);
					},
				});
			}
		});
		this.koreaSource = koreaSource;
	},

	createCctvSource : function() {
		let cctvSource = new ol.source.Vector({
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
		this.cctvSource = cctvSource;
	}
}
