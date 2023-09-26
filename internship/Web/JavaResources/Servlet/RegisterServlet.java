/*
package gmx.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import gmx.upc.user.UserTable;

@WebServlet("/RegisterServlet") // 어노테이션으로 URL지정 web.xml매핑 불필요
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; // 직렬화에 사용되는 고유 ID

	public RegisterServlet() {
		super(); // HttpServlet 호출
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) // http get
			throws ServletException, IOException {
		checkInfo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) // http post
			throws ServletException, IOException {
		checkInfo(request, response);
	}

	private void checkInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8"); // HTTP 응답에 대한 콘텐츠 유형 (html 유형임)과 문자 인코딩을 설정
		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");

		if ((nickname == null || nickname.trim().isEmpty()) || (email == null || email.trim().isEmpty())
				|| (password == null || password.trim().isEmpty())) {
			response.getWriter().print("모든 정보를 입력하세요.");
			return;
		}

		UserTable ut = new UserTable();
		String isCheck = ut.insertValue(email, nickname, password);

		if (isCheck.equals("proceed")) {
			response.getWriter().print("회원가입 성공하셨습니다.");
		} else if (isCheck.contentEquals("duplicate")) {
			response.getWriter().print("아이디가 중복됩니다.");
		}
	}
}
*/

package gmx.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import gmx.upc.user.UserTable;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegisterServlet() {
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

		JSONObject json = new JSONObject(); // Hashmap을 상속 받은 클래스

		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");

		if ((nickname == null || nickname.trim().isEmpty()) || (email == null || email.trim().isEmpty())
				|| (password == null || password.trim().isEmpty())) {
			json.put("message", "모든 정보를 입력하세요.");
			response.getWriter().print(json.toString());
			return;
		}

		UserTable ut = new UserTable();
		String isCheck = ut.insertValue(email, nickname, password);

		if (isCheck.equals("proceed")) {
			json.put("message", "회원가입 성공하셨습니다.");
		} else if (isCheck.contentEquals("duplicate")) {
			json.put("message", "아이디가 중복됩니다.");
		}

		response.getWriter().print(json.toString()); // JSON 형태로 응답
	}
}
