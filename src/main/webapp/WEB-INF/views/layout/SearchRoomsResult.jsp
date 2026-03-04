<%-- 
    Document   : SearchRoomsResult
    Created on : Oct 19, 2025, 9:42:50 AM
    Author     : TR_NGHIA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="dto.Room" %>
<%@ page import="dto.Guest" %>
<%@ page import="util.IConstant" %>

<%
    List<Map<String, Object>> rooms = (List<Map<String, Object>>) request.getAttribute("PROCESSED_ROOMS");
    Integer count = (Integer) request.getAttribute("ROOM_COUNT");
    String checkIn = (String) request.getAttribute("CHECK_IN");
    String checkOut = (String) request.getAttribute("CHECK_OUT");
    Integer guests = (Integer) request.getAttribute("GUESTS");
    String roomType = (String) request.getAttribute("SELECTED_ROOM_TYPE_NAME");
    String error = (String) request.getAttribute("ERROR_MESSAGE");
    boolean isLoggedIn = session.getAttribute("USER_GUEST") != null;

    int roomCount = (count != null) ? count : 0;
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Available Rooms - Misuka Hotel</title>

        <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        
        <style>
            body {
                font-family: 'Inter', sans-serif;
                background: #f8f9fa;
                color: #333;
            }

            h1, h2, h3, h4, h5, h6, .price, .banner h1 {
                font-family: 'Playfair Display', serif;
            }

            .banner {
                background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                    url("${pageContext.request.contextPath}/public/img/banner2.jpg");
                background-size: cover;
                background-position: center;
                padding: 150px 0;
                color: white;
                text-align: center;
            }
            
            .banner h1 {
                font-weight: 700;
            }

            .search-info {
                background: rgba(255,255,255,0.1);
                padding: 15px;
                border-radius: 8px;
                display: inline-block;
                margin-top: 15px;
                backdrop-filter: blur(5px);
            }

            .search-info span {
                margin: 0 15px;
                font-size: 15px;
            }

            .room-card {
                background: #ffffff;
                border-radius: 8px;
                overflow: hidden;
                transition: transform 0.3s, box-shadow 0.3s;
                border: 1px solid #eee;
            }

            .room-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            }

            .room-img {
                height: 280px;
                object-fit: cover;
                width: 100%;
            }

            .room-body {
                padding: 25px;
            }
            
            .room-body h5 {
                font-weight: 600;
                font-size: 1.5rem;
            }

            .price {
                font-size: 26px;
                font-weight: 700;
                color: #B8860B;
                margin: 10px 0;
            }

            .btn-book {
                background: #B8860B;
                border: none;
                color: #fff;
                font-weight: 600;
                padding: 12px 20px;
                border-radius: 5px;
                width: 100%;
                transition: background-color 0.3s;
                font-family: 'Inter', sans-serif;
            }

            .btn-book:hover {
                background: #9c710a;
                color: #fff;
            }
            
            .no-rooms-box {
                background: #ffffff;
                border-radius: 8px;
                padding: 4rem 2rem;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            }
        </style>
    </head>
    <body>
        <%@ include file="../shared/header.jsp" %>
        <%@ include file="../features/login/navbar.jsp" %>

        <div class="banner">
            <h1>Available Rooms</h1>
            <div class="search-info">
                <span><i class="bi bi-calendar-check"></i> <%= checkIn%> - <%= checkOut%></span>
                <span><i class="bi bi-people"></i> <%= guests%> guests</span>
                <span><i class="bi bi-door-open"></i> <%= roomType%></span>
            </div>
        </div>

        <div class="container my-5">
            <% if (error != null) {%>
            <div class="alert alert-danger"><%= error%></div>
            <% }%>

            <div class="d-flex justify-content-between align-items-center mb-4 bg-white p-3 rounded shadow-sm">
                <span class="fs-5"><strong>Found <%= roomCount%> rooms</strong></span>
                <a href="<%= IConstant.ACTION_HOME%>" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left"></i> Back
                </a>
            </div>

            <% if (rooms != null && !rooms.isEmpty()) { %>
            <div class="row g-4">
                <% for (Map<String, Object> r : rooms) {
                        Room room = (Room) r.get("room");
                        String type = (String) r.get("typeName");
                        Integer cap = (Integer) r.get("capacity");
                        String price = (String) r.get("priceFormatted");
                        String img = (String) r.get("imageUrl");
                        String fallback = (String) r.get("fallbackImageUrl");
                %>
                <div class="col-md-6 col-lg-4">
                    <div class="room-card">
                        <img src="<%= img%>" class="room-img" alt="Room <%= room.getRoomNumber()%>"
                             onerror="this.src='<%= fallback%>'">
                        <div class="room-body">
                            <h5>Room <%= room.getRoomNumber()%></h5>
                            <small class="text-muted"><%= type%> • Max <%= cap%> guests</small>
                            <div class="price"><%= price%>/night</div>

                            <% if (isLoggedIn) {%>
                            <form action="<%= IConstant.ACTION_PRE_BOOKING%>" method="post">
                                <input type="hidden" name="roomId" value="<%= room.getRoomId()%>">
                                <input type="hidden" name="checkInDate" value="<%= checkIn%>">
                                <input type="hidden" name="checkOutDate" value="<%= checkOut%>">
                                <button type="submit" class="btn-book">Select Room</button>
                            </form>
                            <% } else { %>
                            <button class="btn-book" data-bs-toggle="modal" data-bs-target="#loginModal">
                                Login to Book
                            </button>
                            <% } %>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% } else {%>
            <div class="text-center no-rooms-box">
                <i class="bi bi-search display-3 text-muted mb-3"></i>
                <h5>No Matching Rooms Found</h5>
                <p class="text-muted">Please try again with different criteria</p>
                <a href="<%= IConstant.ACTION_HOME%>" class="btn btn-book" style="width: auto;">Search Again</a>
            </div>
            <% }%>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <%@ include file="../shared/footer.jsp" %>
    </body>
</html>