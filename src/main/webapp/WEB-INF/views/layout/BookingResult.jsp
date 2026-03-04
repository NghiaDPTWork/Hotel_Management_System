<%-- 
    Document   : BookingResult
    Created on : Oct 19, 2025, 9:42:50 AM
    Author     : TR_NGHIA
--%>

<%@page import="java.math.BigDecimal"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.temporal.ChronoUnit"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    boolean isSuccess = Boolean.TRUE.equals(request.getAttribute("SUCCESS"));
    NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    Booking booking = (Booking) request.getAttribute("BOOKING");
    Room room = (Room) request.getAttribute("ROOM");
    Guest guest = (Guest) request.getAttribute("GUEST");
    RoomType roomType = (RoomType) request.getAttribute("ROOM_TYPE");
    ArrayList<ChoosenService> chosenServices = (ArrayList<ChoosenService>) request.getAttribute("CHOSEN_SERVICES");
    ArrayList<Service> allServices = (ArrayList<Service>) request.getAttribute("SERVICE_LIST");
    String paymentMethod = (String) request.getAttribute("PAYMENT_METHOD");

    double totalAmount = 0.0;
    try {
        String amt = (String) request.getAttribute("TOTAL_AMOUNT");
        if (amt != null) {
            totalAmount = Double.parseDouble(amt);
        }
    } catch (Exception e) {
    }

    double taxRate = 5.0;
    try {
        Double tax = (Double) request.getAttribute("TAX_RATE");
        if (tax != null) {
            taxRate = tax;
        }
    } catch (Exception e) {
    }

    double depositRate = 30.0;
    try {
        Double deposit = (Double) request.getAttribute("DEPOSIT_RATE");
        if (deposit != null) {
            depositRate = deposit;
        }
    } catch (Exception e) {
    }

    long nights = 1;
    double roomTotal = 0;
    if (isSuccess && booking != null && roomType != null) {
        try {
            LocalDate checkIn = booking.getCheckInDate().toLocalDate();
            LocalDate checkOut = booking.getCheckOutDate().toLocalDate();
            nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights <= 0) {
                nights = 1;
            }

            BigDecimal price = roomType.getPricePerNight();
            roomTotal = price != null ? BigDecimal.valueOf(nights).multiply(price).doubleValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Booking Result</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', sans-serif;
                background: #FEFDFB;
                padding: 20px;
                line-height: 1.6;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
                background: white;
                border-radius: 8px;
                padding: 30px;
                box-shadow: 0 2px 10px rgba(212, 175, 55, 0.15);
            }
            .status {
                text-align: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 2px solid #E8DCC4;
            }
            .status h2 {
                color: #28a745;
                margin: 10px 0;
            }
            .status.error h2 {
                color: #dc3545;
            }
            .section {
                margin: 20px 0;
            }
            .section h3 {
                font-size: 16px;
                color: #D4AF37;
                margin-bottom: 10px;
                padding-bottom: 5px;
                border-bottom: 1px solid #E8DCC4;
            }
            .row {
                display: flex;
                justify-content: space-between;
                padding: 8px 0;
                font-size: 14px;
            }
            .row span:first-child {
                color: #666666;
            }
            .row span:last-child {
                font-weight: 500;
                color: #2C2C2C;
            }
            .payment-info {
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .payment-info i {
                color: #D4AF37;
                font-size: 16px;
            }
            .subtotal {
                margin-top: 15px;
                padding-top: 10px;
                border-top: 1px solid #E8DCC4;
            }
            .tax-row {
                color: #666666;
            }
            .total {
                margin-top: 15px;
                padding-top: 15px;
                border-top: 2px solid #D4AF37;
                font-size: 18px;
                font-weight: bold;
                color: #D4AF37;
                display: flex;
                justify-content: space-between;
            }
            .deposit-row {
                color: #28a745;
                font-weight: bold;
            }
            .due-row {
                color: #dc3545;
                font-weight: bold;
            }
            .btn {
                display: inline-block;
                margin-top: 20px;
                padding: 10px 25px;
                background: #D4AF37;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                text-align: center;
            }
            .btn:hover {
                background: #B8941E;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="status <%= isSuccess ? "" : "error"%>">
                <h2><%= isSuccess ? "✓ Booking Successful" : "✗ Booking Failed"%></h2>
                <p><%= isSuccess ? "Thank you for your booking" : "Please try again"%></p>
            </div>


            <%!
                public Service findService(int id, ArrayList<Service> list) {
                    if (list == null) {
                        return null;
                    }
                    for (Service s : list) {
                        if (s.getServiceId() == id) {
                            return s;
                        }
                    }
                    return null;
                }
            %>


            <% if (isSuccess && booking != null) {
                    double servicesTotal = 0.0;
                    if (chosenServices != null && !chosenServices.isEmpty()) {
                        for (ChoosenService cs : chosenServices) {
                            Service s = findService(cs.getServiceId(), allServices);
                            if (s != null && s.getPrice() != null) {
                                double sub = BigDecimal.valueOf(cs.getQuantity()).multiply(s.getPrice()).doubleValue();
                                servicesTotal += sub;
                            }
                        }
                    }

                    double subtotal = roomTotal + servicesTotal;
                    double taxAmount = subtotal * (taxRate / 100.0);
                    double grandTotal = subtotal + taxAmount;
                    double depositAmount = grandTotal * (depositRate / 100.0);
                    double amountDue = grandTotal - depositAmount;
            %>
            <div class="section">
                <h3>Information</h3>
                <div class="row">
                    <span>Booking ID:</span>
                    <span>#<%= booking.getBookingId()%></span>
                </div>
                <% if (guest != null) {%>
                <div class="row">
                    <span>Guest:</span>
                    <span><%= guest.getFullName()%></span>
                </div>
                <% }%>
                <% if (room != null && roomType != null) {%>
                <div class="row">
                    <span>Room:</span>
                    <span><%= room.getRoomNumber()%> (<%= roomType.getTypeName()%>)</span>
                </div>
                <% }%>
            </div>

            <div class="section">
                <h3>Duration</h3>
                <div class="row">
                    <span>Check-in:</span>
                    <span><%= booking.getCheckInDate().toLocalDate().format(df)%></span>
                </div>
                <div class="row">
                    <span>Check-out:</span>
                    <span><%= booking.getCheckOutDate().toLocalDate().format(df)%></span>
                </div>
                <div class="row">
                    <span>Nights:</span>
                    <span><%= nights%></span>
                </div>
            </div>

            <div class="section">
                <h3>Payment Information</h3>
                <div class="row">
                    <span>Payment Method:</span>
                    <span class="payment-info">
                        <% if (paymentMethod != null) {
                                String icon = "";
                                if (paymentMethod.equals("Cash")) {
                                    icon = "fa-money-bill-wave";
                                } else if (paymentMethod.equals("Credit Card")) {
                                    icon = "fa-credit-card";
                                } else if (paymentMethod.equals("Debit Card")) {
                                    icon = "far fa-credit-card";
                                } else if (paymentMethod.equals("Bank Transfer")) {
                                    icon = "fa-university";
                                } else if (paymentMethod.equals("E-Wallet")) {
                                    icon = "fa-wallet";
                                } else {
                                    icon = "fa-money-check";
                                }
                        %>
                        <i class="fas <%= icon%>"></i>
                        <%= paymentMethod%>
                        <% } else {%>
                        N/A
                        <% }%>
                    </span>
                </div>
                <div class="row">
                    <span>Payment Status:</span>
                    <span>Deposit</span>
                </div>
            </div>

            <div class="section">
                <h3>Charges</h3>
                <div class="row">
                    <span>Room rate (<%= nights%> night<%= nights > 1 ? "s" : ""%>):</span>
                    <span><%= vnd.format(roomTotal)%> VND</span>
                </div>

                <% if (chosenServices != null && !chosenServices.isEmpty()) {
                        for (ChoosenService cs : chosenServices) {
                            Service s = findService(cs.getServiceId(), allServices);
                            if (s != null && s.getPrice() != null) {
                                double sub = BigDecimal.valueOf(cs.getQuantity()).multiply(s.getPrice()).doubleValue();
                %>
                <div class="row">
                    <span><%= s.getServiceName()%> (x<%= cs.getQuantity()%>):</span>
                    <span><%= vnd.format(sub)%> VND</span>
                </div>
                <% }
                        }
                    }%>

                <div class="row subtotal">
                    <span>Subtotal:</span>
                    <span><%= vnd.format(subtotal)%> VND</span>
                </div>

                <div class="row tax-row">
                    <span>Tax (<%= String.format("%.1f", taxRate)%>%):</span>
                    <span><%= vnd.format(taxAmount)%> VND</span>
                </div>

                <div class="total">
                    <span>Grand Total:</span>
                    <span><%= vnd.format(grandTotal)%> VND</span>
                </div>

                <div class="row deposit-row">
                    <span>Deposit Paid (<%= String.format("%.1f", depositRate)%>%):</span>
                    <span><%= vnd.format(depositAmount)%> VND</span>
                </div>

                <div class="row due-row">
                    <span>Amount Due:</span>
                    <span><%= vnd.format(amountDue)%> VND</span>
                </div>
            </div>
            <% }%>

            <a href="HomeController" class="btn">Back to Home</a>
        </div>
    </body>
</html>