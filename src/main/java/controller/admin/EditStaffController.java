package controller.admin;

import dao.StaffDAO;
import dto.Staff;
import util.IConstant;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/editStaff")
public class EditStaffController extends HttpServlet {

    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String staffIdStr = request.getParameter("staffId");
        if (staffIdStr != null && !staffIdStr.isEmpty()) {
            try {
                int staffId = Integer.parseInt(staffIdStr);
                Staff staff = staffDAO.getStaffById(staffId);
                request.setAttribute("EDIT_STAFF", staff);
            } catch (Exception e) {
                request.setAttribute("ERROR_MSG", "Invalid staff ID");
            }
        }
        request.getRequestDispatcher(IConstant.PAGE_STAFF_PRE_EDITION).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String staffIdStr = request.getParameter("staffId");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String statusStr = request.getParameter("status");

        try {
            int staffId = Integer.parseInt(staffIdStr);
            Staff currentStaff = staffDAO.getStaffById(staffId);
            
            if (currentStaff == null) {
                response.sendRedirect(IConstant.ACTION_MANAGER_STAFFS);
                return;
            }

            String passwordHash = (password != null && !password.isEmpty()) ? password : currentStaff.getPasswordHash();
            Staff updatedStaff = new Staff(staffId, fullName.trim(), role.trim(), username.trim(), passwordHash, phone, email, "1".equals(statusStr));
            
            boolean success = staffDAO.updateStaff(updatedStaff);
            request.setAttribute("IS_SUCCESS", success);
            request.setAttribute("MESSAGE", success ? "Updated successfully" : "Update failed");
            request.getRequestDispatcher(IConstant.PAGE_STAFF_RESULT_EDITION).forward(request, response);
        } catch (Exception e) {
            request.setAttribute("ERROR_MSG", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
