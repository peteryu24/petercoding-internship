package gmx.file.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONObject;

import gmx.upc.file.FileTable;

@WebServlet("/EditFileServlet")
public class EditFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 수정할 파일 정보를 가져옴
        String strFileId = request.getParameter("fileId");
        int fileId = 0; // 초기값 설정

        if (strFileId != null) {
            fileId = Integer.parseInt(strFileId);
        }

        String newFileName = request.getParameter("newFileName");
        String newFilePath = request.getParameter("newFilePath");

        // FileTable 클래스의 updateFile 메소드 호출
        FileTable fileTable = new FileTable();
        boolean isFileUpdated = fileTable.updateFile(fileId, newFileName, newFilePath);

        // 파일 업데이트 결과에 따라 응답 생성
        JSONObject json = new JSONObject();
        if (isFileUpdated) {
            json.put("status", "success");
            json.put("message", "파일 업데이트 성공.");
        } else {
            json.put("status", "error");
            json.put("message", "파일 업데이트 실패.");
        }

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json.toString());
    }
}

