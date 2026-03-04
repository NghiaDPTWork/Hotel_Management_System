<%@page import="dto.Staff"%>
<%@page import="util.IConstant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Staff sessionStaff = (Staff) session.getAttribute("USER_STAFF");
    boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));
    
    boolean showStaffName = (isLoggedIn && sessionStaff != null && sessionStaff.isStatus());
    String staffNameDisplay = showStaffName ? sessionStaff.getFullName() : "Đăng nhập";
    
    Staff editStaff = (Staff) request.getAttribute("EDIT_STAFF");
    String errorMsg = (String) request.getAttribute("ERROR_MSG");
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa Nhân viên</title>

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

            .card-body {
                padding: 2rem;
            }

            .card-header {
                background-color: #fff;
                border-bottom: 1px solid #f0f0f0;
                font-weight: 600;
                color: #333;
                font-size: 1.1rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                padding: 0.75rem 1.25rem;
            }

            .sidebar-menu .list-group-item {
                font-size: 1rem;
                font-weight: 600;
                color: #333;
                border-radius: 0;
                padding-top: 1rem;    
                padding-bottom: 1rem;  
            }
            .sidebar-menu .list-group-item.active {
                background-color: #1a66c9;
                border-color: #1a66c9;
                color: #ffffff;
            }
            .sidebar-menu .list-group-item:not(.active):hover {
                background-color: #f8f9fa;
            }
            .sidebar-menu .list-group-item small {
                font-size: 0.85rem;
                font-weight: normal;
                color: #6c757d;
            }
            .sidebar-menu .list-group-item:first-child {
                border-top-left-radius: 8px;
                border-top-right-radius: 8px;
            }
            .sidebar-menu .list-group-item:last-child {
                border-bottom-left-radius: 8px;
                border-bottom-right-radius: 8px;
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
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_SYSTEM %>"><i class="bi bi-gear-fill me-1"></i> Cấu hình hệ thống</a>
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
                                    <%= staffNameDisplay %>
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
            <div class="row">

                <div class="col-lg-3">
                    <div class="card sidebar-menu p-0">
                        <div class="list-group list-group-flush">
                            <a href="<%= IConstant.ACTION_MANAGER_STAFFS %>" class="list-group-item list-group-item-action">
                                <i class="bi bi-list-task me-2"></i>
                                Quản lý nhân viên
                            </a>
                            <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="list-group-item list-group-item-action active">
                                <i class="bi bi-pencil-square me-2"></i>
                                Chỉnh sửa nhân viên
                                <small class="d-block">Thêm, xóa, sửa, cập nhật</small>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action">
                                <i class="bi bi-calendar-check me-2"></i>
                                Phân công ca làm
                            </a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-9">
                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-pencil-square me-2"></i> Chỉnh sửa thông tin nhân viên
                        </div>
                        <div class="card-body">
                            
                            <% if (errorMsg != null) { %>
                                <div class="alert alert-danger" role="alert">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <%= errorMsg %>
                                </div>
                                <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="btn btn-primary">
                                    <i class="bi bi-arrow-left me-2"></i> Quay lại danh sách
                                </a>
                            <% } else if (editStaff != null) { %>
                                
                                <form action="<%= IConstant.ACTION_EDIT_STAFF %>" method="POST">
                                    <input type="hidden" name="staffId" value="<%= editStaff.getStaffId() %>">
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="fullName" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" id="fullName" name="fullName" 
                                                   value="<%= editStaff.getFullName() %>" required>
                                        </div>
                                        
                                        <div class="col-md-6 mb-3">
                                            <label for="role" class="form-label">Chức vụ <span class="text-danger">*</span></label>
                                            <select class="form-select" id="role" name="role" required>
                                                <option value="Receptionist" <%= "Receptionist".equals(editStaff.getRole()) ? "selected" : "" %>>Lễ tân (Receptionist)</option>
                                                <option value="Manager" <%= "Manager".equals(editStaff.getRole()) ? "selected" : "" %>>Quản lý (Manager)</option>
                                                <option value="Housekeeping" <%= "Housekeeping".equals(editStaff.getRole()) ? "selected" : "" %>>Dọn phòng (Housekeeping)</option>
                                                <option value="ServiceStaff" <%= "ServiceStaff".equals(editStaff.getRole()) ? "selected" : "" %>>Phục vụ (Service Staff)</option>
                                                <option value="Admin" <%= "Admin".equals(editStaff.getRole()) ? "selected" : "" %>>Quản trị viên (Admin)</option>
                                                <option value="Repair" <%= "Repair".equals(editStaff.getRole()) ? "selected" : "" %>>Sửa chữa (Repair)</option>
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="username" class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" id="username" name="username" 
                                                   value="<%= editStaff.getUsername() %>" required readonly>
                                            <small class="text-muted">Tên đăng nhập không thể thay đổi</small>
                                        </div>
                                        
                                        <div class="col-md-6 mb-3">
                                            <label for="password" class="form-label">Mật khẩu mới</label>
                                            <input type="password" class="form-control" id="password" name="password" 
                                                   placeholder="Để trống nếu không đổi mật khẩu">
                                            <small class="text-muted">Chỉ nhập nếu muốn thay đổi mật khẩu</small>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="phone" class="form-label">Số điện thoại</label>
                                            <input type="tel" class="form-control" id="phone" name="phone" 
                                                   value="<%= editStaff.getPhone() != null ? editStaff.getPhone() : "" %>">
                                        </div>
                                        
                                        <div class="col-md-6 mb-3">
                                            <label for="email" class="form-label">Email</label>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   value="<%= editStaff.getEmail() != null ? editStaff.getEmail() : "" %>">
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                                            <select class="form-select" id="status" name="status" required>
                                                <option value="1" <%= editStaff.isStatus() ? "selected" : "" %>>Đang làm việc</option>
                                                <option value="0" <%= !editStaff.isStatus() ? "selected" : "" %>>Đã nghỉ</option>
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <hr class="my-4">
                                    
                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="bi bi-check-circle me-2"></i> Cập nhật thông tin
                                        </button>
                                        <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="btn btn-secondary">
                                            <i class="bi bi-x-circle me-2"></i> Hủy bỏ
                                        </a>
                                    </div>
                                </form>
                                
                            <% } else { %>
                                <div class="alert alert-warning" role="alert">
                                    <i class="bi bi-info-circle-fill me-2"></i>
                                    Không có thông tin nhân viên để chỉnh sửa
                                </div>
                                <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="btn btn-primary">
                                    <i class="bi bi-arrow-left me-2"></i> Quay lại danh sách
                                </a>
                            <% } %>
                            
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="fab-container">
            <a href="#" class="fab fab-zalo d-flex flex-column" title="Hỗ trợ Zalo">
                <span class="fab-text">Zalo</span>
            </a>
            <a href="#" class="fab fab-hotro" title="Hỗ trợ">
                <i class="bi bi-headset"></i>
            </a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>