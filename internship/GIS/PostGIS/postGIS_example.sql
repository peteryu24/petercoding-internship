-- 1. point 생성
select st_makepoint(127.725704804671, 37.8778388677082);

-- 2. srid 추가
select st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326);

-- 3. 5179 좌표변환
select st_transform(st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326), 5179);

-- as_text
select st_astext(st_transform(st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326), 5179));

-- 포인트가 속한 읍면동 뽑기
select * from xeus."TL_SCCO_EMD" where st_contains( "GEOMETRY", st_transform(st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326) ,5179) )
-- 내가 속한 읍면동 뽑기 
select * from xeus.kais_emd_As where st_contains( _geometry, st_transform(st_setsrid(st_makepoint(127.7503291, 37.8557345), 4326), 5186) )


-- 포인트 100m buffer
select st_buffer(st_transform(st_setsrid(st_makepoint(263846.4536899561, 586688.9485874075), 5186),5179), 100);

-- 100m buffer 포인트 읍면동 조회(st_intersects)
select * from xeus."TL_SCCO_EMD" where st_intersects( "GEOMETRY", st_buffer(st_transform(st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326), 5179),100) )

-- 1000m buffer 포인트 내가 속한 읍면동 조회(st_intersects)
select * from xeus.kais_emd_As where st_intersects(  _geometry, st_buffer(st_transform(st_setsrid(st_makepoint(127.7503291, 37.8557345), 4326), 5186),1000) )

-- 100m buffer 포인트 읍면동 조회(st_dwithin)
select * from xeus."TL_SCCO_EMD" where st_dwithin( "GEOMETRY", st_transform(st_setsrid(st_makepoint(127.725704804671, 37.8778388677082), 4326), 5179), 100)

-- 1000m buffer 포인트 내가 속한 읍면동 조회(st_intersects)
select * from xeus.kais_emd_As where st_dwithin(_geometry, st_transform(st_setsrid(st_makepoint(127.7503291, 37.8557345), 4326), 5186),1000) 

-- 1000m buffer 포인트 내가 속한 읍면동 조회(st_distance)
select * from xeus.kais_emd_As where st_distance(  _geometry, st_transform(st_setsrid(st_makepoint(127.7503291, 37.8557345), 4326), 5186))<1000 


