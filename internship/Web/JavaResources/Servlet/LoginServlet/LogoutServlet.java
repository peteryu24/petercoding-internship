package gmx.login.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogoutServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkLogout(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkLogout(request, response);
	}

	private void checkLogout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		JSONObject json = new JSONObject();

		HttpSession session = request.getSession(false); // 기존 세션 가져오기. 없으면 null을 반환
		if (session != null) {
			session.invalidate(); // 세션 종료
			json.put("message", "로그아웃 되었습니다.");
		} else {
			json.put("message", "이미 로그아웃 상태입니다.");
		}
		response.getWriter().print(json.toString());
	}
}
