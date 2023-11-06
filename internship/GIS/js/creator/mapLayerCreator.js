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
                    let cctvData = this.countCctvPointsAndNames(selectedFeature);
                    // 해당 읍면동에 대한 CCTV 갯수
                    let cctvCount = cctvData.count;
                    let cctvNamesList = cctvData.names; 
                    // 클릭 이동을 위해서 HTML 형식으로 구성
                    context += `<div>CCTV 수: ${cctvCount}개</div><ul>`;
                    cctvNamesList.forEach((name) => {
                    	// <a href="#"로 가짜(더미) 하이퍼링크, name.replace(/'/g, "\\'")로
						// 자바스크립트 에러 방지, return false로 가짜(더미) 하이퍼링크
                        context += `<li><a href="#" onclick="mapLayerCreator.moveToCctvCenter('${name.replace(/'/g, "\\'")}'); return false;">${name}</a></li>`;
                    });
                    context += '</ul>';
                }else { // (layer === this.layers.cctvLayer)
                	if(this.popUps.emdPopUp) {
                        this.popUps.emdPopUp.setPosition(undefined);
                    }
                }
                // 팝업 표시 위치
                let popUpCentroid = ol.extent.getCenter(selectedFeature.getGeometry().getExtent());
                popUp.getElement().innerHTML = context;
                popUp.setPosition(popUpCentroid);
            } else {
            	// 숨김
                popUp.setPosition(undefined);
            }
        });

        return popUp;
    },
    
    countCctvPointsAndNames: function(emdFeature) {
        let count = 0; 
        let cctvNames = []; 
        let cctvLayer = this.layers.cctvLayer; 
        let emdGeometry = emdFeature.getGeometry();

        // TODO 리펙토링 필요? 모든 CCTV 순회
        cctvLayer.getSource().forEachFeature(function(cctvFeature) {
            // 일단 모든 CCTV의 지오메트리 가져오기
            let cctvGeometry = cctvFeature.getGeometry();
              
            // CCTV 포인트가 읍면동 레이어 안에 있는지 확인
            if (emdGeometry.intersectsCoordinate(cctvGeometry.getCoordinates())) {
                count++;
                // 배열에 넣기 (javaScript에서는 push라는 메소드 사용)
                cctvNames.push(cctvFeature.get('cctv_nm'));
            }
        });
        
        return {count: count, names: cctvNames}; 
    },
    
    createResetButton: function(coordinates) {
    	// 리셋 버튼
        let resetButton = document.createElement('button');
        resetButton.innerHTML = 'Reset';
        // 화살표 함수(this 바인딩)
        resetButton.onclick = () => {
        	// 하이라이트 리셋( 화살표 함수 안 쓸거면 var self = mapLayerCreator; 이런식으로 설정)
            this.resetCctvHighlight(); // 화살표 함수를 사용했기 때문에 this는
										// mapLayerCreator, 화살표 함수 안 쓰면
										// resetButton 가리킴
            // 리셋버튼 제거
            baseMapCreator.baseMap.daumMap.removeOverlay(this.resetButtonOverlay);
        };

        this.resetButtonOverlay = new ol.Overlay({
            element: resetButton,
            position: coordinates,
            positioning: 'top-center',
            offset: [0, 15], 
        });

        baseMapCreator.baseMap.daumMap.addOverlay(this.resetButtonOverlay);
    },

    // 하이라이트 리셋 함수
    resetCctvHighlight: function () {
       
        let cctvLayer = this.layers.cctvLayer;
        // TODO 모든 cctv 참조하기에... cctv 많아지면...
        cctvLayer.getSource().getFeatures().forEach(function (feature) {
        	// 기본 스타일로~
            feature.setStyle(undefined); 
        });
        // 적용을 위해 새로고침
        cctvLayer.getSource().changed();
        
        let defaultView = baseMapCreator.baseMap.daumMap.getView();
        defaultView.animate({ // animate으로
            zoom: 12,
            duration: 1000 
        });
    },
    

    moveToCctvCenter: function(cctvName) {
    	// cctv 돌면서 cctv_nm을 키로 검색
    	let cctvFeature = this.layers.cctvLayer.getSource().getFeatures().find(function(feature) {
            return feature.get('cctv_nm') === cctvName;
        });
        // 만약 존재하면
        if (cctvFeature) {
        	// 해당 cctv의 좌표 얻기
            let coordinates = cctvFeature.getGeometry().getCoordinates();
            // 중심을 센터로
            baseMapCreator.baseMap.daumMap.getView().animate({
                center: coordinates,
                zoom: 15, 
                duration: 1000 
            });

            let highlightStyle = new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 10,
                    fill: new ol.style.Fill({
                        color: '#ff334b'
                    })
                })
            });

            cctvFeature.setStyle(highlightStyle);
            this.createResetButton(coordinates);

        }
    }

}
