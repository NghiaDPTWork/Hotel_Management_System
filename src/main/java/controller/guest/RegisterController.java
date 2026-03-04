package controller.guest;

import controller.feature.EmailSender;
import dao.GuestDAO;
import dto.Guest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private GuestDAO guestDAO;

    @Override
    public void init() throws ServletException {
        guestDAO = new GuestDAO();
    }

    public boolean validate(String email, String idNumber) {
        return guestDAO.checkDuplicateEmail(email) || guestDAO.checkDuplicateIdNumber(idNumber);
    }

    public boolean addGuest(String fullName, String phone, String email, String password, String address, String idNumber, String dateOfBirth) {
        return guestDAO.addGuest(new Guest(fullName, phone, email, address, idNumber, dateOfBirth, password));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ===============================================
        // ============ ENCODING CONFIGURATION ===========
        // ===============================================
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // ===============================================
        // ========== REGISTRATION DATA RETRIEVAL ========
        // ===============================================
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String dateOfBirth = req.getParameter("dateOfBirth");
        String address = req.getParameter("address");
        String idNumber = req.getParameter("idNumber");

        try {
            // ===============================================
            // ========== VALIDATION AND REGISTRATION ========
            // ===============================================
            if (!validate(email, idNumber)) {
                boolean accountCreated = addGuest(fullName, phone, email, password, address, idNumber, dateOfBirth);

                if (accountCreated) {
                    // ===============================================
                    // ========== SEND WELCOME EMAIL ASYNC ===========
                    // ===============================================
                    final String finalEmail = email;
                    final String finalFullName = fullName;
                    final String finalPhone = phone;
                    final String finalIdNumber = idNumber;
                    final String finalDateOfBirth = dateOfBirth;

                    new Thread(() -> {
                        sendWelcomeEmail(finalEmail, finalFullName, finalPhone, finalIdNumber, finalDateOfBirth);
                    }).start();

                    // ===============================================
                    // ========= SET ATTRIBUTES FOR SUCCESS ==========
                    // ===============================================
                    req.setAttribute("fullName", fullName);
                    req.setAttribute("email", email);
                    req.setAttribute("phone", phone);
                    req.setAttribute("dateOfBirth", dateOfBirth);
                    req.setAttribute("address", address);
                    req.setAttribute("idNumber", idNumber);
                    req.setAttribute("SUCCESS_MSG", "Account created successfully! Please check your email.");

                    // ===============================================
                    // ========== FORWARD TO HOME PAGE ===============
                    // ===============================================
                    req.getRequestDispatcher(IConstant.PAGE_HOME).forward(req, resp);
                } else {
                    req.setAttribute("ERROR_MSG_REGISTER", "Failed to create account");
                    req.getRequestDispatcher(IConstant.PAGE_HOME).forward(req, resp);
                }

            } else {
                // ===============================================
                // ======== VALIDATION FAILURE HANDLING ==========
                // ===============================================
                req.setAttribute("ERROR_MSG_REGISTER", "Email or ID number is already used");
                req.getRequestDispatcher(IConstant.PAGE_HOME).forward(req, resp);
                return;
            }

        } catch (Exception e) {
            // ===============================================
            // ========== GENERAL ERROR HANDLING =============
            // ===============================================
            e.printStackTrace();
            req.setAttribute("ERROR_MSG_REGISTER", "Fail to create account");
            req.getRequestDispatcher(IConstant.PAGE_ERROR).forward(req, resp);
        }
    }

    protected boolean sendWelcomeEmail(String recipientEmail, String fullName, String phone, String idNumber, String dateOfBirth) {
        try {
            // ===============================================
            // ========== DATE FORMATTING ====================
            // ===============================================
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = dateFormatter.format(currentDate);

            // ===============================================
            // ========== EMAIL HTML CONTENT =================
            // ===============================================
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
                    // Header
                    + "<div style='background: linear-gradient(135deg, #D4AF37 0%%, #B8941E 100%%); padding: 40px; text-align: center;'>"
                    + "<h1 style='color: #ffffff; margin: 0; font-size: 32px; font-family: \"Playfair Display\", serif;'>Welcome to Misuka Hotel</h1>"
                    + "<p style='color: #ffffff; margin: 10px 0 0 0; opacity: 0.9; font-size: 16px;'>Your account has been created successfully</p>"
                    + "</div>"
                    // Main Content
                    + "<div style='padding: 30px;'>"
                    + "<p style='color: #2C2C2C; font-size: 18px; line-height: 1.6;'>Dear <strong>%s</strong>,</p>"
                    + "<p style='color: #555; font-size: 16px; line-height: 1.6;'>Thank you for registering with <strong>Misuka Hotel</strong>. We are delighted to have you as part of our community!</p>"
                    + "<p style='color: #555; font-size: 16px; line-height: 1.6;'>Your account has been successfully created and you can now enjoy our services.</p>"
                    // Account Information
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Account Information</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse; color: #2C2C2C; font-size: 15px;'>"
                    + "<tr><td style='padding: 10px 0; color: #666; width: 40%%;'>Full Name:</td><td style='padding: 10px 0; font-weight: bold;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Email:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Phone:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>ID Number:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Date of Birth:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "<tr><td style='padding: 10px 0; color: #666;'>Registration Date:</td><td style='padding: 10px 0;'>%s</td></tr>"
                    + "</table>"
                    + "</div>"
                    // What's Next Section
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>What's Next?</h2>"
                    + "<ul style='color: #555; font-size: 15px; line-height: 1.8; padding-left: 20px;'>"
                    + "<li>Browse our available rooms and special offers</li>"
                    + "<li>Make your first booking with ease</li>"
                    + "<li>Enjoy exclusive member benefits and discounts</li>"
                    + "<li>Access your booking history anytime</li>"
                    + "</ul>"
                    + "</div>"
                    // Hotel Features
                    + "<div style='padding: 20px 0; margin: 20px 0; border-bottom: 1px solid #f0f0f0;'>"
                    + "<h2 style='color: #B8941E; margin: 0 0 15px 0; font-size: 22px; font-family: \"Playfair Display\", serif;'>Our Services</h2>"
                    + "<table style='width: 100%%; border-collapse: collapse;'>"
                    + "<tr>"
                    + "<td style='width: 50%%; padding: 10px;'>"
                    + "<div style='padding: 15px; background-color: #f9f9f9; border-radius: 6px;'>"
                    + "<div style='color: #B8941E; font-size: 36px; margin-bottom: 8px;'>&#127976;</div>"
                    + "<div style='color: #2C2C2C; font-weight: bold; font-size: 14px;'>Luxury Rooms</div>"
                    + "<div style='color: #666; font-size: 13px;'>Comfortable & modern</div>"
                    + "</div>"
                    + "</td>"
                    + "<td style='width: 50%%; padding: 10px;'>"
                    + "<div style='padding: 15px; background-color: #f9f9f9; border-radius: 6px;'>"
                    + "<div style='color: #B8941E; font-size: 36px; margin-bottom: 8px;'>&#127860;</div>"
                    + "<div style='color: #2C2C2C; font-weight: bold; font-size: 14px;'>Restaurant</div>"
                    + "<div style='color: #666; font-size: 13px;'>Delicious cuisine</div>"
                    + "</div>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td style='width: 50%%; padding: 10px;'>"
                    + "<div style='padding: 15px; background-color: #f9f9f9; border-radius: 6px;'>"
                    + "<div style='color: #B8941E; font-size: 36px; margin-bottom: 8px;'>&#128134;</div>"
                    + "<div style='color: #2C2C2C; font-weight: bold; font-size: 14px;'>Spa & Wellness</div>"
                    + "<div style='color: #666; font-size: 13px;'>Relax & rejuvenate</div>"
                    + "</div>"
                    + "</td>"
                    + "<td style='width: 50%%; padding: 10px;'>"
                    + "<div style='padding: 15px; background-color: #f9f9f9; border-radius: 6px;'>"
                    + "<div style='color: #B8941E; font-size: 36px; margin-bottom: 8px;'>&#128663;</div>"
                    + "<div style='color: #2C2C2C; font-weight: bold; font-size: 14px;'>Transport</div>"
                    + "<div style='color: #666; font-size: 13px;'>Airport shuttle</div>"
                    + "</div>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</div>"
                    // Contact Info
                    + "<div style='background-color: #f9f9f9; border-radius: 6px; padding: 20px; margin: 30px 0 0;'>"
                    + "<p style='color: #555; font-size: 13px; margin: 0; line-height: 1.6;'>"
                    + "<strong>Need Help?</strong><br>"
                    + "• Email: support@misukahotel.com<br>"
                    + "• Phone: 1900-xxxx<br>"
                    + "• Address: 123 Hotel Street, City<br>"
                    + "• Website: www.misukahotel.com"
                    + "</p>"
                    + "</div>"
                    + "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #f0f0f0;'>"
                    + "<p style='color: #555; font-size: 15px; line-height: 1.6;'>We look forward to serving you!</p>"
                    + "<p style='color: #B8941E; font-size: 16px; font-weight: bold; margin: 20px 0 0 0;'>The Misuka Hotel Team</p>"
                    + "</div>"
                    + "</div>"
                    // Footer
                    + "<div style='background-color: #ffffff; padding: 30px; text-align: center; border-top: 1px solid #f0f0f0;'>"
                    + "<p style='color: #666; font-size: 14px; margin: 0;'>Thank you for choosing Misuka Hotel!</p>"
                    + "<p style='color: #999; font-size: 12px; margin: 10px 0 0 0;'>© 2025 Misuka Hotel Management System. All rights reserved.</p>"
                    + "<p style='color: #999; font-size: 11px; margin: 10px 0 0 0;'>This is an automated message. Please do not reply to this email.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>",
                    fullName, // Dear %s
                    fullName, // Full Name value
                    recipientEmail, // Email value
                    phone, // Phone value
                    idNumber, // ID Number value
                    dateOfBirth, // Date of Birth value
                    formattedDate // Registration Date value
            );

            // ===============================================
            // ========== SEND EMAIL =========================
            // ===============================================
            EmailSender emailSender = new EmailSender();
            boolean result = emailSender.sendHtmlEmail(
                    recipientEmail,
                    "Welcome to Misuka Hotel - Account Created Successfully",
                    htmlContent
            );

            if (result) {
                System.out.println("Welcome email sent successfully to: " + recipientEmail);
            } else {
                System.out.println("Failed to send welcome email to: " + recipientEmail);
            }

            return result;

        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
