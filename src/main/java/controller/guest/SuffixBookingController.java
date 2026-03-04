package controller.guest;

import dao.BookingDAO;
import dao.BookingServiceDAO;
import dao.GuestDAO;
import dao.PaymentDAO;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dao.ServiceDAO;
import dao.SystemConfigDAO;
import dto.Booking;
import dto.BookingService;
import dto.ChoosenService;
import dto.Guest;
import dto.Payment;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/suf_booking")
public class SuffixBookingController extends HttpServlet {

    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private GuestDAO guestDAO;
    private BookingServiceDAO bookingServiceDAO;
    private ServiceDAO serviceDAO;
    private PaymentDAO paymentDAO;
    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        guestDAO = new GuestDAO();
        bookingServiceDAO = new BookingServiceDAO();
        serviceDAO = new ServiceDAO();
        paymentDAO = new PaymentDAO();
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ===============================================
        // ============== CHECK SUCCESS STATUS ===========
        // ===============================================
        String successParam = req.getParameter("success");
        boolean isSuccess = "true".equals(successParam);

        if (!isSuccess) {
            req.setAttribute("SUCCESS", false);
            req.setAttribute("ERROR_MSG", "Fail to create booking");
            req.getRequestDispatcher(IConstant.PAGE_BOOKING_RESULT).forward(req, resp);
            return;
        }

        // ===============================================
        // ========== GET BOOKING ID & RETRIEVE DATA =====
        // ===============================================
        String bookingIdStr = req.getParameter("bookingId");
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            req.setAttribute("SUCCESS", false);
            req.setAttribute("ERROR_MSG", "Can not get Booking Id");
            req.getRequestDispatcher(IConstant.PAGE_BOOKING_RESULT).forward(req, resp);
            return;
        }

        int bookingId = Integer.parseInt(bookingIdStr);
        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            req.setAttribute("SUCCESS", false);
            req.setAttribute("ERROR_MSG", "Your booking can not be created");
            req.getRequestDispatcher(IConstant.PAGE_BOOKING_RESULT).forward(req, resp);
            return;
        }

        // ===============================================
        // ============ RETRIEVE RELATED DATA ============
        // ===============================================
        Guest guest = guestDAO.getGuestById(booking.getGuestId());
        Room room = roomDAO.getRoomById(booking.getRoomId());
        RoomType roomType = roomTypeDAO.getRoomTypeById(room.getRoomTypeId());

        List<BookingService> bookingServices = bookingServiceDAO.getBookingServiceByBookingId(bookingId);
        ArrayList<Service> allServices = serviceDAO.getAllService();

        Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
        String paymentMethod = payment != null ? payment.getPaymentMethod() : "N/A";

        // ===============================================
        // ======== TAX & DEPOSIT RATE RETRIEVAL =========
        // ===============================================
        SystemConfig taxConfig = systemConfigDAO.getSystemConfigByName("TaxRate");
        double taxRate = 5.0;

        if (taxConfig != null && taxConfig.isStatus()) {
            try {
                taxRate = Double.parseDouble(taxConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing tax rate: " + e.getMessage());
                taxRate = 5.0;
            }
        }

        SystemConfig depositConfig = systemConfigDAO.getSystemConfigByName("BookingDepositRate");
        double depositRate = 30.0;

        if (depositConfig != null && depositConfig.isStatus()) {
            try {
                depositRate = Double.parseDouble(depositConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing deposit rate: " + e.getMessage());
                depositRate = 30.0;
            }
        }

        // ===============================================
        // ============ CALCULATE TOTAL AMOUNT ===========
        // ===============================================
        long numberOfNights = ChronoUnit.DAYS.between(
                booking.getCheckInDate().toLocalDate(),
                booking.getCheckOutDate().toLocalDate()
        );

        if (numberOfNights <= 0) {
            numberOfNights = 1;
        }

        BigDecimal roomTotal = roomType.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
        BigDecimal servicesTotal = BigDecimal.ZERO;

        // Convert BookingService to ChoosenService for JSP
        ArrayList<ChoosenService> chosenServices = new ArrayList<>();
        if (bookingServices != null && !bookingServices.isEmpty()) {
            for (BookingService bs : bookingServices) {
                Service service = serviceDAO.getServiceById(bs.getServiceId());
                if (service != null && service.getPrice() != null) {
                    BigDecimal serviceItemTotal = service.getPrice()
                            .multiply(BigDecimal.valueOf(bs.getQuantity()));
                    servicesTotal = servicesTotal.add(serviceItemTotal);

                    ChoosenService cs = new ChoosenService(
                            bs.getServiceId(),
                            bs.getQuantity(),
                            bs.getServiceDate()
                    );
                    chosenServices.add(cs);
                }
            }
        }

        BigDecimal subtotal = roomTotal.add(servicesTotal);
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(taxRate / 100.0));
        BigDecimal grandTotal = subtotal.add(taxAmount);

        // ===============================================
        // =========== SET ATTRIBUTES FOR VIEW ===========
        // ===============================================
        req.setAttribute("SUCCESS", true);
        req.setAttribute("BOOKING", booking);
        req.setAttribute("GUEST", guest);
        req.setAttribute("ROOM", room);
        req.setAttribute("ROOM_TYPE", roomType);
        req.setAttribute("CHOSEN_SERVICES", chosenServices);
        req.setAttribute("SERVICE_LIST", allServices);
        req.setAttribute("PAYMENT_METHOD", paymentMethod);
        req.setAttribute("TOTAL_AMOUNT", String.valueOf(grandTotal.doubleValue()));
        req.setAttribute("TAX_RATE", taxRate);
        req.setAttribute("DEPOSIT_RATE", depositRate);

        // ===============================================
        // ========== FORWARD TO RESULT PAGE =============
        // ===============================================
        req.getRequestDispatcher(IConstant.PAGE_BOOKING_RESULT).forward(req, resp);
    }
}
