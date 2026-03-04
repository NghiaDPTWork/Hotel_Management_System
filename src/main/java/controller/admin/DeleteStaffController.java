package controller.admin;

import dao.StaffDAO;
import dto.Staff;
import util.IConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/deleteStaff")
public class DeleteStaffController extends HttpServlet {

    private static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    private static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";

    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ===============================================
        // ========== PARAMETER RETRIEVAL ================
        // ===============================================
        String staffIdParam = request.getParameter("staffId");

        try {
            // ===============================================
            // ============ PARAMETER VALIDATION =============
            // ===============================================
            if (staffIdParam == null || staffIdParam.trim().isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "Staff ID not found!");
                request.getRequestDispatcher(IConstant.ACTION_EDIT_STAFFS).forward(request, response);
                return;
            }
            
            int staffId = Integer.parseInt(staffIdParam);

            // ===============================================
            // ========== STAFF EXISTENCE CHECK ==============
            // ===============================================
            Staff existingStaff = staffDAO.getStaffById(staffId);
            if (existingStaff == null) {
                request.setAttribute(ERROR_MESSAGE, "Staff member does not exist in the system!");
                request.getRequestDispatcher(IConstant.ACTION_EDIT_STAFFS).forward(request, response);
                return;
            }

            // ===============================================
            // ======== PREVENT SELF-DELETION CHECK ==========
            // ===============================================
            Staff currentStaff = (Staff) request.getSession().getAttribute("USER_STAFF");
            if (currentStaff != null && currentStaff.getStaffId() == staffId) {
                request.setAttribute(ERROR_MESSAGE, "You cannot delete your own account!");
                request.getRequestDispatcher(IConstant.ACTION_EDIT_STAFFS).forward(request, response);
                return;
            }

            // ===============================================
            // =========== DATABASE DELETE OPERATION =========
            // ===============================================
            boolean isDeleted = staffDAO.deleteStaff(staffId);

            if (isDeleted) {
                request.setAttribute(SUCCESS_MESSAGE, "Staff member \"" + existingStaff.getFullName() + "\" deleted successfully!");
                System.out.println("Staff deleted: " + existingStaff.getFullName() + " (ID: " + staffId + ")");
            } else {
                request.setAttribute(ERROR_MESSAGE, "Failed to delete staff! Please try again.");
                System.err.println("Failed to delete staff ID: " + staffId);
            }

        } catch (NumberFormatException e) {
            // ===============================================
            // ======== NUMBER FORMAT EXCEPTION ==============
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "Invalid staff ID format!");
            System.err.println("Error parsing staffId: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            // ===============================================
            // ============ GENERAL EXCEPTION ================
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "An error occurred: " + e.getMessage());
            System.err.println("Error in DeleteStaffController: " + e.getMessage());
            e.printStackTrace();
        }

        // ===============================================
        // ===== FORWARD TO STAFF MANAGEMENT PAGE ========
        // ===============================================
        request.getRequestDispatcher(IConstant.ACTION_EDIT_STAFFS).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Delete Staff Controller - Handles staff deletion";
    }
}