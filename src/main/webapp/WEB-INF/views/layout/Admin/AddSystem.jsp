<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>
<%--
    Document   : AddSystem
    Created on : Nov 3, 2025, 2:11:36 PM
    Author     : TR_NGHIA
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Cấu hình Hệ thống</title>

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
                                <a href="<%= IConstant.ACTION_MANAGER_SYSTEM%>">Cấu hình hệ thống</a>
                            </li>
                            <li class="breadcrumb-item active" aria-current="page">Thêm cấu hình mới</li>
                        </ol>
                    </nav>

                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-plus-circle me-2"></i>
                            Thêm Cấu hình Hệ thống Mới
                        </div>
                        <div class="card-body">
                            <form action="<%= IConstant.ACTION_ADD_SYSTEM %>" method="post" id="addConfigForm">
                                
                                <!-- ConfigName -->
                                <div class="mb-3">
                                    <label for="configName" class="form-label">
                                        <i class="bi bi-tag text-primary"></i> Tên cấu hình 
                                        <span class="required">*</span>
                                    </label>
                                    <input type="text" 
                                           class="form-control" 
                                           id="configName" 
                                           name="configName" 
                                           required 
                                           maxlength="100"
                                           placeholder="Ví dụ: CheckInTime, TaxRate, MaxGuestsPerRoom...">
                                    <small class="text-muted">Tên định danh duy nhất cho cấu hình (không có khoảng trắng, sử dụng CamelCase)</small>
                                </div>

                                <!-- ConfigValue -->
                                <div class="mb-3">
                                    <label for="configValue" class="form-label">
                                        <i class="bi bi-input-cursor-text text-primary"></i> Giá trị cấu hình 
                                        <span class="required">*</span>
                                    </label>
                                    <textarea class="form-control" 
                                              id="configValue" 
                                              name="configValue" 
                                              rows="4" 
                                              required 
                                              placeholder="Nhập giá trị cho cấu hình...&#10;Ví dụ:&#10;- Thời gian: 14:00&#10;- Số: 100000&#10;- Phần trăm: 10&#10;- Văn bản: Allowed"></textarea>
                                    <small class="text-muted">Giá trị tương ứng với tên cấu hình</small>
                                </div>

                                <!-- Status -->
                                <div class="mb-4">
                                    <label class="form-label">
                                        <i class="bi bi-toggle-on text-success"></i> Trạng thái
                                    </label>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" 
                                               type="checkbox" 
                                               id="status" 
                                               name="status" 
                                               value="true"
                                               checked>
                                        <label class="form-check-label" for="status">
                                            <span id="statusText">Đang áp dụng</span>
                                        </label>
                                    </div>
                                    <small class="text-muted">Bật để áp dụng cấu hình này ngay sau khi thêm</small>
                                </div>

                                <!-- Action Buttons -->
                                <div class="d-flex gap-2 justify-content-end">
                                    <a href="<%= IConstant.ACTION_MANAGER_SYSTEM%>" 
                                       class="btn btn-secondary">
                                        <i class="bi bi-x-circle me-1"></i> Hủy
                                    </a>
                                    <button type="reset" class="btn btn-outline-secondary">
                                        <i class="bi bi-arrow-counterclockwise me-1"></i> Đặt lại
                                    </button>
                                    <button type="submit" class="btn btn-primary-custom">
                                        <i class="bi bi-check-circle me-1"></i> Thêm cấu hình
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
            // Toggle status text khi thay đổi checkbox
            document.getElementById('status').addEventListener('change', function () {
                const statusText = document.getElementById('statusText');
                if (this.checked) {
                    statusText.textContent = 'Đang áp dụng';
                    statusText.classList.remove('text-secondary');
                    statusText.classList.add('text-success');
                } else {
                    statusText.textContent = 'Không áp dụng';
                    statusText.classList.remove('text-success');
                    statusText.classList.add('text-secondary');
                }
            });

            // Validation tên cấu hình (không có khoảng trắng)
            document.getElementById('configName').addEventListener('input', function () {
                this.value = this.value.replace(/\s/g, '');
            });
        </script>
    </body>
</html>