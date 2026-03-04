<%-- 
    Document   : EditBookingResult
    Created on : Oct 30, 2025, 3:21:48 PM
    Author     : TR_NGHIA
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.IConstant" %>

<%
    // 1. Đọc thông báo từ Requesst (do EditBookingController gửi)
    String successMessage = (String) request.getAttribute("SUCCESS_MESSAGE");
    String errorMessage = (String) request.getAttribute("ERROR_MESSAGE");

    boolean isSuccess = (successMessage != null && !successMessage.isEmpty());
    String message = isSuccess ? successMessage : errorMessage;

    // 3. Lấy bookingId từ URL (do EditBookingController gửi)
    String bookingId = request.getParameter("bookingId");
    if (bookingId == null) bookingId = "";
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Result</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
        <%-- Lấy style y hệt trang BookingResult.jsp --%>
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
                color: #28a745; /* Màu xanh thành công */
                margin: 10px 0;
            }
            .status.error h2 {
                color: #dc3545; /* Màu đỏ thất bại */
            }
            .btn-group {
                text-align: center;
                margin-top: 20px;
            }
            .btn {
                display: inline-block;
                margin: 10px 5px 0 5px;
                padding: 10px 25px;
                background: #D4AF37;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                text-align: center;
                font-weight: 500;
            }
            .btn:hover {
                background: #B8941E;
            }
            .btn-secondary {
                background: #6c757d;
            }
            .btn-secondary:hover {
                background: #5a6268;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <%-- Phần hiển thị thông báo --%>
            <div class="status <%= isSuccess ? "" : "error"%>">
                <h2><%= isSuccess ? "✓ Update Successful" : "✗ Update Failed"%></h2>
                <p>
                    <%= (message != null) ? message : (isSuccess ? "Your changes have been saved." : "An unknown error occurred. Please try again.") %>
                </p>
            </div>

            <%-- Nút bấm quay về --%>
            <div class="btn-group">
                <a href="viewBooking?bookingId=<%= bookingId %>" class="btn">
                    <i class="fa fa-arrow-left"></i> Back to Booking
                </a>
                <a href="HomeController" class="btn btn-secondary">
                    <i class="fa fa-home"></i> Back to Home
                </a>
            </div>
        </div>
    </body>
</html>