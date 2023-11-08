/*
 *  <var> 
 *  함수 외부에서 선언시 전역 범위
 *  함수 내부에서 선언시 함수 범위   
 *  재 선언, 수정 가능 
 *  var의 호이스팅(단점): 맨 위로 끌어올려지고, 초기화 됨(undefined)
 *   
 *  <let>
 *  블록 범위 
 *  수정 가능, 재선언 불가
 *  let의 호이스팅: var처럼 초기화 X (Reference Error)
 *   
 *  <const>
 *  블록 범위
 *  수정, 재선언 불가(java의 final 상수 느낌)
 *  따라서 선언 즉시 초기화되어야 함
 *  (메소드 const 경우 내부 속성은 수정 가능 java의 final list 느낌) 
 *  const의 호이스팅: 초기화 X
 *  
 */
var layerController = {

	onOffLayer : function(layerName) {

		// 레이어 끄는 로직
		const layer = mapLayerCreator.layers[layerName];

		if (!layer) {
			console.error("레이어가 없습니다!");
			return;
		}

		const onOff = layer.getVisible();
		layer.setVisible(!onOff);

		// 팝업 끄는 로직
		const popUpsMapping = {
			'emdLayer' : 'emdPopUp',
			'cctvLayer' : 'cctvPopUp'
		};

		if (!(layerName === 'koreaLayer')) {
			const popUpName = popUpsMapping[layerName];
			mapLayerCreator.popUps[popUpName].setPosition(null);
		}

	},

	setStyleWithResponse : function(layer, response) {
		let newStyle = new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : response.strokeColor,
				width : response.strokeWidth
			}),
			fill : new ol.style.Fill({
				color : response.fillColor
			}),
			text : new ol.style.Text({
				font : response.font,
				fill : new ol.style.Fill({
					color : response.textColor
				}),
				stroke : new ol.style.Stroke({
					color : response.textStrokeColor,
					width : response.textStrokeWidth
				})
			})
		});
		
		layer.setStyle(newStyle);
	},

	addStyle : function(attributeName, layer) {
		$.ajax({
			url : 'map/getLayerStyle.do',
			method : 'POST',
			data : {
				attributeName : attributeName
			},
			dataType : 'json',
			success : function(response) {
				console.log("success", response);
				layerController.setStyleWithResponse(layer, response); 
			},
			error : function(error) {
				console.error("스타일 정보를 가져오는 데 실패했습니다.", error);
			}
		});
	}
}
