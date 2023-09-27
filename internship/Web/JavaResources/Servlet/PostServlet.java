package gmx.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gmx.upc.post.PostTable;

@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String email = (String) request.getSession().getAttribute("userEmail");

		PostTable pt = new PostTable();
		Boolean isCheck = pt.insertValue(title, content, email);

		if (isCheck) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write("error");
		}
	}
}
