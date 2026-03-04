package controller.guest;

import dao.BookingDAO;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dto.Booking;
import dto.Guest;
import dto.Room;
import dto.RoomType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/viewBookings")
public class ViewBookingsController extends HttpServlet {

    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // ===============================================
        // ============ GET GUEST FROM SESSION ===========
        // ===============================================
        HttpSession session = request.getSession();
        Guest loggedInGuest = (Guest) session.getAttribute("USER_GUEST");

        // ===============================================
        // ============ INITIALIZE DATA HOLDERS ==========
        // ===============================================
        ArrayList<Booking> bookingHistory = null;
        Map<Integer, Room> roomDetailsMap = new HashMap<>();
        Map<Integer, RoomType> roomTypeDetailsMap = new HashMap<>();
        String errorMessage = null;

        // ===============================================
        // ============ CHECK GUEST LOGIN STATUS =========
        // ===============================================
        if (loggedInGuest != null) {
            int guestId = loggedInGuest.getGuestId();
            try {
                // ===============================================
                // ============ FETCH BOOKING HISTORY ============
                // ===============================================
                bookingHistory = bookingDAO.getBookingByGuestId(guestId);

                if (bookingHistory != null && !bookingHistory.isEmpty()) {
                    // ===============================================
                    // ======== POPULATE ROOM & ROOMTYPE MAPS ========
                    // ===============================================
                    for (Booking booking : bookingHistory) {
                        int roomId = booking.getRoomId();
                        if (!roomDetailsMap.containsKey(roomId)) {
                            Room room = roomDAO.getRoomById(roomId);
                            if (room != null) {
                                roomDetailsMap.put(roomId, room);
                                int roomTypeId = room.getRoomTypeId();
                                if (!roomTypeDetailsMap.containsKey(roomTypeId)) {
                                    RoomType roomType = roomTypeDAO.getRoomTypeById(roomTypeId);
                                    if (roomType != null) {
                                        roomTypeDetailsMap.put(roomTypeId, roomType);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    bookingHistory = new ArrayList<>();
                }
                System.out.println("Fetched " + bookingHistory.size() + " bookings for guest ID: " + guestId);

            } catch (Exception e) {
                // ===============================================
                // ================== HANDLE EXCEPTION ===========
                // ===============================================
                System.out.println("Error fetching booking history for guest ID " + guestId + ": " + e.getMessage());
                e.printStackTrace();
                errorMessage = "Error loading booking history. Please try again.";
                bookingHistory = new ArrayList<>();
            }
        } else {
            // ===============================================
            // ============ HANDLE GUEST NOT LOGGED IN ========
            // ===============================================
            errorMessage = "Please log in to view your booking history.";
            bookingHistory = new ArrayList<>();
        }

        // ===============================================
        // ============ SET ATTRIBUTES FOR VIEW ==========
        // ===============================================
        request.setAttribute("BOOKING_HISTORY_LIST", bookingHistory);
        request.setAttribute("ROOM_DETAILS_MAP", roomDetailsMap);
        request.setAttribute("ROOM_TYPE_DETAILS_MAP", roomTypeDetailsMap);
        request.setAttribute("ERROR_MESSAGE", errorMessage);

        // ===============================================
        // ============== FORWARD TO VIEW PAGE ===========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_ROOMS_BOOKING).forward(request, response);
    }

}
