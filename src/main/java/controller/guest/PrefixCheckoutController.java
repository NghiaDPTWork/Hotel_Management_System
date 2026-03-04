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

/**
 *
 * @author TR_NGHIA
 */
@WebServlet("/pre_checkout")
public class PrefixCheckoutController extends HttpServlet {

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

        try {
            // ===============================================
            // =========== GET BOOKING ID FROM REQUEST ========
            // ===============================================
            String bookingIdStr = req.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Booking ID is required");
                req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
                return;
            }

            int bookingId = Integer.parseInt(bookingIdStr);

            // ===============================================
            // ============== GET BOOKING DETAIL ==============
            // ===============================================
            Booking booking = bookingDAO.getBookingById(bookingId);
            Guest guest = guestDAO.getGuestById(booking.getGuestId());
            Room room = roomDAO.getRoomById(booking.getRoomId());
            RoomType roomType = roomTypeDAO.getRoomTypeById(room.getRoomTypeId());

            // ===============================================
            // ============== GET BOOKING SERVICES ===========
            // ===============================================
            ArrayList<BookingService> bookingServices = bookingServiceDAO.getBookingServiceByBookingId(bookingId);
            ArrayList<Service> services = new ArrayList<>();

            if (bookingServices != null && !bookingServices.isEmpty()) {
                for (BookingService bs : bookingServices) {
                    Service service = serviceDAO.getServiceById(bs.getServiceId());
                    if (service != null) {
                        services.add(service);
                    }
                }
            }

            // ===============================================
            // ============= CALCULATE ROOM FEE ===============
            // ===============================================
            long numberOfNights = ChronoUnit.DAYS.between(
                    booking.getCheckInDate().toLocalDate(),
                    booking.getCheckOutDate().toLocalDate()
            );
            if (numberOfNights < 1) {
                numberOfNights = 1;
            }

            BigDecimal roomFee = roomType.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));

            // ===============================================
            // =========== CALCULATE SERVICE TOTAL ===========
            // ===============================================
            BigDecimal serviceTotalFee = BigDecimal.ZERO;
            if (bookingServices != null && !bookingServices.isEmpty()) {
                for (int i = 0; i < bookingServices.size(); i++) {
                    Service service = services.get(i);
                    BookingService bs = bookingServices.get(i);
                    if (service != null && service.getPrice() != null) {
                        BigDecimal serviceItemTotal = service.getPrice()
                                .multiply(BigDecimal.valueOf(bs.getQuantity()));
                        serviceTotalFee = serviceTotalFee.add(serviceItemTotal);
                    }
                }
            }

            // ===============================================
            // ======== GET TAX RATE FROM DATABASE ===========
            // ===============================================
            SystemConfig taxConfig = systemConfigDAO.getSystemConfigByName("TaxRate");
            double taxRate = 30.0;

            if (taxConfig != null && taxConfig.isStatus()) {
                try {
                    taxRate = Double.parseDouble(taxConfig.getConfigValue());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing tax rate: " + e.getMessage());
                    taxRate = 10.0;
                }
            }

            // ===============================================
            // =============== CALCULATE TAX & TOTAL =========
            // ===============================================
            BigDecimal subtotal = roomFee.add(serviceTotalFee);
            BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(taxRate / 100.0));
            BigDecimal grandTotal = subtotal.add(taxAmount);

            // ===============================================
            // =========== GET PAYMENT INFORMATION ============
            // ===============================================
            ArrayList<Payment> payments = paymentDAO.getPaymentsByBookingId(bookingId);
            double totalPaid = 0.0;

            if (payments != null) {
                for (Payment p : payments) {
                    if (!"Failed".equalsIgnoreCase(p.getStatus())) {
                        totalPaid += p.getAmount();
                    }
                }
            }

            // ===============================================
            // ========== CALCULATE REMAINING AMOUNT =========
            // ==============================================
            double remainingAmount = grandTotal.doubleValue() - totalPaid;
            if (remainingAmount < 0) {
                remainingAmount = 0;
            }

            // ===============================================
            // ================= SET ATTRIBUTES ===============
            // ===============================================
            req.setAttribute("BOOKING", booking);
            req.setAttribute("GUEST", guest);
            req.setAttribute("ROOM", room);
            req.setAttribute("ROOM_TYPE", roomType);
            req.setAttribute("BOOKING_SERVICES", bookingServices);
            req.setAttribute("SERVICES", services);
            req.setAttribute("PAYMENTS", payments);

            // Thông tin tính toán ?? hi?n th?
            req.setAttribute("NUMBER_OF_NIGHTS", numberOfNights);
            req.setAttribute("ROOM_FEE", roomFee);
            req.setAttribute("SERVICE_TOTAL_FEE", serviceTotalFee);
            req.setAttribute("SUBTOTAL", subtotal);
            req.setAttribute("TAX_AMOUNT", taxAmount);
            req.setAttribute("GRAND_TOTAL", grandTotal);
            req.setAttribute("TOTAL_PAID", totalPaid);
            req.setAttribute("REMAINING_AMOUNT", remainingAmount);

            // Forward ??n trang checkout
            req.getRequestDispatcher(IConstant.PAGE_CHECKOUT).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("ERROR_MESSAGE", "Error: " + e.getMessage());
            req.getRequestDispatcher(IConstant.PAGE_CHECKOUT).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Prefix Checkout Controller - Prepares checkout information with payment calculations";
    }
}
