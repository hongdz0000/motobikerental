/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.StuffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.List;
import model.Stuff;

/**
 *
 * @author MSI LAPTOP
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class StuffServlet extends HttpServlet {
   
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
            out.println("<title>Servlet StuffServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StuffServlet at " + request.getContextPath () + "</h1>");
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
    throws ServletException, IOException {String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        StuffDAO dao = new StuffDAO();

        switch (action) {
            case "list":
                // 1. Lấy từ khóa tìm kiếm (mặc định rỗng)
                String txtSearch = request.getParameter("txt");
                if (txtSearch == null) txtSearch = "";

                // 2. Lấy trang hiện tại (mặc định là trang 1)
                String pageIdx = request.getParameter("page");
                int index = (pageIdx == null || pageIdx.isEmpty()) ? 1 : Integer.parseInt(pageIdx);
                
                int pageSize = 5; // Số lượng dòng trên 1 trang

                // 3. Gọi DAO lấy dữ liệu phân trang & tổng số bản ghi
                List<Stuff> list = dao.getStuffWithPaging(txtSearch, index, pageSize);
                int totalRecords = dao.countSearch(txtSearch);
                int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                // 4. Đẩy dữ liệu sang JSP
                request.setAttribute("stuffList", list);
                request.setAttribute("txt", txtSearch);
                request.setAttribute("currentPage", index);
                request.setAttribute("totalPages", totalPages);
                
                request.getRequestDispatcher("/templates/admin/stuff.jsp").forward(request, response);
                break;

            case "delete":
                int idDel = Integer.parseInt(request.getParameter("id"));
                dao.delete(idDel);
                response.sendRedirect("stuff");
                break;

            case "edit":
                int idEdit = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("item", dao.getById(idEdit));
                request.getRequestDispatcher("/templates/admin/stuff-form.jsp").forward(request, response);
                break;

            case "add":
                request.getRequestDispatcher("/templates/admin/stuff-form.jsp").forward(request, response);
                break;
        }
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
           request.setCharacterEncoding("UTF-8");
        StuffDAO dao = new StuffDAO();
        
        try {
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));

            // --- XỬ LÝ UPLOAD FILE ---
            Part filePart = request.getPart("imageFile"); // Tên input file trong JSP
            String fileName = null;

            if (filePart != null && filePart.getSize() > 0) {
                // Đường dẫn lưu file
                String buildPath = request.getServletContext().getRealPath("/uploads");
                File buildDirFile = new File(buildPath);
                File projectRootDir = buildDirFile.getParentFile().getParentFile().getParentFile();
                String projectPath = projectRootDir.getAbsolutePath() + File.separator + "web" + File.separator + "uploads";

                new File(buildPath).mkdirs();
                new File(projectPath).mkdirs();

                // Xóa ảnh cũ nếu là Update
                if (idStr != null && !idStr.isEmpty()) {
                    Stuff oldStuff = dao.getById(Integer.parseInt(idStr));
                    if (oldStuff != null && oldStuff.getStuffIcon() != null) {
                        String oldFileName = oldStuff.getStuffIcon().replace("uploads/", "");
                        new File(buildPath + File.separator + oldFileName).delete();
                        new File(projectPath + File.separator + oldFileName).delete();
                    }
                }

                // Lưu ảnh mới
                String uniqueFileName = "stuff_" + System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                filePart.write(buildPath + File.separator + uniqueFileName);
                java.nio.file.Files.copy(
                    new File(buildPath + File.separator + uniqueFileName).toPath(),
                    new File(projectPath + File.separator + uniqueFileName).toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                fileName = "uploads/" + uniqueFileName;
            } else {
                // Nếu không chọn ảnh mới, giữ ảnh cũ
                fileName = request.getParameter("existingIcon");
            }

            Stuff s = new Stuff();
            s.setStuffName(name);
            s.setBasePricePerDay(price);
            s.setStuffIcon(fileName);

            if (idStr == null || idStr.isEmpty()) {
                dao.insert(s);
            } else {
                s.setStuffId(Integer.parseInt(idStr));
                dao.update(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("stuff");
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
