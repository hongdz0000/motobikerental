/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MotorbikeDAO;
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
import model.Motorbike;

/**
 *
 * @author MSI LAPTOP
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class AdminBikeServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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
            out.println("<title>Servlet AdminBikeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminBikeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        MotorbikeDAO dao = new MotorbikeDAO();
        int pageSize = 5; // Số lượng xe trên mỗi trang

        try {
            switch (action) {
                case "add":
                    // Mở form trống để thêm mới xe
                    request.getRequestDispatcher("/templates/admin/bike-form.jsp").forward(request, response);
                    break;

                case "edit":
                    // Lấy thông tin xe theo ID để đổ vào Form sửa
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Motorbike bike = dao.getMotorbikeById(editId);
                    request.setAttribute("bike", bike);
                    request.getRequestDispatcher("/templates/admin/bike-form.jsp").forward(request, response);
                    break;

                case "delete":
                    // Xóa xe và quay lại danh sách
                    int delId = Integer.parseInt(request.getParameter("id"));
                    dao.delete(delId);
                    response.sendRedirect("AdminBike?action=list");
                    break;

                case "list":
                case "search":
                default:
                    // 1. Lấy từ khóa tìm kiếm
                    String txtSearch = request.getParameter("txtSearch");
                    if (txtSearch == null) {
                        txtSearch = "";
                    }

                    // 2. Xử lý số trang (mặc định là trang 1)
                    String pageStr = request.getParameter("page");
                    int pageIndex = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);

                    // 3. Lấy danh sách xe đã phân trang từ DAO
                    List<Motorbike> list = dao.getBikesPaging(txtSearch, pageIndex, pageSize);

                    // 4. Tính toán tổng số trang để hiển thị thanh chuyển trang
                    int totalRecords = dao.getTotalMotorbikes(txtSearch);
                    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                    // 5. Đẩy dữ liệu sang JSP
                    request.setAttribute("bikeList", list);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", pageIndex);
                    request.setAttribute("txtSearch", txtSearch); // Để giữ từ khóa trong ô search và link phân trang

                    request.getRequestDispatcher("/templates/admin/bike.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        MotorbikeDAO dao = new MotorbikeDAO();

        try {
            // 1. Đọc các trường text từ form
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String brand = request.getParameter("brand");
            double price = Double.parseDouble(request.getParameter("pricePerDay"));
            String status = request.getParameter("status");
            String description = request.getParameter("description");
            String engineSize = request.getParameter("engineSize");
            String transmission = request.getParameter("transmission");
            int year = Integer.parseInt(request.getParameter("manufactureYear"));

            // 2. Xử lý Upload Ảnh
            Part filePart = request.getPart("image");
            String fileName = null;

            if (filePart != null && filePart.getSize() > 0) {
                // TÌM ĐƯỜNG DẪN CÁC THƯ MỤC (Giống logic UploadAvatarServlet)
                String buildPath = request.getServletContext().getRealPath("/uploads");
                File buildDirFile = new File(buildPath);

                // .getParentFile() 3 lần để về thư mục gốc Project, rồi vào web/uploads
                File projectRootDir = buildDirFile.getParentFile().getParentFile().getParentFile();
                String projectPath = projectRootDir.getAbsolutePath() + File.separator + "web" + File.separator + "uploads";

                // Đảm bảo các thư mục tồn tại
                new File(buildPath).mkdirs();
                new File(projectPath).mkdirs();

                // --- XÓA ẢNH CŨ (NẾU LÀ UPDATE) ---
                if (idStr != null && !idStr.isEmpty()) {
                    Motorbike oldBike = dao.getMotorbikeById(Integer.parseInt(idStr)); // Cần hàm này trong DAO
                    if (oldBike != null && oldBike.getBikeIcon() != null) {
                        // Lấy tên file từ đường dẫn "uploads/ten_file.jpg"
                        String oldFileName = oldBike.getBikeIcon().replace("uploads/", "");

                        // Không xóa nếu là ảnh mặc định (nếu có)
                        if (!oldFileName.equalsIgnoreCase("default.jpg") && !oldFileName.isEmpty()) {
                            File oldFileBuild = new File(buildPath + File.separator + oldFileName);
                            File oldFileProject = new File(projectPath + File.separator + oldFileName);

                            if (oldFileBuild.exists()) {
                                oldFileBuild.delete();
                            }
                            if (oldFileProject.exists()) {
                                oldFileProject.delete();
                            }
                        }
                    }
                }

                // --- LƯU ẢNH MỚI ---
                String uniqueFileName = "bike_" + System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

                // Ghi vào build (để hiển thị ngay)
                filePart.write(buildPath + File.separator + uniqueFileName);

                // Copy sang project (để lưu vĩnh viễn)
                java.nio.file.Files.copy(
                        new File(buildPath + File.separator + uniqueFileName).toPath(),
                        new File(projectPath + File.separator + uniqueFileName).toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                fileName = "uploads/" + uniqueFileName;
            } else {
                // Nếu không upload ảnh mới, lấy ảnh cũ từ hidden field
                fileName = request.getParameter("existingImage");
            }

            // 3. Tạo đối tượng Motorbike và lưu vào DB
            Motorbike b = new Motorbike();
            b.setName(name);
            b.setBrand(brand);
            b.setCurrentPricePerDay(price);
            b.setStatus(status);
            b.setDescription(description);
            b.setEngineSize(engineSize);
            b.setTransmission(transmission);
            b.setManufactureYear(year);
            b.setBikeIcon(fileName);

            if (idStr == null || idStr.isEmpty()) {
                dao.insert(b);
            } else {
                b.setId(Integer.parseInt(idStr));
                dao.update(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("AdminBike");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
