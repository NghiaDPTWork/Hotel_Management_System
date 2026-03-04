<%--
    Document    : bookingForm 
    Created on : Oct 12, 2025, 2:00:00 PM
    Author      : TR_NGHIA
--%>

<%@page import="dto.RoomType"%>
<%@page import="java.util.List"%>
<%@page import="util.IConstant"%>

<section class="booking-section">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-11">
                <div class="card shadow-lg booking-card">
                    <div class="card-body p-4">
                        
                        <form id="bookingForm" action="<%= IConstant.ACTION_SEARCH_ROOMS %>" method="post">
                            <div class="row g-3">

                                <!-- Arrival Date -->
                                <div class="col-lg-4 col-md-6">
                                    <label for="arrivalDate" class="form-label text-muted small">Arrival Date</label>
                                    <input type="date" 
                                           class="form-control" 
                                           id="arrivalDate" 
                                           name="arrivalDate"
                                           value="<%= request.getAttribute("arrivalDate") != null ? request.getAttribute("arrivalDate") : ""%>"
                                           required>
                                    <div class="invalid-feedback">
                                        Please select an arrival date.
                                    </div>
                                </div>

                                <!-- Departure Date -->
                                <div class="col-lg-4 col-md-6">
                                    <label for="departureDate" class="form-label text-muted small">Departure Date</label>
                                    <input type="date" 
                                           class="form-control" 
                                           id="departureDate" 
                                           name="departureDate"
                                           value="<%= request.getAttribute("departureDate") != null ? request.getAttribute("departureDate") : ""%>"
                                           required>
                                    <div class="invalid-feedback">
                                        Please select a departure date.
                                    </div>
                                </div>

                                <!-- Room Type - CH? HI?N TH? TÊN -->
                                <div class="col-lg-4 col-md-12">
                                    <label for="roomTypeId" class="form-label text-muted small">Room Type</label>
                                    <select class="form-select" id="roomTypeId" name="roomTypeId">
                                        <option value="">All Room Types</option>
                                        <%
                                            List<RoomType> roomTypeList = (List<RoomType>) request.getAttribute("ROOM_TYPE_LIST");
                                            String selectedRoomType = (String) request.getParameter("roomType");

                                            if (roomTypeList != null && !roomTypeList.isEmpty()) {
                                                for (RoomType roomType : roomTypeList) {
                                                    int roomTypeId = roomType.getRoomTypeId();
                                                    String typeName = roomType.getTypeName() != null ? roomType.getTypeName() : "Unknown Type";
                                                    boolean isSelected = selectedRoomType != null && selectedRoomType.equals(String.valueOf(roomTypeId));
                                        %>
                                        <option value="<%= roomTypeId%>" <%= isSelected ? "selected" : ""%>>
                                            <%= typeName%>
                                        </option>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <option value="" disabled>No room types available</option>
                                        <%
                                            }
                                        %>
                                    </select>
                                    <div class="invalid-feedback">
                                        Please select a room type.
                                    </div>
                                </div>

                                <!-- Adults -->
                                <div class="col-md-6">
                                    <label for="adults" class="form-label text-muted small">Adults</label>
                                    <input type="number" 
                                           class="form-control" 
                                           id="adults" 
                                           name="adults" 
                                           value="<%= request.getAttribute("adults") != null ? request.getAttribute("adults") : "2"%>" 
                                           min="1"
                                           required>
                                    <div class="invalid-feedback">
                                        The number of adults must be at least 1.
                                    </div>
                                </div>

                                <!-- Children -->
                                <div class="col-md-6">
                                    <label for="children" class="form-label text-muted small">Children</label>
                                    <input type="number" 
                                           class="form-control" 
                                           id="children" 
                                           name="children" 
                                           value="<%= request.getAttribute("children") != null ? request.getAttribute("children") : "0"%>" 
                                           min="0">
                                </div>

                                <!-- Submit Button -->
                                <div class="col-12">
                                    <%
                                        if (session.getAttribute("USER_GUEST") == null) {
                                    %>
                                    <button type="button" 
                                            class="btn btn-warning w-100 py-2 mt-2" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#loginModal">
                                        Check Availability
                                    </button>
                                    <%
                                    } else {
                                    %>
                                    <input type="hidden" name="action" value="<%= IConstant.ACTION_SEARCH_ROOMS %>">
                                    <button type="submit" class="btn btn-warning w-100 py-2 mt-2">
                                        Check Availability
                                    </button>
                                    <%
                                        }
                                    %>
                                </div>
                            </div>
                        </form>
                                
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Room Detail Modal -->
    <div class="modal fade" id="roomDetailModal" tabindex="-1" aria-labelledby="roomDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalRoomTitle">Room Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <img id="modalRoomImage" src="" class="img-fluid rounded" alt="Room Image">
                        </div>
                        <div class="col-md-6">
                            <p id="modalRoomDescription" class="text-muted"></p>
                            <h5>Amenities</h5>
                            <ul id="modalRoomAmenities" class="room-amenities-list">
                                <li><i class="bi bi-wifi"></i> Free Wi-Fi</li>
                                <li><i class="bi bi-tv"></i> Smart TV</li>
                                <li><i class="bi bi-cup-hot"></i> Coffee Maker</li>
                            </ul>
                            <div class="mt-3">
                                <p id="modalRoomPrice" class="room-price mb-0 fs-4"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" id="bookNowBtn" class="btn btn-warning">Book This Room</button>
                </div>
            </div>
        </div>
    </div>

</section>

<script>
    /*========== BOOKING FORM DATE LOGIC ==========*/
    const arrivalDateInput = document.getElementById('arrivalDate');
    const departureDateInput = document.getElementById('departureDate');
    if (arrivalDateInput && departureDateInput) {
        const today = new Date().toISOString().split('T')[0];
        arrivalDateInput.setAttribute('min', today);
        arrivalDateInput.addEventListener('change', function () {
            if (this.value) {
                const arrivalDate = new Date(this.value);
                arrivalDate.setDate(arrivalDate.getDate() + 1);
                const nextDay = arrivalDate.toISOString().split('T')[0];
                departureDateInput.setAttribute('min', nextDay);
                if (departureDateInput.value && departureDateInput.value < nextDay) {
                    departureDateInput.value = '';
                }
            }
        });
    }

    /*========== CHECK VALID FIELD IN FORM ==========*/
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', function (event) {
            event.preventDefault();
            let isFormValid = true;

            const arrivalDate = document.getElementById('arrivalDate');
            const departureDate = document.getElementById('departureDate');
            const roomType = document.getElementById('roomTypeId');
            const adults = document.getElementById('adults');

            const fields = [arrivalDate, departureDate, roomType, adults];
            fields.forEach(field => {
                if (field)
                    field.classList.remove('is-invalid')
            });

            if (!arrivalDate || arrivalDate.value === '') {
                arrivalDate.classList.add('is-invalid');
                isFormValid = false;
            }
            if (!departureDate || departureDate.value === '') {
                departureDate.classList.add('is-invalid');
                isFormValid = false;
            }
            if (!roomType || roomType.value === '') {
                roomType.classList.add('is-invalid');
                isFormValid = false;
            }
            if (!adults || adults.value < 1) {
                adults.classList.add('is-invalid');
                isFormValid = false;
            }

            if (isFormValid) {
                this.submit();
            }
        });
    }
</script>