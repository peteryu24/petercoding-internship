createLayerWithInteraction: function(layerConfig) {
    let newLayer = new ol.layer[layerConfig.layerType]({
        name: layerConfig.name,
        visible: true,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        type: layerConfig.geometryType,
        zIndex: layerConfig.zIndex,
        minResolution: 0,
        maxResolution: Infinity,
        group: "지적 기반",
        source: mapSourceCreator[layerConfig.source]
    });

    this.baseMap.daumMap.addLayer(newLayer);

    let interaction = new ol.interaction.Select({
        layers: [newLayer],
        condition: ol.events.condition.click
    });

    this.baseMap.daumMap.addInteraction(interaction);

    this.popUps[layerConfig.popUpName] = new ol.Overlay({
        element: document.createElement('div'),
        positioning: 'bottom-center',
        offset: layerConfig.popUpOffset,
        stopEvent: true
    });

    this.baseMap.daumMap.addOverlay(this.popUps[layerConfig.popUpName]);

    interaction.on('select', (event) => {
        if (event.selected.length > 0) {
            let selectedFeature = event.selected[0];
            let featureName = selectedFeature.get(layerConfig.featureAttribute);
            let clickedCoordinates = event.mapBrowserEvent.coordinate;

            this.popUps[layerConfig.popUpName].getElement().innerHTML = featureName;
            this.popUps[layerConfig.popUpName].setPosition(clickedCoordinates);
        } else {
            this.popUps[layerConfig.popUpName].setPosition(undefined);
        }
    });
},

createEmdLayer: function() {
    this.createLayerWithInteraction({
        layerType: "Vector",
        name: "읍면동WFS",
        geometryType: "MULTIPOLYGON",
        zIndex: 1,
        source: "emdSource",
        popUpName: "emdPopUp",
        popUpOffset: [0, 0],
        featureAttribute: 'emd_kor_nm'
    });
},

createCctvLayer: function() {
    this.createLayerWithInteraction({
        layerType: "Vector",
        name: "CCTV",
        geometryType: "POINT",
        zIndex: 2,
        source: "cctvSource",
        popUpName: "cctvPopUp",
        popUpOffset: [0, -10],
        featureAttribute: 'cctv_nm'
    });
},
