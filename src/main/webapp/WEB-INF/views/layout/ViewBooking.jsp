<%-- 
    Document   : ViewBooking
    Created on : Oct 30, 2025, 10:33:06 AM
    Author     : TR_NGHIA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.temporal.ChronoUnit" %>

<%
    Booking booking = (Booking) request.getAttribute("BOOKING");
    Guest guest = (Guest) request.getAttribute("GUEST");
    Room room = (Room) request.getAttribute("ROOM");
    RoomType roomType = (RoomType) request.getAttribute("ROOM_TYPE");
    ArrayList<BookingService> bookingServices = (ArrayList<BookingService>) request.getAttribute("BOOKING_SERVICES");
    ArrayList<Service> services = (ArrayList<Service>) request.getAttribute("SERVICES");
    Payment payment = (Payment) request.getAttribute("PAYMENT");

    NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    long nights = 0;
    double roomTotal = 0;
    double serviceTotal = 0;
    double grandTotal = 0;

    if (booking != null && roomType != null) {
        try {
            nights = ChronoUnit.DAYS.between(
                    booking.getCheckInDate().toLocalDate(),
                    booking.getCheckOutDate().toLocalDate()
            );
            if (nights <= 0) {
                nights = 1;
            }

            BigDecimal price = roomType.getPricePerNight();
            if (price != null) {
                roomTotal = BigDecimal.valueOf(nights).multiply(price).doubleValue();
            }
        } catch (Exception e) {
            nights = 1;
        }
    }

    if (bookingServices != null && services != null) {
        for (int i = 0; i < bookingServices.size(); i++) {
            if (i < services.size()) {
                BookingService bs = bookingServices.get(i);
                Service s = services.get(i);
                if (s.getPrice() != null) {
                    serviceTotal += BigDecimal.valueOf(bs.getQuantity())
                            .multiply(s.getPrice()).doubleValue();
                }
            }
        }
    }

    grandTotal = roomTotal + serviceTotal;

    String status = (booking != null && booking.getStatus() != null) ? booking.getStatus() : "Unknown";
    String badgeClass = "bg-secondary";
    String statusLower = status.toLowerCase();
    if (statusLower.equals("reserved"))
        badgeClass = "bg-success";
    else if (statusLower.contains("checked-in"))
        badgeClass = "bg-primary";
    else if (statusLower.contains("checked-out"))
        badgeClass = "bg-danger";
    else if (statusLower.equals("pending"))
        badgeClass = "bg-warning";
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Booking Details - Misuka Hotel</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background: #f5f5f5;
            }
            .banner {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                    url("<%= request.getContextPath()%>/public/img/banner1.jpg");
                background-size: cover;
                background-position: center;
                padding: 80px 0;
                color: white;
                text-align: center;
            }
            .detail-container {
                max-width: 900px;
                margin: -50px auto 40px;
                background: white;
                border-radius: 8px;
                padding: 30px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                position: relative;
                z-index: 10;
            }
            .section {
                margin: 25px 0;
                padding: 20px;
                background: #f8f9fa;
                border-radius: 6px;
            }
            .section h5 {
                color: #D4AF37;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 2px solid #D4AF37;
            }
            .info-row {
                display: flex;
                justify-content: space-between;
                padding: 8px 0;
                border-bottom: 1px solid #e0e0e0;
            }
            .info-row:last-child {
                border-bottom: none;
            }
            .info-label {
                color: #666;
                font-weight: 500;
            }
            .info-value {
                font-weight: 600;
                text-align: right;
            }
            .total-section {
                background: #fff3cd;
                padding: 20px;
                border-radius: 6px;
                margin-top: 20px;
            }
            .total-row {
                display: flex;
                justify-content: space-between;
                font-size: 20px;
                font-weight: bold;
                color: #D4AF37;
            }
            .btn-back {
                background: #6c757d;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
            }
            .btn-back:hover {
                background: #5a6268;
            }
            .status-badge {
                font-size: 16px;
                padding: 8px 16px;
            }
        </style>
    </head>
    <body>
        <%@ include file="../shared/header.jsp" %>
        <%@ include file="../features/login/navbar.jsp" %>

        <div class="banner">
            <h1>Booking Details</h1>
            <% if (booking != null) {%>
            <p>Booking ID: #<%= booking.getBookingId()%></p>
            <% }%>
        </div>

        <div class="detail-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4>Booking Information</h4>
                <div>
                    <span class="badge <%= badgeClass%> status-badge"><%= status%></span>
                </div>
            </div>

            <% if (booking != null) { %>

            <div class="section">
                <h5><i class="fa fa-user"></i> Guest Information</h5>
                <% if (guest != null) {%>
                <div class="info-row">
                    <span class="info-label">Full Name:</span>
                    <span class="info-value"><%= guest.getFullName()%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Email:</span>
                    <span class="info-value"><%= guest.getEmail()%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Phone:</span>
                    <span class="info-value"><%= guest.getPhone()%></span>
                </div>
                <% } else { %>
                <p class="text-muted">No guest information available</p>
                <% } %>
            </div>

            <div class="section">
                <h5><i class="fa fa-bed"></i> Room Information</h5>
                <% if (room != null && roomType != null) {%>
                <div class="info-row">
                    <span class="info-label">Room Number:</span>
                    <span class="info-value"><%= room.getRoomNumber()%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Room Type:</span>
                    <span class="info-value"><%= roomType.getTypeName()%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Capacity:</span>
                    <span class="info-value"><%= roomType.getCapacity()%> guests</span>
                </div>
                <% } else { %>
                <p class="text-muted">No room information available</p>
                <% }%>
            </div>

            <div class="section">
                <h5><i class="fa fa-calendar"></i> Stay Duration</h5>
                <div class="info-row">
                    <span class="info-label">Check-In:</span>
                    <span class="info-value"><%= booking.getCheckInDate().toLocalDate().format(df)%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Check-Out:</span>
                    <span class="info-value"><%= booking.getCheckOutDate().toLocalDate().format(df)%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Number of Nights:</span>
                    <span class="info-value"><%= nights%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Booking Date:</span>
                    <span class="info-value"><%= booking.getBookingDate().format(df)%></span>
                </div>
            </div>

            <div class="section">
                <h5><i class="fa fa-dollar-sign"></i> Cost Breakdown</h5>
                <div class="info-row">
                    <span class="info-label">Room Charge (<%= nights%> nights):</span>
                    <span class="info-value"><%= vnd.format(roomTotal)%> VND</span>
                </div>

                <% if (bookingServices != null && !bookingServices.isEmpty() && services != null) { %>
                <div class="mt-3">
                    <strong class="d-block mb-2">Services:</strong>
                    <% for (int i = 0; i < bookingServices.size(); i++) {
                            if (i < services.size()) {
                                BookingService bs = bookingServices.get(i);
                                Service s = services.get(i);
                                if (s.getPrice() != null) {
                                    double subtotal = BigDecimal.valueOf(bs.getQuantity())
                                            .multiply(s.getPrice()).doubleValue();
                    %>
                    <div class="info-row">
                        <span class="info-label">
                            <%= s.getServiceName()%> (x<%= bs.getQuantity()%>)
                            <% if (bs.getServiceDate() != null) {%>
                            <small class="text-muted">- <%= bs.getServiceDate().format(df)%></small>
                            <% }%>
                        </span>
                        <span class="info-value"><%= vnd.format(subtotal)%> VND</span>
                    </div>
                    <%
                            }
                        }
                    } %>
                </div>
                <% }%>

                <div class="info-row mt-3 pt-3 border-top">
                    <span class="info-label"><strong>Service Total:</strong></span>
                    <span class="info-value"><strong><%= vnd.format(serviceTotal)%> VND</strong></span>
                </div>
            </div>

            <% if (payment != null) {%>
            <div class="section">
                <h5><i class="fa fa-credit-card"></i> Payment Information</h5>
                <div class="info-row">
                    <span class="info-label">Payment Date:</span>
                    <span class="info-value"><%= payment.getPaymentDate().format(df)%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Amount Paid:</span>
                    <span class="info-value"><%= vnd.format(payment.getAmount())%> VND</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Payment Method:</span>
                    <span class="info-value text-uppercase"><%= payment.getPaymentMethod()%></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Payment Status:</span>
                    <span class="info-value"><%= payment.getStatus()%></span>
                </div>
            </div>
            <% }%>

            <div class="total-section">
                <div class="total-row">
                    <span>GRAND TOTAL:</span>
                    <span><%= vnd.format(grandTotal)%> VND</span>
                </div>
            </div>

            <% } else { %>
            <div class="alert alert-warning">
                <i class="fa fa-exclamation-triangle"></i> Booking not found
            </div>
            <% }%>

            <div class="text-center mt-4">
                <a href="<%= IConstant.ACTION_VIEW_BOOKINGS %>" class="btn btn-back">
                    <i class="fa fa-arrow-left"></i> Back to History
                </a>
            </div>
        </div>

        <%@ include file="../shared/footer.jsp" %>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    </body>
</html>