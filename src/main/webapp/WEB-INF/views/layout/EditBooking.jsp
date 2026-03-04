<%-- 
    Document   : EditBooking
    Created on : Oct 19, 2025, 9:42:50 AM
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
    ArrayList<Service> allServices = (ArrayList<Service>) request.getAttribute("ALL_SERVICES");

    NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));
    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter inputDf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    long nights = 0;
    double roomTotal = 0;
    double serviceTotal = 0;

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
        for (int i = 0; i < bookingServices.size() && i < services.size(); i++) {
            Service s = services.get(i);
            if (s.getPrice() != null) {
                serviceTotal += BigDecimal.valueOf(bookingServices.get(i).getQuantity())
                        .multiply(s.getPrice()).doubleValue();
            }
        }
    }

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
        <title>Edit Booking - Misuka Hotel</title>
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
            .card {
                background: white;
                border-radius: 8px;
                padding: 25px;
                margin: 20px 0;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .card-title {
                color: #D4AF37;
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 20px;
                padding-bottom: 10px;
                border-bottom: 2px solid #D4AF37;
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .card-title i {
                font-size: 20px;
            }
            .info-grid {
                display: grid;
                gap: 15px;
            }
            .info-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 15px;
                background: #f8f9fa;
                border-radius: 6px;
                border-left: 3px solid #D4AF37;
            }
            .info-label {
                color: #666;
                font-weight: 500;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .info-label i {
                color: #D4AF37;
                font-size: 14px;
            }
            .info-value {
                font-weight: 600;
                color: #333;
                text-align: right;
            }
            .info-value.highlight {
                color: #D4AF37;
                font-size: 18px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group.full {
                width: 100%;
            }
            .form-group label {
                display: block;
                font-weight: 500;
                margin-bottom: 8px;
                color: #333;
            }
            .service-selector {
                display: flex;
                gap: 10px;
                align-items: stretch;
            }
            .service-selector select {
                flex: 1;
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 14px;
                background: white;
            }
            .service-selector select:focus {
                outline: none;
                border-color: #D4AF37;
            }
            .service-selector .btn-add {
                width: 45px;
                height: 45px;
                background: #D4AF37;
                color: white;
                border: none;
                border-radius: 5px;
                font-size: 24px;
                font-weight: 600;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .service-selector .btn-add:hover {
                background: #B8941E;
            }
            .service-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 15px;
                margin: 10px 0;
                background: #f8f9fa;
                border-radius: 6px;
                border-left: 4px solid #D4AF37;
            }
            .service-item[data-disabled="true"] {
                background: #f1f3f5;
                border-left-color: #6c757d;
                opacity: 0.85;
            }
            .service-left {
                flex: 1;
            }
            .service-name {
                font-weight: 600;
                color: #333;
                font-size: 15px;
                margin-bottom: 5px;
            }
            .service-details {
                font-size: 12px;
                color: #666;
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
            }
            .service-details i {
                color: #D4AF37;
                margin-right: 4px;
            }
            .service-controls {
                display: flex;
                gap: 10px;
                align-items: center;
                flex-wrap: wrap;
            }
            .quantity-input, .service-date {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 13px;
            }
            .quantity-input {
                width: 70px;
                text-align: center;
            }
            .service-date {
                width: 140px;
            }
            .quantity-input:focus, .service-date:focus {
                outline: none;
                border-color: #D4AF37;
            }
            .quantity-input:disabled, .service-date:disabled {
                background: #e9ecef;
                cursor: not-allowed;
            }
            .service-price {
                font-weight: 700;
                color: #D4AF37;
                font-size: 16px;
                min-width: 120px;
                text-align: right;
            }
            .btn-remove {
                background: #dc3545;
                color: white;
                border: none;
                width: 35px;
                height: 35px;
                border-radius: 50%;
                font-size: 20px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                line-height: 1;
            }
            .btn-remove:hover {
                background: #c82333;
            }
            .no-services {
                text-align: center;
                padding: 40px 20px;
                color: #999;
                font-style: italic;
            }
            .no-services i {
                font-size: 48px;
                display: block;
                margin-bottom: 15px;
                opacity: 0.3;
            }
            .status-badge-inline {
                display: inline-block;
                padding: 4px 10px;
                border-radius: 12px;
                font-size: 11px;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .status-badge {
                font-size: 16px;
                padding: 8px 16px;
                border-radius: 20px;
            }
            .btn-save {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 12px 35px;
                border-radius: 25px;
                font-weight: 600;
            }
            .btn-save:hover {
                background-color: #218838;
            }
            .btn-cancel {
                background-color: #6c757d;
                color: white;
                border: none;
                padding: 12px 35px;
                border-radius: 25px;
                font-weight: 600;
                text-decoration: none;
                display: inline-block;
            }
            .btn-cancel:hover {
                background-color: #5a6268;
                color: white;
            }
            .payment-status {
                padding: 6px 15px;
                border-radius: 15px;
                font-weight: 600;
                font-size: 13px;
                display: inline-block;
            }
            .payment-status.paid {
                background: #d4edda;
                color: #155724;
            }
            .payment-status.pending {
                background: #fff3cd;
                color: #856404;
            }
            .payment-status.failed {
                background: #f8d7da;
                color: #721c24;
            }
        </style>
    </head>
    <body>
        <%@ include file="../shared/header.jsp" %>
        <%@ include file="../features/login/navbar.jsp" %>

        <div class="banner">
            <h1>Edit Booking</h1>
            <% if (booking != null) {%>
            <p>Booking ID: #<%= booking.getBookingId()%></p>
            <% }%>
        </div>

        <div class="detail-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4><i class="fa fa-edit"></i> Edit Booking Information</h4>
                <span class="badge <%= badgeClass%> status-badge"><%= status%></span>
            </div>

            <% if (booking != null) { %>

            <div class="card">
                <div class="card-title">
                    <i class="fa fa-user"></i> Guest Information
                </div>
                <div class="info-grid">
                    <% if (guest != null) {%>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-id-card"></i> Full Name
                        </span>
                        <span class="info-value"><%= guest.getFullName()%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-envelope"></i> Email
                        </span>
                        <span class="info-value"><%= guest.getEmail()%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-phone"></i> Phone Number
                        </span>
                        <span class="info-value"><%= guest.getPhone()%></span>
                    </div>
                    <% } else { %>
                    <div class="text-center text-muted py-3">
                        <i class="fa fa-user-slash fa-2x mb-2 opacity-25"></i>
                        <p>No guest information available</p>
                    </div>
                    <% } %>
                </div>
            </div>

            <div class="card">
                <div class="card-title">
                    <i class="fa fa-bed"></i> Room Information
                </div>
                <div class="info-grid">
                    <% if (room != null && roomType != null) {%>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-door-open"></i> Room Number
                        </span>
                        <span class="info-value"><%= room.getRoomNumber()%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-home"></i> Room Type
                        </span>
                        <span class="info-value"><%= roomType.getTypeName()%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-users"></i> Capacity
                        </span>
                        <span class="info-value"><%= roomType.getCapacity()%> guests</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-moon"></i> Duration
                        </span>
                        <span class="info-value"><%= nights%> night<%= nights > 1 ? "s" : ""%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-money-bill-wave"></i> Room Charge
                        </span>
                        <span class="info-value highlight"><%= vnd.format(roomTotal)%> VND</span>
                    </div>
                    <% }%>
                </div>
            </div>

            <div class="card">
                <div class="card-title">
                    <i class="fa fa-calendar-alt"></i> Stay Duration
                </div>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-calendar-check"></i> Check-In Date
                        </span>
                        <span class="info-value"><%= booking.getCheckInDate().toLocalDate().format(df)%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-calendar-times"></i> Check-Out Date
                        </span>
                        <span class="info-value"><%= booking.getCheckOutDate().toLocalDate().format(df)%></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">
                            <i class="fa fa-clock"></i> Total Nights
                        </span>
                        <span class="info-value highlight"><%= nights%></span>
                    </div>
                </div>
            </div>

            <form method="post" action="<%= IConstant.ACTION_EDIT_BOOKING%>">
                <input type="hidden" name="bookingId" value="<%= booking.getBookingId()%>">

                <div class="card">
                    <div class="card-title">
                        <i class="fa fa-concierge-bell"></i> Additional Services
                    </div>

                    <div class="form-group full">
                        <label>Select Service to Add</label>
                        <div class="service-selector">
                            <select id="service-select">
                                <option value="">-- Choose Service --</option>
                                <% if (allServices != null) {
                                        for (Service service : allServices) {%>
                                <option value="<%= service.getServiceId()%>" 
                                        data-price="<%= service.getPrice()%>" 
                                        data-name="<%= service.getServiceName()%>">
                                    <%= service.getServiceName()%> (+<%= vnd.format(service.getPrice())%> VND)
                                </option>
                                <%  }
                                    }%>
                            </select>
                            <button type="button" id="add-service-btn" class="btn-add">+</button>
                        </div>
                    </div>

                    <div id="selected-services-list" style="margin-top: 15px;">
                        <% if (bookingServices != null && !bookingServices.isEmpty()) {
                                for (int i = 0; i < bookingServices.size() && i < services.size(); i++) {
                                    BookingService bs = bookingServices.get(i);
                                    Service s = services.get(i);
                                    if (s != null && s.getPrice() != null) {
                                        int serviceStatus = bs.getStatus();
                                        boolean isReadOnly = (serviceStatus != 0);
                                        String statusText = "Pending";
                                        String statusBadge = "bg-warning text-dark";

                                        if (serviceStatus == 1) {
                                            statusText = "In Progress";
                                            statusBadge = "bg-primary";
                                        } else if (serviceStatus == 2) {
                                            statusText = "Completed";
                                            statusBadge = "bg-success";
                                        } else if (serviceStatus == -1) {
                                            statusText = "Canceled";
                                            statusBadge = "bg-danger";
                                        }

                                        double subtotal = BigDecimal.valueOf(bs.getQuantity())
                                                .multiply(s.getPrice()).doubleValue();
                        %>
                        <div class="service-item" 
                             data-service-id="<%= s.getServiceId()%>"
                             data-service-price="<%= s.getPrice()%>"
                             data-disabled="<%= isReadOnly%>">
                            <div class="service-left">
                                <div class="service-name"><%= s.getServiceName()%></div>
                                <div class="service-details">
                                    <span>
                                        <i class="fa fa-calendar"></i>
                                        <span class="date-display"></span>
                                    </span>
                                    <span>
                                        <i class="fa fa-tag"></i>
                                        <%= vnd.format(s.getPrice())%> VND/unit
                                    </span>
                                    <% if (isReadOnly) {%>
                                    <span class="status-badge-inline <%= statusBadge%>"><%= statusText%></span>
                                    <% }%>
                                </div>
                            </div>
                            <div class="service-controls">
                                <input type="number" 
                                       name="serviceQuantity" 
                                       class="quantity-input" 
                                       min="1" 
                                       value="<%= bs.getQuantity()%>" 
                                       <%= isReadOnly ? "disabled" : ""%>>
                                <input type="date" 
                                       name="serviceDate" 
                                       class="service-date" 
                                       value="<%= bs.getServiceDate().format(inputDf)%>"
                                       min="<%= booking.getCheckInDate().toLocalDate().format(inputDf)%>"
                                       max="<%= booking.getCheckOutDate().toLocalDate().format(inputDf)%>"
                                       <%= isReadOnly ? "disabled" : ""%>>
                                <input type="hidden" name="serviceId" value="<%= s.getServiceId()%>">
                                <div class="service-price"><%= vnd.format(subtotal)%> VND</div>
                                <% if (!isReadOnly) { %>
                                <button type="button" class="btn-remove">×</button>
                                <% } %>
                            </div>
                        </div>
                        <% }
                                }
                            }%>
                    </div>

                    <div id="no-services-msg" class="no-services" style="<%= (bookingServices != null && !bookingServices.isEmpty()) ? "display: none;" : ""%>">
                        <i class="fa fa-inbox"></i>
                        <div>No services added yet</div>
                    </div>
                </div>

                <% if (payment != null) {%>
                <div class="card">
                    <div class="card-title">
                        <i class="fa fa-credit-card"></i> Payment Information
                    </div>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">
                                <i class="fa fa-wallet"></i> Payment Method
                            </span>
                            <span class="info-value text-uppercase"><%= payment.getPaymentMethod()%></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">
                                <i class="fa fa-info-circle"></i> Payment Status
                            </span>
                            <span class="info-value">
                                <span class="payment-status <%= payment.getStatus().toLowerCase().contains("paid") ? "paid" : payment.getStatus().toLowerCase().contains("pending") ? "pending" : "failed"%>">
                                    <%= payment.getStatus()%>
                                </span>
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">
                                <i class="fa fa-money-bill"></i> Amount Paid
                            </span>
                            <span class="info-value highlight"><%= vnd.format(payment.getAmount())%> VND</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">
                                <i class="fa fa-calendar"></i> Payment Date
                            </span>
                            <span class="info-value"><%= payment.getPaymentDate().format(df)%></span>
                        </div>
                    </div>
                </div>
                <% }%>

                <div class="text-center mt-4">
                    <button type="submit" class="btn-save me-3">
                        <i class="fa fa-save"></i> Save Changes
                    </button>
                    <a href="<%= IConstant.ACTION_VIEW_BOOKING%>?bookingId=<%= booking.getBookingId()%>" class="btn-cancel">
                        <i class="fa fa-times"></i> Cancel
                    </a>
                </div>
            </form>

            <% } else { %>
            <div class="alert alert-warning">
                <i class="fa fa-exclamation-triangle"></i> Booking not found
            </div>
            <% }%>
        </div>

        <%@ include file="../shared/footer.jsp" %>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
        
        <script>
            const checkInDate = '<%= booking != null ? booking.getCheckInDate().toLocalDate().format(inputDf) : ""%>';
            const checkOutDate = '<%= booking != null ? booking.getCheckOutDate().toLocalDate().format(inputDf) : ""%>';
            const serviceSelect = document.getElementById('service-select');
            const addServiceBtn = document.getElementById('add-service-btn');
            const selectedServicesList = document.getElementById('selected-services-list');
            const noServicesMsg = document.getElementById('no-services-msg');
            const form = document.querySelector('form');

            function formatCurrency(amount) {
                return new Intl.NumberFormat('vi-VN', {
                    minimumFractionDigits: 0,
                    maximumFractionDigits: 2
                }).format(amount);
            }

            function formatDate(dateString) {
                if (!dateString) return '';
                const date = new Date(dateString);
                const day = String(date.getDate()).padStart(2, '0');
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const year = date.getFullYear();
                return day + '/' + month + '/' + year;
            }

            function validateServiceDate(dateInput) {
                const selectedDate = new Date(dateInput.value);
                const checkIn = new Date(checkInDate);
                const checkOut = new Date(checkOutDate);
                selectedDate.setHours(0, 0, 0, 0);
                checkIn.setHours(0, 0, 0, 0);
                checkOut.setHours(0, 0, 0, 0);
                if (selectedDate < checkIn || selectedDate > checkOut) {
                    alert('Ngày dịch vụ phải nằm trong khoảng Check-in (' + formatDate(checkInDate) +
                            ') và Check-out (' + formatDate(checkOutDate) + ')');
                    dateInput.value = checkInDate;
                    dateInput.style.borderColor = '#dc3545';
                    return false;
                }
                return true;
            }

            function checkDuplicateService(serviceId, serviceDate, excludeItem = null) {
                const allServices = document.querySelectorAll('.service-item:not([data-disabled="true"])');
                for (let item of allServices) {
                    if (item === excludeItem) continue;
                    const itemServiceId = item.getAttribute('data-service-id');
                    const itemDate = item.querySelector('.service-date')?.value;
                    if (itemServiceId === serviceId && itemDate === serviceDate) {
                        return true;
                    }
                }
                return false;
            }
            
            function updateServicePrice(serviceItem) {
                const price = parseFloat(serviceItem.getAttribute('data-service-price'));
                const quantity = parseInt(serviceItem.querySelector('.quantity-input')?.value) || 1;
                const subtotal = price * quantity;
                const priceDisplay = serviceItem.querySelector('.service-price');
                if (priceDisplay) {
                    priceDisplay.textContent = formatCurrency(subtotal) + ' VND';
                }
            }
            
            function removeService(button) {
                const serviceItem = button.closest('.service-item');
                if (serviceItem) {
                    serviceItem.remove();
                }
                const remainingServices = selectedServicesList.querySelectorAll('.service-item');
                if (remainingServices.length === 0) {
                    noServicesMsg.style.display = 'block';
                }
            }

            function addService() {
                const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
                if (!selectedOption || !selectedOption.value) {
                    alert('Vui lòng chọn một dịch vụ');
                    return;
                }
                const serviceId = selectedOption.value;
                const serviceName = selectedOption.getAttribute('data-name');
                const servicePrice = parseFloat(selectedOption.getAttribute('data-price'));
                if (checkDuplicateService(serviceId, checkInDate)) {
                    alert('Dịch vụ "' + serviceName + '" đã được thêm cho ngày ' +
                            formatDate(checkInDate) + '. Vui lòng chọn ngày khác hoặc sửa mục đã có.');
                    return;
                }
                const serviceItem = document.createElement('div');
                serviceItem.className = 'service-item';
                serviceItem.setAttribute('data-service-id', serviceId);
                serviceItem.setAttribute('data-service-price', servicePrice);
                serviceItem.setAttribute('data-disabled', 'false');
                const subtotal = servicePrice * 1;
                serviceItem.innerHTML = `
                    <div class="service-left">
                        <div class="service-name">\${serviceName}</div>
                        <div class="service-details">
                            <span>
                                <i class="fa fa-calendar"></i>
                                <span class="date-display">\${formatDate(checkInDate)}</span>
                            </span>
                            <span>
                                <i class="fa fa-tag"></i>
                                \${formatCurrency(servicePrice)} VND/unit
                            </span>
                        </div>
                    </div>
                    <div class="service-controls">
                        <input type="number" 
                               name="serviceQuantity" 
                               class="quantity-input" 
                               min="1" 
                               value="1">
                        <input type="date" 
                               name="serviceDate" 
                               class="service-date" 
                               value="\${checkInDate}"
                               min="\${checkInDate}"
                               max="\${checkOutDate}">
                        <input type="hidden" name="serviceId" value="\${serviceId}">
                        <div class="service-price">\${formatCurrency(subtotal)} VND</div>
                        <button type="button" class="btn-remove">×</button>
                    </div>
                `;
                selectedServicesList.appendChild(serviceItem);
                noServicesMsg.style.display = 'none';
                serviceSelect.selectedIndex = 0;
            }

            document.addEventListener('DOMContentLoaded', function () {
                if (addServiceBtn) {
                    addServiceBtn.addEventListener('click', addService);
                }
                document.querySelectorAll('.service-item').forEach(item => {
                    const dateInput = item.querySelector('.service-date');
                    const dateDisplay = item.querySelector('.date-display');
                    if (dateInput && dateDisplay) {
                        dateDisplay.textContent = formatDate(dateInput.value);
                    }
                });
                if (selectedServicesList) {
                    selectedServicesList.addEventListener('click', function(e) {
                        if (e.target.classList.contains('btn-remove')) {
                            const serviceItem = e.target.closest('.service-item');
                            if (serviceItem && serviceItem.getAttribute('data-disabled') !== 'true') {
                                removeService(e.target);
                            }
                        }
                    });
                    selectedServicesList.addEventListener('input', function(e) {
                        const serviceItem = e.target.closest('.service-item');
                        if (!serviceItem || serviceItem.getAttribute('data-disabled') === 'true') return;
                        if (e.target.classList.contains('quantity-input')) {
                            let qty = parseInt(e.target.value) || 1;
                            if (qty < 1) qty = 1;
                            e.target.value = qty;
                            updateServicePrice(serviceItem);
                        }
                        e.target.style.borderColor = '';
                    });
                    selectedServicesList.addEventListener('change', function(e) {
                        const serviceItem = e.target.closest('.service-item');
                        if (!serviceItem || serviceItem.getAttribute('data-disabled') === 'true') return;
                        if (e.target.classList.contains('service-date')) {
                            if (!validateServiceDate(e.target)) return;
                            const newDate = e.target.value;
                            const serviceId = serviceItem.getAttribute('data-service-id');
                            const serviceName = serviceItem.querySelector('.service-name')?.textContent;
                            const dateDisplay = serviceItem.querySelector('.date-display');
                            if (checkDuplicateService(serviceId, newDate, serviceItem)) {
                                alert('Dịch vụ "' + serviceName + '" đã được thêm cho ngày ' +
                                      formatDate(newDate) + '. Vui lòng chọn ngày khác.');
                                const oldDate = e.target.defaultValue || checkInDate;
                                e.target.value = oldDate; 
                                if (dateDisplay) dateDisplay.textContent = formatDate(oldDate);
                                return;
                            }
                            if (dateDisplay) dateDisplay.textContent = formatDate(newDate);
                        }
                    });
                }
                if (form) {
                    form.addEventListener('submit', function (e) {
                        const editableServices = document.querySelectorAll('.service-item:not([data-disabled="true"])');
                        let isValid = true;
                        let errorMessages = [];
                        const selectedServices = new Map();
                        editableServices.forEach((item, index) => {
                            const serviceId = item.getAttribute('data-service-id');
                            const dateInput = item.querySelector('.service-date');
                            const quantityInput = item.querySelector('.quantity-input');
                            if (dateInput && dateInput.value) {
                                if (!validateServiceDate(dateInput)) {
                                    isValid = false;
                                    errorMessages.push('Dịch vụ #' + (index + 1) + ': Ngày không hợp lệ.');
                                }
                                const dateKey = dateInput.value;
                                if (!selectedServices.has(serviceId)) {
                                    selectedServices.set(serviceId, new Set());
                                }
                                if (selectedServices.get(serviceId).has(dateKey)) {
                                    isValid = false;
                                    dateInput.style.borderColor = '#dc3545';
                                    errorMessages.push('Lỗi trùng lặp: Dịch vụ "' + item.querySelector('.service-name')?.textContent + '" vào ngày ' + formatDate(dateKey));
                                } else {
                                    selectedServices.get(serviceId).add(dateKey);
                                }
                            }
                            if (quantityInput) {
                                const qty = parseInt(quantityInput.value) || 0;
                                if (qty < 1) {
                                    isValid = false;
                                    quantityInput.style.borderColor = '#dc3545';
                                    errorMessages.push('Dịch vụ #' + (index + 1) + ': Số lượng phải ít nhất là 1');
                                }
                            }
                        });
                        if (!isValid) {
                            e.preventDefault();
                            alert('Vui lòng sửa các lỗi sau:\n\n' + [...new Set(errorMessages)].join('\n')); 
                            return false;
                        }
                        if (editableServices.length > 0) {
                            if (!confirm('Bạn có chắc muốn lưu thay đổi cho ' + editableServices.length + ' dịch vụ?')) {
                                e.preventDefault();
                                return false;
                            }
                        }
                        return true;
                    });
                }
            });
        </script>
    </body>
</html>