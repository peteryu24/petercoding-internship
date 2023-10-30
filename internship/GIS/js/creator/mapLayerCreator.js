var mapLayerCreator = {
	
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

	createLayer: function() {
		// 여기서 this는 mapLayerCreator
		// this = mapLayerCreator 객체 내부에 있는 메소드를 실행하기 위해
		this.createSggLayer();
		this.createEmdLayer();
		this.createKoreaLayer();
		this.createCctvLayer();
	},

	createSggLayer: function() {
		this.layers.sggLayer = new ol.layer.Tile({ // wms
			name: '시군구WMS',
			visible: true,
			zIndex: 0,
			updateWhileAnimating: false,
			updateWhileInteracting: false,
			minResolution: 2,
			maxResolution: Infinity,
			source: new ol.source.TileWMS({
				url: "http://127.0.0.1:8000/xeus/wfs", // wms 요청 url
				params: {
					'LAYERS': 'kais_sig_as',
					'FORMAT': 'image/png',
					'STYLES': '',
					'TRANSPARENT': 'TRUE'
				}
			})
		});

		baseMapCreator.baseMap.daumMap.addLayer(this.layers.sggLayer);
	},

	createEmdLayer: function() {
		this.layers.emdLayer = new ol.layer.Vector({ // wfs
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
			source: mapSourceCreator.emdSource,
			// 각 feature마다 style 지정
			style: layerController.addStyle('emd_kor_nm')
		});

		baseMapCreator.baseMap.daumMap.addLayer(this.layers.emdLayer);
		// 리턴받은 popUp을 popUps.emdPopUp에 담아줌
		this.popUps.emdPopUp = this.createPopup(this.layers.emdLayer, 'emd_kor_nm');
	},

	createKoreaLayer: function() {
		this.layers.koreaLayer = new ol.layer.Vector({
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
			source: mapSourceCreator.koreaSource,
			style: layerController.addStyle('kais_korea_as')
		});
		
		baseMapCreator.baseMap.daumMap.addLayer(this.layers.koreaLayer);
	},

	createCctvLayer: function() {
		this.layers.cctvLayer = new ol.layer.Vector({
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
			source: mapSourceCreator.cctvSource
		});
		
		baseMapCreator.baseMap.daumMap.addLayer(this.layers.cctvLayer);
		
		this.popUps.cctvPopUp = this.createPopup(this.layers.cctvLayer, 'cctv_nm');		
	},
	
	createPopup: function(layer, featureName) {	
        let clickedLayer = new ol.interaction.Select({
        	// 이벤트를 감지할 레이어 배열로 전달
            layers: [layer],
            // 조건: 클릭시
            condition: ol.events.condition.click
        });

        baseMapCreator.baseMap.daumMap.addInteraction(clickedLayer);

        let popUp = new ol.Overlay({
        	// popUp.className = 'tooltip'; 추후 css 적용하기 위해
            element: document.createElement('div'),
            positioning: 'bottom-center',
            // feature 바로 위
            offset: [0, 0],
            // 맵에는 영향을 주지 않도록
            stopEvent: true
        });

        baseMapCreator.baseMap.daumMap.addOverlay(popUp);
        
        // 클릭된 피처가 있는 경우
        clickedLayer.on('select', (event) => {
            if (event.selected.length > 0) {
            	// 배열로 담김
                let selectedFeature = event.selected[0];
                let label = selectedFeature.get(featureName); // forEachFeatureAtPixel
                let popUpCentroid = ol.extent.getCenter(selectedFeature
						.getGeometry().getExtent());
                // let coordinates = event.mapBrowserEvent.coordinate;
                popUp.getElement().innerHTML = label;
                popUp.setPosition(popUpCentroid);
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
