<%--
    Document   : ViewBookings
    Created on : Oct 19, 2025, 9:42:50 AM
    Author     : TR_NGHIA
--%>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dto.Guest, dto.Booking, dto.Room, dto.RoomType" %>
<%@ page import="java.util.ArrayList, java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="util.IConstant" %> <%-- *** GIẢ ĐỊNH BẠN IMPORT IConstant TỪ ĐÂY *** --%>

<%
    Guest user1 = (Guest) session.getAttribute("USER_GUEST");
    ArrayList<Booking> bookingHistory = (ArrayList<Booking>) request.getAttribute("BOOKING_HISTORY_LIST");
    Map<Integer, Room> roomMap = (Map<Integer, Room>) request.getAttribute("ROOM_DETAILS_MAP");
    Map<Integer, RoomType> typeMap = (Map<Integer, RoomType>) request.getAttribute("ROOM_TYPE_DETAILS_MAP");
    String errorMessage = (String) request.getAttribute("ERROR_MESSAGE");
    
    // Checkout messages
    String errorMsgCheckout = (String) request.getAttribute("ERROR_MSG_CHECKOUT");
    String successMsgCheckout = (String) request.getAttribute("SUCCESS_MSG_CHECKOUT");
    
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Booking History - Misuka Hotel</title>

        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">

        <style>
            :root {
                --font-heading: 'Playfair Display', serif;
                --font-body: 'Lato', sans-serif;
                --color-gold: #D4AF37;
                --color-dark-gold: #B8941E;
                --color-charcoal: #2C2C2C;
                --color-offwhite: #FEFDFB;
            }

            body {
                font-family: var(--font-body);
                background-color: var(--color-offwhite);
                color: var(--color-charcoal);
            }

            h1, h2, h3, h4, h5, h6 {
                font-family: var(--font-heading);
            }

            .page-banner {
                background-image: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), url("<%= request.getContextPath()%>/public/img/banner1.jpg");
                background-size: cover;
                background-position: center;
                padding: 8rem 0;
            }

            .page-banner h1 {
                color: #fff;
            }

            .table-container {
                background-color: #fff;
                padding: 2rem;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.07);
                margin-top: -50px;
                position: relative;
                z-index: 10;
            }

            .welcome-bar {
                background-color: #fff;
                padding: 1.5rem 2rem;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
                margin-bottom: 2.5rem;
            }

            .welcome-bar span {
                font-weight: 500;
                color: var(--color-charcoal);
                font-size: 1.25rem;
            }

            .no-booking-message {
                text-align: center;
                padding: 3rem 1rem;
                background-color: #fff;
                border: 1px dashed #E8DCC4;
                border-radius: 8px;
                color: #6c757d;
            }

            .btn-gold {
                background-color: var(--color-gold);
                border-color: var(--color-gold);
                color: var(--color-charcoal);
            }

            .btn-gold:hover {
                background-color: var(--color-dark-gold);
                border-color: var(--color-dark-gold);
                color: var(--color-charcoal);
            }

            /* *** CSS MỚI: Đồng bộ các nút bấm *** */
            .btn-dark {
                background-color: var(--color-charcoal);
                border-color: var(--color-charcoal);
                color: var(--color-offwhite);
            }
            .btn-dark:hover {
                background-color: #404040; /* A slightly lighter charcoal */
                border-color: #404040;
                color: var(--color-offwhite);
            }
            
            .btn-outline-dark {
                border-color: var(--color-charcoal);
                color: var(--color-charcoal);
            }
            .btn-outline-dark:hover {
                background-color: var(--color-charcoal);
                color: var(--color-offwhite);
            }
            
            /* Giữ lại nút màu Vàng thương hiệu (cho nút Edit) */
            .btn-warning {
                background-color: var(--color-gold);
                border-color: var(--color-gold);
                color: var(--color-charcoal);
            }

            .btn-warning:hover {
                background-color: var(--color-dark-gold);
                border-color: var(--color-dark-gold);
                color: var(--color-charcoal);
            }
            
            /* *** CSS MỚI: Tinh chỉnh màu chữ của Status *** */
            /* Đảm bảo màu chữ của trạng thái "Pending" và "Checked-out" đủ đậm */
            .badge.bg-light { 
                color: var(--color-charcoal) !important;
            }
            .badge.bg-warning-subtle { 
                color: #664d03 !important;
            }
            
            /* (Xóa bỏ các class .badge.bg-success ... vì Bootstrap 5.3 đã có sẵn) */
            /* (Xóa bỏ class .btn-primary ... vì ta dùng btn-dark) */
            
            /* Alert auto-dismiss animation */
            @keyframes slideDown {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            
            .alert-dismissible {
                animation: slideDown 0.3s ease-out;
                position: relative;
                z-index: 1050;
                margin-bottom: 1.5rem;
            }
            
            .alert-fixed {
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                z-index: 9999;
                min-width: 400px;
                max-width: 600px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            }
        </style>
    </head>
    <body>

        <%@ include file="../shared/header.jsp" %>
        <%@ include file="../features/login/navbar.jsp" %>

        <section class="page-banner">
            <div class="container text-center">
                <h1 class="display-4 fw-bold">Booking History</h1>
            </div>
        </section>

        <main class="container my-4">

            <div class="welcome-bar d-flex justify-content-between align-items-center">
                <span class="fs-4">
                    <% if (user1 != null) {%>
                    Welcome <%= user1.getFullName()%>! This is your booking history.
                    <% } else { %>
                    Please log in to view your booking history.
                    <% }%>
                </span>
                <a href="<%= IConstant.ACTION_HOME%>" class="btn btn-outline-secondary">
                    <i class="fa-solid fa-arrow-left me-2"></i>Go Back
                </a>
            </div>

            <% if (successMsgCheckout != null && !successMsgCheckout.isEmpty()) {%>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <strong>Success!</strong> <%= successMsgCheckout%>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>

            <% if (errorMsgCheckout != null && !errorMsgCheckout.isEmpty()) {%>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>
                <strong>Error!</strong> <%= errorMsgCheckout%>
                <button type,"" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>

            <% if (errorMessage != null && !errorMessage.isEmpty()) {%>
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <%= errorMessage%>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>

            <% if (bookingHistory != null && !bookingHistory.isEmpty()) { %>
            <div class="table-container">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th scope="col" class="text-center" style="width: 8%;">#ID</th>
                                <th scope="col" style="width: 12%;">Room No.</th>
                                <th scope="col" style="width: 20%;">Room Type</th>
                                <th scope="col" style="width: 13%;">Check-In</th>
                                <th scope="col" style="width: 13%;">Check-Out</th>
                                <th scope="col" class="text-center" style="width: 12%;">Status</th>
                                <th scope="col" class="text-center" style="width: 22%;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Booking b : bookingHistory) {
                                    Room room = roomMap.get(b.getRoomId());
                                    RoomType type = (room != null) ? typeMap.get(room.getRoomTypeId()) : null;
                                    String status = (b.getStatus() != null && !b.getStatus().isEmpty()) ? b.getStatus() : "Unknown";

                                    String badgeClasses = "text-secondary-emphasis bg-secondary-subtle border border-secondary-subtle"; // Mặc định
                                    String statusLower = status.toLowerCase();
                                    
                                    if (statusLower.equals("reserved"))
                                        badgeClasses = "text-success-emphasis bg-success-subtle border border-success-subtle";
                                    else if (statusLower.equals("checked-in") || statusLower.equals("checkedin"))
                                        badgeClasses = "text-primary-emphasis bg-primary-subtle border border-primary-subtle";
                                    else if (statusLower.equals("checked-out") || statusLower.equals("checkedout"))
                                        badgeClasses = "text-dark-emphasis bg-light border border-dark-subtle"; // Màu trung tính (đã xong)
                                    else if (statusLower.equals("pending"))
                                        badgeClasses = "text-warning-emphasis bg-warning-subtle border border-warning-subtle";
                            %>
                            <tr>

                                <th scope="row" class="text-center"><%= b.getBookingId()%></th>
                                <td><%= (room != null) ? room.getRoomNumber() : "N/A"%></td>
                                <td><%= (type != null) ? type.getTypeName() : "N/A"%></td>
                                <td><%= (b.getCheckInDate() != null) ? b.getCheckInDate().toLocalDate().format(df) : "N/A"%></td>
                                <td><%= (b.getCheckOutDate() != null) ? b.getCheckOutDate().toLocalDate().format(df) : "N/A"%></td>

                                <td class="text-center">
                                    <span class="badge <%= badgeClasses%> rounded-pill px-3 py-2">
                                        <%= status%>
                                    </span>
                                </td>

                                <td class="text-center">
                                    <a href="<%= IConstant.ACTION_VIEW_BOOKING%>?bookingId=<%= b.getBookingId()%>" 
                                       class="btn btn-sm btn-dark me-1">
                                        <i class="fa-solid fa-eye me-1"></i>View
                                    </a>
                                    
                                    <% if (statusLower.equals("reserved") || statusLower.equals("pending") || statusLower.equals("checked-in")) {%>
                                    <a href="<%= IConstant.ACTION_PRE_EDIT_BOOKING%>?bookingId=<%= b.getBookingId()%>" 
                                       class="btn btn-sm btn-warning me-1">
                                        <i class="fa-solid fa-pen-to-square me-1"></i>Edit
                                    </a>
                                    <% } %>
                                    
                                    <% if (statusLower.equals("checked-in")) {%>
                                    <a href="<%= IConstant.ACTION_PRE_CHECKOUT_BOOKING%>?bookingId=<%= b.getBookingId()%>" 
                                       class="btn btn-sm btn-outline-dark">
                                        <i class="fa-solid fa-right-from-bracket me-1"></i>Check-out
                                    </a>
                                    <% } %>
                                </td>

                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } else { %>
            <% if (errorMessage == null || errorMessage.isEmpty()) {%>
            <div class="no-booking-message">
                <i class="fas fa-folder-open fa-3x mb-3"></i>
                <p class="fs-5">You don't have any booking history yet.</p>
                <a href="<%= request.getContextPath()%>/home" class="btn btn-gold mt-3">Book a Room Now</a>
            </div>
            <% } %>
            <% }%>

        </main>

        <%@ include file="../shared/footer.jsp" %>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
        
        <script>
            // Auto-dismiss alerts after 5 seconds
            document.addEventListener('DOMContentLoaded', function() {
                const alerts = document.querySelectorAll('.alert-dismissible');
                alerts.forEach(function(alert) {
                    setTimeout(function() {
                        const bsAlert = new bootstrap.Alert(alert);
                        bsAlert.close();
                    }, 5000); // 5 seconds
                });
            });
        </script>

    </body>
</html>