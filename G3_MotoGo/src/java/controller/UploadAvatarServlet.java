/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class UploadAvatarServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UploadAvatarServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadAvatarServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String userId = request.getParameter("userId");
    UserDAO dao = new UserDAO();
    
    try {
        Part filePart = request.getPart("avatarFile");
        if (filePart != null && filePart.getSize() > 0) {
            
            // 1. TÌM ĐƯỜNG DẪN CÁC THƯ MỤC
            // Build Path: .../target/projectName/uploads hoặc .../build/web/uploads
            String buildPath = request.getServletContext().getRealPath("/uploads");
            
            // Project Path: Tìm ngược về thư mục 'web/uploads' trong source code của bạn
            File buildDirFile = new File(buildPath);
            // .getParentFile() 3 lần để từ 'build/web/uploads' về lại thư mục gốc Project
            File projectRootDir = buildDirFile.getParentFile().getParentFile().getParentFile();
            String projectPath = projectRootDir.getAbsolutePath() + File.separator + "web" + File.separator + "uploads";

            // Đảm bảo thư mục tồn tại
            new File(buildPath).mkdirs();
            new File(projectPath).mkdirs();

            // 2. XÓA ẢNH CŨ (NẾU CÓ)
            User currentUser = dao.getAccountByUserId(userId); // Bạn cần đảm bảo UserDAO có hàm này
            if (currentUser != null && currentUser.getAvatar() != null) {
                String oldAvatarName = currentUser.getAvatar().replace("uploads/", "");
                
                // Không xóa nếu là ảnh mặc định (tùy tên file mặc định của bạn)
                if (!oldAvatarName.equals("default.jpg") && !oldAvatarName.isEmpty()) {
                    File oldFileBuild = new File(buildPath + File.separator + oldAvatarName);
                    File oldFileProject = new File(projectPath + File.separator + oldAvatarName);
                    
                    if (oldFileBuild.exists()) oldFileBuild.delete();
                    if (oldFileProject.exists()) oldFileProject.delete();
                }
            }

            // 3. LƯU ẢNH MỚI
            String fileName = "avatar_" + userId + "_" + System.currentTimeMillis() + ".jpg";
            
            // Ghi vào thư mục Build để hiển thị ngay
            filePart.write(buildPath + File.separator + fileName);
            
            // Ghi vào thư mục Project để lưu vĩnh viễn (copy file vừa ghi sang)
            java.nio.file.Files.copy(
                new File(buildPath + File.separator + fileName).toPath(), 
                new File(projectPath + File.separator + fileName).toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            // 4. CẬP NHẬT DATABASE & SESSION
            String avatarRelativePath = "uploads/" + fileName;
            boolean isUpdated = dao.updateAvatar(userId, avatarRelativePath);

            if (isUpdated) {
                // Cập nhật Session 'user'
                User userSession = (User) request.getSession().getAttribute("user");
                if (userSession != null) userSession.setAvatar(avatarRelativePath);
                
                // Cập nhật Session 'profileUser' (nếu có dùng riêng)
                User profileSession = (User) request.getSession().getAttribute("profileUser");
                if (profileSession != null) profileSession.setAvatar(avatarRelativePath);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    // Quay lại trang Profile
    response.sendRedirect(request.getContextPath() + "/Profile?userId=" + userId);
}



    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
