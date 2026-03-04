<%--
    Document   : AdminDashboard
    Created on : Oct 19, 2025, 9:42:50 AM
    Author     : TR_NGHIA
--%>

<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .navbar-custom {
                background-color: #1a66c9;
            }
            .navbar-custom .navbar-nav .nav-link {
                color: #ffffffb3;
                padding-left: 1rem;
                padding-right: 1rem;
                font-size: 0.9rem;
            }
            .navbar-custom .navbar-nav .nav-link.active,
            .navbar-custom .navbar-nav .nav-link:hover {
                color: #ffffff;
                background-color: #1256ad;
            }
            .navbar-custom .nav-link,
            .navbar-custom .dropdown-item {
                font-size: 0.9rem;
            }
            .navbar-custom .top-nav-link {
                color: #ffffff;
                font-size: 0.85rem;
            }
            .navbar-custom .top-nav-link:hover {
                color: #f0f0f0;
            }
            .card {
                border: none;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                border-radius: 8px;
                margin-bottom: 1.5rem;
                height: calc(100% - 1.5rem);
            }
            .card-header {
                background-color: #fff;
                border-bottom: 1px solid #f0f0f0;
                font-weight: 600;
                color: #333;
                font-size: 0.9rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                padding-top: 0.75rem;
                padding-bottom: 0.75rem;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .card-header-menu {
                font-size: 0.8rem;
                font-weight: normal;
                color: #0d6efd;
                text-decoration: none;
            }
            .card-header-menu:hover {
                text-decoration: underline;
            }
            .stat-number {
                font-size: 1.75rem;
                font-weight: 700;
                color: #1a66c9;
            }
            .stat-label {
                font-size: 0.9rem;
                color: #6c757d;
            }
            .stat-icon {
                font-size: 2.5rem;
                color: #ffc107;
            }
            .activity-feed .list-group-item {
                border: none;
                padding: 0.75rem 0.25rem;
                border-bottom: 1px solid #f0f0f0;
            }
            .activity-feed .list-group-item:last-child {
                border-bottom: none;
            }
            .activity-feed .activity-icon {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                background-color: #e9f5ff;
                color: #0d6efd;
                font-size: 0.9rem;
                margin-right: 10px;
            }
            .activity-feed .activity-text {
                font-size: 0.85rem;
                color: #555;
                line-height: 1.4;
            }
            .activity-feed .activity-time {
                font-size: 0.75rem;
                color: #999;
            }
            .chart-placeholder {
                display: flex;
                align-items: center;
                justify-content: center;
                color: #aaa;
                border-radius: 5px;
                position: relative;
            }
            .bar-chart-placeholder {
                min-height: 120px;
                width: 100%;
                display: flex;
                align-items: flex-end;
                justify-content: space-around;
                padding: 0 10px;
                border-bottom: 1px solid #ccc;
            }
            .bar-chart-placeholder .bar {
                width: 30%;
                background-color: #0d6efd;
            }
            .bar-chart-placeholder .bar.red {
                background-color: #dc3545;
            }
            .list-style-dot {
                list-style: none;
                padding-left: 0;
                font-size: 0.9rem;
            }
            .list-style-dot li {
                position: relative;
                padding-left: 1.5rem;
                margin-bottom: 0.5rem;
            }
            .list-style-dot li::before {
                content: '●';
                position: absolute;
                left: 0;
                top: 0;
                font-size: 1.2rem;
            }
            .list-style-dot li.dot-blue::before {
                color: #0d6efd;
            }
            .list-style-dot li.dot-red::before {
                color: #dc3545;
            }
            .fab-container {
                position: fixed;
                bottom: 20px;
                right: 20px;
                display: flex;
                flex-direction: column;
                gap: 10px;
                z-index: 1050;
            }
            .fab {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                background-color: #0d6efd;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.2rem;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                text-decoration: none;
                transition: background-color 0.3s;
            }
            .fab:hover {
                background-color: #0a58ca;
                color: white;
            }
            .fab-zalo {
                background-color: #0068ff;
            }
            .fab-zalo:hover {
                background-color: #005ae0;
            }
            .fab-text {
                font-size: 0.7rem;
                font-weight: bold;
                line-height: 1;
            }
            .fab-hotro {
                background-color: #1a66c9;
            }
            .fab-hotro:hover {
                background-color: #1256ad;
            }

            /* Style cho ảnh cố định của biểu đồ */
            .static-chart-img {
                width: 100%;
                height: 300px; /* Hoặc chiều cao bạn muốn */
                object-fit: cover;
                border-radius: 5px;
                background: #f9f9f9;
                border: 1px solid #eee;
            }
        </style>
    </head>
    <body>

        <%
            // ======================================================================
            // PHẦN 1: LẤY DỮ LIỆU TỪ SERVLET (VỚI TÊN VIẾT HOA)
            // ======================================================================

            // Bộ định dạng số
            NumberFormat vndFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            // Dữ liệu Lễ tân
            int checkInsToday = (request.getAttribute("CHECK_INS_TODAY") != null) ? (Integer) request.getAttribute("CHECK_INS_TODAY") : 0;
            int checkOutsToday = (request.getAttribute("CHECK_OUTS_TODAY") != null) ? (Integer) request.getAttribute("CHECK_OUTS_TODAY") : 0;
            int currentlyOccupied = (request.getAttribute("CURRENTLY_OCCUPIED") != null) ? (Integer) request.getAttribute("CURRENTLY_OCCUPIED") : 0;
            int overdueCheckouts = (request.getAttribute("OVERDUE_CHECKOUTS") != null) ? (Integer) request.getAttribute("OVERDUE_CHECKOUTS") : 0;

            // Dữ liệu Đặt phòng
            int newBookingsToday = (request.getAttribute("NEW_BOOKINGS_TODAY") != null) ? (Integer) request.getAttribute("NEW_BOOKINGS_TODAY") : 0;
            int canceledBookingsToday = (request.getAttribute("CANCELED_BOOKINGS_TODAY") != null) ? (Integer) request.getAttribute("CANCELED_BOOKINGS_TODAY") : 0;

            // Dữ liệu Doanh thu & Thu/Chi
            double todayRevenue = (request.getAttribute("TODAY_REVENUE") != null) ? (Double) request.getAttribute("TODAY_REVENUE") : 0.0;
            int todayInvoices = (request.getAttribute("TODAY_INVOICES") != null) ? (Integer) request.getAttribute("TODAY_INVOICES") : 0;
            double todayExpenses = (request.getAttribute("TODAY_EXPENSES") != null) ? (Double) request.getAttribute("TODAY_EXPENSES") : 0.0;
            double todayNetIncome = (request.getAttribute("TODAY_NET_INCOME") != null) ? (Double) request.getAttribute("TODAY_NET_INCOME") : 0.0;
            double averagePerRoom = (request.getAttribute("AVERAGE_PER_ROOM") != null) ? (Double) request.getAttribute("AVERAGE_PER_ROOM") : 0.0;

            // Định dạng tiền tệ
            String formattedRevenue = vndFormatter.format(todayRevenue);
            String formattedExpenses = vndFormatter.format(todayExpenses);
            String formattedNetIncome = vndFormatter.format(todayNetIncome);
            String formattedAverage = vndFormatter.format(averagePerRoom); 

            // Dữ liệu Công suất
            double occupancyRate = (request.getAttribute("OCCUPANCY_RATE") != null) ? (Double) request.getAttribute("OCCUPANCY_RATE") : 0.0;
            String formattedOccupancy = String.format("%.2f", occupancyRate);

            // Dữ liệu Staff (Từ Session)
            String contextPath = request.getContextPath();
            Staff staff = (Staff) session.getAttribute("USER_STAFF");
            boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));

            boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
            String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";


        %>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand" href="<%= IConstant.ACTION_HOME_FOR_ADMIN%>">MISUKA</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="mainNavbar">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" href="<%= IConstant.ACTION_ADMIN_HOME%>"><i class="bi bi-house-door-fill me-1"></i> Tổng quan</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_ROOMS%>"><i class="bi bi-grid-fill me-1"></i> Phòng</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_STAFFS%>"><i class="bi bi-people-fill me-1"></i> Nhân viên</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_SYSTEM%>"><i class="bi bi-gear-fill me-1"></i> Cấu hình hệ thống</a>
                        </li>
                    </ul>

                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#"><i class="bi bi-question-circle me-1"></i> Hỗ trợ</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#"><i class="bi bi-building me-1"></i> Chi nhánh trung tâm</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#"><i class="bi bi-bell-fill"></i></a>
                        </li>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="navbarDropdownUser" role="button" data-bs-toggle="dropdown" aria-expanded="false">

                                <i class="bi bi-person-circle" style="font-size: 1.5rem; color: white;"></i>
                                <span class="d-none d-lg-inline-block ms-1 text-white">
                                    <%= staffNameDisplay%>
                                </span>

                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownUser">
                                <li><a class="dropdown-item" href="#"><i class="bi bi-clock-history me-2"></i> Lịch sử hoạt động</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="<%= IConstant.ACTION_LOGOUT_FOR_ADMIN %>"><i class="bi bi-box-arrow-right me-2"></i> Đăng xuất</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container-fluid p-3 p-md-4">
            <div class="row">

                <div class="col-lg-9">
                    <div class="row">
                        <div class="col-md-6 col-lg-4">
                            <div class="card">
                                <div class="card-header">
                                    <span>DOANH THU HÔM NAY</span>
                                </div>
                                <div class="card-body">
                                    <div class="stat-number"><%= formattedRevenue%></div>
                                    <div class="stat-label mb-3">Tổng</div>
                                    <div class="d-flex justify-content-between text-center mt-4">
                                        <div>
                                            <div class="stat-label">Trung bình</div>
                                            <div class="fw-bold"><%= formattedAverage%> <span class="small text-muted">/Phòng</span></div>
                                        </div>
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-receipt stat-icon me-2"></i>
                                            <div>
                                                <div class="stat-label">Hóa đơn</div>
                                                <div class="fw-bold fs-4"><%= todayInvoices%></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-lg-4">
                            <div class="card">
                                <div class="card-header">
                                    <span>THU - CHI HÔM NAY</span>
                                </div>
                                <div class="card-body">
                                    <div class="stat-number"><%= formattedNetIncome%></div>
                                    <div class="d-flex mt-3">
                                        <div class="w-50">
                                            <ul class="list-style-dot">
                                                <li class="dot-blue">
                                                    Tổng thu <span class="fw-bold d-block"><%= formattedRevenue%></span>
                                                </li>
                                                <li class="dot-red">
                                                    Tổng chi <span class="fw-bold d-block"><%= formattedExpenses%></span>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="w-50 ps-2">
                                            <div class="chart-placeholder bar-chart-placeholder">
                                                <div class="bar" style="height: 100%;"></div>
                                                <div class="bar red" style="height: <%= (todayRevenue > 0) ? (todayExpenses / todayRevenue * 100) : 0%>%;"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12 col-lg-4">
                            <div class="card">
                                <div class="card-header">
                                    <span>HOẠT ĐỘNG LỄ TÂN HÔM NAY</span>
                                </div>
                                <div class="card-body text-center d-flex justify-content-around flex-wrap p-3">
                                    <div class="p-2">
                                        <div class="stat-label">Đã nhận</div>
                                        <div class="fw-bold fs-5"><%= checkInsToday%> <span class="text-muted small">Phòng</span></div>
                                    </div>
                                    <div class="p-2">
                                        <div class="stat-label">Đã trả</div>
                                        <div class="fw-bold fs-5"><%= checkOutsToday%> <span class="text-muted small">Phòng</span></div>
                                    </div>
                                    <div class="p-2">
                                        <div class="stat-label">Có khách</div>
                                        <div class="fw-bold fs-5"><%= currentlyOccupied%> <span class="text-muted small">Phòng</span></div>
                                    </div>
                                    <div class="p-2">
                                        <div class="stat-label">Quá dự kiến</div>
                                        <div class="fw-bold fs-5"><%= overdueCheckouts%> <span class="text-muted small">Phòng</span></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <span>TỔNG QUAN HỆ THỐNG</span>
                                    <a href="#" class="card-header-menu">Tháng này <i class="bi bi-chevron-down"></i></a>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <h5>Công suất phòng</h5>
                                            <div class="mb-3">
                                                <span class="fw-bold fs-5 text-primary"><i class="bi bi-person-fill"></i> <%= formattedOccupancy%>%</span>
                                                <span class="text-muted small ms-1">Trung bình</span>
                                            </div>

                                            <h5>Đặt phòng hôm nay</h5>
                                            <div class="d-flex justify-content-around p-3">
                                                <div class="p-2 text-center">
                                                    <div class="stat-label">Đặt mới</div>
                                                    <div class="fw-bold fs-5"><%= newBookingsToday%></div>
                                                </div>
                                                <div class="p-2 text-center">
                                                    <div class="stat-label">Hủy</div>
                                                    <div class="fw-bold fs-5"><%= canceledBookingsToday%></div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="chart-placeholder line-chart-placeholder">
                                            [Biểu đồ đường (Line Chart) ở đây]
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="col-lg-3">
                    <div class="card activity-feed">
                        <div class="card-header">CÁC HOẠT ĐỘNG GẦN ĐÂY</div>
                        <div class="card-body" style="max-height: 600px; overflow-y: auto;">
                            <ul class="list-group list-group-flush">

                                <li class="list-group-item">
                                    <div class="d-flex">
                                        <div class="activity-icon"><i class="bi bi-person-fill"></i></div>
                                        <div>
                                            <div class="activity-text">
                                                <strong>Staff</strong> vừa tạo hóa đơn <strong>50,99</strong>
                                            </div>
                                            <div class="activity-time">Vài phút trước</div>
                                        </div>
                                    </div>
                                </li>
                                <li class="list-group-item">
                                    <div class="d-flex">
                                        <div class="activity-icon"><i class="bi bi-person-fill"></i></div>
                                        <div>
                                            <div class="activity-text">
                                                <strong>Staff</strong> vừa tạo hóa đơn <strong>79,99</strong>
                                            </div>
                                            <div class="activity-time">Vài phút trước</div>
                                        </div>
                                    </div>
                                </li>
                                <li class="list-group-item">
                                    <div class="d-flex">
                                        <div class="activity-icon"><i class="bi bi-person-fill"></i></div>
                                        <div>
                                            <div class="activity-text">
                                                <strong>Staff</strong> vừa tạo hóa đơn <strong>159,98</strong>
                                            </div>
                                            <div class="activity-time">Vài phút trước</div>
                                        </div>
                                    </div>
                                </li>
                                <li class="list-group-item">
                                    <div class="d-flex">
                                        <div class="activity-icon"><i class="bi bi-person-fill"></i></div>
                                        <div>
                                            <div class="activity-text">
                                                <strong>Staff</strong> vừa tạo hóa đơn <strong>169,8</strong>
                                            </div>
                                            <div class="activity-time">Vài phút trước</div>
                                        </div>
                                    </div>
                                </li>
                                <li class="list-group-item">
                                    <div class="d-flex">
                                        <div class="activity-icon"><i class="bi bi-person-fill"></i></div>
                                        <div>
                                            <div class="activity-text">
                                                <strong>Staff</strong> vừa tạo hóa đơn <strong>45,5</strong>
                                            </div>
                                            <div class="activity-time">Vài phút trước</div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="fab-container">
            <a href="#" class="fab fab-zalo d-flex flex-column" title="Hỗ trợ Zalo">
                <span class="fab-text">Zalo</span>
            </a>
            <a href="#" class="fab fab-hotro" title="Hỗ Trợ">
                <i class="bi bi-headset"></i>
            </a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>