<%@page import="dto.SystemConfig"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.IConstant"%>
<%@page import="dto.Staff"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Cấu hình Hệ thống</title>

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
                padding: 1.25rem;
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
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .btn-add-config {
                background-color: #1a66c9;
                border: none;
                color: #ffffff;
                font-weight: 600;
                font-size: 0.9rem;
            }
            .btn-add-config:hover {
                background-color: #1256ad;
                color: #ffffff;
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

            .config-value {
                font-family: 'Courier New', monospace;
                background-color: #f8f9fa;
                padding: 0.25rem 0.5rem;
                border-radius: 4px;
                font-size: 0.9rem;
            }
        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow-sm">
            <div class="container-fluid">
                <a class="navbar-brand" href="<%= IConstant.ACTION_HOME_FOR_ADMIN %>">MISUKA</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
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
                            <a class="nav-link" href="<%= IConstant.ACTION_MANAGER_STAFFS%>"><i class="bi bi-people-fill me-1"></i> Nhân viên</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="<%= IConstant.ACTION_MANAGER_SYSTEM%>"><i class="bi bi-gear-fill me-1"></i> Cấu hình hệ thống</a>
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
                        <%
                            Staff staff = (Staff) session.getAttribute("USER_STAFF");
                            boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));

                            boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
                            String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";
                        %>
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

                <div class="col-lg-3">
                    <div class="card sidebar-menu p-0">
                        <div class="list-group list-group-flush">
                            <a href="<%= IConstant.ACTION_MANAGER_ROOMS%>" class="list-group-item list-group-item-action">
                                <i class="bi bi-grid-fill me-2"></i>
                                Quản lý phòng
                            </a>
                            <a href="<%= IConstant.ACTION_VIEW_EDIT_SERVICE %>" class="list-group-item list-group-item-action">
                                <i class="bi bi-cpu-fill me-2"></i>
                                Quản lý dịch vụ
                            </a>
                            <a href="<%= IConstant.ACTION_VIEW_EDIT_SYSTEM%>" class="list-group-item list-group-item-action active" aria-current="true">
                                <i class="bi bi-sliders me-2"></i>
                                Cấu hình hệ thống
                            </a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-9">
                    <div class="card">
                        <div class="card-header">
                            <span>Cấu hình hệ thống</span>
                            <a href="<%= IConstant.ACTION_PRE_ADD_SYSTEM %>" class="btn btn-sm btn-add-config">
                                <i class="bi bi-plus-lg"></i> Thêm cấu hình mới
                            </a>
                        </div>
                        <div class="card-body">
                            <%
                                // Hiển thị thông báo từ request
                                String successMessage = (String) request.getAttribute("SUCCESS_MESSAGE");
                                String errorMessage = (String) request.getAttribute("ERROR_MESSAGE");

                                if (successMessage != null) {
                            %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="bi bi-check-circle-fill me-2"></i>
                                <strong>Thành công!</strong> <%= successMessage%>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <%
                                }

                                if (errorMessage != null) {
                            %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                <strong>Lỗi!</strong> <%= errorMessage%>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <%
                                }

                                ArrayList<SystemConfig> configs = (ArrayList<SystemConfig>) request.getAttribute("CONFIG_LIST");

                                if (configs == null || configs.isEmpty()) {
                            %>
                            <div class="alert alert-info" role="alert">
                                <i class="bi bi-info-circle me-2"></i>
                                Chưa có cấu hình nào trong hệ thống.
                            </div>
                            <%
                            } else {
                            %>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th scope="col" style="width: 8%;">STT</th>
                                            <th scope="col" style="width: 35%;">Tên cấu hình</th>
                                            <th scope="col" style="width: 32%;">Giá trị</th>
                                            <th scope="col" style="width: 10%;">Trạng thái</th>
                                            <th scope="col" style="width: 15%;">Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            int index = 1;
                                            for (SystemConfig config1 : configs) {
                                                String statusClass = config1.isStatus() ? "badge bg-success" : "badge bg-secondary";
                                                String statusText = config1.isStatus() ? "Đang áp dụng" : "Không áp dụng";
                                        %>
                                        <tr>
                                            <td class="text-center"><strong><%= index++%></strong></td>
                                            <td>
                                                <strong><%= config1.getConfigName()%></strong>
                                            </td>
                                            <td>
                                                <code class="config-value"><%= config1.getConfigValue()%></code>
                                            </td>
                                            <td>
                                                <span class="<%= statusClass%>"><%= statusText%></span>
                                            </td>
                                            <td>
                                                <!-- Nút chỉnh sửa -->
                                                <form action="<%= IConstant.ACTION_PRE_EDIT_SYSTEM%>" method="get" style="display: inline;">
                                                    <input type="hidden" name="configId" value="<%= config1.getConfigId()%>">
                                                    <button type="submit" 
                                                            class="btn btn-sm btn-outline-primary" 
                                                            title="Chỉnh sửa">
                                                        <i class="bi bi-pencil"></i>
                                                    </button>
                                                </form>

                                                <!-- Nút xóa -->
                                                <form action="<%= IConstant.ACTION_DELETE_SYSTEM%>" method="post" style="display: inline;">
                                                    <input type="hidden" name="configId" value="<%= config1.getConfigId()%>">
                                                    <button type="submit" 
                                                            class="btn btn-sm btn-outline-danger" 
                                                            title="Xóa"
                                                            onclick="return confirm('Bạn có chắc chắn muốn xóa cấu hình này?')">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>

                    <%-- Phần hướng dẫn sử dụng --%>
                    <div class="card">
                        <div class="card-header">
                            <i class="bi bi-lightbulb me-2"></i>
                            Hướng dẫn sử dụng
                        </div>
                        <div class="card-body">
                            <h6 class="fw-bold">Một số cấu hình quan trọng trong hệ thống:</h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <ul class="mb-0">
                                        <li><strong>CheckInTime:</strong> Giờ nhận phòng (14:00)</li>
                                        <li><strong>CheckOutTime:</strong> Giờ trả phòng (12:00)</li>
                                        <li><strong>CancellationPeriod:</strong> Thời gian hủy phòng (giờ)</li>
                                        <li><strong>MaxGuestsPerRoom:</strong> Số khách tối đa/phòng</li>
                                        <li><strong>TaxRate:</strong> Thuế VAT (%)</li>
                                        <li><strong>DefaultCurrency:</strong> Đơn vị tiền tệ (VND)</li>
                                    </ul>
                                </div>
                                <div class="col-md-6">
                                    <ul class="mb-0">
                                        <li><strong>BookingDepositRate:</strong> Tỷ lệ đặt cọc (%)</li>
                                        <li><strong>LateCheckOutFee:</strong> Phí checkout muộn</li>
                                        <li><strong>EarlyCheckInFee:</strong> Phí checkin sớm</li>
                                        <li><strong>PetPolicy:</strong> Chính sách thú cưng</li>
                                        <li><strong>SmokingPolicy:</strong> Chính sách hút thuốc</li>
                                        <li><strong>MinBookingDays:</strong> Số ngày đặt tối thiểu</li>
                                    </ul>
                                </div>
                            </div>
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