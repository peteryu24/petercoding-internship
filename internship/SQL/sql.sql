# team을 창립년도 순으로 정렬하여 조회 하시오
select * from exam.team order by orig_yyyy;

# team에서 region_name 이 수원 또는 전남인 팀을 조회 하시오
select * from exam.team where region_name='수원' or region_name = '전남';

# team에서 2000년 이후 생성된 팀(orig_yyyy)을 년도순으로 정렬하시오
select * from exam.team where orig_yyyy > '2000' order by orig_yyyy;

# team, stadium을 조인하여 팀별로 아래 정보를 조회 하시오
(team_id, team_name, stadium_name, seat_count)
select
t.team_id,
t.team_name,
s.stadium_name,
s.seat_count
from exam.team t,
exam.stadium s
where t.stadium_id = s.stadium_id;

# team, stadium을 조인하여 경기장 좌석수가 40000 보다 큰 경기장을 가지고 있는
 팀을 조회하되 좌석수가 많은 순으로 아래 항목을 조회 하시오
(team_id, team_name, stadium_name, seat_count)
select
t.team_id,
t.team_name,
s.stadium_name,
s.seat_count
from exam.team t,
exam.stadium s
where t.stadium_id = s.stadium_id
and s.seat_count > 40000
order by s.seat_count desc;

#team에서 ddd를 조회하된 중복을 제거하고 조회 하시오
select distinct ddd from exam.team;

#team에서 지역번호(ddd)기준으로 지역별 구단 갯수를 조회 하기
select ddd, count(ddd) from exam.team group by ddd order by ddd;

# stadium을 전체 조회하되 팀의 홈구장인 경우 team_id, team_name을 포함하여 조회하시오
 단 team이 없는 경우 stadium 정보만 출력하시오
(team_id, team_name, stadium_name, seat_count)
select
t.team_id,
t.team_name,
s.stadium_name,
s.seat_count
from exam.stadium s
left join exam.team t on t.stadium_id = s.stadium_id;
 또는 
select
t.team_id,
t.team_name,
s.stadium_name,
s.seat_count
from exam.team t
right join exam.stadium s on t.stadium_id = s.stadium_id ;
