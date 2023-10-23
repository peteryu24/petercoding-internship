function getStyleForLayer(attributeName) {
	return function(feature, resolution) {
		var txt = feature.get(attributeName);
		return new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'rgba(38, 195, 70, 1)',
				width : 2
			}),
			fill : new ol.style.Fill({
				color : 'rgba(0, 0, 0, 0)'
			}),
			text : new ol.style.Text({
				font : '15px Calibri,sans-serif',
				text : txt,
				fill : new ol.style.Fill({
					color : '#fff'
				}),
				stroke : new ol.style.Stroke({
					color : 'rgba(38, 195, 70, 1)',
					width : 3
				})
			})
		});
	};
}

/*function getStyleFromServer(attributeName, callback) {
    $.ajax({
        url: "map/getLayerStyle.do", 
        type: "GET",
        data: {
        	attributeName: attributeName
        },
        success: function(response) {
            callback(response);
        },
        error: function(error) {
            console.error(error);
        }
    });
}

function getStyleForLayer(attributeName) {
    return function(feature, resolution) {
        getStyleFromServer(attributeName, function(styleData) {
            var txt = feature.get(attributeName);
            
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: styleData.strokeColor,
                    width: styleData.strokeWidth
                }),
                fill: new ol.style.Fill({
                    color: styleData.fillColor
                }),
                text: new ol.style.Text({
                    font: styleData.font,
                    text: txt,
                    fill: new ol.style.Fill({
                        color: styleData.textColor
                    }),
                    stroke: new ol.style.Stroke({
                        color: styleData.textStrokeColor,
                        width: styleData.textStrokeWidth
                    })
                })
            });
        });
    };
}*/
