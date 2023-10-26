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
		const layer = mapLayerCreator[layerName];
		
		// whiteList
		if (!layer) {
			console.error("레이어가 없습니다!");
		}
		const onOff = layer.getVisible();
		layer.setVisible(!onOff);

	},

	getStyleFromDB : function(attributeName) {
		return new Promise((resolve, reject) => {
			$.ajax({
				url : 'map/getLayerStyle.do',
				method : 'GET',
				data : {
					attributeName : attributeName
				},
				 dataType : 'json'
	        })
	        .done(function(response) {
	            resolve(response);
	        })
	        .fail(function(error) {
	            reject(error);
	        });
	    });
	},

	addStyle : function(attributeName) {
		layerController.getStyleFromDB(attributeName)
		// promise 가 succes일 때
		.then(response => {      
			let styleData = response;
			
			console.log("styleData" , styleData)
			console.log("strokeColor ", styleData.strokeColor)
			
			return function(feature, resolution) {
				let txt = feature.get(attributeName);
				return new ol.style.Style({
					stroke : new ol.style.Stroke({
						color : styleData.strokeColor,
						width : styleData.strokeWidth
					}),
					fill : new ol.style.Fill({
						color : styleData.fillColor
					}),
					text : new ol.style.Text({
						font : styleData.font,
						text : txt,
						fill : new ol.style.Fill({
							color : styleData.textcolor
						}),
						stroke : new ol.style.Stroke({
							color : styleData.textStrokeColor,
							width : styleData.textStrokeWidth,
						})
					})
				});
			};
	    })
	    .catch(error => {
	    	console.error("Error:", error);
	    });
	}
}
