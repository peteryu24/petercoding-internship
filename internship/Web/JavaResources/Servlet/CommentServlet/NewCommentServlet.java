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
        /*  
		 * 응답의 MIME 타입 설정 
		 * 클라이언트에게  JSON 형식의 데이터를 전송
		 * 데이터는 UTF-8 문자 인코딩
		*/
        response.setContentType("application/json; charset=UTF-8");

        /*
		 * 파싱 객체 선언
		 * 클라이언트에게 받은 JSON 형식의 문자열을
		 * java 객체로 변환하는데 사용
		 */
        JSONParser parser = new JSONParser();
        JSONObject jsonRequest = null;

        // 클라이언트로부터 받은 JSON 데이터를 파싱.
        try {
            jsonRequest = (JSONObject) parser.parse(request.getReader());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        
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

        /*
		 * 문자열로 반환하면 클라이언트의 언어나 프레임워크에 관계없이 다양한 환경에서 활용 가능
		 * JSON형식의 문자열을 클라이언트에게 반환하기 위해 JSON객체를 문자열로 반환
		 */
        response.getWriter().write(jsonResponse.toString());
    }
}
