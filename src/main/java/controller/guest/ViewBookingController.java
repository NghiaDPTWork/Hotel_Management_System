package controller.guest;

import dao.BookingDAO;
import dao.BookingServiceDAO;
import dao.GuestDAO;
import dao.PaymentDAO;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dao.ServiceDAO;
import dto.Booking;
import dto.BookingService;
import dto.Guest;
import dto.Payment;
import dto.Room;
import dto.RoomType;
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

@WebServlet("/viewBooking")
public class ViewBookingController extends HttpServlet {

    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private GuestDAO guestDAO;
    private BookingServiceDAO bookingServiceDAO;
    private ServiceDAO serviceDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        guestDAO = new GuestDAO();
        bookingServiceDAO = new BookingServiceDAO();
        serviceDAO = new ServiceDAO();
        paymentDAO = new PaymentDAO();
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

            int bookingId;
            
            try {
                bookingId = Integer.parseInt(bookingIdStr);
            } catch (NumberFormatException e) {
                req.setAttribute("ERROR_MESSAGE", "Invalid Booking ID format");
                req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
                return;
            }

            // ===============================================
            // ============== GET BOOKING DETAIL ==============
            // ===============================================
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                req.setAttribute("ERROR_MESSAGE", "Booking not found with ID: " + bookingId);
                req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
                return;
            }

            // ===============================================
            // ================= GET GUEST INFO ===============
            // ===============================================
            Guest guest = guestDAO.getGuestById(booking.getGuestId());
            if (guest == null) {
                req.setAttribute("ERROR_MESSAGE", "Guest information not found");
                req.getRequestDispatcher(IConstant.ACTION_HOME).forward(req, resp);
                return;
            }

            // ===============================================
            // ================= GET ROOM INFO ================
            // ===============================================
            Room room = roomDAO.getRoomById(booking.getRoomId());
            if (room == null) {
                req.setAttribute("ERROR_MESSAGE", "Room information not found");
                req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
                return;
            }

            RoomType roomType = roomTypeDAO.getRoomTypeById(room.getRoomTypeId());
            if (roomType == null) {
                req.setAttribute("ERROR_MESSAGE", "Room type information not found");
                req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
                return;
            }

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
            // =============== GET PAYMENT INFO ==============
            // ===============================================
            ArrayList<Payment> allPayments = paymentDAO.getPaymentList();
            Payment payment = null;
            
            if (allPayments != null) {
                for (Payment p : allPayments) {
                    if (p.getBookingId() == bookingId) {
                        payment = p;
                        break;
                    }
                }
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
            req.setAttribute("PAYMENT", payment);

            // ===============================================
            // ============== FORWARD TO VIEW PAGE ===========
            // ===============================================
            req.getRequestDispatcher(IConstant.PAGE_ROOM_BOOKING).forward(req, resp);
            
        } catch (Exception e) {
            // ===============================================
            // ========== HANDLE UNEXPECTED ERRORS ===========
            // ===============================================
            e.printStackTrace();
            req.setAttribute("ERROR_MESSAGE", "An unexpected error occurred while loading booking details");
            req.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(req, resp);
        }
    }
}