package gmx.post.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONObject;
import gmx.upc.post.PostTable;
import gmx.upc.file.FileTable;

@MultipartConfig
@WebServlet("/PostFileServlet")
public class PostFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        checkInfo(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        checkInfo(request, response);
    }

    private void checkInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        // Changed from JSON parsing to getParameter
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String email = (String) request.getSession().getAttribute("userEmail");

        if (title == null || content == null) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("status", "error");
            errorJson.put("message", "Title or Content missing.");
            response.getWriter().write(errorJson.toString());
            return;
        }

        PostTable pt = new PostTable();
        int postId = pt.insertValue(title, content, email);

        // 파일 업로드를 위해 업로드된 파일 파트를 가져옴
        Collection<Part> fileParts = request.getParts();

        for (Part filePart : fileParts) { // 업로드 된 각 파일에 대해
            String fileName = filePart.getSubmittedFileName();

            if (fileName != null && !fileName.isEmpty()) {
                // 파일 업로드 처리
                String userHome = System.getProperty("user.home");
                String filePath = userHome + "/Desktop/" + fileName; // 파일 저장장

                try {
                    filePart.write(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("status", "error");
                    errorJson.put("message", "Failed to save file.");
                    response.getWriter().write(errorJson.toString());
                    return;
                }

                // 파일 정보를 DB에 저장
                FileTable fileTable = new FileTable();
                boolean isFileSaved = fileTable.insertFile(postId, fileName, filePath);

                if (!isFileSaved) {
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("status", "error");
                    errorJson.put("message", "Failed to save file information in the database.");
                    response.getWriter().write(errorJson.toString());
                    return;
                }
            }
        }

        JSONObject json = new JSONObject();
        if (postId > 0) {
            json.put("status", "success");
            json.put("postId", postId);
        } else {
            json.put("status", "error");
            json.put("message", "게시글 작성 및 파일 업로드 실패.");
        }
        response.getWriter().write(json.toString());
    }
}
