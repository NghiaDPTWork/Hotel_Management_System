<%@page import="dto.SystemConfig"%>
<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả Chỉnh sửa Cấu hình</title>

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
                padding: 0.75rem 1.25rem;
            }

            .result-icon {
                font-size: 4rem;
                margin-bottom: 1rem;
            }

            .success-icon {
                color: #198754;
            }

            .error-icon {
                color: #dc3545;
            }

            .btn-primary-custom {
                background-color: #1a66c9;
                border: none;
                color: #ffffff;
                font-weight: 600;
            }
            .btn-primary-custom:hover {
                background-color: #1256ad;
                color: #ffffff;
            }

            .config-info {
                background-color: #f8f9fa;
                padding: 1rem;
                border-radius: 6px;
                border-left: 4px solid #1a66c9;
            }

            .config-info-label {
                font-weight: 600;
                color: #666;
                font-size: 0.9rem;
            }

            .config-info-value {
                font-size: 1rem;
                color: #333;
                word-wrap: break-word;
            }
        </style>
    </head>
    <body>
        <%
            Boolean editSuccess = (Boolean) request.getAttribute("EDIT_SUCCESS");
            String successMessage = (String) request.getAttribute("SUCCESS_MESSAGE");
            String errorMessage = (String) request.getAttribute("ERROR_MESSAGE");
            SystemConfig updatedConfig = (SystemConfig) request.getAttribute("UPDATED_CONFIG");

            Staff staff = (Staff) session.getAttribute("USER_STAFF");
            boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));
            boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
            String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";
        %>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand" href="<%= IConstant.ACTION_HOME_FOR_ADMIN %>">MISUKA</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="mainNavbar">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_ADMIN_HOME%>">
                                <i class="bi bi-house-door-fill me-1"></i> Tổng quan
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_ROOMS%>">
                                <i class="bi bi-grid-fill me-1"></i> Phòng
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_STAFFS%>">
                                <i class="bi bi-people-fill me-1"></i> Nhân viên
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="<%= IConstant.ACTION_MANAGER_SYSTEM%>">
                                <i class="bi bi-gear-fill me-1"></i> Cấu hình hệ thống
                            </a>
                        </li>
                    </ul>

                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#">
                                <i class="bi bi-question-circle me-1"></i> Hỗ trợ
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#">
                                <i class="bi bi-building me-1"></i> Chi nhánh trung tâm
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link top-nav-link" href="#">
                                <i class="bi bi-bell-fill"></i>
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" 
                               id="navbarDropdownUser" role="button" data-bs-toggle="dropdown">
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
                    <!-- Breadcrumb -->
                    <nav aria-label="breadcrumb" class="mb-3">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="<%= IConstant.ACTION_MANAGER_SYSTEM%>">Cấu hình hệ thống</a>
                            </li>
                            <li class="breadcrumb-item active" aria-current="page">Kết quả chỉnh sửa</li>
                        </ol>
                    </nav>

                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-clipboard-check me-2"></i>
                            Kết quả Chỉnh sửa Cấu hình
                        </div>
                        <div class="card-body text-center">
                            <%
                                if (editSuccess != null && editSuccess) {
                                    // THÀNH CÔNG
                            %>
                            <div class="result-icon success-icon">
                                <i class="bi bi-check-circle-fill"></i>
                            </div>
                            <h3 class="text-success mb-3">Cập nhật thành công!</h3>
                            <p class="text-muted mb-4">
                                <%= successMessage != null ? successMessage : "Cấu hình đã được cập nhật thành công."%>
                            </p>

                            <%
                                if (updatedConfig != null) {
                            %>
                            <!-- Thông tin cấu hình đã cập nhật -->
                            <div class="config-info text-start mb-4">
                                <div class="row mb-2">
                                    <div class="col-md-4">
                                        <span class="config-info-label">ID Cấu hình:</span>
                                    </div>
                                    <div class="col-md-8">
                                        <span class="config-info-value"><%= updatedConfig.getConfigId()%></span>
                                    </div>
                                </div>
                                <div class="row mb-2">
                                    <div class="col-md-4">
                                        <span class="config-info-label">Tên cấu hình:</span>
                                    </div>
                                    <div class="col-md-8">
                                        <span class="config-info-value"><strong><%= updatedConfig.getConfigName()%></strong></span>
                                    </div>
                                </div>
                                <div class="row mb-2">
                                    <div class="col-md-4">
                                        <span class="config-info-label">Giá trị mới:</span>
                                    </div>
                                    <div class="col-md-8">
                                        <span class="config-info-value">
                                            <code style="background-color: #e9ecef; padding: 0.25rem 0.5rem; border-radius: 4px;">
                                                <%= updatedConfig.getConfigValue()%>
                                            </code>
                                        </span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-4">
                                        <span class="config-info-label">Trạng thái:</span>
                                    </div>
                                    <div class="col-md-8">
                                        <%
                                            String statusBadge = updatedConfig.isStatus() ? "badge bg-success" : "badge bg-secondary";
                                            String statusText = updatedConfig.isStatus() ? "Đang áp dụng" : "Không áp dụng";
                                        %>
                                        <span class="<%= statusBadge%>"><%= statusText%></span>
                                    </div>
                                </div>
                            </div>
                            <%
                                }
                            %>

                            <div class="d-flex gap-2 justify-content-center">
                                <a href="<%= IConstant.ACTION_VIEW_EDIT_SYSTEM %>" class="btn btn-primary-custom">
                                    <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách
                                </a>
                                <%
                                    if (updatedConfig != null) {
                                %>
                                <a href="<%= IConstant.ACTION_PRE_EDIT_SYSTEM%>?configId=<%= updatedConfig.getConfigId()%>" 
                                   class="btn btn-outline-secondary">
                                    <i class="bi bi-pencil me-1"></i> Chỉnh sửa tiếp
                                </a>
                                <%
                                    }
                                %>
                            </div>

                            <%
                            } else {
                                // THẤT BẠI
                            %>
                            <div class="result-icon error-icon">
                                <i class="bi bi-x-circle-fill"></i>
                            </div>
                            <h3 class="text-danger mb-3">Cập nhật thất bại!</h3>
                            <div class="alert alert-danger text-start" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                <strong>Lỗi:</strong> <%= errorMessage != null ? errorMessage : "Đã xảy ra lỗi không xác định."%>
                            </div>

                            <div class="d-flex gap-2 justify-content-center">
                                <a href="<%= IConstant.ACTION_VIEW_EDIT_SYSTEM%>" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách
                                </a>
                                <a href="javascript:history.back()" class="btn btn-outline-primary">
                                    <i class="bi bi-arrow-counterclockwise me-1"></i> Thử lại
                                </a>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        
        <%
            // Auto redirect sau 5 giây nếu thành công
            if (editSuccess != null && editSuccess) {
        %>
        <script>
            setTimeout(function() {
                window.location.href = '<%= IConstant.ACTION_VIEW_EDIT_SYSTEM%>';
            }, 10000);
        </script>
        <%
            }
        %>
    </body>
</html>