package gmx.post.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import gmx.upc.post.PostTable;

@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     checkInfo(request, response);
 }

 protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     checkInfo(request, response);
 }

 private void checkInfo(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
     // 응답의 MIME 타입 설정  
     response.setContentType("application/json; charset=UTF-8");

     // 파싱 객체 선언
     JSONParser parser = new JSONParser();
     JSONObject jsonRequest = null;

     // 클라이언트로부터 받은 JSON 데이터를 파싱.
     try {
         jsonRequest = (JSONObject) parser.parse(request.getReader());
     } catch (ParseException e) {
         e.printStackTrace();
         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 잘못된 요청에 대한 HTTP 상태 코드 설정.
         return;
     }

     // 필요한 정보 추출.
     String title = (String) jsonRequest.get("title");
     String content = (String) jsonRequest.get("content");
     String email = (String) request.getSession().getAttribute("userEmail");

     PostTable pt = new PostTable();
     Boolean isCheck = pt.insertValue(title, content, email);

     JSONObject json = new JSONObject();

     if (isCheck) {
         json.put("status", "success");
     } else {
         json.put("status", "error");
     }

     // JSON 객체를 문자열로 변환
     response.getWriter().write(json.toString());
 }
}
