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
import dto.SystemConfig;
import util.IConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/editBooking")
public class EditBookingController extends HttpServlet {

    private BookingServiceDAO bookingServiceDAO;
    private BookingDAO bookingDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;
    private ServiceDAO serviceDAO;
    private PaymentDAO paymentDAO;
    private SystemConfigDAO systemConfigDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookingServiceDAO = new BookingServiceDAO();
        bookingDAO = new BookingDAO();
        guestDAO = new GuestDAO();
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
        serviceDAO = new ServiceDAO();
        paymentDAO = new PaymentDAO();
        systemConfigDAO = new SystemConfigDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // ===============================================
            // ========== BOOKING ID VALIDATION ==============
            // ===============================================
            String bookingIdStr = request.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
                request.setAttribute("ERROR_MESSAGE", "Booking ID is required");
                request.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(request, response);
                return;
            }

            int bookingId;
            try {
                bookingId = Integer.parseInt(bookingIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("ERROR_MESSAGE", "Invalid Booking ID format");
                request.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(request, response);
                return;
            }

            // ===============================================
            // ========== VERIFY BOOKING EXISTS ==============
            // ===============================================
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                request.setAttribute("ERROR_MESSAGE", "Booking not found with ID: " + bookingId);
                request.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(request, response);
                return;
            }

            // ===============================================
            // ======== DELETE EXISTING SERVICES =============
            // ===============================================
            boolean deleteSuccess = bookingServiceDAO.deleteByBookingIdAndStatus(bookingId, 0);
            if (!deleteSuccess) {
                request.setAttribute("ERROR_MESSAGE", "Failed to remove existing services");
                request.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(request, response);
                return;
            }

            // ===============================================
            // ========== SERVICE DATA COLLECTION ============
            // ===============================================
            String[] serviceIds = request.getParameterValues("serviceId");
            String[] serviceQuantities = request.getParameterValues("serviceQuantity");
            String[] serviceDates = request.getParameterValues("serviceDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // ===============================================
            // ========== ADD NEW BOOKING SERVICES ===========
            // ===============================================
            if (serviceIds != null && serviceIds.length > 0) {
                if (serviceQuantities == null || serviceDates == null
                        || serviceIds.length != serviceQuantities.length
                        || serviceIds.length != serviceDates.length) {
                    request.setAttribute("ERROR_MESSAGE", "Service data is incomplete or mismatched");
                    request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                    return;
                }

                for (int i = 0; i < serviceIds.length; i++) {
                    try {
                        int serviceId = Integer.parseInt(serviceIds[i]);
                        int quantity = Integer.parseInt(serviceQuantities[i]);
                        LocalDate serviceDate = LocalDate.parse(serviceDates[i], formatter);

                        if (quantity <= 0) {
                            request.setAttribute("ERROR_MESSAGE", "Service quantity must be greater than 0");
                            request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                            return;
                        }

                        BookingService bookingService = new BookingService();
                        bookingService.setBookingId(bookingId);
                        bookingService.setServiceId(serviceId);
                        bookingService.setQuantity(quantity);
                        bookingService.setServiceDate(serviceDate);
                        bookingService.setStatus(0);

                        boolean addSuccess = bookingServiceDAO.addBookingService(bookingService);
                        if (!addSuccess) {
                            request.setAttribute("ERROR_MESSAGE", "Failed to add service ID: " + serviceId);
                            request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("ERROR_MESSAGE", "Invalid service data format at index " + i);
                        request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                        return;
                    } catch (DateTimeParseException e) {
                        request.setAttribute("ERROR_MESSAGE", "Invalid date format at index " + i);
                        request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                        return;
                    }
                }
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
            // =========== EMAIL & RESPONSE HANDLING =========
            // ===============================================
            Guest guest = guestDAO.getGuestById(booking.getGuestId());
            if (guest == null) {
                request.setAttribute("ERROR_MESSAGE", "Guest information not found");
                request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);
                return;
            }

            // Send confirmation email asynchronously
            if (guest.getEmail() != null && !guest.getEmail().trim().isEmpty()) {
                final int finalBookingId = bookingId;
                final String finalEmail = guest.getEmail();
                final double finalTaxRate = taxRate;
                final double finalDepositRate = depositRate;

                new Thread(() -> {
                    try {
                        sendServiceUpdateConfirmationEmail(finalEmail, finalBookingId, finalTaxRate, finalDepositRate);
                    } catch (Exception e) {
                        System.err.println("Failed to send confirmation email: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }

            // ===============================================
            // ========== SUCCESS RESPONSE HANDLING ==========
            // ===============================================
            request.setAttribute("SUCCESS_MESSAGE", "Booking services updated successfully!");
            request.getRequestDispatcher(IConstant.PAGE_EDIT_BOOKING_RESULT).forward(request, response);

        } catch (Exception e) {
            // ===============================================
            // ========== GENERAL ERROR HANDLING =============
            // ===============================================
            e.printStackTrace();
            request.setAttribute("ERROR_MESSAGE", "An unexpected error occurred while updating booking services");
            request.getRequestDispatcher(IConstant.ACTION_VIEW_BOOKINGS).forward(request, response);
        }
    }

    // ===============================================
    // ======= SERVICE UPDATE EMAIL METHOD ===========
    // ===============================================
    protected boolean sendServiceUpdateConfirmationEmail(String recipientEmail, int bookingId, double taxRate, double depositRate) {
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
                    if (service != null) {
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
            }

            BigDecimal subtotal = roomTotal.add(servicesTotal);
            BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(taxRate / 100.0));
            BigDecimal grandTotal = subtotal.add(taxAmount);

            // Calculate total paid amount
            BigDecimal totalPaid = BigDecimal.ZERO;
            ArrayList<Payment> payments = paymentDAO.getPaymentsByBookingId(bookingId);
            if (payments != null && !payments.isEmpty()) {
                for (Payment p : payments) {
                    totalPaid = totalPaid.add(BigDecimal.valueOf(p.getAmount()));
                }
            }

            BigDecimal amountDue = grandTotal.subtract(totalPaid);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Handle null values safely
            String guestDOB = (guest.getDateOfBirth() != null)
                    ? guest.getDateOfBirth()
                    : "N/A";
            String guestIdNumber = (guest.getIdNumber() != null)
                    ? guest.getIdNumber()
                    : "N/A";

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
                    + "<div style='background: linear-gradient(135deg, #4A90E2 0%%, #357ABD 100%%); padding: 40px; text-align: center;'>"
                    + "<h1 style='color: #ffffff; margin: 0; font-size: 32px; font-family: \"Playfair Display\", serif;'>Service Update Confirmation</h1>"
                    + "<p style='color: #ffffff; margin: 10px 0 0 0; opacity: 0.9; font-size: 16px;'>Booking ID: #%d</p>"
                    + "</div>"
                    + "<div style='padding: 30px;'>"
                    + "<p style='color: #2C2C2C; font-size: 18px; line-height: 1.6;'>Dear <strong>%s</strong>,</p>"
                    + "<p style='color: #555; font-size: 16px; line-height: 1.6;'>Your booking services have been successfully updated. Below are the updated details of your reservation:</p>"
                    + "<div style='background-color: #E8F4FD; border-left: 4px solid #4A90E2; padding: 15px; margin: 20px 0; border-radius: 4px;'>"
                    + "<p style='color: #2C2C2C; font-size: 14px; margin: 0; line-height: 1.6;'>"
                    + "<strong>? Important:</strong> Your service selection has been modified. Please review the updated payment summary below."
                    + "</p>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Reservation Details</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Booking ID:</td><td style='padding: 10px 0; font-weight: bold;'>#%d</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Booking Date:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Status:</td><td style='padding: 10px 0;'><span style='background-color: #357ABD; color: white; padding: 4px 12px; border-radius: 12px; font-size: 13px;'>%s</span></td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Guest Information</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Full Name:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Citizen ID:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Date of Birth:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Room Information</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Room Number:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room Type:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Capacity:</td><td style='padding: 10px 0;'>%d guests</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Price/Night:</td><td style='padding: 10px 0; font-weight: bold;'>%,d VND</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Stay Details</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Check-in:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Check-out:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Total Nights:</td><td style='padding: 10px 0;'>%d nights</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "%s"
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Updated Payment Summary</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Room Total (%d nights):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Services Total:</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 1px solid #eee;'><td style='padding: 10px 0; color: #666;'><strong>Subtotal:</strong></td><td style='padding: 10px 0; text-align: right; font-weight: bold;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Tax (%.1f%%):</td><td style='padding: 10px 0; text-align: right;'>%,d VND</td></tr>"
                    + "<tr style='border-top: 2px solid #357ABD;'><td style='padding: 14px 0 10px; font-size: 18px; font-weight: bold;'>Grand Total:</td><td style='padding: 14px 0 10px; color: #357ABD; font-size: 22px; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Total Paid:</td><td style='padding: 10px 0; color: #28a745; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Amount Due:</td><td style='padding: 10px 0; color: #dc3545; font-weight: bold; text-align: right;'>%,d VND</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #f9f9f9; border-radius: 6px; padding: 20px; margin: 30px 0 0;'>"
                    + "<p style='color: #555; font-size: 13px; margin: 0; line-height: 1.6;'>"
                    + "<strong>Please Note:</strong><br>"
                    + "• Your services have been updated. Any additional charges will be payable upon check-out.<br>"
                    + "• If the new total is less than previously paid, a refund will be processed.<br>"
                    + "• Please bring identification for check-in.<br>"
                    + "• Check-in time: 14:00 | Check-out time: 12:00.<br>"
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
                    guestIdNumber,
                    guestDOB,
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
                            + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Updated Services</h2>"
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
                            + servicesHtml.toString()
                            + "</tbody>"
                            + "</table>"
                            + "</div>"
                    ) : "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #357ABD; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Updated Services</h2>"
                    + "<p style='color: #666; font-style: italic;'>No services selected for this booking.</p>"
                    + "</div>",
                    numberOfNights,
                    roomTotal.intValue(),
                    servicesTotal.intValue(),
                    subtotal.intValue(),
                    taxRate,
                    taxAmount.intValue(),
                    grandTotal.intValue(),
                    totalPaid.intValue(),
                    amountDue.intValue()
            );

            EmailSender emailSender = new EmailSender();
            boolean result = emailSender.sendHtmlEmail(
                    recipientEmail,
                    "Service Update Confirmation #" + bookingId + " - Hotel Management System",
                    htmlContent
            );

            if (result) {
                System.out.println("? Service update confirmation email sent successfully to: " + recipientEmail);
            }

            return result;

        } catch (Exception e) {
            System.err.println("? Error sending service update confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
