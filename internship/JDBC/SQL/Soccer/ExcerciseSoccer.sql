--1. 강릉종합경기장에서 경기가 예정되어 있는 팀이름과 해당 팀에 소속된 선수목록을 조회
SELECT 
	t.team_name    --팀이름과 소속된 선수목록 조회
	,p.player_name
FROM 	
	exam.stadium s -- 강릉종합경기장 추출을 위해
	,exam.team t
	,exam.player p
WHERE 	
	s.stadium_name = '강릉종합경기장'
	AND s.stadium_id = t.stadium_id -- 강릉종합경기장에서 경기하는 팀 목록
	AND t.team_id = p.team_id; -- 해당 팀의 선수들 목록


--2. 각 팀별로 키가 180 이상, 몸무게 90 이하인 선수들의 카운트를 조회. 단, 반드시 문자가 아닌 숫자로 비교
SELECT 
    t.team_name
    ,COUNT(p.player_id) AS 선수수 -- 카운트 조회
FROM 
    exam.team t
    ,exam.player p
WHERE 
    t.team_id = p.team_id
    AND CAST(NULLIF(p.height, '') AS INTEGER) >= 180 --NULLIF를 사용하지 않을 경우 CAST 함수 떄문에 NULL인 데이터에 대해 에러 발생
    AND CAST(NULLIF(p.weight, '') AS INTEGER) <= 90 -- NULLIF(asd,'')인 경우 앞과 뒤를 비교해 같으면 NULL, 다를 경우 첫번째 인자 반환
GROUP BY 
    t.team_name; -- 팀별로


--3. 수원월드컵경기장에서 경기가 예정되어 있는 팀에서 'DF' 포지션을 담당하는 선수의 정보를 조회
SELECT 
	t.team_name	
	,p.*
FROM 
	exam.stadium s
	,exam.team t
	,exam.player p
WHERE 
	s.stadium_name = '수원월드컵경기장'
	AND s.stadium_id = t.stadium_id -- 수원월드컵경기장에서 결기가 예정되어 있는 팀들
	AND t.team_id = p.team_id -- 각 팀에 소속된 선수들
	AND p.position_name = 'DF'; -- 중에서 수비수들


--4. 팀 이름에 'FC' 단어가 포함된 팀 이름을 조회하고 각 팀마다 'MF' 포지션을 담당하는 선수의 수를 조회
SELECT 
	t.team_name
	,COUNT(p.player_id) AS 선수수
FROM 
	exam.team t
	,exam.player p
WHERE 
	t.team_id = p.team_id
	AND t.team_name LIKE '%FC%' -- 팀 이름 사이에 아무곳이나 FC가 포함된 
	AND p.position_name = 'MF' -- 팀 중에서 선수들 중에서 미드필더들
GROUP BY 
	t.team_name; -- 각팀 마다


--5. 각 팀에서 '1990'년생 이상인 선수의 수를 조회

-- 'MM-DD-YYYY' 형식의 날짜 값을 'YYYY-MM-DD' 형식으로 업데이트 (NULL 값은 무시)
UPDATE exam.player -- 생년원일에 대한 형식이 자유자재라 정렬화 필요
SET birth_date = (
    CASE WHEN birth_date IS NOT NULL 
        THEN TO_DATE( -- NULL이 아닌 경우에만 / TO_DATE: 문자 타입을 날짜 데이터 유형으로
                CASE WHEN POSITION('-' IN birth_date) = 3 
	            --WHEN SUBSTRING(birth_date, 3, 1) = '-' 3번째 위치에 1글자가 '-' 인 경우
			THEN -- 3번째 위치에 -가 있을 경우
				SUBSTRING(birth_date, 7, 4) -- 년도 가져오기 
				|| '-' || SUBSTRING(birth_date, 1, 2) -- 월 가져오기 / || 문자열 합치는 연산자
				|| '-' || SUBSTRING(birth_date, 4, 2) -- 일 가져오기
                        -- 최종적으로 YYYY-MM-DD 형식으로 
		ELSE -- YYYY-MM-DD 인 경우
			birth_date
         END, 'YYYY-MM-DD') -- To_DATE 형식 지정
         
    ELSE NULL -- NULL인 경우 그대로 유지
END); -- 만약 YYYY-DD-MM인 경우에는 처리 불가?


-- 방법 1 (직접비교)
SELECT
    t.team_id
    ,t.team_name
    ,COUNT(p.player_id) AS 선수수
FROM 
	exam.team t
	,exam.player p
WHERE
    t.team_id = p.team_id 
    AND p.birth_date IS NOT NULL 
    AND p.birth_date >= '1990-01-01'
    --AND p.birth_date >= '1990'
GROUP BY 
	t.team_id
	,t.team_name;


-- 방법 2 (substring)
SELECT
    t.team_id
    ,t.team_name
    ,COUNT(p.player_id) AS 선수수
FROM 	
	exam.team t
	,exam.player p
WHERE 
       t.team_id = p.team_id
	AND p.birth_date IS NOT NULL 
	AND SUBSTRING(p.birth_date, 1, 4) >= '1990'	
GROUP BY 
	t.team_id
	,t.team_name;
