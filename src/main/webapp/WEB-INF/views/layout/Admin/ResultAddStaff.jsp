<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%--
    Document   : resultAdd
    Created on : Nov 2, 2025, 4:30:00 PM
    Author     : TR_NGHIA
--%>

<%
    Staff staff = (Staff) session.getAttribute("USER_STAFF");
    boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));

    boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
    String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";

    // Lấy kết quả từ request
    Boolean isSuccess = (Boolean) request.getAttribute("IS_SUCCESS");
    String message = (String) request.getAttribute("MESSAGE");
    String messageType = (String) request.getAttribute("MESSAGE_TYPE");
    String staffName = (String) request.getAttribute("STAFF_NAME");

    if (isSuccess == null) {
        isSuccess = false;
    }
    if (message == null) {
        message = "Không có thông tin";
    }
    if (messageType == null)
        messageType = "error";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả thêm nhân viên</title>

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

            .navbar-custom .top-nav-link {
                color: #ffffff;
                font-size: 0.85rem;
            }

            .navbar-custom .top-nav-link:hover {
                color: #f0f0f0;
            }

            .result-container {
                min-height: 60vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .result-card {
                border: none;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                border-radius: 12px;
                background-color: #ffffff;
                max-width: 600px;
                width: 100%;
            }

            .result-icon {
                font-size: 5rem;
                margin-bottom: 1rem;
            }

            .result-icon.success {
                color: #28a745;
            }

            .result-icon.error {
                color: #dc3545;
            }

            .result-title {
                font-size: 1.8rem;
                font-weight: 700;
                margin-bottom: 1rem;
            }

            .result-message {
                font-size: 1.1rem;
                color: #6c757d;
                margin-bottom: 2rem;
            }

            .btn-primary {
                background-color: #1a66c9;
                border-color: #1a66c9;
                padding: 0.75rem 2rem;
                font-size: 1rem;
            }

            .btn-primary:hover {
                background-color: #1256ad;
                border-color: #1256ad;
            }

            .btn-secondary {
                padding: 0.75rem 2rem;
                font-size: 1rem;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand" href="<%= IConstant.ACTION_HOME_FOR_ADMIN %>">MISUKA</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="mainNavbar">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_ADMIN_HOME%>"><i class="bi bi-house-door-fill me-1"></i> Tổng quan</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_ROOMS%>"><i class="bi bi-grid-fill me-1"></i> Phòng</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="<%= IConstant.ACTION_MANAGER_STAFFS%>"><i class="bi bi-people-fill me-1"></i> Nhân viên</a>
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
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="navbarDropdownUser" role="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle" style="font-size: 1.5rem; color: white;"></i>
                                <span class="d-none d-lg-inline-block ms-1 text-white">
                                    <%= staffNameDisplay%>
                                </span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="#"><i class="bi bi-clock-history me-2"></i> Lịch sử hoạt động</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="<%= IConstant.ACTION_LOGOUT_FOR_ADMIN %>"><i class="bi bi-box-arrow-right me-2"></i> Đăng xuất</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container-fluid result-container">
            <div class="result-card text-center p-5">

                <% if ("success".equals(messageType)) { %>
                <i class="bi bi-check-circle-fill result-icon success"></i>
                <h2 class="result-title text-success">Thành công!</h2>
                <% } else { %>
                <i class="bi bi-x-circle-fill result-icon error"></i>
                <h2 class="result-title text-danger">Thất bại!</h2>
                <% }%>

                <p class="result-message">
                    <%= message%>
                </p>

                <div class="d-flex gap-3 justify-content-center">
                    <a href="<%= IConstant.ACTION_MANAGER_STAFFS%>" class="btn btn-primary">
                        <i class="bi bi-list-ul me-2"></i>
                        Danh sách nhân viên
                    </a>

                    <% if ("success".equals(messageType)) {%>
                    <a href="<%= IConstant.ACTION_ADD_STAFF%>" class="btn btn-secondary">
                        <i class="bi bi-plus-lg me-2"></i>
                        Thêm nhân viên khác
                    </a>
                    <% } else { %>
                    <a href="javascript:history.back()" class="btn btn-secondary">
                        <i class="bi bi-arrow-left me-2"></i>
                        Thử lại
                    </a>
                    <% } %>
                </div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <% if ("success".equals(messageType)) {%>
        <script>
            // Auto redirect sau 5 giây nếu thành công
            setTimeout(function () {
                window.location.href = '<%= IConstant.ACTION_MANAGER_STAFFS%>';
            }, 5000);
        </script>
        <% }%>
    </body>
</html>