<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="../js/jquery-3.2.0.min.js"></script>
<script type="text/javascript" src="../js/proj4js-2.3.15/proj4.js"></script>
<!-- 오픈레이어스 6 -->
<link rel="stylesheet" type="text/css" href="../js/ol-v6.4.3/ol.css">
<script type="text/javascript" src="../js/ol-v6.4.3/ol.js"></script>

<script>

proj4.defs('EPSG:3857', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs');
proj4.defs('EPSG:4326', '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs');
proj4.defs('EPSG:900913', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs');


proj4.defs('EPSG:5179', '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');


proj4.defs('EPSG:5180', '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5182', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=550000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5183', '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
proj4.defs('EPSG:5184', '+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');

proj4.defs('EPSG:5185', '+proj=tmerc +lat_0=38 +lon_0=125 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5186', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5187', '+proj=tmerc +lat_0=38 +lon_0=129 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
proj4.defs('EPSG:5188', '+proj=tmerc +lat_0=38 +lon_0=131 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');

//오류보정된 Bessel
proj4.defs('EPSG:5173', '+proj=tmerc +lat_0=38 +lon_0=125.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5174', '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5175', '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=550000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5176', '+proj=tmerc +lat_0=38 +lon_0=129.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs('EPSG:5177', '+proj=tmerc +lat_0=38 +lon_0=131.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
proj4.defs("EPSG:5178", "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=bessel +units=m +no_defs");
ol.proj.proj4.register(proj4);

var console = window.console || {
    log: function () {}
};

/*
 * centroid 함수로 center 구하기
 *
 * BUT!
 * MultiPolygon의 경우, 빈 공간이 center가 될 수도 있음.
 */
var center = [263846.4536899561, 586688.9485874075]; // 춘천
var targetCRS = 5186;

//맵 설정
var map = null;
$(document).ready(function () {
	console.log(ol)
    var map = new ol.Map({
        controls: ol.control.defaults().extend([]),
        renderer: "webgl",
        logo: false,
        target: "map",
        layers: [],
        interactions: ol.interaction.defaults({
            dragPan: false,
            mouseWheelZoom: false
        }).extend([
                new ol.interaction.DragPan({
                    kinetic: false
                }),
                new ol.interaction.MouseWheelZoom({
                    duration: 0
                })
            ]),
        view: new ol.View({
            projection: ol.proj.get("EPSG:5186"),
            center: [263846.4536899561, 586688.9485874075],
            zoom: 11,
            minZoom: 8,
            maxZoom: 23
        })
    });

    var _daumProjection = new ol.proj.get('EPSG:5181');
    var _daumOrigin = [-30000, -60000];
    var _daumResolutions = [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25, 0.125];
    var _daumMapLayer = new ol.layer.Tile({
        name: "다음맵",
        visible: true,
        source: new ol.source.XYZ({
            projection: _daumProjection,
            tileUrlFunction: function (coordinate) {
                var level = 14 - coordinate[0];
                var row = (coordinate[2] * -1) - 1;
                var col = coordinate[1];
                var subdomain = ((level + col) % 4) + 1;
                return "http://map" + subdomain + ".daumcdn.net/map_2d/1909dms/L" + level + "/" + row + "/" + col + ".png";
            },
            tileGrid: new ol.tilegrid.TileGrid({
                origin: _daumOrigin,
                resolutions: _daumResolutions
            })
        }),
    });

    map.addLayer(_daumMapLayer);

    var emdSource = new ol.source.Vector({
        strategy: ol.loadingstrategy.bbox,
        loader: function (extent, resolution, projection) {
            var _format = new ol.format.GeoJSON();
            $.ajax({
                url: "http://127.0.0.1:8000/xeus/wfs",
                data: {
                    service: 'WFS',
                    version: '1.1.0',
                    request: 'GetFeature',
                    typename: 'gmx:kais_emd_as',
                    outputFormat: 'json',
                    srsname: 'EPSG:5186',
                    bbox: extent.join(',') + ',' + 'EPSG:5186'
                },
                dataType: 'json',
                success: function (data) {
                    var features = _format.readFeatures(data);
                    emdSource.addFeatures(features);
                },
            });
        }
    });

    var emdWFSLayer = new ol.layer.Vector({
        name: '읍면동WFS',
        visible: true,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        type: "MULTIPOLYGON",
        zIndex: 0,
        fullName: "",
        minResolution: 0,
        maxResolution: Infinity,
        group: "지적 기반",
        source: emdSource,
        style: function (feature, resolution) {
            var txt = feature.get('emd_kor_nm');
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: 'rgba(38, 195, 70, 1)',
                    width: 2
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(0, 0, 0, 0)'
                }),
                text: new ol.style.Text({
                    font: '15px Calibri,sans-serif',
                    text: txt,
                    fill: new ol.style.Fill({
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(38, 195, 70, 1)',
                        width: 3
                    })
                })
            });
        }
    });

    map.addLayer(emdWFSLayer);

    var sigWMSLayer = new ol.layer.Tile({
        name: '시군구WMS',
        visible: true,
        zIndex: 1,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        minResolution: 2,
        maxResolution: Infinity,
        source: new ol.source.TileWMS({
            url: "http://127.0.0.1:8000/xeus/wfs", //wms 요청 url
            params: {
                'LAYERS': 'kais_sig_as',
                'FORMAT': 'image/png',
                'STYLES': '',
                'TRANSPARENT': 'TRUE'
            }
        })
    });

    map.addLayer(sigWMSLayer);

    var emdSource2 = new ol.source.Vector({
        // bbox strategy
        strategy: ol.loadingstrategy.bbox,
        loader: function (extent, resolution, projection) {
            var _format = new ol.format.GeoJSON();
            $.ajax({
                url: "http://127.0.0.1:8000/xeus/wfs",
                data: {
                    service: 'WFS',
                    version: '1.1.0',
                    request: 'GetFeature',
                    typename: 'gmx:kais_korea_as',
                    outputFormat: 'json',
                    srsname: 'EPSG:5186',
                    // 필요한 feature 만 불러오기(성능 저하 방지)
                    bbox: extent.join(',') + ',' + 'EPSG:5186'
                },
                dataType: 'json',
                success: function (data) {
                    var features = _format.readFeatures(data);
                    emdSource2.addFeatures(features);
                },
            });
        }
    });

    var koreaWFSLayer = new ol.layer.Vector({
        name: '도로중심선',
        visible: true,
        updateWhileAnimating: false,
        updateWhileInteracting: false,
        type: "MULTIPOLYGON",
        zIndex: 2,
        fullName: "",
        minResolution: 0,
        maxResolution: Infinity,
        group: "지적 기반",
        source: emdSource2,
        style: function (feature, resolution) {
            var txt = feature.get('kais_korea_as');
            return new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: 'rgba(38, 195, 70, 1)',
                    width: 2
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(0, 0, 0, 0)'
                }),
                text: new ol.style.Text({
                    font: '15px Calibri,sans-serif',
                    text: txt,
                    fill: new ol.style.Fill({
                        color: '#fff'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(38, 195, 70, 1)',
                        width: 3
                    })
                })
            });
        }
    });

    map.addLayer(koreaWFSLayer);

});

</script>
<style>
	#map {
           width: 100%;
           height: 100%;
           position: absolute;
           margin:0;
           padding:0;
           background-color: #cfc2c3; /* 흰색 */
       }
</style>


<meta charset="UTF-8">
<base href="http://localhost:8080/egov40/">
<title>Map</title>
</head>
<body>
	<div id="map"></div>
</body>
</html>
