var mapLayerCreator = {
	sggLayer : null,
	emdLayer : null,
	koreaLayer : null,
	cctvLayer : null,
	daumMap : null,

	createLayer : function() {
		// 여기서 this는 mapLayerCreator
		this.createSggLayer(); // mapLayerCreator 객체 내부에 있는 메소드를 실행하기 위해
		this.createEmdLayer();
		this.createKoreaLayer();
		this.createCctvLayer();
	},

	createDaumMap : function() {
		// OpenLayer 맵 객체 할당
		this.daumMap = new ol.Map({
			// ol의 기본 컨트롤러 사용
			controls : ol.control.defaults().extend([]),
			/*
			 * WebGL (Web Graphics Library)을 사용하여 맵을 렌더링 canvas, dom 같은
			 * renderer도 존재하지만 WebGL이 가장 효율적인 방법
			 */
			renderer : "webgl",
			// OpenLayers 워터마크 비활성화
			logo : false,
			// 'map'이라는 ID의 맵을 사용
			target : "map",
			// 맵에 추가될 layer 배열
			layers : [],
			// 맵의 상호작용
			interactions : ol.interaction.defaults({
				// 드래그 기능 비활성화
				dragPan : false,
				// 휠 확대/축소 비활성화
				mouseWheelZoom : false
			}).extend([ new ol.interaction.DragPan({ // 드래그로 맵 이동 활성화
				// 기네시 효과 비활성화 (드래그 후 관성)
				kinetic : false
			}), new ol.interaction.MouseWheelZoom({ // 휠 확대/축소 활성화
				// 숫자가 커질수록 부드러운 움직임(다소 느릴수도 있음) ex) Google Earth
				duration : 95
			}) ]),
			// 뷰 설정
			view : new ol.View({
				// 맵의 투명 설정
				projection : ol.proj.get("EPSG:5186"),
				// 맵의 초기 중심 좌표
				center : [ 263846.4536899561, 586688.9485874075 ],
				// 초기 확대 레벨
				zoom : 13,
				// 최소 축소 레벨
				minZoom : 7,
				// 최대 확대 레벨
				maxZoom : 19
			})
		});
		// 5181 좌표게를 사용하는 프로젝션 객체 가져옴
		let _daumProjection = new ol.proj.get('EPSG:5181'); // 5181??????????
		// 다음 지도 서비스의 타일 원점
		let _daumOrigin = [ -30000, -60000 ];
		// 확대 레벨별 해상도
		let _daumResolutions = [ 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4,
				2, 1, 0.5, 0.25, 0.125 ];
		// 다음 지도 타일 레이어 생성
		let daumMapLayer = new ol.layer.Tile({
			// 레이어 이름 설정
			name : "다음맵",
			// 활성화: 초기에 레이어가 표시됨
			visible : true,
			// 레이어 소스 설정
			source : new ol.source.XYZ({
				// 타일의 프로젝션을 5181로 설정
				projection : _daumProjection,
				tileUrlFunction : function(coordinate) {
					// 14에서 현재 확대 레벨을 뺀 값
					let level = 14 - coordinate[0];
					// Y행 좌표는 OpenLayers의 좌표 체계와 반대
					let row = (coordinate[2] * -1) - 1;
					// X 좌표
					let col = coordinate[1];
					/*
					 * 서브 도메인 값을 계산하여 여러 서버에서 타일을 로드할 수 있도록 분산 동시 다운로드 증가, 로딩 속도
					 * 향상
					 */
					let subdomain = ((level + col) % 4) + 1;
					// 최종 타일 url 반환
					return "http://map" + subdomain
							+ ".daumcdn.net/map_2d/1909dms/L" + level + "/"
							+ row + "/" + col + ".png";
				},
				// 타일 그리드 설정
				tileGrid : new ol.tilegrid.TileGrid({
					// 타일 원점 설정
					origin : _daumOrigin,
					// 확대 레벨별 해상도 설정
					resolutions : _daumResolutions
				})
			}),
		});

		this.daumMap.addLayer(daumMapLayer);
	},

	createSggLayer : function() {
		this.sggLayer = new ol.layer.Tile({ // wms
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

		this.daumMap.addLayer(this.sggLayer);
	},

	createEmdLayer : function() {
		this.emdLayer = new ol.layer.Vector({ // wfs
			name : '읍면동WFS',
			visible : true,
			// 애니메이션중에는 레이어 업데이트 비활성화
			updateWhileAnimating : false,
			// 사용자와 상호작용 중에는 레이어 업데이트 비활성화
			updateWhileInteracting : false,
			type : "MULTIPOLYGON",
			// 레이어의 z-index
			zIndex : 1,
			// 레이어 전체 이름 설정
			fullName : "",
			// 레이어가 표시되는 최소 해상도
			minResolution : 0,
			// 최대 해상도
			maxResolution : Infinity,
			// 레이어 그룹 이름
			group : "지적 기반",
			source : mapSourceCreator.emdSource,
			// 각 feature마다 style 지정
			style : layerController.addStyle('emd_kor_nm')
		// addStyle("emdLayer")
		});

		this.daumMap.addLayer(this.emdLayer);

		let clickedEmd = new ol.interaction.Select({
			layers : [ this.emdLayer ],
			condition : ol.events.condition.click
		});

		this.daumMap.addInteraction(clickedEmd);

		let emdPopup = new ol.Overlay({
			element : document.createElement('div'),
			positioning : 'bottom-center',
			offset : [ 0, 0 ],
			stopEvent : true
		});

		this.daumMap.addOverlay(emdPopup);

		clickedEmd.on('select', function(event) {
			if (event.selected.length > 0) {
				let selectedFeature = event.selected[0];

				let emdName = selectedFeature.get('emd_kor_nm');
				// centroid 함수
				let emdCentroid = ol.extent.getCenter(selectedFeature.getGeometry().getExtent());
				//let clickedCoordinates = event.mapBrowserEvent.coordinate;

				emdPopup.getElement().innerHTML = emdName;
				emdPopup.setPosition(emdCentroid);
			} else {
				emdPopup.setPosition(undefined);
			}
		});
	},

	createKoreaLayer : function() {
		this.koreaLayer = new ol.layer.Vector({
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
			source : mapSourceCreator.koreaSource,
			style : layerController.addStyle('kais_korea_as')
		// addStyle("koreaLayer")
		});
		this.daumMap.addLayer(this.koreaLayer);

	},

	createCctvLayer : function() {
		this.cctvLayer = new ol.layer.Vector({
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
			source : mapSourceCreator.cctvSource
		// style : addStyle('asset_cctv')
		});

		let clickedCctv = new ol.interaction.Select({
			// 이벤트를 감지할 레이어 배열로 전달
			layers : [ this.cctvLayer ],
			// 조건: 클릭시
			condition : ol.events.condition.click
		});
				
		this.daumMap.addInteraction(clickedCctv);

		// popUp.className = 'tooltip'; 추후 css 적용하기 위해
		let cctvPopup = new ol.Overlay({
			element : document.createElement('div'),
			positioning : 'bottom-center',
			// cctv point 바로 위
			offset : [ 0, -10 ],
			// 맵에는 영향을 주지 않도록
			stopEvent : true
		});
		
		this.daumMap.addOverlay(cctvPopup);

		clickedCctv.on('select', function(event) {
			// 클릭된 피처가 있는 경우
			if (event.selected.length > 0) { // 반대는 event.deselected
				let selectedFeature = event.selected[0];
				// 배열로 담김
				// map.forEachFeatureAtPixel 로 변경하기
				// _annox, _annoy는 실제 좌표가 아님

				// 선택된 feature의 지오메트리 객체의 좌표
				// ol.Feature 안에서만
				/*
				 * let clickedCoordinates = selectedFeature.getGeometry()
				 * .getCoordinates(); $.ajax({ url :
				 * "map/getCctvNameByCoordinates.do", type : "GET", data : { x :
				 * clickedCoordinates[0], y : clickedCoordinates[1] }, success :
				 * function(response) { // 팝업 문구 cctvPopup.innerHTML = response; //
				 * 팝업 위치를 아까 클릭했던 위치로
				 * popUpLayOut.setPosition(clickedCoordinates); }, error :
				 * function(error) { console.error("Error:", error); } }); }
				 */

				let cctvName = selectedFeature.get("cctv_nm");
				let clickedCoordinates = event.mapBrowserEvent.coordinate;

				cctvPopup.getElement().innerHTML = cctvName;
				cctvPopup.setPosition(clickedCoordinates);
			}

			// else => event.deselected
			// 다른 거 클릭시 숨김 null, undefined, 경도 위도의 좌표 배열[x, y]

			else {
				cctvPopup.setPosition(null);
			}

		});

		this.daumMap.addLayer(this.cctvLayer);

	}
}
