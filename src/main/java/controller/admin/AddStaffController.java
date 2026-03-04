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

@WebServlet("/admin/addStaff")
public class AddStaffController extends HttpServlet {

    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(IConstant.PAGE_STAFF_ADD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        String validationError = ValidationUtil.validateStaff(fullName, role, username, password, phone, email);
        
        if (validationError != null) {
            forwardWithResult(request, response, false, validationError, "error", fullName);
            return;
        }

        try {
            if (staffDAO.isUsernameExist(username.trim())) {
                forwardWithResult(request, response, false, "Username already exists!", "error", fullName);
            } else if (staffDAO.isEmailExist(email.trim())) {
                forwardWithResult(request, response, false, "Email is already in use!", "error", fullName);
            } else {
                Staff newStaff = new Staff(0, fullName.trim(), role.trim(), username.trim(), password, phone.trim(), email.trim(), true);
                boolean isSuccess = staffDAO.addStaff(newStaff);
                
                String message = isSuccess ? "Staff \"" + fullName + "\" added successfully!" : "Failed to add staff!";
                String messageType = isSuccess ? "success" : "error";
                forwardWithResult(request, response, isSuccess, message, messageType, fullName);
            }
        } catch (Exception e) {
            forwardWithResult(request, response, false, "Error: " + e.getMessage(), "error", fullName);
        }
    }

    private void forwardWithResult(HttpServletRequest request, HttpServletResponse response, boolean success, String message, String type, String name) 
            throws ServletException, IOException {
        request.setAttribute("IS_SUCCESS", success);
        request.setAttribute("MESSAGE", message);
        request.setAttribute("MESSAGE_TYPE", type);
        request.setAttribute("STAFF_NAME", name);
        request.getRequestDispatcher(IConstant.PAGE_STAFF_RESULT_ADD).forward(request, response);
    }
}
