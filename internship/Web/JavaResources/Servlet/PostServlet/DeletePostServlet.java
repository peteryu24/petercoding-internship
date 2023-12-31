package gmx.post.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import gmx.upc.post.PostTable;

@WebServlet("/DeletePostServlet")
public class DeletePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeletePostServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkInfo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkInfo(request, response);
	}

	private void checkInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8"); // JSON 타입 설정
		/*
		 * 이렇게 나누어서 타입 지정도 가능
		 * response.setContentType("application/json");
		 * response.setCharacterEncoding("UTF-8");
		 */

		JSONObject json = new JSONObject(); // Hashmap을 상속 받은 클래스


		/*
		 * response: 웹 서버의 응답을 나타내는 객체
		 * getWriter(): response 객체의 메소드 PrintWirter 반환
		 * json.toString(): json 형태의 데이터를 문자열 형태(json 베이스)로 반환
		 */
		String postIdStr = request.getParameter("postId");
		int postId = Integer.parseInt(postIdStr);
		
		PostTable pt = new PostTable();
		String isCheck = pt.delete(postId);
		

		if (isCheck.equals("proceed")) {
			json.put("message", "삭제 완료되었습니다.");
		} else if (isCheck.equals("fail")) {
			json.put("message", "삭제 실패하였습니다.");
		}

		response.getWriter().print(json.toString()); // JSON 형태로 응답
	}
}
