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
import dto.ChoosenService;
import dto.Guest;
import dto.Payment;
import dto.Room;
import dto.RoomType;
import dto.Service;
import dto.SystemConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */
@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private BookingServiceDAO bookingServiceDAO;
    private PaymentDAO paymentDAO;
    private GuestDAO guestDAO;
    private RoomTypeDAO roomTypeDAO;
    private ServiceDAO serviceDAO;
    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        bookingServiceDAO = new BookingServiceDAO();
        paymentDAO = new PaymentDAO();
        guestDAO = new GuestDAO();
        roomTypeDAO = new RoomTypeDAO();
        serviceDAO = new ServiceDAO();
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ===============================================
        // =========== SESSION & PARAMETER SETUP =========
        // ===============================================
        HttpSession session = req.getSession();

        int roomId = Integer.parseInt(req.getParameter("roomId"));
        int guestId = 0;
        Guest loggedInGuest = (Guest) session.getAttribute("USER_GUEST");
        if (loggedInGuest != null) {
            guestId = loggedInGuest.getGuestId();
        }

        String checkInDate = req.getParameter("checkInDate");
        String checkOutDate = req.getParameter("checkOutDate");
        String totalAmount = req.getParameter("totalAmount");
        String paymentMethod = req.getParameter("paymentMethod");

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "Cash";
        }

        // ===============================================
        // ======== TAX & DEPOSIT RATE RETRIEVAL =========
        // ===============================================
        SystemConfig taxConfig = systemConfigDAO.getSystemConfigByName("TaxRate");
        double taxRate = 5.0;
        
        if (taxConfig != null && taxConfig.isStatus()) {
            try {
                taxRate = Double.parseDouble(taxConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing tax rate: " + e.getMessage());
                taxRate = 5.0;
            }
        }
        
        SystemConfig depositConfig = systemConfigDAO.getSystemConfigByName("BookingDepositRate");
        double depositRate = 30.0;
        
        if (depositConfig != null && depositConfig.isStatus()) {
            try {
                depositRate = Double.parseDouble(depositConfig.getConfigValue());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing deposit rate: " + e.getMessage());
                depositRate = 30.0;
            }
        }

        // ===============================================
        // ============= DATE TIME PROCESSING ============
        // ===============================================
        LocalDate inDate = LocalDate.parse(checkInDate);
        LocalDate outDate = LocalDate.parse(checkOutDate);
        LocalDate bookingDate = LocalDate.now();

        LocalDateTime inDateTime = inDate.atStartOfDay();
        LocalDateTime outDateTime = outDate.atTime(23, 59, 59);

        // ===============================================
        // ============ SERVICE DATA COLLECTION ==========
        // ===============================================
        ArrayList<ChoosenService> services = new ArrayList<>();
        String[] serviceIds = req.getParameterValues("serviceId");
        String[] serviceQuantities = req.getParameterValues("serviceQuantity");
        String[] serviceDates = req.getParameterValues("serviceDate");

        if (serviceIds != null && serviceQuantities != null && serviceDates != null) {
            for (int i = 0; i < serviceIds.length; i++) {
                ChoosenService service = new ChoosenService(
                        Integer.parseInt(serviceIds[i]),
                        Integer.parseInt(serviceQuantities[i]),
                        LocalDate.parse(serviceDates[i])
                );
                services.add(service);
            }
        }

        // ===============================================
        // ========== BOOKING TRANSACTION PROCESS ========
        // ===============================================
        int newBookingId = 0;
        boolean success = false;

        try {
            // Create main booking
            Booking newBooking = new Booking(guestId, roomId, inDateTime, outDateTime, bookingDate, "Reserved");
            newBookingId = bookingDAO.addBooking(newBooking);

            if (newBookingId <= 0) {
                throw new Exception("Failed to create booking");
            }

            // Add booking services
            for (ChoosenService service : services) {
                BookingService bookingService = new BookingService(
                        newBookingId,
                        service.getServiceId(),
                        service.getQuantity(),
                        service.getServiceDate(),
                        0
                );

                if (!bookingServiceDAO.addBookingService(bookingService)) {
                    throw new Exception("Failed to add booking service");
                }
            }

            // Create payment record with dynamic deposit rate
            double depositAmount = (Double.parseDouble(totalAmount) * depositRate) / 100.0;
            Payment payment = new Payment(newBookingId, bookingDate, depositAmount, paymentMethod, "Deposit");

            if (!paymentDAO.addPayment(payment)) {
                throw new Exception("Failed to create payment");
            }

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            success = false;

            // ===============================================
            // =============== ROLLBACK ON FAILURE ===========
            // ===============================================
            if (newBookingId > 0) {
                try {
                    System.out.println("Rolling back booking ID: " + newBookingId);
                    bookingServiceDAO.deleteByBookingId(newBookingId);
                    paymentDAO.deleteByBookingId(newBookingId);
                    bookingDAO.deleteBooking(newBookingId);
                    System.out.println("Rollback completed successfully");
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                    rollbackEx.printStackTrace();
                }
            }
        }

        // ===============================================
        // =========== EMAIL & RESPONSE HANDLING =========
        // ===============================================
        if (success) {
            // Send confirmation email asynchronously
            if (loggedInGuest != null && loggedInGuest.getEmail() != null && !loggedInGuest.getEmail().trim().isEmpty()) {
                final int finalBookingId = newBookingId;
                final String finalEmail = loggedInGuest.getEmail();
                final double finalTaxRate = taxRate;
                final double finalDepositRate = depositRate;

                new Thread(() -> {
                    sendBookingConfirmationEmail(finalEmail, finalBookingId, finalTaxRate, finalDepositRate);
                }).start();
            }

            resp.sendRedirect(IConstant.ACTION_SUF_BOOKING + "?bookingId=" + newBookingId + "&success=true");
        } else {
            resp.sendRedirect(IConstant.ACTION_SUF_BOOKING + "?success=false");
        }
    }

    // ===============================================
    // ======= MAKE BOOKING EMAIL METHOD ===========
    // ===============================================
    protected boolean sendBookingConfirmationEmail(String recipientEmail, int bookingId, double taxRate, double depositRate) {
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
            BigDecimal depositAmount = grandTotal.multiply(BigDecimal.valueOf(depositRate / 100.0));
            BigDecimal amountDue = grandTotal.subtract(depositAmount);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
                    + "<div style='background: linear-gradient(135deg, #D4AF37 0%%, #B8941E 100%%); padding: 40px; text-align: center;'>"
                    + "<h1 style='color: #ffffff; margin: 0; font-size: 32px; font-family: \"Playfair Display\", serif;'>Booking Confirmation</h1>"
                    + "<p style='color: #ffffff; margin: 10px 0 0 0; opacity: 0.9; font-size: 16px;'>Booking ID: #%d</p>"
                    + "</div>"
                    + "<div style='padding: 30px;'>"
                    + "<p style='color: #2C2C2C; font-size: 18px; line-height: 1.6;'>Dear <strong>%s</strong>,</p>"
                    + "<p style='color: #555; font-size: 16px; line-height: 1.6;'>Thank you for booking with Misuka Hotel. Below are the details of your reservation:</p>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Reservation Details</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Booking ID:</td><td style='padding: 10px 0; font-weight: bold;'>#%d</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Booking Date:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Status:</td><td style='padding: 10px 0;'><span style='background-color: #B8941E; color: white; padding: 4px 12px; border-radius: 12px; font-size: 13px;'>%s</span></td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Guest Information</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Full Name:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Citizen ID:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Date of Birth:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Room Information</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Room Number:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room Type:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Capacity:</td><td style='padding: 10px 0;'>%d guests</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Price/Night:</td><td style='padding: 10px 0; font-weight: bold;'>%,d VND</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Stay Details</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Check-in:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Check-out:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Total Nights:</td><td style='padding: 10px 0;'>%d nights</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "%s"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Payment Summary</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room Total (%d nights):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Services Total:</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 1px solid #eee;'><td style='padding: 10px 0; color: #666;'><strong>Subtotal:</strong></td><td style='padding: 10px 0; text-align: right; font-weight: bold;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Tax (%.1f%%):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 2px solid #B8941E;'><td style='padding: 14px 0 10px; font-size: 18px; font-weight: bold;'>Grand Total:</td><td style='padding: 14px 0 10px; color: #B8941E; font-size: 22px; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Deposit Paid (%.1f%%):</td><td style='padding: 10px 0; color: #28a745; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Amount Due:</td><td style='padding: 10px 0; color: #dc3545; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #f9f9f9; border-radius: 6px; padding: 20px; margin: 30px 0 0;'>"
                    + "<p style='color: #555; font-size: 13px; margin: 0; line-height: 1.6;'>"
                    + "<strong>Please Note:</strong><br>"
                    + "• Please bring identification for check-in.<br>"
                    + "• Check-in time: 14:00 | Check-out time: 12:00.<br>"
                    + "• The remaining balance is payable upon check-out.<br>"
                    + "• For assistance, please contact: support@hotel.com or call: 1900-xxxx"
                    + "</p>"
                    + "</div>"
                    + "</div>"
                    + "<div style='background-color: #ffffff; padding: 30px; text-align: center; border-top: 1px solid #f0f0f0;'>"
                    + "<p style='color: #666; font-size: 14px; margin: 0;'>Thank you for choosing our services!</p>"
                    + "<p style='color: #999; font-size: 12px; margin: 10px 0 0 0;'>© 2025 Hotel Management System. All rights reserved.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>",
                    bookingId,
                    guest.getFullName(),
                    bookingId,
                    booking.getBookingDate().format(dateFormatter),
                    booking.getStatus(),
                    guest.getFullName(),
                    guest.getIdNumber(),
                    guest.getDateOfBirth(),
                    room.getRoomNumber(),
                    roomType.getTypeName(),
                    roomType.getCapacity(),
                    roomType.getPricePerNight().intValue(),
                    booking.getCheckInDate().format(dateTimeFormatter),
                    booking.getCheckOutDate().format(dateTimeFormatter),
                    numberOfNights,
                    bookingServices != null && !bookingServices.isEmpty()
                    ? String.format(
                            "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                            + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Services Booked</h2>"
                            + "<table style='width: 100%%; border-collapse: collapse; font-size: 15px;'>"
                            + "<thead>"
                            + "<tr style='background-color: #f9f9f9;'>"
                            + "<th style='padding: 12px; text-align: left; color: #2C2C2C;'>Service</th>"
                            + "<th style='padding: 12px; text-align: center; color: #2C2C2C;'>Qty</th>"
                            + "<th style='padding: 12px; text-align: center; color: #2C2C2C;'>Service Date</th>"
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
                    depositRate,
                    depositAmount.intValue(),
                    amountDue.intValue()
            );

            EmailSender emailSender = new EmailSender();
            boolean result = emailSender.sendHtmlEmail(
                    recipientEmail,
                    "Booking Confirmation #" + bookingId + " - Hotel Management System",
                    htmlContent
            );

            if (result) {
                System.out.println("? Booking confirmation email sent successfully to: " + recipientEmail);
            }

            return result;

        } catch (Exception e) {
            System.err.println("? Error sending booking confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}