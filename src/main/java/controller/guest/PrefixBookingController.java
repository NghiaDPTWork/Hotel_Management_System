package controller.guest;
import dao.PaymentDAO;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dao.ServiceDAO;
import dao.SystemConfigDAO;
import dto.Guest;
import dto.Room;
import dto.RoomType;
import dto.Service;
import dto.SystemConfig;
import util.IConstant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/pre_booking")
public class PrefixBookingController extends HttpServlet {
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private ServiceDAO serviceDAO;
    private PaymentDAO paymentDAO;
    private SystemConfigDAO systemConfigDAO;
    
    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        serviceDAO = new ServiceDAO();
        paymentDAO = new PaymentDAO();
        systemConfigDAO = new SystemConfigDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // ===============================================
        // ============ SESSION & PARAMETERS =============
        // ===============================================
        HttpSession session = req.getSession();
        String roomIdStr = req.getParameter("roomId");
        String checkIn = req.getParameter("checkInDate");
        String checkOut = req.getParameter("checkOutDate");
        
        // ===============================================
        // ============ USER LOGIN VALIDATION ============
        // ===============================================
        Guest loggedInGuest = (Guest) session.getAttribute("USER_GUEST");
        if (loggedInGuest == null) {
            resp.sendRedirect(IConstant.ACTION_HOME);
            return;
        }
        
        // ===============================================
        // ============= ROOM DATA RETRIEVAL =============
        // ===============================================
        int roomId = Integer.parseInt(roomIdStr);
        Room room = roomDAO.getRoomById(roomId);
        RoomType roomType = roomTypeDAO.getRoomTypeById(room.getRoomTypeId());
        
        // ===============================================
        // ============ SERVICE DATA RETRIEVAL ===========
        // ===============================================
        ArrayList<Service> allServices = serviceDAO.getAllService();
        
        // ===============================================
        // ============= TAX RATE RETRIEVAL ==============
        // ===============================================
        SystemConfig taxConfig = systemConfigDAO.getSystemConfigByName("TaxRate");
        double taxRate = 5.0;
        
        if (taxConfig != null && taxConfig.isStatus()) {
            // Status = 1: Use configured value
            try {
                taxRate = Double.parseDouble(taxConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing tax rate: " + e.getMessage());
                taxRate = 5.0; 
            }
        }
        
        // ===============================================
        // ========= DEPOSIT RATE RETRIEVAL ==============
        // ===============================================
        SystemConfig depositConfig = systemConfigDAO.getSystemConfigByName("BookingDepositRate");
        double depositRate = 30.0;
        
        if (depositConfig != null && depositConfig.isStatus()) {
            // Status = 1: Use configured value
            try {
                depositRate = Double.parseDouble(depositConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing deposit rate: " + e.getMessage());
                depositRate = 30.0;
            }
        }
        
        // ===============================================
        // =========== SET ATTRIBUTES FOR VIEW ===========
        // ===============================================
        req.setAttribute("ROOM", room);
        req.setAttribute("ROOM_TYPE", roomType);
        req.setAttribute("CHECK_IN", checkIn);
        req.setAttribute("CHECK_OUT", checkOut);
        req.setAttribute("ALL_SERVICES", allServices);
        req.setAttribute("GUEST", loggedInGuest);
        req.setAttribute("TAX", taxRate);
        req.setAttribute("DEPOSIT_RATE", depositRate);
        
        // ===============================================
        // ========== FORWARD TO BOOKING PAGE ============
        // ===============================================
        req.getRequestDispatcher(IConstant.PAGE_BOOKING).forward(req, resp);
    }
}