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
                	// 해당 읍면동에 대한 CCTV 정보들
                    let cctvData = this.countCctvPointsInEmdLayer(selectedFeature);
                    // 해당 읍면동에 대한 CCTV 갯수
                    let cctvCount = cctvData.count;
                    let cctvNamesList = cctvData.names; 

                    context += `<div>CCTV 수: ${cctvCount}개</div><ul>`;
                    cctvNamesList.forEach((name) => {
                        context += `<li><a href="#" onclick="mapLayerCreator.centerMapOnCctv('${name.replace(/'/g, "\\'")}'); return false;">${name}</a></li>`;
                    });
                    context += '</ul>';
                }
                let popUpCentroid = ol.extent.getCenter(selectedFeature.getGeometry().getExtent());
                popUp.getElement().innerHTML = context;
                popUp.setPosition(popUpCentroid);
            } else {
                popUp.setPosition(undefined);
            }
        });

        return popUp;
    },
    
    countCctvPointsInEmdLayer: function(emdFeature) {
        var count = 0; 
        var cctvNames = []; 
        var cctvLayer = this.layers.cctvLayer; 
        var emdGeometry = emdFeature.getGeometry();


        cctvLayer.getSource().forEachFeature(function(cctvFeature) {
            // 일단 모든 CCTV의 지오메트리를 가져옵니다.
            var cctvGeometry = cctvFeature.getGeometry();
              
            // CCTV 포인트가 읍면동 레이어 안에 있는지 확인
            if (emdGeometry.intersectsCoordinate(cctvGeometry.getCoordinates())) {
                count++;
                // CCTV 이름을 배열에 추가
                cctvNames.push(cctvFeature.get('cctv_nm'));
            }
        });
        
        return {count: count, names: cctvNames}; 
    },
    createResetButton: function (coordinates) {
        var self = this;

        // Create a reset button element
        var resetButton = document.createElement('button');
        resetButton.innerHTML = 'Reset';
        resetButton.className = 'reset-button';

        // Define what happens on click
        resetButton.onclick = function () {
            self.resetCctvHighlight();
            baseMapCreator.baseMap.daumMap.removeOverlay(self.resetButtonOverlay);
        };

        // Create an overlay for the reset button
        this.resetButtonOverlay = new ol.Overlay({
            element: resetButton,
            position: coordinates,
            positioning: 'top-center',
            offset: [0, 0], // Adjust as needed
        });

        // Add the overlay to the map
        baseMapCreator.baseMap.daumMap.addOverlay(this.resetButtonOverlay);
    },
    resetCctvHighlight: function () {
        // Assuming that this function is meant to reset the style of all CCTV features
        var cctvLayer = this.layers.cctvLayer;
        cctvLayer.getSource().getFeatures().forEach(function (feature) {
            feature.setStyle(undefined); // This resets the feature to its default style
        });
        cctvLayer.getSource().changed(); // Refresh the source to apply the style changes
        
        var defaultView = baseMapCreator.baseMap.daumMap.getView();
        defaultView.animate({
            zoom: 12,
            duration: 1000 
        });
    },
    

    centerMapOnCctv: function(cctvName) {
        var self = this;
        var cctvFeature = this.layers.cctvLayer.getSource().getFeatures().find(function(feature) {
            return feature.get('cctv_nm') === cctvName;
        });

        if (cctvFeature) {
            var coordinates = cctvFeature.getGeometry().getCoordinates();
            baseMapCreator.baseMap.daumMap.getView().animate({
                center: coordinates,
                zoom: 15, // Adjust the zoom level as needed
                duration: 1000 // Duration for animation
            });

            // Define a highlight style
            var highlightStyle = new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 10,
                    fill: new ol.style.Fill({
                        color: '#ffcc33'
                    }),
                    stroke: new ol.style.Stroke({
                        color: '#fff',
                        width: 2
                    })
                })
            });

            // Apply the highlight style
            cctvFeature.setStyle(highlightStyle);
            this.createResetButton(coordinates);

        }
    }

}
