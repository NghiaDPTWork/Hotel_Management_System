<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%--
    Document   : AddStaff
    Created on : Nov 2, 2025, 3:59:13 PM
    Author     : TR_NGHIA
--%>

<%
    Staff staff = (Staff) session.getAttribute("USER_STAFF");
    boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));

    boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
    String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Nhân viên Mới</title>

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
                background-color: #ffffff;
                margin-bottom: 1.5rem;
            }

            .card-header {
                background-color: #fff;
                border-bottom: 1px solid #f0f0f0;
                font-weight: 600;
                color: #333;
                font-size: 1.1rem;
                padding: 1rem 1.25rem;
            }

            .card-body {
                padding: 1.5rem;
            }

            .form-label {
                font-weight: 600;
                color: #333;
                margin-bottom: 0.5rem;
            }

            .form-control:focus,
            .form-select:focus {
                border-color: #1a66c9;
                box-shadow: 0 0 0 0.25rem rgba(26, 102, 201, 0.25);
            }

            .btn-primary {
                background-color: #1a66c9;
                border-color: #1a66c9;
            }

            .btn-primary:hover {
                background-color: #1256ad;
                border-color: #1256ad;
            }

            .required {
                color: #dc3545;
            }

            .alert {
                border-radius: 8px;
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

        <div class="container-fluid p-3 p-md-4">
            <div class="row justify-content-center">
                <div class="col-lg-8">

                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-person-plus-fill me-2"></i>
                            Thêm Nhân viên Mới
                        </div>
                        <div class="card-body">
                            <form action="<%= IConstant.ACTION_ADD_STAFF%>" method="POST" id="addStaffForm">

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="fullName" class="form-label">
                                            Họ và tên <span class="required">*</span>
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="fullName" 
                                               name="fullName" 
                                               placeholder="Nguyễn Văn A"
                                               required>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="role" class="form-label">
                                            Chức vụ <span class="required">*</span>
                                        </label>
                                        <select class="form-select" id="role" name="role" required>
                                            <option value="">-- Chọn chức vụ --</option>
                                            <option value="Manager">Manager</option>
                                            <option value="Receptionist">Receptionist</option>
                                            <option value="Housekeeping">Housekeeping</option>
                                            <option value="ServiceStaff">Service Staff</option>
                                            <option value="Admin">Admin</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="username" class="form-label">
                                            Tên đăng nhập <span class="required">*</span>
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="username" 
                                               name="username" 
                                               placeholder="username123"
                                               required>
                                        <div class="form-text">
                                            Tên đăng nhập phải là duy nhất
                                        </div>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="password" class="form-label">
                                            Mật khẩu <span class="required">*</span>
                                        </label>
                                        <input type="password" 
                                               class="form-control" 
                                               id="password" 
                                               name="password" 
                                               placeholder="••••••••"
                                               required>
                                        <div class="form-text">
                                            Tối thiểu 6 ký tự
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="phone" class="form-label">
                                            Số điện thoại <span class="required">*</span>
                                        </label>
                                        <input type="tel" 
                                               class="form-control" 
                                               id="phone" 
                                               name="phone" 
                                               placeholder="0901234567"
                                               pattern="[0-9]{10,11}"
                                               required>
                                        <div class="form-text">
                                            10-11 số
                                        </div>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="email" class="form-label">
                                            Email <span class="required">*</span>
                                        </label>
                                        <input type="email" 
                                               class="form-control" 
                                               id="email" 
                                               name="email" 
                                               placeholder="example@hotel.com"
                                               required>
                                    </div>
                                </div>

                                <hr class="my-4">

                                <div class="d-flex justify-content-between">
                                    <a href="<%= IConstant.ACTION_MANAGER_STAFFS%>" class="btn btn-secondary">
                                        <i class="bi bi-arrow-left me-1"></i> Quay lại
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-check-lg me-1"></i> Thêm nhân viên
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <script>
            // Validate form trước khi submit
            document.getElementById('addStaffForm').addEventListener('submit', function (e) {
                const password = document.getElementById('password').value;
                const phone = document.getElementById('phone').value;

                // Kiểm tra mật khẩu
                if (password.length < 6) {
                    e.preventDefault();
                    alert('Mật khẩu phải có ít nhất 6 ký tự!');
                    return false;
                }

                // Kiểm tra số điện thoại
                if (!/^[0-9]{10,11}$/.test(phone)) {
                    e.preventDefault();
                    alert('Số điện thoại phải có 10-11 chữ số!');
                    return false;
                }
            });
        </script>
    </body>
</html>