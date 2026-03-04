<%-- 
    Document   : AddService
    Created on : Nov 8, 2025, 7:02:28 PM
    Author     : TR_NGHIA
--%>

<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%--
    Document   : AddService
    Created on : Nov 8, 2025, 7:02:28 PM
    Author     : TR_NGHIA
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Dịch vụ</title>

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

            .form-label {
                font-weight: 600;
                color: #333;
            }

            .required {
                color: #dc3545;
            }
        </style>
    </head>
    <body>
        <%
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
                                <a href="<%= IConstant.ACTION_VIEW_EDIT_SERVICE%>">Quản lý dịch vụ</a>
                            </li>
                            <li class="breadcrumb-item active" aria-current="page">Thêm dịch vụ mới</li>
                        </ol>
                    </nav>

                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-plus-circle me-2"></i>
                            Thêm Dịch vụ Mới
                        </div>
                        <div class="card-body">
                            <%
                                // Hiển thị thông báo lỗi nếu có
                                String errorMessage = (String) request.getAttribute("ERROR_MESSAGE");
                                if (errorMessage != null) {
                            %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                <strong>Lỗi!</strong> <%= errorMessage%>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <%
                                }
                            %>

                            <form action="<%= IConstant.ACTION_ADD_SERVICE %>" method="post" id="addServiceForm">
                                
                                <!-- ServiceName -->
                                <div class="mb-3">
                                    <label for="serviceName" class="form-label">
                                        <i class="bi bi-tag text-primary"></i> Tên dịch vụ 
                                        <span class="required">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="serviceName" 
                                           name="serviceName" 
                                           required 
                                           maxlength="100"
                                           placeholder="Ví dụ: Buffet Breakfast, Laundry Service, Airport Transfer...">
                                    <small class="text-muted">Tên dịch vụ cung cấp cho khách hàng</small>
                                </div>

                                <!-- ServiceType -->
                                <div class="mb-3">
                                    <label for="serviceType" class="form-label">
                                        <i class="bi bi-list-ul text-primary"></i> Loại dịch vụ 
                                        <span class="required">*</span>
                                    </label>
                                    <select class="form-select" 
                                            id="serviceType" 
                                            name="serviceType" 
                                            required>
                                        <option value="">-- Chọn loại dịch vụ --</option>
                                        <option value="Food">Food (Ăn uống)</option>
                                        <option value="Laundry">Laundry (Giặt ủi)</option>
                                        <option value="Transport">Transport (Vận chuyển)</option>
                                        <option value="Spa">Spa (Chăm sóc sức khỏe)</option>
                                        <option value="Entertainment">Entertainment (Giải trí)</option>
                                        <option value="RoomService">Room Service (Phục vụ phòng)</option>
                                        <option value="Other">Other (Khác)</option>
                                    </select>
                                    <small class="text-muted">Phân loại dịch vụ theo nhóm</small>
                                </div>

                                <!-- Price -->
                                <div class="mb-4">
                                    <label for="price" class="form-label">
                                        <i class="bi bi-cash-coin text-success"></i> Giá dịch vụ (VNĐ) 
                                        <span class="required">*</span>
                                    </label>
                                    <div class="input-group">
                                        <input type="number" 
                                               class="form-control" 
                                               id="price" 
                                               name="price" 
                                               required 
                                               min="0"
                                               step="1000"
                                               placeholder="Nhập giá dịch vụ">
                                        <span class="input-group-text">VNĐ</span>
                                    </div>
                                    <small class="text-muted">Giá tiền cho một lần sử dụng dịch vụ</small>
                                </div>

                                <!-- Action Buttons -->
                                <div class="d-flex gap-2 justify-content-end">
                                    <a href="<%= IConstant.ACTION_VIEW_EDIT_SERVICE%>" 
                                       class="btn btn-secondary">
                                        <i class="bi bi-x-circle me-1"></i> Hủy
                                    </a>
                                    <button type="reset" class="btn btn-outline-secondary">
                                        <i class="bi bi-arrow-counterclockwise me-1"></i> Đặt lại
                                    </button>
                                    <button type="submit" class="btn btn-primary-custom">
                                        <i class="bi bi-check-circle me-1"></i> Thêm dịch vụ
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
            // Format số tiền khi nhập
            document.getElementById('price').addEventListener('input', function (e) {
                // Chỉ cho phép nhập số
                this.value = this.value.replace(/[^0-9]/g, '');
            });

            // Validation form trước khi submit
            document.getElementById('addServiceForm').addEventListener('submit', function (e) {
                const price = document.getElementById('price').value;
                
                if (price <= 0) {
                    e.preventDefault();
                    alert('Giá dịch vụ phải lớn hơn 0!');
                    return false;
                }
            });
        </script>
    </body>
</html>