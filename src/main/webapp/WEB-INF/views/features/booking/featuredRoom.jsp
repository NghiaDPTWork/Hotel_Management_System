<%--
    Document   : featuredRoom
    Created on : Oct 5, 2025, 3:45:10 PM
    Author     : TR_NGHIA
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.Room" %>
<%@ page import="dto.RoomType" %>
<%@ page import="dto.Guest" %>
<%@ page import="util.IConstant" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%!
    public RoomType getRoomTypeDetails(int roomTypeId, List<RoomType> allRoomTypes) {
        if (allRoomTypes == null) {
            return null;
        }
        for (RoomType rt : allRoomTypes) {
            if (rt.getRoomTypeId() == roomTypeId) {
                return rt;
            }
        }
        return null;
    }

    public String getRoomTypeName(int roomTypeId, List<RoomType> allRoomTypes) {
        RoomType rt = getRoomTypeDetails(roomTypeId, allRoomTypes);
        return (rt != null) ? rt.getTypeName() : "Unknown";
    }
%>

<%
    List<Room> roomList = (List<Room>) request.getAttribute("ROOM_LIST");
    List<RoomType> roomTypeLis = (List<RoomType>) request.getAttribute("ROOM_TYPE_LIST");
    String errorMessage = (String) request.getAttribute("errorMessage");
    int delay = 0;
    int count = 0;
    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
    formatter.setMaximumFractionDigits(0);
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Featured Rooms - Luxury Hotel</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --font-heading: 'Playfair Display', serif;
                --font-body: 'Lato', sans-serif;
                --color-gold: #ffc107;
                --color-charcoal: #1a1a1a;
                --color-offwhite: #f8f7f5;
                --color-light-grey: #f8f9fa;
            }
            body {
                font-family: var(--font-body);
                background-color: var(--color-light-grey);
                color: var(--color-charcoal);
            }
            h1, h2, h3, h4, h5, h6, .section-title {
                font-family: var(--font-heading);
            }
            .section-padding {
                padding: 4rem 0;
            }
            .section-title {
                margin-bottom: 0.5rem;
                font-weight: 700;
            }
            .room-card {
                transition: all 0.3s ease;
                border: none;
            }
            .card-hover:hover {
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
                transform: translateY(-5px);
            }
            .room-image {
                height: 250px;
                object-fit: cover;
                border-top-left-radius: 0.375rem;
                border-top-right-radius: 0.375rem;
            }

            .room-price {
                font-family: var(--font-heading);
                font-size: 1.5rem;
                font-weight: bold;
                color: var(--color-gold);
            }
            .btn-warning {
                font-weight: bold;
                padding: 0.5rem 1rem;
                font-size: 0.9rem;
            }
            .scroll-animate {
                opacity: 0;
                transform: translateY(30px);
                transition: opacity 0.6s ease-out, transform 0.6s ease-out;
            }
            .scroll-animate.animated {
                opacity: 1;
                transform: translateY(0);
            }
        </style>
    </head>
    <body>
        <section id="rooms" class="section-padding bg-light">
            <div class="container">
                <div class="d-flex justify-content-between align-items-center mb-5 scroll-animate">
                    <div>
                        <h2 class="section-title">Featured Rooms</h2>
                        <p class="text-muted">Discover rooms designed for your comfort and delight.</p>
                    </div>
                    <a href="#booking" class="btn btn-outline-dark">View All</a>
                </div>
                <div class="row g-4 gy-5">
                    <%
                        if (roomList != null && !roomList.isEmpty()) {
                            for (Room room : roomList) {
                                if (count >= 6) {
                                    break;
                                }

                                int roomId = room.getRoomId();
                                String roomNumber = room.getRoomNumber();
                                RoomType currentRoomType = getRoomTypeDetails(room.getRoomTypeId(), roomTypeLis);
                                String typeName = (currentRoomType != null) ? currentRoomType.getTypeName() : "Standard";
                                String price = (currentRoomType != null) ? String.format("%,.2f", currentRoomType.getPricePerNight()) : "N/A";

                                int capacity = (currentRoomType != null) ? currentRoomType.getCapacity() : 2;
                    %>
                    <div class="col-lg-4 col-md-6 scroll-animate" style="animation-delay: <%= delay * 0.2%>s;">
                        <div class="card card-hover h-100 room-card">
                            <img src="${pageContext.request.contextPath}/public/img/room-<%= count + 1 %>.jpg"
                                 class="card-img-top room-image"
                                 alt="<%= typeName%>">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title"><%= typeName%> - Room <%= roomNumber%></h5>
                                <p class="card-text text-muted small">
                                    <strong>Experience luxury and comfort</strong> in our <strong><%= typeName%></strong>.
                                    Perfectly designed for <strong><%= capacity%></strong> guests with modern amenities,
                                    elegant furnishings, and premium services. Your comfort is our priority.
                                </p>

                                <div class="d-flex flex-wrap gap-3 mb-3 small text-muted">
                                    <span><i class="fas fa-users text-warning me-1"></i> Max <%= capacity%> guests</span>
                                    <span><i class="fas fa-wifi text-warning me-1"></i> Free Wi-Fi</span>
                                    <span><i class="fas fa-tv text-warning me-1"></i> Smart TV</span>
                                    <span><i class="fas fa-coffee text-warning me-1"></i> Coffee Maker</span>
                                </div>

                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <p class="room-price mb-0">$<%= price %> <span class="fs-6 text-muted fw-normal">/ night</span></p>
                                    <a href="#booking" class="btn btn-warning btn-sm">View Detail</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
                            delay++;
                            count++;
                        }
                    } else {
                    %>
                    <div class="col-12 text-center">
                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle me-2"></i>
                            No rooms available at the moment. Please check back later.
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </section>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const observer = new IntersectionObserver((entries) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.classList.add('animated');
                            observer.unobserve(entry.target);
                        }
                    });
                }, {threshold: 0.1});
                document.querySelectorAll('.scroll-animate').forEach((el) => {
                    observer.observe(el);
                });
            });
        </script>
    </body>
</html>