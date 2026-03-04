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

@WebServlet("/admin/editSystem")
public class EditSystemController extends HttpServlet {

    private static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    private static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";

    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        systemConfigDAO = new SystemConfigDAO();
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

        try {
            // ===============================================
            // ========== FORM DATA RETRIEVAL ================
            // ===============================================
            String configIdStr = request.getParameter("configId");
            String configValue = request.getParameter("configValue");
            String statusStr = request.getParameter("status");

            // ===============================================
            // ======== CONFIGURATION ID VALIDATION ==========
            // ===============================================
            if (configIdStr == null || configIdStr.trim().isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "Configuration ID is missing!");
                request.setAttribute("EDIT_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_VIEW_EDIT).forward(request, response);
                return;
            }

            // ===============================================
            // ======== CONFIGURATION VALUE VALIDATION =======
            // ===============================================
            if (configValue == null || configValue.trim().isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "Configuration value cannot be empty!");
                request.setAttribute("EDIT_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_EDIT_RESULT).forward(request, response);
                return;
            }

            int configId = Integer.parseInt(configIdStr);
            boolean status = (statusStr != null && "true".equals(statusStr));

            // ===============================================
            // ======== RETRIEVE CURRENT CONFIGURATION =======
            // ===============================================
            SystemConfig currentConfig = systemConfigDAO.getSystemConfigById(configId);

            if (currentConfig == null) {
                request.setAttribute(ERROR_MESSAGE, "Configuration with ID " + configId + " not found!");
                request.setAttribute("EDIT_SUCCESS", false);
                request.getRequestDispatcher(IConstant.PAGE_SYSTEM_EDIT_RESULT).forward(request, response);
                return;
            }

            // ===============================================
            // ======== UPDATED CONFIG OBJECT CREATION =======
            // ===============================================
            SystemConfig updatedConfig = new SystemConfig(
                configId,
                currentConfig.getConfigName(), // Keep original name
                configValue.trim(),
                status
            );

            // ===============================================
            // =========== DATABASE UPDATE OPERATION =========
            // ===============================================
            boolean isSuccess = systemConfigDAO.updateSystemConfig(updatedConfig);

            if (isSuccess) {
                request.setAttribute(SUCCESS_MESSAGE, "Configuration updated successfully!");
                request.setAttribute("EDIT_SUCCESS", true);
                request.setAttribute("UPDATED_CONFIG", updatedConfig);
                System.out.println("System config updated: " + currentConfig.getConfigName() + " (ID: " + configId + ")");
            } else {
                request.setAttribute(ERROR_MESSAGE, "Failed to update configuration! Please try again.");
                request.setAttribute("EDIT_SUCCESS", false);
                System.err.println("Failed to update config ID: " + configId);
            }

        } catch (NumberFormatException e) {
            // ===============================================
            // ======== NUMBER FORMAT EXCEPTION ==============
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "Invalid data format!");
            request.setAttribute("EDIT_SUCCESS", false);
            System.err.println("Error parsing number in EditSystemController: " + e.getMessage());

        } catch (Exception e) {
            // ===============================================
            // ============ GENERAL EXCEPTION ================
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "An error occurred: " + e.getMessage());
            request.setAttribute("EDIT_SUCCESS", false);
            System.err.println("Error in EditSystemController: " + e.getMessage());
            e.printStackTrace();
        }

        // ===============================================
        // ======== FORWARD TO RESULT PAGE ===============
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_SYSTEM_EDIT_RESULT).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Edit System Configuration Controller - Handles system config updates";
    }
}