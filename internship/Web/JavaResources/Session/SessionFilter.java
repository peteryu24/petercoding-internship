package gmx.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionFilter {

    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 새로 생성하지 않음

        if (session != null && session.getAttribute("userEmail") != null) {
            // 세션에 userEmail 속성이 있으면 사용자가 로그인한 상태로 판단
            return true;
        } else {
            return false;
        }
    }
}
