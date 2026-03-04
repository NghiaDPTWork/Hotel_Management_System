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

@WebServlet("/admin/deleteSystem")
public class DeleteSystemController extends HttpServlet {

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

        // ===============================================
        // ========== PARAMETER RETRIEVAL ================
        // ===============================================
        String configIdStr = request.getParameter("configId");

        try {
            // ===============================================
            // ============ PARAMETER VALIDATION =============
            // ===============================================
            if (configIdStr == null || configIdStr.trim().isEmpty()) {
                request.setAttribute(ERROR_MESSAGE, "Configuration ID is missing!");
            } else {
                try {
                    int configId = Integer.parseInt(configIdStr);

                    // ===============================================
                    // ========== CONFIG EXISTENCE CHECK =============
                    // ===============================================
                    SystemConfig configToDelete = systemConfigDAO.getSystemConfigById(configId);

                    if (configToDelete == null) {
                        request.setAttribute(ERROR_MESSAGE, "Configuration with ID " + configId + " not found!");
                    } else {
                        // ===============================================
                        // =========== DATABASE DELETE OPERATION =========
                        // ===============================================
                        boolean isDeleted = systemConfigDAO.deleteSystemConfig(configId);

                        if (isDeleted) {
                            request.setAttribute(SUCCESS_MESSAGE, 
                                "Configuration \"" + configToDelete.getConfigName() + "\" deleted successfully!");
                            System.out.println("System config deleted: " + configToDelete.getConfigName() + " (ID: " + configId + ")");
                        } else {
                            request.setAttribute(ERROR_MESSAGE, 
                                "Failed to delete configuration \"" + configToDelete.getConfigName() + "\"! Please try again.");
                            System.err.println("Failed to delete config ID: " + configId);
                        }
                    }

                } catch (NumberFormatException e) {
                    // ===============================================
                    // ======== NUMBER FORMAT EXCEPTION ==============
                    // ===============================================
                    request.setAttribute(ERROR_MESSAGE, "Invalid configuration ID format!");
                    System.err.println("Error parsing configId: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            // ===============================================
            // ============ GENERAL EXCEPTION ================
            // ===============================================
            request.setAttribute(ERROR_MESSAGE, "An error occurred while deleting configuration: " + e.getMessage());
            System.err.println("Error in DeleteSystemController: " + e.getMessage());
            e.printStackTrace();
        }

        // ===============================================
        // ======== RELOAD CONFIGURATION LIST ============
        // ===============================================
        try {
            ArrayList<SystemConfig> configList = systemConfigDAO.getAllSystemConfig();

            if (configList != null && !configList.isEmpty()) {
                request.setAttribute("CONFIG_LIST", configList);
                System.out.println("Loaded " + configList.size() + " system configurations");
            } else {
                request.setAttribute("CONFIG_LIST", new ArrayList<SystemConfig>());
                System.out.println("No system configurations found");
            }

        } catch (Exception e) {
            System.err.println("Error loading config list: " + e.getMessage());
            request.setAttribute("CONFIG_LIST", new ArrayList<SystemConfig>());
        }

        // ===============================================
        // ==== FORWARD TO SYSTEM MANAGEMENT PAGE ========
        // ===============================================
        request.getRequestDispatcher(IConstant.ACTION_VIEW_EDIT_SYSTEM).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Delete System Configuration Controller - Handles system config deletion";
    }
}