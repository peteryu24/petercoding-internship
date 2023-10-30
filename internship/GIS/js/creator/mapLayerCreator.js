var mapLayerCreator = {
	
	baseMap: {daumMap: null},

	layers: {
        sggLayer: null,
        emdLayer: null,
        koreaLayer: null,
        cctvLayer: null,
    },
   
    popUps: {
        emdPopUp: null,
        cctvPopUp: null,
    },

	createLayer : function() {
		// 여기서 this는 mapLayerCreator
		// this = mapLayerCreator 객체 내부에 있는 메소드를 실행하기 위해
		this.createSggLayer();
		this.createEmdLayer();
		this.createKoreaLayer();
		this.createCctvLayer();

	},

	createDaumMap : function() {
		// OpenLayer 맵 객체 할당
		this.baseMap.daumMap = new ol.Map({
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

		this.baseMap.daumMap.addLayer(daumMapLayer);
	},

	createSggLayer : function() {
		this.layers.sggLayer = new ol.layer.Tile({ // wms
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

		this.baseMap.daumMap.addLayer(this.layers.sggLayer);
	},

	createEmdLayer : function() {
		this.layers.emdLayer = new ol.layer.Vector({ // wfs
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
		});

		this.baseMap.daumMap.addLayer(this.layers.emdLayer);
		// 팝업 생성하면서 popUps.emdPopUp에 담아줌
		this.popUps.emdPopUp = this.createPopup(this.layers.emdLayer, 'emd_kor_nm');
	},

	createKoreaLayer : function() {
		this.layers.koreaLayer = new ol.layer.Vector({
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
		});
		
		this.baseMap.daumMap.addLayer(this.layers.koreaLayer);
	},

	createCctvLayer : function() {
		this.layers.cctvLayer = new ol.layer.Vector({
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
		});
		
		this.baseMap.daumMap.addLayer(this.layers.cctvLayer);
		
		this.popUps.cctvPopUp = this.createPopup(this.layers.cctvLayer, 'cctv_nm');		
	},
	
	createPopup: function(layer, featureName) {	
        let clickedLayer = new ol.interaction.Select({
        	// 이벤트를 감지할 레이어 배열로 전달
            layers: [layer],
            // 조건: 클릭시
            condition: ol.events.condition.click
        });

        this.baseMap.daumMap.addInteraction(clickedLayer);

        let popUp = new ol.Overlay({
        	// popUp.className = 'tooltip'; 추후 css 적용하기 위해
            element: document.createElement('div'),
            positioning: 'bottom-center',
            // feature 바로 위
            offset: [0, 0],
            // 맵에는 영향을 주지 않도록
            stopEvent: true
        });

        this.baseMap.daumMap.addOverlay(popUp);
        
        // 클릭된 피처가 있는 경우
        clickedLayer.on('select', (event) => {
            if (event.selected.length > 0) {
            	// 배열로 담김
                let selectedFeature = event.selected[0];
                let label = selectedFeature.get(featureName);
                let coordinates = event.mapBrowserEvent.coordinate;

                popUp.getElement().innerHTML = label;
                popUp.setPosition(coordinates);
            } else {
                popUp.setPosition(undefined);
                // else => event.deselected
    			// 다른 거 클릭시 숨김 null, undefined, 경도 위도의 좌표 배열[x, y]
            }
        });
        // popUps.emdPopUp랑 popUps.cctvPopUp에 할당하기 위해
        return popUp;
    }

}
