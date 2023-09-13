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

# team 별로 소속 선수 수를 조회 하되 선수가 많은 순으로 정렬 하시오
(team_id, team_name, 선수수)
select
t.team_id as 아이디,
t.team_name as 팀명,
count(p.player_id) as 소속선수
from exam.team t,
exam.player p
where t.team_id = p.team_id
group by t.team_id, t.team_name
order by 소속선수 desc;

# team 별로 소속 선수 수를 조회 하되 선수가 많은 순으로 정렬 하시오
 단 소속선수가 10명 이하인 팀만 조회 하시오
(team_id, team_name, 선수수)
select
t.team_id as 아이디,
t.team_name as 팀명,
count(p.player_id) as 소속선수
from exam.team t,
exam.player p
where t.team_id = p.team_id
group by t.team_id, t.team_name
having count(p.player_id) < 10
order by 소속선수 desc;

# player에서 키나 몸무게 정보가 없는 선수를 조회 하시오
select *
from exam.player p
where p.weight='' or p.height = '';

# team 별, position_name별로 소속 선수를 조회 하시오
 단 팀명, 포지션별로 정렬하시오
(아이디, 팀명, 포지션, 선수수)
select
t.team_id as 아이디,
t.team_name as 팀명,
p.position_name as 포지션,
count(p.player_id) as 소속선수
from exam.team t,
exam.player p
where t.team_id = p.team_id
group by t.team_id, t.team_name, p.position_name
order by 팀명, 포지션 ;

# 삼성블루윙즈 소속 선수들의 키와 몸무게를 숫자형식으로 읽어 조회 하시오 
 단, 키, 몸무게 정보가 없을 경우 -1을 출력하고 선수명으로 정렬하시오
(team_name, player_name, position_name, weight, height)
select
t.team_name as team,
p.player_name as player,
p.position_name as position,
case when p.weight = '' then -1
else cast(p.weight as integer) end as weight,
case when p.height = '' then -1
else cast(p.height as integer) end as height
from exam.team t,
exam.player p
where t.team_id = p.team_id
and t.team_name ='삼성블루윙즈'
order by p.player_name;

# 국내 선수의 포지션별, 평균 몸무게와 평균 키를 포지션별로 정렬하시오 
단 weight, height 값이 없는 경우는 계산에서 제외 하고
 평균키와 평균 몸무게는 소수점 이하 2자리까지 표현하시오(반올림 적용)
(position_name, avg_weidht, avg_height)
select
p.position_name as position_name,
round(avg(cast(p.weight as integer)),2) as avg_weight,
round(avg(cast(p.height as integer)),2) as avg_height
from exam.player p
where p.weight <> '' and p.height <>''
group by p.position_name
order by position_name ;

# 팀별, 홈경기장정보, 포지션별 선수수를 조회하시오 
 단, 정렬순서(팀 오름차순, position_name 내림차순, )
(team_id, team_name, stadium_name,seat_count, position_name, player_count)
select
t.team_id,
t.team_name,
s.stadium_name,
s.seat_count,
p.position_name,
count(p.player_id)
from exam.team t,
exam.stadium s,
exam.player p
where t.stadium_id = s.stadium_id
and p.team_id = t.team_id
group by t.team_id, t.team_name, s.stadium_name, s.seat_count, p.position_name
order by t.team_id, p.position_name desc ;

# player에서 가장 많은 MF를 보유한 팀정보를 조회 하시오 
(team_id, team_name, position_name, player_count)
select
t.team_id,
t.team_name,
p.position_name,
count(cast(p.player_id as integer)) as player_count
from exam.team t,
exam.player p
where p.team_id = t.team_id
and p.weight <> '' and p.height <>''
and p.position_name ='MF'
group by t.team_id, t.team_name, p.position_name
order by player_count desc
limit 1;


