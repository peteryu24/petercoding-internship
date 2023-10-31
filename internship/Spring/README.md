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
<h2>Spring</h2>
<h3>Spring 동작 구조</h3>
web.xml - Dispatcher-Servlet<br>
servlet-context.xml - Handler-Mapping<br>
root-context.xml - view와 관련되지 않은 객체 정의 Service Layor, Repository(DAO), DB등 비즈니스 로직(Model)관련된 설정<br>
web.xml -> servlet-context.xml -> HomeController.java -> servlet-context.xml -> home.jsp<br>
<br><br>
<h3>Spring 기본 폴더 구조</h3>
src/main/java - java 파일<br>
src/main/resources - 서버가 실행될 때 필요한 파일들<br>
src/test/java - 테스트 파일<br>
src/test/resources - 테스트할 때 사용되는 파일들<br>
WEB-INF/spring - 스프링 설정 파일<br>
WEB-INF/views - jsp 파일<br>
web.xml - 웹설정 파일<br>
target file - build 프로세스의 마지막 output을 포함
            - maven으로 빌드하면 생기는 jar 파일을 저장하는 용도 
            - target은 커밋 대상이 아니며, 지워도 maven build를 하면 다시 생성됌. 실서버에 반영할 때는 target 밑에 있는 jar이나 war를 배포하게 됌.
pom.xml - maven 설정 파일<br>
maven 이란? 자바용 프로젝트 관리 도구; 미리 작성된 xml 파일을 이용해 라이브러리를 자동으로 다운하거나 빌드
<br><br>
<h3>Bean</h3>
Spring에 의하여 생성되고 관리되는 자바 객체<br>
서블릿과 동일하게 싱글톤 패턴의 성질로 id 중복 불가<br>
@Configuration을 이용하면 Spring Project 에서의 Configuration 역할을 하는 Class를 지정할 수 있습니다. <br>
해당 File 하위에 Bean 으로 등록하고자 하는 Class에 @Bean Annotation을 사용해주면 간단하게 Bean을 등록할 수 있습니다. <br>
스프링 빈은 스프링 컨테이너의 생명주기와 연결되어 있음. 컨테이너 시작시 빈이 생성되고, 컨테이너가 종료될 때 빈이 파괴됌.
스프링 빈 과 서블릿은 싱글톤 패턴 스프링 빈 생성
<h5>Bean 설정법</h5>
1. xml 기반의 설정: &lt;bean&gt; 태그를 사용하여 xml파일 내에서 빈을 정의하고 수성
2. 어노테이션 기반의 설정: @Component, @Service, @Repository, @Controller을 사용하여 자바 클래스를 빈으로 표시하고, @Autowired를 사용하여 의존성 주입
3. Java 기반의 설정: @Configuration과 @Bean을 사용하여 자바 코드 내에서 빈을 정의하고 구성

@Component:

기본적인 컴포넌트 어노테이션으로, 해당 클래스를 스프링 빈으로 등록합니다.
@Service, @Repository, @Controller는 사실상 @Component의 특수한 형태입니다. 각각은 특정 계층 또는 특정 목적에 대한 시멘틱을 추가합니다.
@Service:

서비스 계층의 클래스를 나타냅니다.
비즈니스 로직 처리를 주로 담당하며, DAO나 리포지토리와 함께 동작합니다.
@Repository:

데이터 액세스 객체 (DAO) 계층의 클래스를 나타냅니다.
데이터베이스의 CRUD 연산을 담당하며, 일반적으로 JPA, Hibernate, MyBatis와 같은 ORM 도구와 함께 사용됩니다.
예외 변환 기능이 있어 데이터베이스 관련 예외를 스프링의 DataAccessException으로 변환합니다.
@Controller:

웹 애플리케이션의 프레젠테이션 계층의 클래스를 나타냅니다.
웹 요청을 받아 처리하고, 적절한 응답을 반환합니다.
대체로 Spring MVC와 함께 사용되며, @RequestMapping과 같은 추가 어노테이션들과 결합하여 사용됩니다.
@Configuration:

자바 기반의 스프링 설정 클래스를 나타냅니다.
전통적인 XML 기반의 설정 대신, 자바 클래스에서 스프링 빈의 정의와 구성을 수행합니다.
@Bean과 주로 함께 사용됩니다.
@Bean:

@Configuration 클래스 내에서 메서드에 사용되며, 해당 메서드가 반환하는 객체를 스프링 빈으로 등록합니다.
@Bean 어노테이션을 사용하면 프로그래밍적으로 빈을 정의할 수 있으므로, 복잡한 초기화 로직이나 런타임에 의존성 주입이 필요할 때 유용합니다.

서비스 계층 (Service Layer):

목적: 비즈니스 로직을 포함하고 조정합니다.
특징:
DAO나 리포지토리와 같은 데이터 액세스 계층과 상호 작용하여 데이터를 가져오거나 변경합니다.
트랜잭션 관리, 보안, 로깅 등과 같은 크로스 커팅 관심사를 처리할 수 있습니다.
도메인 모델과 함께 작동하여 핵심 비즈니스 연산을 수행합니다.
스프링과의 관계: @Service 어노테이션을 사용하여 해당 계층의 컴포넌트를 나타냅니다.
데이터 액세스 객체 (DAO) 계층:

목적: 데이터베이스와의 상호 작용을 추상화하고, 데이터 액세스를 중앙 집중화합니다.
특징:
CRUD (Create, Read, Update, Delete) 연산과 같은 기본적인 데이터베이스 작업을 수행합니다.
데이터베이스와의 연결 관리, 쿼리 생성 및 실행, 결과 집합 처리 등의 작업을 처리합니다.
다양한 데이터 소스 (RDBMS, NoSQL 데이터베이스, 웹 서비스 등)와의 상호 작용을 지원할 수 있습니다.
스프링과의 관계: @Repository 어노테이션을 사용하여 해당 계층의 컴포넌트를 나타냅니다.
프레젠테이션 계층 (Presentation Layer):

목적: 사용자 인터페이스와 상호 작용하고, 사용자의 입력을 처리하며, 적절한 응답을 제공합니다.
특징:
웹 애플리케이션의 경우, HTTP 요청 처리, 뷰 선택 및 렌더링, 리다이렉션 등의 작업을 수행합니다.
사용자의 입력을 검증하고, 서비스 계층이나 다른 비즈니스 로직으로 전달합니다.
웹 페이지, RESTful API, 웹 서비스, 데스크톱 응용 프로그램 등 다양한 사용자 인터페이스를 포함할 수 있습니다.
스프링과의 관계: @Controller 또는 @RestController 어노테이션을 사용하여 웹 애플리케이션의 프레젠테이션 계층 컴포넌트를 나타냅니다.




