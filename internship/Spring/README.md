<h2>MVC 페턴이란</h2>
소프트웨어 엔지니어링에서 주로 사용되는 디자인 패턴 중 하나<br>
애플리케이션의 로직을 세 가지의 구성 요소로 나누어 개발<br>
특히 사용자 인터페이스를 가진 애플리케이션에서 사용<br>
<br><br>
<h4>MODEL:</h4> 데이터 및 비즈니스 로직; 핵심적인 데이터와 연산 로직을 담당; 사용자 인터페이스나 표시 방식에 대해 알지 못함(VIEW)<br>
<h4>VIEW:</h4> 사용자 인터페이스; 사용자에게 데이터를 표시하거나 입력을 받음; UI요소; 데이터나 비즈니스 로직에 대해 직접 알지 못함(MODEL)<br>
<h4>CONTROLLER:</h4> MODEL과 VIEW의 통신; VIEW에서 입력을 받아 MODEL에 데이터 조작을 지시하거나 MODEL의 변경 사항을 확인하여 VIEW에 표시하도록 지시; MODEl과 VIEW를 알고 있어야 함<br>
<br><br>
<h4>장점</h4>
각 구성 요소는 독립적으로 개발되어 유지보수성이 뛰어나며 유지보수시 다른 부분에 미치는 영향 최소화, 독립적인 특성으로 다른 플랫폼이나 인터페이스에서 사용하기 요소<br>
<h4>단점</h4>
초기 설계및 개발에 더 많은 시간과 노력이 필요하며 간단한 애플리케이션에서는 비적합(오버킬)
<br><br>
<h2>Dispatcher-Servlet</h2>
클라이언트로부터 어떠한 요청이 들어오면 톰캣과 같은 서블릿 컨테이너가 요청을 받는데,<br>
이때 제일 앞에서 서버로 들어오는 모든 요청을 처리하는 프론트 컨트롤러(적절한 세부 컨트롤러로 작업을 위임)<br>
<br>
이때 처리하는 url 패턴을 지정해주어야 하는데 일반적으로는 /*.do와 같이 /로 시작하여 .do로 끝남<br>
따라서 web.xml의 역할을 상당히 축소화시킴<br>
<h4>처리 과정</h4>
<h5>요청 단계</h5>
1. 클라이언트 요청: 웹 브라우저에서 서버에 HTTP 요청을 보냄(URL, HTTP메소드(GET,POST등), 헤더 정보, 본문 데이터(POST 데이터))<br>
2. 서버 도착: 웹 서버(Apache)에 도착하고, 웹 서버는 요청을 해당 애플리케이션 서버(Tomcat)에 전달<br>
3. Dispatcher-Servlet 접근(web.xml에 설정): Dispatcher-Servlet이 요청을 받아 처리<br>
4. 핸들러 매핑(servlet-context.xml에 설정): Dispatcher-Servlet은 핸들러 매핑을 사용하여 해당 요청을 처리할 Controller 탐색<br>
5. 컨트롤러 실행: 찾아진 컨트롤러의 메소드 실행(비즈니스 로직을 처리하거나 필요한 경우 DB에 접근하여 필요한 데이터를 조작/조회)<br>
6. 뷰 이름 반환: 컨트롤러는 처리 결과와 함께 뷰 이름 반환<br>
<h5>응답 단계</h5>
1. 뷰 리졸루션: 반환된 뷰 이름을 바탕으로 Dispatcher-Servlet은 뷰 리졸버를 사용하여 실제 뷰를 찾음<br>
2. 뷰 렌더링: 찾아진 뷰는 데이터를 사용하여 렌더링 됌. 응답 데이터(HTML, JSON, XML)생성<br>
3. 응답 생성: Dispatcher-Servlet은 렌더링된 뷰의 결과와 함께 HTTP응답 생성(상태코드(200,300,404),gpej 헤더 정보, 응답 본문 등)<br>
4. 클라이언트로 응답 전송: 웹 서버를 통해 클라이언트(웹 브라우저)로 응답 전송<br>
<br><br>
<h2>ORM</h2>
객체 지향 프로그래밍 언어와 관계형 DB사이의 데이터를 매핑하기 위한 프로그래밍 기술<br>
주요 목적은 객체 지향적인 코드와 관계형 DB의 데이터를 자연스럽게 연결하고 동기화 하는 것<br>
<br>
1. 객체-테이블 매핑: 매핑을 가능하게 함. 하나의 객체 인스턴스는 DB테이블의 한 로우와 연결될 수 있음<br>
2. DB 추상화: 개발자가 SQL 쿼리를 직접 작성할 필요 X<br>
3. CRUD 작업: 기본적인 CRUD를 메소드를 통해 수행 가능<br>
<br><br>
<h2>Spring 동작 구조</h2>
web.xml - Dispatcher-Servlet<br>
servlet-context.xml - Handler-Mapping<br>
root-context.xml - view와 관련되지 않은 객체 정의 Service Layor, Repository(DAO), DB등 비즈니스 로직(Model)관련된 설정<br>
web.xml -> servlet-context.xml -> HomeController.java -> servlet-context.xml -> home.jsp<br>

