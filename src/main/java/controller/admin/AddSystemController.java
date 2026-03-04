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

@WebServlet("/admin/addSystem")
public class AddSystemController extends HttpServlet {

    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(IConstant.PAGE_SYSTEM_ADD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ===============================================
        // ========== FORM DATA RETRIEVAL ================
        // ===============================================
        String configName = request.getParameter("configName");
        String configValue = request.getParameter("configValue");
        String statusStr = request.getParameter("status");

        // ===============================================
        // ========= INITIALIZE RESULT VARIABLES =========
        // ===============================================
        boolean isSuccess = false;
        String message = "";

        try {
            // ===============================================
            // ============ INPUT VALIDATION =================
            // ===============================================
            if (configName == null || configName.trim().isEmpty()) {
                message = "Configuration name cannot be empty!";
                request.setAttribute("ERROR_MESSAGE", message);
                request.setAttribute("ADD_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_ADD_RESULT).forward(request, response);
                return;
            }

            if (configValue == null || configValue.trim().isEmpty()) {
                message = "Configuration value cannot be empty!";
                request.setAttribute("ERROR_MESSAGE", message);
                request.setAttribute("ADD_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_ADD_RESULT).forward(request, response);
                return;
            }

            // ===============================================
            // ========== STATUS PARAMETER PARSING ===========
            // ===============================================
            boolean status = (statusStr != null && "true".equals(statusStr));

            // ===============================================
            // ========= DUPLICATE CHECK VALIDATION ==========
            // ===============================================
            if (systemConfigDAO.isConfigNameExist(configName.trim())) {
                message = "Configuration name \"" + configName + "\" already exists in the system!";
                request.setAttribute("ERROR_MESSAGE", message);
                request.setAttribute("ADD_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_ADD_RESULT).forward(request, response);
                return;
            }

            // ===============================================
            // ========= SYSTEM CONFIG OBJECT CREATION =======
            // ===============================================
            SystemConfig newConfig = new SystemConfig(
                0,
                configName.trim(),
                configValue.trim(),
                status
            );

            // ===============================================
            // =========== DATABASE INSERT OPERATION =========
            // ===============================================
            isSuccess = systemConfigDAO.addSystemConfig(newConfig);

            if (isSuccess) {
                message = "Configuration \"" + configName + "\" added successfully!";
                request.setAttribute("SUCCESS_MESSAGE", message);
                request.setAttribute("ADD_SUCCESS", true);
                request.setAttribute("CONFIG_NEW", newConfig);
                System.out.println("New system config added: " + configName + " = " + configValue);
            } else {
                message = "Failed to add configuration! Please try again.";
                request.setAttribute("ERROR_MESSAGE", message);
                request.setAttribute("ADD_SUCCESS", false);
                System.err.println("Failed to add system config: " + configName);
            }

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            message = "An error occurred: " + e.getMessage();
            request.setAttribute("ERROR_MESSAGE", message);
            request.setAttribute("ADD_SUCCESS", false);
            System.err.println("Error in AddSystemController: " + e.getMessage());
            e.printStackTrace();
        }

        // ===============================================
        // ======== FORWARD TO RESULT PAGE ===============
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_SYSTEM_ADD_RESULT).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Add System Configuration Controller - Handles system config creation";
    }
}