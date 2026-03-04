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

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/pre_editSystem")
public class PrefixEditSystemController extends HttpServlet {

    private static final String ERROR_MESSAGE = "ERROR_MESSAGE";

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

        try {
            // ===============================================
            // ========== PARAMETER RETRIEVAL ================
            // ===============================================
            String configIdStr = request.getParameter("configId");

            // ===============================================
            // ============ PARAMETER VALIDATION =============
            // ===============================================
            if (configIdStr == null || configIdStr.trim().isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "Configuration ID not found!");
                response.sendRedirect(IConstant.ACTION_MANAGER_SYSTEM);
                return;
            }

            // ===============================================
            // ============ PARSE CONFIG ID ==================
            // ===============================================
            int configId = Integer.parseInt(configIdStr);

            // ===============================================
            // ====== RETRIEVE CONFIG FROM DATABASE ==========
            // ===============================================
            SystemConfig config = systemConfigDAO.getSystemConfigById(configId);

            if (config != null) {
                request.setAttribute("CONFIG", config);
                System.out.println("System config loaded for editing: " + config.getConfigName() + " (ID: " + configId + ")");
                
                // ===============================================
                // ======== FORWARD TO EDIT CONFIG FORM ==========
                // ===============================================
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_EDIT).forward(request, response);
            } else {
                request.setAttribute(ERROR_MESSAGE, "Configuration with ID " + configId + " not found!");
                System.err.println("System config not found: ID " + configId);
                response.sendRedirect(IConstant.ACTION_MANAGER_SYSTEM);
            }

        } catch (NumberFormatException e) {
            // ===============================================
            // ======== NUMBER FORMAT EXCEPTION ==============
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "Invalid configuration ID format!");
            System.err.println("Error parsing configId in PrefixEditSystemController: " + e.getMessage());
            response.sendRedirect(IConstant.ACTION_MANAGER_SYSTEM);

        } catch (Exception e) {
            // ===============================================
            // ============ GENERAL EXCEPTION ================
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "An error occurred while loading configuration information!");
            System.err.println("Error in PrefixEditSystemController: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(IConstant.ACTION_MANAGER_SYSTEM);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Prefix Edit System Controller - Loads system configuration for editing";
    }
}