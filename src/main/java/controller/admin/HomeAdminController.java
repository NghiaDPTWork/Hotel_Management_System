package controller.admin;

import dao.BookingDAO;
import dao.PaymentDAO;
import dao.RoomDAO;
import dto.Booking;
import dto.Payment;
import util.IConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/admin")
public class HomeAdminController extends HttpServlet {

    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;
    private RoomDAO roomDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        roomDAO = new RoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            // ===============================================
            // =========== GET CURRENT DATE ==================
            // ===============================================
            LocalDate today = LocalDate.now();

            // ===============================================
            // ========= RETRIEVE DATA FROM DATABASE =========
            // ===============================================
            List<Booking> allBookings = bookingDAO.getAllBookings();
            int totalRooms = roomDAO.getTotalRoomCount();

            // ===============================================
            // ========= INITIALIZE STATISTICS ===============
            // ===============================================
            int checkInsToday = 0;
            int checkOutsToday = 0;
            int currentlyOccupied = 0;
            int overdueCheckouts = 0;
            int newBookingsToday = 0;
            int canceledBookingsToday = 0;

            // ===============================================
            // ========== CALCULATE BOOKING STATISTICS =======
            // ===============================================
            for (Booking booking : allBookings) {
                // Check-ins today
                if (booking.getCheckInDate() != null 
                        && booking.getCheckInDate().toLocalDate().isEqual(today) 
                        && "Checked-in".equalsIgnoreCase(booking.getStatus())) {
                    checkInsToday++;
                }

                // Check-outs today
                if (booking.getCheckOutDate() != null 
                        && booking.getCheckOutDate().toLocalDate().isEqual(today) 
                        && "Checked-out".equalsIgnoreCase(booking.getStatus())) {
                    checkOutsToday++;
                }

                // Currently occupied rooms
                if ("Checked-in".equalsIgnoreCase(booking.getStatus())) {
                    currentlyOccupied++;

                    // Overdue checkouts
                    if (booking.getCheckOutDate() != null 
                            && booking.getCheckOutDate().toLocalDate().isBefore(today)) {
                        overdueCheckouts++;
                    }
                }

                // New and canceled bookings today
                if (booking.getBookingDate() != null && booking.getBookingDate().isEqual(today)) {
                    if ("Canceled".equalsIgnoreCase(booking.getStatus())) {
                        canceledBookingsToday++;
                    } else {
                        newBookingsToday++;
                    }
                }
            }

            // ===============================================
            // ========== CALCULATE OCCUPANCY RATE ===========
            // ===============================================
            double occupancyRate = 0;
            if (totalRooms > 0) {
                occupancyRate = ((double) currentlyOccupied / totalRooms) * 100;
            }

            // ===============================================
            // ======== SET BOOKING STATISTICS ATTRIBUTES ====
            // ===============================================
            request.setAttribute("CHECK_INS_TODAY", checkInsToday);
            request.setAttribute("CHECK_OUTS_TODAY", checkOutsToday);
            request.setAttribute("CURRENTLY_OCCUPIED", currentlyOccupied);
            request.setAttribute("OVERDUE_CHECKOUTS", overdueCheckouts);
            request.setAttribute("NEW_BOOKINGS_TODAY", newBookingsToday);
            request.setAttribute("CANCELED_BOOKINGS_TODAY", canceledBookingsToday);
            request.setAttribute("OCCUPANCY_RATE", occupancyRate);

            // ===============================================
            // ========== CALCULATE FINANCIAL METRICS ========
            // ===============================================
            double todayRevenue = paymentDAO.getRevenueByDate(today);
            int todayInvoices = paymentDAO.getInvoiceCountByDate(today);

            double averagePerRoom = 0;
            if (todayInvoices > 0) {
                averagePerRoom = todayRevenue / todayInvoices;
            }

            double todayExpenses = 0.0; 
            double todayNetIncome = todayRevenue - todayExpenses;

            // ===============================================
            // ========== PREPARE RECENT ACTIVITIES ==========
            // ===============================================
            ArrayList<Payment> recentPayments = paymentDAO.getRecentPayments(5);
            ArrayList<String> recentActivities = new ArrayList<>();
            NumberFormat vndFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            for (Payment payment : recentPayments) {
                String staffName = "Staff";
                String amount = vndFormatter.format(payment.getAmount());
                recentActivities.add(String.format("%s created invoice %s VND", staffName, amount));
            }

            // ===============================================
            // ======== SET FINANCIAL ATTRIBUTES =============
            // ===============================================
            request.setAttribute("TODAY_REVENUE", todayRevenue);
            request.setAttribute("TODAY_INVOICES", todayInvoices);
            request.setAttribute("AVERAGE_PER_ROOM", averagePerRoom);
            request.setAttribute("TODAY_EXPENSES", todayExpenses);
            request.setAttribute("TODAY_NET_INCOME", todayNetIncome);
            request.setAttribute("RECENT_ACTIVITIES", recentActivities);

            // ===============================================
            // ============== LOGGING DASHBOARD DATA =========
            // ===============================================
            System.out.println("Dashboard loaded - Date: " + today);
            System.out.println("Check-ins today: " + checkInsToday + " | Check-outs today: " + checkOutsToday);
            System.out.println("Occupancy: " + currentlyOccupied + "/" + totalRooms + " (" + String.format("%.2f%%", occupancyRate) + ")");
            System.out.println("Revenue today: " + vndFormatter.format(todayRevenue) + " VND");

            // ===============================================
            // ======== FORWARD TO ADMIN DASHBOARD ===========
            // ===============================================
            request.getRequestDispatcher(IConstant.PAGE_ADMIN).forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in HomeAdminController: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "An error occurred while loading dashboard data.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home Admin Controller - Handles admin dashboard with statistics and metrics";
    }
}