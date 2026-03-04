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
import java.util.ArrayList;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/editStaffs")
public class StaffsEditionController extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        try {
            // ===============================================
            // ========= RETRIEVE ALL STAFF FROM DB ==========
            // ===============================================
            ArrayList<Staff> staffList = staffDAO.getAllStaff();

            // ===============================================
            // ========= SET ATTRIBUTES FOR JSP ==============
            // ===============================================
            request.setAttribute("STAFF_LIST", staffList);

            // ===============================================
            // ============== LOGGING DATA ===================
            // ===============================================
            System.out.println("Staff list loaded for edition: " + (staffList != null ? staffList.size() : 0) + " staff members");

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in StaffsEditionController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR_MESSAGE", "An error occurred while loading staff list!");
        }

        // ===============================================
        // ===== FORWARD TO STAFF EDITION PAGE ===========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_STAFFS_EDITION).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Staffs Edition Controller - Displays staff list for editing management";
    }
}