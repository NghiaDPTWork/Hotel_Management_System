package controller.admin;

import dao.SystemConfigDAO;
import dto.SystemConfig;
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

@WebServlet("/admin/viewEditSystem")
public class ViewEditSystemController extends HttpServlet {

    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            // ===============================================
            // ====== RETRIEVE SYSTEM CONFIGS FROM DB ========
            // ===============================================
            ArrayList<SystemConfig> configList = systemConfigDAO.getAllSystemConfig();

            // ===============================================
            // ========= VALIDATE AND SET ATTRIBUTES =========
            // ===============================================
            if (configList != null && !configList.isEmpty()) {
                request.setAttribute("CONFIG_LIST", configList);
                System.out.println("System configurations loaded: " + configList.size() + " configs");
            } else {
                // Empty list - set empty ArrayList and info message
                request.setAttribute("CONFIG_LIST", new ArrayList<SystemConfig>());
                request.setAttribute("MESSAGE", "No system configurations found.");
                System.out.println("No system configurations found in database");
            }

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in ViewEditSystemController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("CONFIG_LIST", new ArrayList<SystemConfig>());
            request.setAttribute("ERROR_MESSAGE", "An error occurred while loading system configurations. Please try again!");
        }

        // ===============================================
        // ===== FORWARD TO SYSTEM VIEW/EDIT PAGE ========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_SYSTEM_VIEW_EDIT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Edit System Controller - Displays system configuration list for viewing and editing";
    }
}