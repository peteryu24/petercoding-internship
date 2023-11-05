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
        
        let popUpElement = document.createElement('div');
        popUpElement.className = 'tooltip';
        
        let popUp = new ol.Overlay({  
        	element: popUpElement,
            positioning: 'bottom-center',
            // feature 바로 위
            offset: [0, 0],
            // 맵에는 영향을 주지 않도록(클릭 이벤트 맵에 전달 X)
            stopEvent: true
        });

        baseMapCreator.baseMap.daumMap.addOverlay(popUp);
        
        clickedLayer.on('select', (event) => {
            if (event.selected.length > 0) {
                let selectedFeature = event.selected[0];
                let context = selectedFeature.get(featureName);
                if(layer === this.layers.emdLayer) {
                	// CCTV 포인트의 수와 이름을 가져옵니다.
                    let cctvData = this.countCctvPointsInEmdLayer(selectedFeature);
                    let cctvCount = cctvData.count;
                    let cctvNamesList = cctvData.names; // 이미 배열로 가정

                    // context에 CCTV 포인트의 수를 추가합니다.
                    context += '<div>CCTV 수: ' + cctvCount + '개</div>';
                    
                    // CCTV 이름 목록을 추가합니다. 각 이름은 별도의 줄에 표시됩니다.
                    for (let name of cctvNamesList) {
                        context += '<div>' + name + '</div>';
                    }
                  }
                let popUpCentroid = ol.extent.getCenter(selectedFeature.getGeometry().getExtent());
                popUp.getElement().innerHTML = context; // innerHTML을 사용하여 HTML을 설정합니다.
                popUp.setPosition(popUpCentroid);
            } else {
                popUp.setPosition(undefined);
            }
        });


        // popUps.emdPopUp랑 popUps.cctvPopUp에 할당하기 위해
        return popUp;
    },
    countCctvPointsInEmdLayer: function(emdFeature) {
        var count = 0; // 포인트를 세기 위한 변수입니다.
        var cctvNames = []; // CCTV 이름을 담을 배열입니다.
        var cctvLayer = this.layers.cctvLayer; // CCTV 레이어에 접근합니다.
        var emdGeometry = emdFeature.getGeometry();

        // CCTV Layer의 모든 피처(포인트)를 순회합니다.
        cctvLayer.getSource().forEachFeature(function(cctvFeature) {
            // 각 CCTV 피처의 지오메트리를 가져옵니다.
            var cctvGeometry = cctvFeature.getGeometry();
              
            // CCTV 포인트가 멀티폴리곤 안에 포함되어 있는지 확인합니다.
            if (emdGeometry.intersectsCoordinate(cctvGeometry.getCoordinates())) {
                count++; // 포함되어 있다면 카운트를 증가시킵니다.
                cctvNames.push(cctvFeature.get('cctv_nm')); // CCTV 이름을 배열에 추가합니다.
            }
        });

        return {count: count, names: cctvNames}; // 개수와 이름의 리스트를 함께 반환합니다.
    }
}
