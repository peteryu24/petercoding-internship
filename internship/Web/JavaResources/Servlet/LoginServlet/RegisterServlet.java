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

package gmx.login.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import gmx.upc.user.UserTable;

@WebServlet("/RegisterServlet") // 어노테이션으로 URL지정 web.xml매핑 불필요
public class RegisterServlet extends HttpServlet {
	/*
	 * 직렬화에 사용되는 고유 ID
	 * 
	 * 객체 직렬화란 java객체를 바이트 스트림으로 변환하는 과정
	 * 메모리 상에서만 존재하며 메모리에서만 유지
	 * 파일에 저장하거나 네트워크를 통해 다른 시스템에 전송하기 위해 직렬화 사용
	 * 
	 * java.io.Serializable를 import 해야 사용 가능
	 * 
	 * 객체를 파일에 저장하고 나중에 불러올 때
	 * 객체를 네트워크를 통해 다른 시스템으로 전송할 때
	 * 분산 시스템에서 객체를 공유할 때
	 * 캐싱된 데이터를 저장하고 다시 사용할 때
	 */
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
		/*
		 * 이렇게 나누어서 타입 지정도 가능
		 * response.setContentType("application/json");
		 * response.setCharacterEncoding("UTF-8");
		 */

		JSONObject json = new JSONObject(); // Hashmap을 상속 받은 클래스

		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");

		if ((nickname == null || nickname.trim().isEmpty()) || (email == null || email.trim().isEmpty())
				|| (password == null || password.trim().isEmpty())) {
			json.put("message", "모든 정보를 입력하세요.");
			/*
			 * response: 웹 서버의 응답을 나타내는 객체
			 * getWriter(): response 객체의 메소드 PrintWirter 반환
			 * json.toString(): json 형태의 데이터를 문자열 형태(json 베이스)로 반환
			 */
			response.getWriter().print(json.toString());
			return;
		}

		UserTable ut = new UserTable();
		String isCheck = ut.insertValue(email, nickname, password);

		if (isCheck.equals("proceed")) {
			json.put("message", "회원가입 성공하셨습니다.");
		} else if (isCheck.equals("duplicate")) {
			json.put("message", "아이디가 중복됩니다.");
		}

		response.getWriter().print(json.toString()); // JSON 형태로 응답
	}
}
