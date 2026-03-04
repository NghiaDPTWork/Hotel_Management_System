package controller.guest;

import dao.BookingDAO;
import dao.BookingServiceDAO;
import dao.GuestDAO;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dao.ServiceDAO;
import dao.PaymentDAO;
import dto.Booking;
import dto.BookingService;
import dto.Guest;
import dto.Room;
import dto.RoomType;
import dto.Service;
import dto.Payment;
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
 *
 * @author TR_NGHIA
 */
@WebServlet("/pre_editBooking")
public class PrefixEditBookingController extends HttpServlet {

    private BookingDAO bookingDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private ServiceDAO serviceDAO;
    private BookingServiceDAO bookingServiceDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookingDAO = new BookingDAO();
        guestDAO = new GuestDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        serviceDAO = new ServiceDAO();
        bookingServiceDAO = new BookingServiceDAO();
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // ===============================================
            // ========== BOOKING ID VALIDATION ==============
            // ===============================================
            String bookingIdStr = request.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                response.sendRedirect(IConstant.ACTION_VIEW_BOOKINGS);
                return;
            }
            int bookingId = Integer.parseInt(bookingIdStr);

            // ===============================================
            // =========== BOOKING DATA RETRIEVAL ============
            // ===============================================
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                response.sendRedirect(IConstant.ACTION_VIEW_BOOKINGS);
                return;
            }

            // ===============================================
            // ========== GUEST AUTHORIZATION CHECK ==========
            // ===============================================
            HttpSession session = request.getSession();
            Guest loggedInGuest = (Guest) session.getAttribute("USER_GUEST");
            if (loggedInGuest != null && booking.getGuestId() != loggedInGuest.getGuestId()) {
                response.sendRedirect(IConstant.ACTION_VIEW_BOOKINGS);
                return;
            }

            // ===============================================
            // ======== GUEST AND ROOM DATA RETRIEVAL ========
            // ===============================================
            Guest guest = guestDAO.getGuestById(booking.getGuestId());
            Room room = roomDAO.getRoomById(booking.getRoomId());
            RoomType roomType = (room != null) ? roomTypeDAO.getRoomTypeById(room.getRoomTypeId()) : null;

            // ===============================================
            // ========= BOOKING SERVICES RETRIEVAL ==========
            // ===============================================
            ArrayList<BookingService> bookingServices = bookingServiceDAO.getBookingServicesByBookingId(bookingId);
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
            // ===== ALL SERVICES AND PAYMENT RETRIEVAL ======
            // ===============================================
            ArrayList<Service> allServices = serviceDAO.getAllService();
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

            // ===============================================
            // =========== SET ATTRIBUTES FOR VIEW ===========
            // ===============================================
            request.setAttribute("BOOKING", booking);
            request.setAttribute("GUEST", guest);
            request.setAttribute("ROOM", room);
            request.setAttribute("ROOM_TYPE", roomType);
            request.setAttribute("BOOKING_SERVICES", bookingServices);
            request.setAttribute("SERVICES", services);
            request.setAttribute("ALL_SERVICES", allServices);
            request.setAttribute("PAYMENT", payment);

            // ===============================================
            // ======== FORWARD TO EDIT BOOKING PAGE =========
            // ===============================================
            request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING).forward(request, response);

        } catch (NumberFormatException e) {
            // ===============================================
            // ======== NUMBER FORMAT ERROR HANDLING =========
            // ===============================================
            e.printStackTrace();
            response.sendRedirect(IConstant.ACTION_VIEW_BOOKINGS);

        } catch (Exception e) {
            // ===============================================
            // ========== GENERAL ERROR HANDLING =============
            // ===============================================
            e.printStackTrace();
            response.sendRedirect(IConstant.ACTION_VIEW_BOOKINGS);
        }
    }

}
