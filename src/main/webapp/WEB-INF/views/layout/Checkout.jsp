<%--
    Document   : checkout
    Created on : Nov 08, 2025
    Author     : TR_NGHIA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    Booking booking = (Booking) request.getAttribute("BOOKING");
    Guest guest = (Guest) request.getAttribute("GUEST");
    Room room = (Room) request.getAttribute("ROOM");
    RoomType roomType = (RoomType) request.getAttribute("ROOM_TYPE");
    ArrayList<BookingService> bookingServices = (ArrayList<BookingService>) request.getAttribute("BOOKING_SERVICES");
    ArrayList<Service> services = (ArrayList<Service>) request.getAttribute("SERVICES");
    ArrayList<Payment> payments = (ArrayList<Payment>) request.getAttribute("PAYMENTS");

    Object nightsObj = request.getAttribute("NUMBER_OF_NIGHTS");
    Long numberOfNights = (nightsObj instanceof Number) ? ((Number) nightsObj).longValue() : 0L;

    Object roomFeeObj = request.getAttribute("ROOM_FEE");
    Double roomFee = (roomFeeObj instanceof Number) ? ((Number) roomFeeObj).doubleValue() : 0.0;

    Object serviceFeeObj = request.getAttribute("SERVICE_TOTAL_FEE");
    Double serviceTotalFee = (serviceFeeObj instanceof Number) ? ((Number) serviceFeeObj).doubleValue() : 0.0;

    Object subtotalObj = request.getAttribute("SUBTOTAL");
    Double subtotal = (subtotalObj instanceof Number) ? ((Number) subtotalObj).doubleValue() : 0.0;

    Object taxObj = request.getAttribute("TAX_AMOUNT");
    Double taxAmount = (taxObj instanceof Number) ? ((Number) taxObj).doubleValue() : 0.0;

    Object grandTotalObj = request.getAttribute("GRAND_TOTAL");
    Double grandTotal = (grandTotalObj instanceof Number) ? ((Number) grandTotalObj).doubleValue() : 0.0;

    Object totalPaidObj = request.getAttribute("TOTAL_PAID");
    Double totalPaid = (totalPaidObj instanceof Number) ? ((Number) totalPaidObj).doubleValue() : 0.0;

    Object remainingObj = request.getAttribute("REMAINING_AMOUNT");
    Double remainingAmount = (remainingObj instanceof Number) ? ((Number) remainingObj).doubleValue() : 0.0;

    NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    String status = (booking != null && booking.getStatus() != null) ? booking.getStatus() : "Unknown";
    String badgeClass = "bg-primary";
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Checkout - Misuka Hotel</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background: #f5f5f5;
            }
            .banner {
                background: linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.4)), url("<%= request.getContextPath()%>/public/img/banner1.jpg");
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
            .remaining-section {
                background: #d1ecf1;
                padding: 20px;
                border-radius: 6px;
                margin-top: 15px;
                border: 2px solid #0c5460;
            }
            .remaining-row {
                display: flex;
                justify-content: space-between;
                font-size: 22px;
                font-weight: bold;
                color: #0c5460;
            }
            .paid-section {
                background: #d4edda;
                padding: 20px;
                border-radius: 6px;
                margin-top: 15px;
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
            .btn-payment {
                background: #ffc107;
                color: #000;
                border: none;
                padding: 12px 30px;
                border-radius: 5px;
                font-weight: bold;
            }
            .btn-payment:hover {
                background: #e0a800;
            }
            .btn-checkout {
                background: #28a745;
                color: white;
                border: none;
                padding: 12px 30px;
                border-radius: 5px;
                font-weight: bold;
            }
            .btn-checkout:hover {
                background: #218838;
            }
            .status-badge {
                font-size: 16px;
                padding: 8px 16px;
            }
            .payment-item {
                padding: 10px;
                background: white;
                border-radius: 4px;
                margin-bottom: 8px;
                border-left: 3px solid #28a745;
            }
            /* Loại bỏ backdrop hoàn toàn */
            .modal-backdrop {
                display: none !important;
            }
            .modal-open {
                overflow: auto !important;
                padding-right: 0 !important;
            }
            .modal {
                background-color: transparent;
                overflow-y: auto;
            }
            .modal-content {
                box-shadow: 0 5px 15px rgba(0,0,0,0.5);
            }
        </style>
    </head>
    <body>
        <%@ include file="../shared/header.jsp" %>
        <%@ include file="../features/login/navbar.jsp" %>

        <div class="banner">
            <h1><i class="fa-solid fa-right-from-bracket"></i> Checkout</h1>
            <% if (booking != null) {%>
            <p>Booking ID: #<%= booking.getBookingId()%></p>
            <% }%>
        </div>

        <div class="detail-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4>Checkout Summary</h4>
                <div><span class="badge <%= badgeClass%> status-badge"><%= status%></span></div>
            </div>

            <% if (booking != null) { %>

            <div class="section">
                <h5><i class="fa fa-user"></i> Guest Information</h5>
                <% if (guest != null) {%>
                <div class="info-row"><span class="info-label">Full Name:</span><span class="info-value"><%= guest.getFullName()%></span></div>
                <div class="info-row"><span class="info-label">Email:</span><span class="info-value"><%= guest.getEmail()%></span></div>
                <div class="info-row"><span class="info-label">Phone:</span><span class="info-value"><%= guest.getPhone()%></span></div>
                    <% }%>
            </div>

            <div class="section">
                <h5><i class="fa fa-bed"></i> Room Information</h5>
                <% if (room != null && roomType != null) {%>
                <div class="info-row"><span class="info-label">Room Number:</span><span class="info-value"><%= room.getRoomNumber()%></span></div>
                <div class="info-row"><span class="info-label">Room Type:</span><span class="info-value"><%= roomType.getTypeName()%></span></div>
                <div class="info-row"><span class="info-label">Check-In:</span><span class="info-value"><%= booking.getCheckInDate().toLocalDate().format(df)%></span></div>
                <div class="info-row"><span class="info-label">Check-Out:</span><span class="info-value"><%= booking.getCheckOutDate().toLocalDate().format(df)%></span></div>
                <div class="info-row"><span class="info-label">Number of Nights:</span><span class="info-value"><%= numberOfNights%></span></div>
                    <% }%>
            </div>

            <div class="section">
                <h5><i class="fa fa-calculator"></i> Cost Breakdown</h5>
                <div class="info-row"><span class="info-label">Room Charge (<%= numberOfNights%> nights):</span><span class="info-value"><%= vnd.format(roomFee)%> VND</span></div>

                <% if (bookingServices != null && !bookingServices.isEmpty() && services != null) { %>
                <div class="mt-3 mb-2"><strong class="d-block mb-2 text-muted">Additional Services:</strong>
                    <% for (int i = 0; i < bookingServices.size(); i++) {
                            if (i < services.size()) {
                                BookingService bs = bookingServices.get(i);
                                Service s = services.get(i);
                                double serviceSubtotal = s.getPrice().doubleValue() * bs.getQuantity();%>
                    <div class="info-row">
                        <span class="info-label"><%= s.getServiceName()%> (x<%= bs.getQuantity()%>) <% if (bs.getServiceDate() != null) {%><small class="text-muted">- <%= bs.getServiceDate().format(df)%></small><% }%></span>
                        <span class="info-value"><%= vnd.format(serviceSubtotal)%> VND</span>
                    </div>
                    <% }
                        } %>
                </div>
                <% }%>

                <div class="info-row mt-3 pt-2 border-top"><span class="info-label">Service Total:</span><span class="info-value"><%= vnd.format(serviceTotalFee)%> VND</span></div>
                <div class="info-row"><span class="info-label">Subtotal:</span><span class="info-value"><%= vnd.format(subtotal)%> VND</span></div>
                <div class="info-row"><span class="info-label">Tax (10%):</span><span class="info-value"><%= vnd.format(taxAmount)%> VND</span></div>
                <div class="info-row mt-2 pt-2 border-top"><span class="info-label"><strong>Grand Total:</strong></span><span class="info-value" style="color:#D4AF37;font-size:18px;"><strong><%= vnd.format(grandTotal)%> VND</strong></span></div>
            </div>

            <% if (payments != null && !payments.isEmpty()) { %>
            <div class="section">
                <h5><i class="fa fa-credit-card"></i> Payment History</h5>
                <div class="payment-history">
                    <% for (Payment p : payments) {
                            if (!"Failed".equalsIgnoreCase(p.getStatus())) {%>
                    <div class="payment-item">
                        <div class="d-flex justify-content-between">
                            <div><strong><%= vnd.format(p.getAmount())%> VND</strong><small class="text-muted d-block"><%= p.getPaymentDate().format(df)%> - <%= p.getPaymentMethod()%></small></div>
                            <span class="badge bg-success align-self-center"><%= p.getStatus()%></span>
                        </div>
                    </div>
                    <% }
                        }%>
                </div>
                <div class="info-row mt-3 pt-2 border-top"><span class="info-label"><strong>Total Paid:</strong></span><span class="info-value" style="color:#28a745;"><strong><%= vnd.format(totalPaid)%> VND</strong></span></div>
            </div>
            <% }%>

            <% if (remainingAmount != null && remainingAmount > 0) {%>
            <div class="remaining-section">
                <div class="remaining-row"><span><i class="fa-solid fa-exclamation-triangle"></i> Remaining Balance:</span><span><%= vnd.format(remainingAmount)%> VND</span></div>
                <small class="text-muted d-block mt-2"><i class="fa-solid fa-info-circle"></i> Please complete payment before checkout</small>
            </div>
            <% } else {%>
            <div class="paid-section">
                <div class="d-flex justify-content-between align-items-center"><span style="font-size:18px;font-weight:bold;color:#155724;"><i class="fa-solid fa-check-circle"></i> Fully Paid</span><span style="font-size:20px;font-weight:bold;color:#155724;"><%= vnd.format(grandTotal)%> VND</span></div>
            </div>
            <% }%>

            <% } else { %>
            <div class="alert alert-warning"><i class="fa fa-exclamation-triangle"></i> Booking not found</div>
            <% }%>

            <div class="text-center mt-4">
                <form action="<%= request.getContextPath() + "/viewBookings"%>" method="get" style="display:inline;">
                    <button type="submit" class="btn btn-back me-2"><i class="fa fa-arrow-left"></i> Back</button>
                </form>

                <% if (remainingAmount != null && remainingAmount > 0 && booking != null) {%>
                <button type="button" class="btn btn-payment" data-bs-toggle="modal" data-bs-target="#paymentConfirmModal">
                    <i class="fa-solid fa-credit-card"></i> Pay Remaining (<%= vnd.format(remainingAmount)%> VND)
                </button>
                <% } else if (booking != null) {%>
                <form action="<%= request.getContextPath() + "/checkoutBooking"%>" method="get" style="display:inline;">
                    <input type="hidden" name="bookingId" value="<%= booking.getBookingId()%>">
                    <button type="submit" class="btn btn-checkout" onclick="return confirm('Are you sure you want to checkout this booking?');">
                        <i class="fa-solid fa-right-from-bracket"></i> Complete Checkout
                    </button>
                </form>
                <% }%>
            </div>

            <% if (remainingAmount != null && remainingAmount > 0 && booking != null && guest != null) {%>
            <div class="modal fade" id="paymentConfirmModal" tabindex="-1" aria-labelledby="paymentConfirmModalLabel" aria-hidden="true" data-bs-backdrop="false">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="paymentConfirmModalLabel"><i class="fa-solid fa-file-invoice-dollar"></i> Confirm Payment</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p>Please review your bill summary before proceeding with the payment for Booking ID: <strong>#<%= booking.getBookingId()%></strong></p>
                            <div class="section p-3 mb-0">
                                <div class="info-row"><span class="info-label">Guest:</span><span class="info-value"><%= guest.getFullName()%></span></div>
                                <div class="info-row"><span class="info-label">Grand Total:</span><span class="info-value"><%= vnd.format(grandTotal)%> VND</span></div>
                                <div class="info-row"><span class="info-label">Total Paid:</span><span class="info-value text-success"><%= vnd.format(totalPaid)%> VND</span></div>
                                <div class="info-row mt-2 pt-2 border-top" style="background:#fff3cd;padding:10px;border-radius:5px;">
                                    <span class="info-label"><strong>AMOUNT TO PAY:</strong></span>
                                    <span class="info-value" style="color:#dc3545;font-size:1.1rem;"><strong><%= vnd.format(remainingAmount)%> VND</strong></span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <form action="<%= IConstant.ACTION_CHECKOUT_BOOKING%>" method="GET">
                                <input type="hidden" name="bookingId" value="<%= booking.getBookingId()%>">
                                <input type="hidden" name="amount" value="<%= remainingAmount%>">
                                <button type="submit" class="btn btn-warning"><i class="fa-solid fa-shield-halved"></i> Proceed to Pay</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <% }%>
        </div>

        <%@ include file="../shared/footer.jsp" %>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                var paymentModal = document.getElementById('paymentConfirmModal');
                if (paymentModal) {
                    paymentModal.addEventListener('show.bs.modal', function () {
                        document.body.style.overflow = 'auto';
                        document.body.style.paddingRight = '0';
                    });
                    paymentModal.addEventListener('hidden.bs.modal', function () {
                        document.body.style.overflow = 'auto';
                        document.body.style.paddingRight = '0';
                    });
                }
            });
        </script>
    </body>
</html>