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

@WebServlet("/admin/pre_editStaff")
public class PrefixEditStaffController extends HttpServlet {

    private static final String ERROR_MSG = "ERROR_MSG";

    private StaffDAO staffDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // ===============================================
            // ========== PARAMETER RETRIEVAL ================
            // ===============================================
            String staffIdStr = request.getParameter("staffId");

            // ===============================================
            // ============ PARAMETER VALIDATION =============
            // ===============================================
            if (staffIdStr == null || staffIdStr.trim().isEmpty()) {
                request.setAttribute(ERROR_MSG, "Invalid staff ID!");
                request.getRequestDispatcher(IConstant.PAGE_STAFF_PRE_EDITION).forward(request, response);
                return;
            }
            
            int staffId = Integer.parseInt(staffIdStr.trim());

            // ===============================================
            // ======== RETRIEVE STAFF FROM DATABASE =========
            // ===============================================
            Staff staff = staffDAO.getStaffById(staffId);

            if (staff != null) {
                request.setAttribute("EDIT_STAFF", staff);
                System.out.println("Staff loaded for editing: " + staff.getFullName() + " (ID: " + staffId + ")");
            } else {
                request.setAttribute(ERROR_MSG, "Staff member with ID " + staffId + " not found!");
                System.err.println("Staff not found: ID " + staffId);
            }

        } catch (NumberFormatException e) {
            // ===============================================
            // ======== NUMBER FORMAT EXCEPTION ==============
            // ===============================================
            request.setAttribute(ERROR_MSG, "Invalid staff ID format!");
            System.err.println("Error parsing staffId in PrefixEditStaffController: " + e.getMessage());

        } catch (Exception e) {
            // ===============================================
            // ============ GENERAL EXCEPTION ================
            // ===============================================
            request.setAttribute(ERROR_MSG, "An error occurred while loading staff information!");
            System.err.println("Error in PrefixEditStaffController: " + e.getMessage());
            e.printStackTrace();
        }

        // ===============================================
        // ======== FORWARD TO EDIT STAFF FORM ===========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_STAFF_PRE_EDITION).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Prefix Edit Staff Controller - Loads staff information for editing";
    }
}