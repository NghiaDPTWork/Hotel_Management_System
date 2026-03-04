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
import java.util.ArrayList;

/**
 * 
 * @author TR_NGHIA
 */
@WebServlet("/admin/viewEditService")
public class ViewEditServiceController extends HttpServlet {
    
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
        
        try {
            // ===============================================
            // ======== RETRIEVE SERVICES FROM DB ============
            // ===============================================
            ArrayList<Service> serviceList = serviceDAO.getAllService();
            
            // ===============================================
            // ========= VALIDATE AND SET ATTRIBUTES =========
            // ===============================================
            if (serviceList != null && !serviceList.isEmpty()) {
                request.setAttribute("SERVICE_LIST", serviceList);
                System.out.println("Services loaded: " + serviceList.size() + " services");
            } else {
                // Empty list - set empty ArrayList and info message
                request.setAttribute("SERVICE_LIST", new ArrayList<Service>());
                request.setAttribute("MESSAGE", "Ch?a có d?ch v? nào trong h? th?ng.");
                System.out.println("No services found in database");
            }
            
        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in ViewEditServiceController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("SERVICE_LIST", new ArrayList<Service>());
            request.setAttribute("ERROR_MESSAGE", "?ã x?y ra l?i khi t?i danh sách d?ch v?. Vui lòng th? l?i!");
        }
        
        // ===============================================
        // ===== FORWARD TO SERVICE VIEW/EDIT PAGE =======
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_SERVICE_VIEW_EDIT).forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "View Edit Service Controller - Displays service list for viewing and editing";
    }
}