package gmx.comment.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import gmx.upc.comment.CommentTable;

@WebServlet("/NewCommentServlet")
public class NewCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public NewCommentServlet() {
    	super();
    }

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
        int postId = ((Long) jsonRequest.get("postId")).intValue(); // JSON에서 long 타입으로 반환되므로 int로 변환
        String comment = (String) jsonRequest.get("comment");
        String email = (String) request.getSession().getAttribute("userEmail");

        CommentTable ct = new CommentTable();
        Boolean isCheck = ct.insertValue(email, postId, comment);

        JSONObject jsonResponse = new JSONObject();

        if (isCheck) {
            jsonResponse.put("success", true);
        } else {
            jsonResponse.put("success", false);
        }

        // JSON 객체를 문자열로 변환
        response.getWriter().write(jsonResponse.toString());
    }
}
