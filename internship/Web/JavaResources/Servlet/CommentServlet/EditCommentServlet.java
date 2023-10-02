package gmx.comment.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import gmx.upc.comment.CommentTable;

@WebServlet("/EditCommentServlet")
public class EditCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public EditCommentServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8"); // JSON 타입 설정

		JSONObject json = new JSONObject();

		String comment = request.getParameter("comment");
		String strCommentId = request.getParameter("commentId");
		int commentId = Integer.parseInt(strCommentId);

		if ((comment == null || comment.trim().isEmpty())) {
			json.put("message", "댓글 내용을 입력하세요.");
			response.getWriter().print(json.toString());
			return;
		}

		CommentTable ct = new CommentTable();
		String isCheck = ct.update(comment, commentId);

	
		if (isCheck.equals("proceed")) {
		    json.put("success", true);
		    response.getWriter().print(json.toString());
		} else if (isCheck.equals("fail")) {
		    json.put("success", false);
		    json.put("message", "댓글 변경에 실패하였습니다.");
		    response.getWriter().print(json.toString());
		}




	}
}
