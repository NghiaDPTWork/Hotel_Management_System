<%@page import="dto.Staff"%>
<%@page import="util.IConstant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Staff sessionStaff = (Staff) session.getAttribute("USER_STAFF");
    boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));
    
    boolean showStaffName = (isLoggedIn && sessionStaff != null && sessionStaff.isStatus());
    String staffNameDisplay = showStaffName ? sessionStaff.getFullName() : "Đăng nhập";
    
    // Lấy kết quả từ controller
    Staff updatedStaff = null;
    String successMsg = "";
    String errorMsg = "";
    if(request.getAttribute("UPDATED_STAFF") != null){
        updatedStaff = (Staff) request.getAttribute("UPDATED_STAFF");
    }
    if(request.getAttribute("SUCCESS_MSG") != null){
        successMsg = (String) request.getAttribute("SUCCESS_MSG");
    }
    if(request.getAttribute("ERROR_MSG") != null){
        errorMsg = (String) request.getAttribute("ERROR_MSG");
    }
    boolean isSuccess = (successMsg != null && updatedStaff != null);
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả cập nhật nhân viên</title>

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

            .card-body {
                padding: 2rem;
            }

            .result-icon {
                font-size: 4rem;
                margin-bottom: 1rem;
            }

            .success-icon {
                color: #28a745;
            }

            .error-icon {
                color: #dc3545;
            }

            .info-table th {
                width: 30%;
                background-color: #f8f9fa;
                font-weight: 600;
            }

            .info-table td {
                width: 70%;
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

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card">
                        <div class="card-body text-center">
                            
                            <% if (isSuccess) { %>
                                <!-- THÀNH CÔNG -->
                                <i class="bi bi-check-circle-fill result-icon success-icon"></i>
                                <h2 class="text-success mb-3">Cập nhật thành công!</h2>
                                <p class="text-muted mb-4"><%= successMsg %></p>
                                
                                <div class="text-start mt-4">
                                    <h5 class="mb-3"><i class="bi bi-person-badge me-2"></i>Thông tin nhân viên sau khi cập nhật:</h5>
                                    
                                    <table class="table table-bordered info-table">
                                        <tbody>
                                            <tr>
                                                <th><i class="bi bi-hash me-2"></i>Mã nhân viên</th>
                                                <td><%= updatedStaff.getStaffId() %></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-person me-2"></i>Họ và tên</th>
                                                <td><strong><%= updatedStaff.getFullName() %></strong></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-briefcase me-2"></i>Chức vụ</th>
                                                <td><%= updatedStaff.getRole() %></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-person-circle me-2"></i>Tên đăng nhập</th>
                                                <td><%= updatedStaff.getUsername() %></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-telephone me-2"></i>Số điện thoại</th>
                                                <td><%= updatedStaff.getPhone() != null ? updatedStaff.getPhone() : "Chưa cập nhật" %></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-envelope me-2"></i>Email</th>
                                                <td><%= updatedStaff.getEmail() != null ? updatedStaff.getEmail() : "Chưa cập nhật" %></td>
                                            </tr>
                                            <tr>
                                                <th><i class="bi bi-toggle-on me-2"></i>Trạng thái</th>
                                                <td>
                                                    <% if (updatedStaff.isStatus()) { %>
                                                        <span class="badge bg-success">Đang làm việc</span>
                                                    <% } else { %>
                                                        <span class="badge bg-secondary">Đã nghỉ</span>
                                                    <% } %>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                
                                <div class="mt-4 d-flex gap-2 justify-content-center">
                                    <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="btn btn-primary">
                                        <i class="bi bi-list-ul me-2"></i>Quay về danh sách
                                    </a>
                                    <a href="<%= IConstant.ACTION_PRE_EDIT_STAFF %>?staffId=<%= updatedStaff.getStaffId() %>" class="btn btn-outline-primary">
                                        <i class="bi bi-pencil me-2"></i>Chỉnh sửa lại
                                    </a>
                                </div>
                                
                            <% } else { %>
                                <!-- THẤT BẠI -->
                                <i class="bi bi-x-circle-fill result-icon error-icon"></i>
                                <h2 class="text-danger mb-3">Cập nhật thất bại!</h2>
                                <div class="alert alert-danger text-start" role="alert">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <strong>Nguyên nhân:</strong> <%= errorMsg != null ? errorMsg : "Có lỗi xảy ra trong quá trình cập nhật" %>
                                </div>
                                
                                <div class="mt-4">
                                    <p class="text-muted">Vui lòng kiểm tra lại thông tin và thử lại.</p>
                                    <div class="d-flex gap-2 justify-content-center">
                                        <a href="<%= IConstant.ACTION_EDIT_STAFFS %>" class="btn btn-primary">
                                            <i class="bi bi-arrow-left me-2"></i>Quay về danh sách
                                        </a>
                                        <a href="javascript:history.back()" class="btn btn-outline-secondary">
                                            <i class="bi bi-arrow-counterclockwise me-2"></i>Thử lại
                                        </a>
                                    </div>
                                </div>
                            <% } %>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>