package gmx.session;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/*") // 모든 페이지에 적용
public class SessionTempFilter implements Filter {

	@Override
	public void init(FilterConfig fconfig) throws ServletException { // 필터가 실행될 때 한번 실행 되는 메소드
	}

	@Override
	public void destroy() { // 필터가 종료될 때 실행되는 메소드

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fchain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(false);

		boolean isLoggedIn = (session != null && session.getAttribute("userEmail") != null);

		// 사용자가 로그인창이나 회원가입창 접근시 접근할 수 있도록
		String login = httpRequest.getContextPath() + "/page/Login/Login.jsp"; // 페이지의 전체 경로를 생성
		String register = httpRequest.getContextPath() + "/page/Login/Login.jsp";
		boolean loginRequest = httpRequest.getRequestURI().equals(login); // 현재 요청이 로그인 서비스의 경로와 일치하는지 확인
		boolean registerRequest = httpRequest.getRequestURI().equals(register);

		if (isLoggedIn || loginRequest || registerRequest) { // 로그인 상태
			System.out.println("session filter");
			fchain.doFilter(request, response);

		} else { // null 상태
			System.out.println("비로그인상태입니다.");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(login);
		}
	}

}
