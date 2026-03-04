package controller.admin;

import dao.ServiceDAO;
import dto.Service;
import util.IConstant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * @author TR_NGHIA
 */
@WebServlet("/admin/addService")
public class AddServiceController extends HttpServlet {

    private ServiceDAO serviceDAO;

    @Override
    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Forward to add service form page
        request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            // ===============================================
            // ========== GET DATA FROM FORM =================
            // ===============================================
            String serviceName = request.getParameter("serviceName");
            String serviceType = request.getParameter("serviceType");
            String priceStr = request.getParameter("price");

            // ===============================================
            // ============== VALIDATE DATA ==================
            // ===============================================
            // Validate service name
            if (serviceName == null || serviceName.trim().isEmpty()) {
                request.setAttribute("ERROR_MESSAGE", "Service name cannot be empty!");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                return;
            }

            // Check if service name already exists
            if (serviceDAO.isServiceNameExists(serviceName.trim())) {
                request.setAttribute("ERROR_MESSAGE", "Service name '" + serviceName.trim() + "' already exists! Please use a different name.");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                return;
            }

            // Validate service type
            if (serviceType == null || serviceType.trim().isEmpty()) {
                request.setAttribute("ERROR_MESSAGE", "Service type cannot be empty!");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                return;
            }

            // Validate price string
            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("ERROR_MESSAGE", "Service price cannot be empty!");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                return;
            }

            // Convert price to BigDecimal and validate
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);

                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("ERROR_MESSAGE", "Service price must be greater than 0!");
                    request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("ERROR_MESSAGE", "Invalid service price format!");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD).forward(request, response);
                return;
            }

            // ===============================================
            // ============= ADD SERVICE TO DB ===============
            // ===============================================
            Service newService = new Service(0, serviceName.trim(), serviceType.trim(), price);
            boolean isAdded = serviceDAO.addService(newService);

            if (isAdded) {
                // Add successfully - Forward to result page with success message
                System.out.println("Service added successfully: " + serviceName);
                request.setAttribute("ADD_SUCCESS", true);
                request.setAttribute("SUCCESS_MESSAGE", "Service '" + serviceName + "' has been added successfully!");
                request.setAttribute("SERVICE_NEW", newService);
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD_RESULT).forward(request, response);
            } else {
                // Add failed - Forward to result page with error message
                System.err.println("Failed to add service: " + serviceName);
                request.setAttribute("ADD_SUCCESS", false);
                request.setAttribute("ERROR_MESSAGE", "Unable to add service. Please try again!");
                request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD_RESULT).forward(request, response);
            }

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in AddServiceController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ADD_SUCCESS", false);
            request.setAttribute("ERROR_MESSAGE", "An error occurred while adding service. Please try again!");
            request.getRequestDispatcher(IConstant.PAGE_SERVICE_ADD_RESULT).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Add Service Controller - Handles adding new services to the system";
    }
}
