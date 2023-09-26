package gmx.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import gmx.upc.user.UserTable;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
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

		response.setContentType("text/html; charset=UTF-8");

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		if ((email == null || email.trim().isEmpty()) || (password == null || password.trim().isEmpty())) {
			response.getWriter().print("모든 정보를 입력하세요.");
			return;
		}

		UserTable ut = new UserTable();
		Boolean isCheck = ut.loginCheck(email, password);

		if (isCheck) {
			HttpSession session = request.getSession();
			session.setAttribute("userEmail", email);
			response.getWriter().print("로그인 성공하셨습니다.");
		} else {
			response.getWriter().print("로그인 실패하셨습니다.");
		}
	}
}
