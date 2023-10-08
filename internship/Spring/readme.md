<h3>egovframework.example.cmmn -> gmx.cmmn.handler</h3>
EgovSampleExcepHndlr.java EgovSampleOthersExcepHndlr.java<br>
ExceptionHandler를 상속하여 occur 메소드를 오버라이딩해서 에러 처리<br>
에러를 핸들러하기에 handler로 작명

<h3>egovframework.example.cmmn.web -> gmx.cmmn.bindrender</h3>
EgovBindingInitializer.java EgovImgPaginationRenderer.java<br>
무언가를 렌더링, 바인딩하기에 bindrender로 작명

<h3>egovframework.example.sample.service -> gmx.fwd.searchvo</h3>
EgovSampleService.java SampleDefaultVO.java SampleVO.java<br>
기본적 검색 조건이나 검색 기능을 구현하기에 search로 작명

<h3>egovframework.example.sample.service.impl -> gmx.fwd.crud.impl</h3>
EgovSampleServiceImpl.java SampleDAO.java SampleMapper.java<br>
crud작업을 수행하기에 crud로 작명

<h3>egovframework.example.sample.web -> gmx.controller</h3>
EgovSampleController.java<br>
@RequsetMapping(vlaue = "/null.do")의 메소드들 구현<br>
.do로 온 것들을 매핑하여 컨트롤 한다는 의미로 controller로 작명

<h3>context:component-scan base-package="egovframework" -> "gmx, egovframework"</h3>
context-common.xml과 dispatcher-servlet.xml에 적용

<h3>src/main/resources/"egovframework" -> gmx</h3>

<h3>WEB-INF/jsp/geomex.jsp로 디렉토리 변경</h3>
dispatcher-servlet.xml ->  p:prefix="/WEB-INF/jsp/" p:suffix=".jsp"/> urlbasedviewResolver
GeomexController.java -> return "geomex";

<h3>기본 자바 파일 삭제</h3>
<h4>주석처리</h4>
<h5>context-aspect.xml</h5>
bean id="egovHandler" class="gmx.cmmn.handler.EgovSampleExcepHndlr"<br>
bean id="otherHandler" class="gmx.cmmn.handler.EgovSampleOthersExcepHndlr"<br>

<h5>dispatcher-servlet.xml</h5>
bean class="gmx.cmmn.bindrender.EgovBindingInitializer"/<br>
bean id="imageRenderer" class="gmx.cmmn.bindrender.EgovImgPaginationRenderer"/<br>

<h5>context-aspect.xml</h5>

<h5>context-transaction.xml</h5>
<h4>src/main/resources/egovframework/sqlmap/"sample" -> pgsql</h4>
<h5>context-mapper.xml</h5>


<h2>경로 변경</h2>
<h3>WEB-INF/connfig/egovframework/springmvc/dispatcher-servlet.xml -> WEB-INF/config/dispatcher-servlet.xml</h3>
다른 폴더 및 파일들은 삭제 후 src/main/resources/spring/context-validaor.xml에서 validaor.xml 참조 부분 주석처리




