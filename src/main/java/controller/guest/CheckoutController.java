package controller.guest;

import controller.feature.EmailSender;
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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */
@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    // ============================================================
    // KHAI BÁO DAO
    // ============================================================
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private BookingServiceDAO bookingServiceDAO;
    private ServiceDAO serviceDAO;
    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        guestDAO = new GuestDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        bookingServiceDAO = new BookingServiceDAO();
        serviceDAO = new ServiceDAO();
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ============================================================
        // VALIDATE SESSION & BOOKING ID
        // ============================================================
        HttpSession session = request.getSession();
        Guest loggedInGuest = (Guest) session.getAttribute("USER_GUEST");

        // Ki?m tra ??ng nh?p
        if (loggedInGuest == null) {
            response.sendRedirect(IConstant.ACTION_HOME);
            return;
        }

        // Validate bookingId t? parameter
        String bookingIdParam = request.getParameter("bookingId");
        if (bookingIdParam == null || bookingIdParam.trim().isEmpty()) {
            request.setAttribute("ERROR_MSG_CHECKOUT", "Invalid booking ID");
            forwardToBookingPage(request, response, loggedInGuest);
            return;
        }

        // Parse bookingId
        int bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("ERROR_MSG_CHECKOUT", "Invalid booking ID format");
            forwardToBookingPage(request, response, loggedInGuest);
            return;
        }

        // ============================================================
        // GET & VALIDATE BOOKING
        // ============================================================
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        // Ki?m tra booking t?n t?i
        if (booking == null) {
            request.setAttribute("ERROR_MSG_CHECKOUT", "Booking not found");
            forwardToBookingPage(request, response, loggedInGuest);
            return;
        }

        // Ki?m tra quy?n checkout
        if (booking.getGuestId() != loggedInGuest.getGuestId()) {
            request.setAttribute("ERROR_MSG_CHECKOUT", "You don't have permission to checkout this booking");
            forwardToBookingPage(request, response, loggedInGuest);
            return;
        }

        // ============================================================
        // GET & VALIDATE AMOUNT
        // ============================================================
        String amountParam = request.getParameter("amount");
        double grandTotal = 0.0;

        if (amountParam != null && !amountParam.trim().isEmpty()) {
            try {
                grandTotal = Double.parseDouble(amountParam);
            } catch (NumberFormatException e) {
                request.setAttribute("ERROR_MSG_CHECKOUT", "Invalid amount format");
                forwardToBookingPage(request, response, loggedInGuest);
                return;
            }
        } else {
            request.setAttribute("ERROR_MSG_CHECKOUT", "Amount is required");
            forwardToBookingPage(request, response, loggedInGuest);
            return;
        }

        Guest guest = guestDAO.getGuestById(booking.getGuestId());

        // ============================================================
        // PROCESS CHECKOUT
        // ============================================================
        boolean success = false;
        try {
            // T?o payment record
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setAmount(grandTotal);
            payment.setPaymentMethod("Online");
            payment.setPaymentDate(LocalDate.now());
            payment.setStatus("Pending");

            if (!paymentDAO.addPayment(payment)) {
                throw new Exception("Failed to add payment record");
            }

            // C?p nh?t tr?ng thái booking
            booking.setStatus("Checked-out");
            if (!bookingDAO.updateBooking(booking)) {
                throw new Exception("Failed to update booking status");
            }

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Checkout failed: " + e.getMessage());
        }

        // ============================================================
        // SEND EMAIL & SET RESPONSE
        // ============================================================
        if (success) {
            // G?i email xác nh?n (async)
            if (guest != null && guest.getEmail() != null && !guest.getEmail().trim().isEmpty()) {
                final String email = guest.getEmail();
                final int finalBookingId = bookingId;
                new Thread(() -> {
                    sendCheckoutConfirmationEmail(email, finalBookingId, 10.0);
                }).start();
            }

            request.setAttribute("SUCCESS_MSG_CHECKOUT", "Checkout completed successfully! Thank you for staying with us.");
        } else {
            request.setAttribute("ERROR_MSG_CHECKOUT", "Checkout failed. Please try again or contact support.");
        }

        // ============================================================
        // B??C 6: RELOAD BOOKING HISTORY & FORWARD
        // ============================================================
        forwardToBookingPage(request, response, loggedInGuest);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // ============================================================
    // HELPER METHOD: FORWARD TO BOOKING PAGE
    // ============================================================
    private void forwardToBookingPage(HttpServletRequest request, HttpServletResponse response, Guest loggedInGuest)
            throws ServletException, IOException {
        
        // Load booking history
        ArrayList<Booking> bookingHistory = bookingDAO.getBookingByGuestId(loggedInGuest.getGuestId());
        request.setAttribute("BOOKING_HISTORY_LIST", bookingHistory);

        // Load room & roomType details
        Map<Integer, Room> roomMap = new HashMap<>();
        Map<Integer, RoomType> typeMap = new HashMap<>();

        if (bookingHistory != null && !bookingHistory.isEmpty()) {
            for (Booking b : bookingHistory) {
                Room r = roomDAO.getRoomById(b.getRoomId());
                if (r != null) {
                    roomMap.put(b.getRoomId(), r);
                    RoomType rt = roomTypeDAO.getRoomTypeById(r.getRoomTypeId());
                    if (rt != null) {
                        typeMap.put(r.getRoomTypeId(), rt);
                    }
                }
            }
        }

        request.setAttribute("ROOM_DETAILS_MAP", roomMap);
        request.setAttribute("ROOM_TYPE_DETAILS_MAP", typeMap);

        request.getRequestDispatcher(IConstant.PAGE_ROOMS_BOOKING).forward(request, response);
    }

    // ============================================================
    // EMAIL METHOD: SEND CHECKOUT CONFIRMATION
    // ============================================================
    protected boolean sendCheckoutConfirmationEmail(String recipientEmail, int bookingId, double taxRate) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                System.err.println("Booking not found with ID: " + bookingId);
                return false;
            }

            Guest guest = guestDAO.getGuestById(booking.getGuestId());
            Room room = roomDAO.getRoomById(booking.getRoomId());
            RoomType roomType = roomTypeDAO.getRoomTypeById(room.getRoomTypeId());
            List<BookingService> bookingServices = bookingServiceDAO.getBookingServiceByBookingId(bookingId);
            List<Payment> payments = paymentDAO.getPaymentsByBookingId(bookingId);

            long numberOfNights = ChronoUnit.DAYS.between(
                    booking.getCheckInDate().toLocalDate(),
                    booking.getCheckOutDate().toLocalDate()
            );

            BigDecimal roomTotal = roomType.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));
            BigDecimal servicesTotal = BigDecimal.ZERO;
            StringBuilder servicesHtml = new StringBuilder();

            if (bookingServices != null && !bookingServices.isEmpty()) {
                for (BookingService bs : bookingServices) {
                    Service service = serviceDAO.getServiceById(bs.getServiceId());
                    BigDecimal serviceItemTotal = service.getPrice().multiply(BigDecimal.valueOf(bs.getQuantity()));
                    servicesTotal = servicesTotal.add(serviceItemTotal);

                    servicesHtml.append(String.format(
                            "<tr>"
                            + "<td style='padding: 12px; border-bottom: 1px solid #eee;'>%s</td>"
                            + "<td style='padding: 12px; border-bottom: 1px solid #eee; text-align: center;'>%d</td>"
                            + "<td style='padding: 12px; border-bottom: 1px solid #eee; text-align: center;'>%s</td>"
                            + "<td style='padding: 12px; border-bottom: 1px solid #eee; text-align: right;'>%,d VND</td>"
                            + "<td style='padding: 12px; border-bottom: 1px solid #eee; text-align: right;'>%,d VND</td>"
                            + "</tr>",
                            service.getServiceName(),
                            bs.getQuantity(),
                            bs.getServiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            service.getPrice().intValue(),
                            serviceItemTotal.intValue()
                    ));
                }
            }

            BigDecimal subtotal = roomTotal.add(servicesTotal);
            BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(taxRate / 100.0));
            BigDecimal grandTotal = subtotal.add(taxAmount);

            double totalPaid = 0.0;
            StringBuilder paymentsHtml = new StringBuilder();

            if (payments != null && !payments.isEmpty()) {
                for (Payment p : payments) {
                    if (!"Failed".equalsIgnoreCase(p.getStatus())) {
                        totalPaid += p.getAmount();
                        paymentsHtml.append(String.format(
                                "<tr>"
                                + "<td style='padding: 10px; border-bottom: 1px solid #eee;'>%s</td>"
                                + "<td style='padding: 10px; border-bottom: 1px solid #eee; text-align: center;'>%s</td>"
                                + "<td style='padding: 10px; border-bottom: 1px solid #eee; text-align: right;'>%,d VND</td>"
                                + "<td style='padding: 10px; border-bottom: 1px solid #eee; text-align: center;'><span style='background-color: #28a745; color: white; padding: 4px 8px; border-radius: 4px; font-size: 12px;'>%s</span></td>"
                                + "</tr>",
                                p.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                p.getPaymentMethod(),
                                (int) p.getAmount(),
                                p.getStatus()
                        ));
                    }
                }
            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            String htmlContent = String.format(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<link href='https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=Lato:wght@400;700&display=swap' rel='stylesheet'>"
                    + "</head>"
                    + "<body style='margin: 0; padding: 0; font-family: \"Lato\", sans-serif; background-color: #f4f4f4;'>"
                    + "<div style='max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.07); border: 1px solid #eaeaea;'>"
                    + "<div style='background: linear-gradient(135deg, #28a745 0%%, #218838 100%%); padding: 40px; text-align: center;'>"
                    + "<h1 style='color: #ffffff; margin: 0; font-size: 32px; font-family: \"Playfair Display\", serif;'>Checkout Complete</h1>"
                    + "<p style='color: #ffffff; margin: 10px 0 0 0; opacity: 0.9; font-size: 16px;'>Thank you for staying with us!</p>"
                    + "</div>"
                    + "<div style='padding: 30px;'>"
                    + "<p style='color: #2C2C2C; font-size: 18px; line-height: 1.6;'>Dear <strong>%s</strong>,</p>"
                    + "<p style='color: #555; font-size: 16px; line-height: 1.6;'>Thank you for choosing Misuka Hotel. We hope you enjoyed your stay. Below is your final receipt:</p>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #28a745; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Booking Summary</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Booking ID:</td><td style='padding: 10px 0; font-weight: bold;'>#%d</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Check-in Date:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Check-out Date:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Total Nights:</td><td style='padding: 10px 0;'>%d nights</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room:</td><td style='padding: 10px 0;'>%s - %s</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "%s"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #28a745; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Final Bill</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room Charges (%d nights):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Services Total:</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 1px solid #eee;'><td style='padding: 10px 0; color: #666;'><strong>Subtotal:</strong></td><td style='padding: 10px 0; text-align: right; font-weight: bold;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Tax (%.1f%%):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 2px solid #28a745;'><td style='padding: 14px 0 10px; font-size: 18px; font-weight: bold;'>Grand Total:</td><td style='padding: 14px 0 10px; color: #28a745; font-size: 22px; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0;'>"
                    + "<h2 style='color: #28a745; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Payment History</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; font-size: 14px;'>"
                    + "<thead>"
                    + "<tr style='background-color: #f9f9f9;'>"
                    + "<th style='padding: 10px; text-align: left; color: #2C2C2C;'>Date</th>"
                    + "<th style='padding: 10px; text-align: center; color: #2C2C2C;'>Method</th>"
                    + "<th style='padding: 10px; text-align: right; color: #2C2C2C;'>Amount</th>"
                    + "<th style='padding: 10px; text-align: center; color: #2C2C2C;'>Status</th>"
                    + "</tr>"
                    + "</thead>"
                    + "<tbody>"
                    + "%s"
                    + "<tr style='border-top: 2px solid #28a745; background-color: #d4edda;'>"
                    + "<td colspan='2' style='padding: 12px; font-weight: bold;'>Total Paid:</td>"
                    + "<td style='padding: 12px; text-align: right; font-weight: bold; color: #28a745;'>%,d VND</td>"
                    + "<td></td>"
                    + "</tr>"
                    + "</tbody>"
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #d4edda; border-left: 4px solid #28a745; border-radius: 6px; padding: 20px; margin: 30px 0;'>"
                    + "<p style='color: #155724; font-size: 16px; margin: 0; line-height: 1.6;'>"
                    + "<strong>? Payment Complete</strong><br>"
                    + "All charges have been settled. Thank you for your payment!"
                    + "</p>"
                    + "</div>"
                    + "<div style='background-color: #f9f9f9; border-radius: 6px; padding: 20px; margin: 30px 0 0;'>"
                    + "<p style='color: #555; font-size: 13px; margin: 0; line-height: 1.6;'>"
                    + "We hope you had a wonderful stay with us. We look forward to welcoming you back soon!<br><br>"
                    + "For any inquiries, please contact us at: support@hotel.com or call: 1900-xxxx"
                    + "</p>"
                    + "</div>"
                    + "</div>"
                    + "<div style='background-color: #ffffff; padding: 30px; text-align: center; border-top: 1px solid #f0f0f0;'>"
                    + "<p style='color: #666; font-size: 14px; margin: 0;'>Thank you for choosing Misuka Hotel!</p>"
                    + "<p style='color: #999; font-size: 12px; margin: 10px 0 0 0;'>© 2025 Hotel Management System. All rights reserved.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>",
                    guest.getFullName(),
                    bookingId,
                    booking.getCheckInDate().format(dateTimeFormatter),
                    booking.getCheckOutDate().format(dateTimeFormatter),
                    numberOfNights,
                    room.getRoomNumber(),
                    roomType.getTypeName(),
                    bookingServices != null && !bookingServices.isEmpty()
                    ? String.format(
                            "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                            + "<h2 style='color: #28a745; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Services Used</h2>"
                            + "<table style='width: 100%%; border-collapse: collapse; font-size: 15px;'>"
                            + "<thead>"
                            + "<tr style='background-color: #f9f9f9;'>"
                            + "<th style='padding: 12px; text-align: left; color: #2C2C2C;'>Service</th>"
                            + "<th style='padding: 12px; text-align: center; color: #2C2C2C;'>Qty</th>"
                            + "<th style='padding: 12px; text-align: center; color: #2C2C2C;'>Date</th>"
                            + "<th style='padding: 12px; text-align: right; color: #2C2C2C;'>Unit Price</th>"
                            + "<th style='padding: 12px; text-align: right; color: #2C2C2C;'>Total</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>"
                            + "%s"
                            + "</tbody>"
                            + "</table>"
                            + "</div>",
                            servicesHtml.toString()
                    ) : "",
                    numberOfNights,
                    roomTotal.intValue(),
                    servicesTotal.intValue(),
                    subtotal.intValue(),
                    taxRate,
                    taxAmount.intValue(),
                    grandTotal.intValue(),
                    paymentsHtml.toString(),
                    (int) totalPaid
            );

            EmailSender emailSender = new EmailSender();
            boolean result = emailSender.sendHtmlEmail(
                    recipientEmail,
                    "Checkout Receipt #" + bookingId + " - Misuka Hotel",
                    htmlContent
            );

            if (result) {
                System.out.println("? Checkout confirmation email sent successfully to: " + recipientEmail);
            }

            return result;

        } catch (Exception e) {
            System.err.println("? Error sending checkout confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
