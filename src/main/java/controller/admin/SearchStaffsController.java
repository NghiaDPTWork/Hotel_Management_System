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

@WebServlet("/admin/searchStaffs")
public class SearchStaffsController extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        try {
            // ===============================================
            // ========== SEARCH PARAMETERS RETRIEVAL ========
            // ===============================================
            String searchTxt = request.getParameter("searchTxt");
            String searchStatus = request.getParameter("searchStatus");

            // ===============================================
            // ========== SEARCH EXECUTION ===================
            // ===============================================
            ArrayList<Staff> staffList;

            if (searchStatus == null || searchStatus.trim().isEmpty() || "all".equalsIgnoreCase(searchStatus.trim())) {
                // Search by keyword only
                staffList = staffDAO.searchStaffByKeyword(searchTxt);
                System.out.println("Search by keyword: " + searchTxt);
            } else {
                try {
                    int status = Integer.parseInt(searchStatus.trim());
                    staffList = staffDAO.searchStaffByKeywordAndStatus(searchTxt, status);
                    System.out.println("Search by keyword: " + searchTxt + " and status: " + status);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid status format: " + searchStatus + ", searching by keyword only");
                    staffList = staffDAO.searchStaffByKeyword(searchTxt);
                }
            }

            // ===============================================
            // ========= SET SEARCH RESULT ATTRIBUTES ========
            // ===============================================
            request.setAttribute("STAFF_SEARCH_LIST", staffList);
            request.setAttribute("SEARCH_VALUE", searchTxt);
            request.setAttribute("SEARCH_STATUS", searchStatus);

            // ===============================================
            // ============== LOGGING RESULTS ================
            // ===============================================
            System.out.println("Search completed: " + (staffList != null ? staffList.size() : 0) + " staff found");

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in SearchStaffsController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR_MESSAGE", "An error occurred while searching for staff!");
        }

        // ===============================================
        // ====== FORWARD TO STAFF MANAGER PAGE ==========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_STAFFS_MANAGER).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Search Staffs Controller - Handles staff search with optional status filter";
    }
}