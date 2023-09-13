--1. 강릉종합경기장에서 경기가 예정되어 있는 팀이름과 해당 팀에 소속된 선수목록을 조회한다.
SELECT 
	t.team_name
	,p.player_name
FROM 	
	exam.stadium s
	,exam.team t
	,exam.player p
WHERE 	
	s.stadium_name = '강릉종합경기장'
AND 	
	s.stadium_id = t.stadium_id
AND 
	t.team_id = p.team_id;



--2. 각 팀별로 키가 180 이상, 몸무게 90 이하인 선수들의 카운트를 조회한다. 단. 조건으로 사용하는 부분은 반드시 문자가 아닌 숫자로 비교한다.
SELECT 
	t.team_name
	,COUNT(p.player_id) AS count_players
FROM 
	exam.team AS t
JOIN 
	exam.player AS p ON t.team_id = p.team_id
WHERE CAST
	(p.height AS INTEGER) >= 180
  AND CAST
	(p.weight AS INTEGER) <= 90
GROUP BY 
	t.team_name;

--3. 수원월드컵경기장에서 경기가 예정되어 있는 팀에서 'DF' 포지션을 담당하는 선수의 정보를 조회한다.
SELECT 
	t.team_name	
	,p.player_name
	,p.position_name
FROM 
	exam.stadium s
	,exam.team t
	,exam.player p
WHERE 
	s.stadium_name = '수원월드컵경기장'
  AND 
	s.stadium_id = t.stadium_id
  AND 
	t.team_id = p.team_id
  AND 
	p.position_name = 'DF';

--4. 팀 이름에 'FC' 단어가 포함된 팀 이름을 조회하고 각 팀마다 'MF' 포지션을 담당하는 선수의 수를 조회한다.
SELECT 
	t.team_name
	,COUNT(p.player_id) AS count_mf_players
FROM 
	exam.team t
	,exam.player p
WHERE 
	t.team_id = p.team_id
  AND 
	t.team_name LIKE '%FC%'
  AND 
	p.position_name = 'MF'
GROUP BY 
	t.team_name;

--5. 각 팀에서 '1990'년생 이상인 선수의 수를 조회한다.
SELECT 
	t.team_name	
	,COUNT(p.player_id) AS count_players
FROM 
	exam.team t
	,exam.player p
WHERE 
	t.team_id = p.team_id
AND 
	CAST(SUBSTRING(p.birth_date, 1, 4) AS INT) >= 1990
GROUP BY 
	t.team_name;
