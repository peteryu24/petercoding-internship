package gmx.post.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gmx.upc.file.FileTable;
import gmx.upc.file.FileVo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/DownloadFileServlet")
public class DownloadFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileIdStr = request.getParameter("fileId");
        if (fileIdStr == null || fileIdStr.isEmpty()) {
            response.getWriter().write("ID null!!");
            return;
        }

        int fileId;
        try {
            fileId = Integer.parseInt(fileIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("유효하지 않은 ID");
            return;
        }

        FileTable ft = new FileTable();
        FileVo fileVo = ft.getFileById(fileId);

        if (fileVo == null) {
            response.getWriter().write("해당 ID에 파일이 없습니다.");
            return;
        }

        File file = new File(fileVo.getFilePath()); // 파일의 실제 경로를 찾아서 File 객체로 생성
        
        if (!file.exists()) {
            response.getWriter().write("서버에 파일이 없습니다.");
            return;
        }

        response.setContentType("application/octet-stream"); // 파일의 확장자에 관계없이 이진 데이터로 처리
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileVo.getFileName() + "\""); // 첨부 파일로 처리(attachment)

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[5000]; // 버퍼를 사용해 파일을 조금씩 읽어서 메모리 소모 최소화
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) { // 파일을 끝까지 읽을 때까지
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace(); 
            response.getWriter().write("Error in downloading the file");
        }
    }
}
