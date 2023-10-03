package gmx.file.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import gmx.upc.file.FileTable;

@WebServlet("/DeleteFileServlet")
public class DeleteFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteFileServlet() {
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
        response.setContentType("application/json; charset=UTF-8");

        JSONObject json = new JSONObject();
        String strFileId = request.getParameter("fileId");
        String strPostId = request.getParameter("postId");

        int fileId = Integer.parseInt(strFileId);
        int postId = Integer.parseInt(strPostId);

        FileTable ft = new FileTable();
        boolean isDeleted = ft.deleteFile(fileId);

        if (isDeleted) {
            json.put("message", "삭제 완료되었습니다.");
        } else {
            json.put("message", "삭제에 실패하였습니다.");
        }

        response.getWriter().print(json.toString());
    }
}
