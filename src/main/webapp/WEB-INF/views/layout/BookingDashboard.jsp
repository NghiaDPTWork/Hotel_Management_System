<%--
    Document    : BookingDashboard 
    Created on : Oct 12, 2025, 2:00:00 PM
    Author      : TR_NGHIA
--%>

<%@page import="util.IConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.*, java.util.*, java.text.NumberFormat" %>

<%
    Guest guest = (Guest) request.getAttribute("GUEST");
    Room room = (Room) request.getAttribute("ROOM");
    RoomType roomType = (RoomType) request.getAttribute("ROOM_TYPE");
    String checkIn = (String) request.getAttribute("CHECK_IN");
    String checkOut = (String) request.getAttribute("CHECK_OUT");
    ArrayList<Service> allServices = (ArrayList<Service>) request.getAttribute("ALL_SERVICES");
    Double taxRate = (Double) request.getAttribute("TAX");
    Double depositRate = (Double) request.getAttribute("DEPOSIT_RATE");

    if (taxRate == null) {
        taxRate = 5.0;
    }
    if (depositRate == null) {
        depositRate = 30.0;
    }

    NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Booking - Hotel Masura</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: Arial, sans-serif;
                background: #FEFDFB;
                color: #2C2C2C;
                line-height: 1.6;
            }
            .container {
                max-width: 1000px;
                margin: 20px auto;
                padding: 0 20px;
            }

            .header {
                background: #2C2C2C;
                color: white;
                padding: 15px 0;
                margin-bottom: 30px;
            }
            .header-content {
                max-width: 1000px;
                margin: 0 auto;
                padding: 0 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .logo {
                font-size: 24px;
                font-weight: bold;
                color: #D4AF37;
            }

            .card {
                background: white;
                border-radius: 8px;
                padding: 25px;
                margin-bottom: 20px;
                box-shadow: 0 2px 4px rgba(212, 175, 55, 0.15);
            }
            .card-title {
                font-size: 20px;
                font-weight: bold;
                margin-bottom: 15px;
                color: #2C2C2C;
                border-bottom: 2px solid #D4AF37;
                padding-bottom: 10px;
            }

            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 15px;
                margin-bottom: 15px;
            }
            .form-group {
                display: flex;
                flex-direction: column;
            }
            .form-group.full {
                grid-column: 1 / -1;
            }

            label {
                font-size: 14px;
                font-weight: 600;
                margin-bottom: 5px;
                color: #666;
            }
            input, select {
                padding: 10px;
                border: 1px solid #E8DCC4;
                border-radius: 4px;
                font-size: 14px;
            }
            input:disabled {
                background: #FEFDFB;
                color: #666;
            }
            input:focus, select:focus {
                outline: none;
                border-color: #D4AF37;
            }

            .service-selector {
                display: flex;
                gap: 10px;
                align-items: flex-end;
            }
            .service-selector select {
                flex: 1;
            }

            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 600;
            }
            .btn-add {
                background: #D4AF37;
                color: white;
                width: 40px;
                height: 40px;
                font-size: 18px;
            }
            .btn-add:hover {
                background: #B8941E;
            }
            .btn-primary {
                background: #D4AF37;
                color: white;
                flex: 1;
            }
            .btn-primary:hover {
                background: #B8941E;
            }
            .btn-secondary {
                background: #666;
                color: white;
                flex: 1;
            }
            .btn-secondary:hover {
                background: #2C2C2C;
            }

            .service-item {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 12px;
                background: #F4E5C3;
                border-radius: 4px;
                margin-bottom: 10px;
            }
            .service-name {
                flex: 1;
                font-weight: 600;
            }
            .service-price {
                font-size: 13px;
                color: #666;
            }
            .service-controls {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .service-controls input[type="number"] {
                width: 60px;
                padding: 5px;
            }
            .service-controls input[type="date"] {
                padding: 5px;
                font-size: 13px;
            }
            .btn-remove {
                background: #e74c3c;
                color: white;
                border: none;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                cursor: pointer;
            }
            .btn-remove:hover {
                background: #c0392b;
            }
            .no-services {
                text-align: center;
                padding: 20px;
                color: #666;
                font-size: 14px;
            }

            .summary {
                background: #FEFDFB;
                padding: 15px;
                border-radius: 4px;
                margin-top: 20px;
                border: 1px solid #E8DCC4;
            }
            .summary-row {
                display: flex;
                justify-content: space-between;
                padding: 8px 0;
                font-size: 14px;
            }
            .summary-row.total {
                border-top: 2px solid #D4AF37;
                margin-top: 10px;
                padding-top: 10px;
                font-size: 18px;
                font-weight: bold;
                color: #D4AF37;
            }
            .summary-row.deposit {
                background: #F4E5C3;
                margin-top: 15px;
                padding: 12px;
                border-radius: 4px;
                font-weight: 600;
                border: 2px solid #D4AF37;
            }

            .button-group {
                display: flex;
                gap: 15px;
                margin-top: 30px;
            }

            .payment-option {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 10px;
            }
            .payment-option input[type="radio"] {
                width: 18px;
                height: 18px;
            }
            .payment-option label {
                margin: 0;
                cursor: pointer;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .payment-option i {
                font-size: 18px;
                color: #D4AF37;
                width: 20px;
            }

            @media (max-width: 768px) {
                .form-row {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <div class="header">
            <div class="header-content">
                <div class="logo">HOTEL MASURA</div>
                <div class="user-info">Guest: <%= guest != null ? guest.getFullName() : "Guest"%></div>
            </div>
        </div>

        <div class="container">
            <form action="<%= IConstant.ACTION_BOOKING%>" method="POST" id="bookingForm">
                <input type="hidden" name="roomId" value="<%= room.getRoomId()%>">
                <input type="hidden" name="checkInDate" value="<%= checkIn%>">
                <input type="hidden" name="checkOutDate" value="<%= checkOut%>">
                <input type="hidden" name="totalAmount" id="totalAmount" value="<%= roomType.getPricePerNight()%>">
                <input type="hidden" name="depositAmount" id="depositAmount" value="0">

                <!-- GUEST INFO -->
                <div class="card">
                    <div class="card-title">Guest Information</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Full Name</label>
                            <input type="text" value="<%= guest.getFullName()%>" disabled>
                        </div>
                        <div class="form-group">
                            <label>Phone Number</label>
                            <input type="text" value="<%= guest.getPhone()%>" disabled>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" value="<%= guest.getEmail()%>" disabled>
                        </div>
                        <div class="form-group">
                            <label>ID Number</label>
                            <input type="text" value="<%= guest.getIdNumber()%>" disabled>
                        </div>
                    </div>
                </div>

                <!-- ROOM INFO -->
                <div class="card">
                    <div class="card-title">Room Information</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Room Number</label>
                            <input type="text" value="<%= room.getRoomNumber()%>" disabled>
                        </div>
                        <div class="form-group">
                            <label>Room Type</label>
                            <input type="text" value="<%= roomType.getTypeName()%>" disabled>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Check-in Date</label>
                            <input type="date" value="<%= checkIn%>" disabled>
                        </div>
                        <div class="form-group">
                            <label>Check-out Date</label>
                            <input type="date" value="<%= checkOut%>" disabled>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Capacity</label>
                            <input type="text" value="<%= roomType.getCapacity()%> guests" disabled>
                        </div>
                        <div class="form-group">
                            <label>Price per Night</label>
                            <input type="text" value="<%= currencyFormat.format(roomType.getPricePerNight())%> VND" disabled>
                        </div>
                    </div>
                </div>

                <!-- SERVICES -->
                <div class="card">
                    <div class="card-title">Additional Services</div>
                    <div class="form-group full">
                        <label>Select Service</label>
                        <div class="service-selector">
                            <select id="service-select">
                                <option value="">-- Choose Service --</option>
                                <% if (allServices != null) {
                                        for (Service service : allServices) {%>
                                <option value="<%= service.getServiceId()%>" 
                                        data-price="<%= service.getPrice()%>" 
                                        data-name="<%= service.getServiceName()%>">
                                    <%= service.getServiceName()%> (+<%= currencyFormat.format(service.getPrice())%> VND)
                                </option>
                                <%  }
                                    }%>
                            </select>
                            <button type="button" id="add-service-btn" class="btn btn-add">+</button>
                        </div>
                    </div>
                    <div id="selected-services-list"></div>
                    <div id="no-services-msg" class="no-services">No services added</div>
                </div>

                <!-- PAYMENT -->
                <div class="card">
                    <div class="card-title">Payment Method</div>
                    <div class="payment-option">
                        <input type="radio" id="payment1" name="paymentMethod" value="Credit Card" required>
                        <label for="payment1"><i class="fas fa-credit-card"></i> Credit Card</label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="payment2" name="paymentMethod" value="Debit Card" required>
                        <label for="payment2"><i class="far fa-credit-card"></i> Debit Card</label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="payment3" name="paymentMethod" value="Online" required>
                        <label for="payment3"><i class="fas fa-calculator"></i> Online </label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="payment3" name="paymentMethod" value="Bank Transfer" required>
                        <label for="payment3"><i class="fas fa-university"></i> Bank Transfer</label>
                    </div>
                </div>

                <!-- SUMMARY -->
                <div class="card">
                    <div class="card-title">Booking Summary</div>
                    <div class="summary">
                        <div class="summary-row">
                            <span>Room (<span id="summary-nights">1</span> night<span id="nights-plural">s</span>)</span>
                            <span id="summary-room-total"><%= currencyFormat.format(roomType.getPricePerNight())%> VND</span>
                        </div>
                        <div id="summary-services"></div>
                        <div class="summary-row" style="border-top: 1px solid #E8DCC4; margin-top: 10px; padding-top: 10px;">
                            <span>Subtotal</span>
                            <span id="summary-subtotal"><%= currencyFormat.format(roomType.getPricePerNight())%> VND</span>
                        </div>
                        <div class="summary-row">
                            <span>Tax (<%= String.format("%.1f", taxRate)%>%)</span>
                            <span id="summary-tax">0 VND</span>
                        </div>
                        <div class="summary-row total">
                            <span>Total Amount</span>
                            <span id="display-total"><%= currencyFormat.format(roomType.getPricePerNight())%> VND</span>
                        </div>
                        <div class="summary-row deposit">
                            <span>Deposit Required (<%= String.format("%.1f", depositRate)%>%)</span>
                            <span id="display-deposit">0 VND</span>
                        </div>
                    </div>
                </div>

                <div class="button-group">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Back</button>
                    <button type="submit" class="btn btn-primary">Confirm Booking</button>
                </div>
            </form>
        </div>

        <script>
            // ===============================================
            // =========== INITIALIZE CONSTANTS ==============
            // ===============================================
            const pricePerNight = <%= roomType.getPricePerNight()%>;
            const checkInDate = '<%= checkIn%>';
            const checkOutDate = '<%= checkOut%>';
            const taxRate = <%= taxRate%>;
            const depositRate = <%= depositRate%>;

            // ===============================================
            // =========== DOM ELEMENT REFERENCES ============
            // ===============================================
            const els = {
                select: document.getElementById('service-select'),
                addBtn: document.getElementById('add-service-btn'),
                list: document.getElementById('selected-services-list'),
                noMsg: document.getElementById('no-services-msg'),
                total: document.getElementById('display-total'),
                deposit: document.getElementById('display-deposit'),
                totalInput: document.getElementById('totalAmount'),
                depositInput: document.getElementById('depositAmount')
            };

            // ===============================================
            // =========== UTILITY FUNCTIONS =================
            // ===============================================
            const fmt = amount => new Intl.NumberFormat('vi-VN').format(amount);

            // ===============================================
            // =========== ADD SERVICE FUNCTION ==============
            // ===============================================
            function addService() {
                // Get selected option
                const opt = els.select.options[els.select.selectedIndex];
                if (!opt || !opt.value)
                    return alert('Please select a service');

                const id = opt.value;
                const name = opt.dataset.name;
                const price = +opt.dataset.price;

                // Check duplicate for same date
                if ([...document.querySelectorAll(`[data-service-id="\${id}"] .service-date`)]
                        .some(d => d.value === checkInDate)) {
                    return alert(`Service "\${name}" already added for \${checkInDate}`);
                }

                // Create service item element
                const item = document.createElement('div');
                item.className = 'service-item';
                item.dataset.serviceId = id;
                item.dataset.servicePrice = price;
                item.innerHTML = `
                <div class="service-name">\${name}</div>
                <div class="service-price">\${fmt(price)} VND</div>
                <div class="service-controls">
                    <input type="number" class="qty" min="1" value="1">
                    <input type="date" class="svc-date" value="\${checkInDate}" min="\${checkInDate}" max="\${checkOutDate}">
                    <input type="hidden" name="serviceId" value="\${id}">
                    <input type="hidden" name="serviceQuantity" class="h-qty" value="1">
                    <input type="hidden" name="serviceDate" class="h-date" value="\${checkInDate}">
                    <button type="button" class="btn-remove">×</button>
                </div>`;

                // Add to list
                els.list.appendChild(item);
                els.noMsg.style.display = 'none';

                // Get input references
                const qty = item.querySelector('.qty');
                const date = item.querySelector('.svc-date');
                const hQty = item.querySelector('.h-qty');
                const hDate = item.querySelector('.h-date');

                // ===============================================
                // =========== QUANTITY CHANGE HANDLER ===========
                // ===============================================
                qty.oninput = () => {
                    let v = +qty.value || 1;
                    if (v < 1)
                        v = 1;
                    qty.value = hQty.value = v;
                    calc();
                };

                // ===============================================
                // =========== DATE CHANGE HANDLER ===============
                // ===============================================
                date.onchange = () => {
                    const newDate = date.value;

                    // Check for duplicate date
                    if ([...document.querySelectorAll(`[data-service-id="\${id}"]`)]
                            .filter(i => i !== item)
                            .some(i => i.querySelector('.svc-date').value === newDate)) {
                        alert(`Service "${name}" already added for \${newDate}`);
                        date.value = hDate.value;
                        return;
                    }
                    hDate.value = newDate;
                };

                // ===============================================
                // =========== REMOVE BUTTON HANDLER =============
                // ===============================================
                item.querySelector('.btn-remove').onclick = () => {
                    item.remove();
                    if (!els.list.children.length)
                        els.noMsg.style.display = 'block';
                    calc();
                };

                // Reset select and recalculate
                els.select.selectedIndex = 0;
                calc();
            }

            // ===============================================
            // =========== CALCULATE TOTAL FUNCTION ==========
            // ===============================================
            function calc() {
                // Calculate number of nights
                const nights = Math.max(1, Math.ceil((new Date(checkOutDate) - new Date(checkInDate)) / 86400000));
                const roomTotal = nights * pricePerNight;

                // Update room summary
                document.getElementById('summary-nights').textContent = nights;
                document.getElementById('nights-plural').textContent = nights > 1 ? 's' : '';
                document.getElementById('summary-room-total').textContent = fmt(roomTotal) + ' VND';

                // Calculate services total
                let svcTotal = 0;
                const svcDiv = document.getElementById('summary-services');
                svcDiv.innerHTML = '';

                els.list.querySelectorAll('.service-item').forEach(item => {
                    const price = +item.dataset.servicePrice;
                    const qty = +item.querySelector('.qty').value || 1;
                    const name = item.querySelector('.service-name').textContent;
                    const sub = price * qty;

                    svcTotal += sub;
                    svcDiv.innerHTML += `
                    <div class="summary-row">
                        <span>\${name} (x\${qty})</span>
                        <span>\${fmt(sub)} VND</span>
                    </div>`;
                });

                // Calculate final amounts
                const subtotal = roomTotal + svcTotal;
                const tax = Math.round(subtotal * taxRate / 100);
                const total = subtotal + tax;
                const dep = Math.round(total * depositRate / 100);

                // Update display
                document.getElementById('summary-subtotal').textContent = fmt(subtotal) + ' VND';
                document.getElementById('summary-tax').textContent = fmt(tax) + ' VND';
                els.total.textContent = fmt(total) + ' VND';
                els.deposit.textContent = fmt(dep) + ' VND';
                els.totalInput.value = total;
                els.depositInput.value = dep;
            }

            // ===============================================
            // =========== INITIALIZE EVENT LISTENERS ========
            // ===============================================
            els.addBtn.onclick = addService;
            calc();
        </script>
    </body>
</html>