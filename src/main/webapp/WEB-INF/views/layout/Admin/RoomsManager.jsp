<%--
    Document   : RoomsManager
    Created on : Oct 19, 2025, 9:42:50 AM
    Author     : TR_NGHIA
--%>

<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="dto.RoomType"%>
<%@page import="java.util.HashMap"%>
<%@page import="dto.Room"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="dto.Staff"%>
<%@page import="util.IConstant"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Sơ đồ phòng</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">

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

            .user-avatar {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                object-fit: cover;
                border: 2px solid #fff;
            }

            .card {
                border: none;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                border-radius: 8px;
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

            .room-toolbar, .filter-bar {
                padding: 1rem 1.5rem;
                background-color: #ffffff;
                border-bottom: 1px solid #dee2e6;
            }

            .filter-bar {
                display: flex;
                flex-wrap: wrap;
                gap: 1rem;
                font-size: 0.9rem;
            }

            .filter-tag .form-check-input {
                margin-top: 0.2rem;
            }

            .accordion-button {
                font-weight: 600;
                color: #333;
            }

            .accordion-button:not(.collapsed) {
                background-color: #f0f5ff;
                color: #1a66c9;
            }

            .accordion-button:focus {
                box-shadow: none;
                border-color: rgba(0,0,0,.125);
            }

            .accordion-item {
                border-radius: 0 !important;
                border: none;
                border-bottom: 1px solid #dee2e6;
            }

            .accordion-body {
                background-color: #f8f9fa;
                padding: 1.5rem;
            }

            .room-card {
                font-size: 0.85rem;
                border-radius: 6px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
                border: 1px solid #e9ecef;
            }

            .room-card .card-header {
                font-size: 0.8rem;
                font-weight: 600;
                padding: 0.5rem 0.75rem;
                border-radius: 6px 6px 0 0;
            }

            .room-card .card-body {
                padding: 0.75rem;
            }

            .room-number {
                font-size: 1.1rem;
                font-weight: 700;
                color: #222;
            }

            .room-type, .room-guest {
                color: #555;
                font-size: 0.9rem;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .room-price-info {
                font-size: 0.85rem;
                color: #666;
            }

            .room-price-info i {
                margin-right: 5px;
            }

            .room-time-tag {
                background-color: #f0f0f0;
                color: #333;
                border-radius: 4px;
                padding: 0.25rem 0.5rem;
                font-size: 0.8rem;
                font-weight: 600;
                text-align: center;
                margin-top: 1rem;
            }

            .status-available .card-header {
                background-color: #fff;
                color: #198754;
            }
            .status-available .status-tag {
                color: #198754;
            }
            .status-available .status-tag i {
                color: #198754;
            }

            .status-dirty .card-header {
                background-color: #fff;
                color: #ffc107;
            }
            .status-dirty .status-tag {
                color: #c89800;
            }
            .status-dirty .status-tag i {
                color: #ffc107;
            }

            .status-occupied {
                background-color: #1a66c9; 
                color: #fff;
            }
            .status-occupied .card-header {
                background-color: #1256ad;
                border-bottom: 1px solid #495057;
            }
            .status-occupied .status-tag {
                color: #fff;
            }
            .status-occupied .room-number,
            .status-occupied .room-guest,
            .status-occupied .room-type {
                color: #fff;
            }
            .status-occupied .room-time-tag {
                background-color: #1256ad;
                color: #fff;
            }

            .status-maintenance .card-header {
                background-color: #fff;
                color: #dc3545;
            }
            .status-maintenance .status-tag {
                color: #dc3545;
            }
            .status-maintenance .status-tag i {
                color: #dc3545;
            }

        </style>
    </head>
    <body>

        <%
            ArrayList<Room> allRooms = (request.getAttribute("ALL_ROOMS_LIST") != null)
                    ? (ArrayList<Room>) request.getAttribute("ALL_ROOMS_LIST")
                    : new ArrayList<>();

            ArrayList<RoomType> allRoomTypes = (request.getAttribute("ALL_ROOM_TYPES_LIST") != null)
                    ? (ArrayList<RoomType>) request.getAttribute("ALL_ROOM_TYPES_LIST")
                    : new ArrayList<>();

            NumberFormat vndFormatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            Map<Integer, RoomType> roomTypeMap = new HashMap<>();
            for (RoomType rt : allRoomTypes) {
                roomTypeMap.put(rt.getRoomTypeId(), rt);
            }

            Map<Character, ArrayList<Room>> floors = new TreeMap<>();
            for (Room room : allRooms) {
                if (room.getRoomNumber() != null && !room.getRoomNumber().isEmpty()) {
                    char floorNum = room.getRoomNumber().charAt(0);
                    floors.putIfAbsent(floorNum, new ArrayList<>());
                    floors.get(floorNum).add(room);
                }
            }

            Staff staff = (Staff) session.getAttribute("USER_STAFF");
            boolean isLoggedIn = (session.getAttribute("IS_LOGIN") != null && (Boolean) session.getAttribute("IS_LOGIN"));

            boolean showStaffName = (isLoggedIn && staff != null && staff.isStatus());
            String staffNameDisplay = showStaffName ? staff.getFullName() : "Đăng nhập";
        %>

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
                            <a class="nav-link active" href="<%= IConstant.ACTION_MANAGER_ROOMS%>"><i class="bi bi-grid-fill me-1"></i> Phòng</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link " href="<%= IConstant.ACTION_MANAGER_STAFFS%>"><i class="bi bi-people-fill me-1"></i> Nhân viên</a>
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

        <div class="room-toolbar d-flex flex-wrap justify-content-between align-items-center">
            <div class="d-flex align-items-center flex-grow-1 me-3">
                <button class="btn btn-primary me-2" title="Sơ đồ"><i class="bi bi-grid-3x3-gap-fill"></i></button>
                <button class="btn btn-outline-secondary me-2" title="Danh sách"><i class="bi bi-list-ul"></i></button>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                    <input type="text" class="form-control" placeholder="Tìm kiếm khách hàng, mã đặt phòng...">
                </div>
            </div>
            <div class="d-flex align-items-center mt-2 mt-md-0">
                <div class="dropdown me-2">
                    <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Bảng giá chung
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">Bảng giá cuối tuần</a></li>
                        <li><a class="dropdown-item" href="#">Bảng giá ngày lễ</a></li>
                    </ul>
                </div>
                <button class="btn btn-warning me-2 fw-bold"><i class="bi bi-plus-lg"></i> Đặt phòng</button>
                <button class="btn btn-outline-secondary"><i class="bi bi-check2-square"></i> Chọn nhiều phòng</button>
            </div>
        </div>


        <div class="accordion" id="floorAccordion">

            <%
                int floorCounter = 0;
                for (Map.Entry<Character, ArrayList<Room>> entry : floors.entrySet()) {
                    char floorNum = entry.getKey();
                    ArrayList<Room> roomsOnThisFloor = entry.getValue();
                    String floorId = "floor" + floorNum;

                    String isCollapsed = (floorCounter == 0) ? "" : "collapsed";
                    String isExpanded = (floorCounter == 0) ? "true" : "false";
                    String isShow = (floorCounter == 0) ? "show" : "";
            %>
            <div class="accordion-item">
                <h2 class="accordion-header" id="heading<%= floorId%>">
                    <button class="accordion-button <%= isCollapsed%>" type="button" data-bs-toggle="collapse" data-bs-target="#collapse<%= floorId%>" aria-expanded="<%= isExpanded%>" aria-controls="collapse<%= floorId%>">
                        Tầng <%= floorNum%> (<%= roomsOnThisFloor.size()%> phòng)
                    </button>
                </h2>
                <div id="collapse<%= floorId%>" class="accordion-collapse collapse <%= isShow%>" aria-labelledby="heading<%= floorId%>" data-bs-parent="#floorAccordion">
                    <div class="accordion-body">

                        <div class="row g-3">

                            <%
                                for (Room room : roomsOnThisFloor) {
                                    String statusClass = "";
                                    String statusIcon = "";
                                    String statusText = "";
                                    String roomTypeText = "Khách lẻ";

                                    RoomType type = roomTypeMap.get(room.getRoomTypeId());
                                    String typeName = "Lỗi loại phòng";
                                    String formattedPrice = "0";

                                    if (type != null) {
                                        typeName = type.getTypeName();
                                        formattedPrice = vndFormatter.format(type.getPricePerNight());
                                    }

                                    if (room.getStatus().equalsIgnoreCase("Available")) {
                                        statusClass = "status-available";
                                        statusIcon = "bi-check-circle-fill";
                                        statusText = "Sạch";
                                        roomTypeText = typeName;
                                    } else if (room.getStatus().equalsIgnoreCase("Dirty")) {
                                        statusClass = "status-dirty";
                                        statusIcon = "bi-exclamation-triangle-fill";
                                        statusText = "Chưa dọn";
                                        roomTypeText = typeName;
                                    } else if (room.getStatus().equalsIgnoreCase("Occupied")) {
                                        statusClass = "status-occupied";
                                        statusIcon = "bi-person-fill";
                                        statusText = "Có khách";
                                        roomTypeText = "Khách lẻ";
                                    } else if (room.getStatus().equalsIgnoreCase("Maintenance")) {
                                        statusClass = "status-maintenance";
                                        statusIcon = "bi-tools";
                                        statusText = "Bảo trì";
                                        roomTypeText = typeName;
                                    }
                            %>

                            <div class="col-xl-2 col-lg-3 col-md-4 col-sm-6">
                                <div class="card room-card <%= statusClass%> h-100">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <span class="status-tag"><i class="bi <%= statusIcon%>"></i> <%= statusText%></span>
                                        <a href="#" class="text-secondary"><i class="bi bi-three-dots-vertical"></i></a>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <div>
                                            <div class="room-number">P.<%= room.getRoomNumber()%></div>
                                            <div class="room-type"><%= roomTypeText%></div>
                                        </div>

                                        <%-- === YÊU CẦU 2 & 3: SỬA LOGIC HIỂN THỊ === --%>
                                        <% if (room.getStatus().equalsIgnoreCase("Occupied")) { %>
                                        <div class="mt-auto">
                                            <div class="room-time-tag"> Không khả dụng </div>
                                        </div>
                                        <% } else {%>
                                        <div class="mt-auto">
                                            <hr class="my-2">
                                            <div class="room-price-info"><i class="bi bi-clock"></i> 12:00 - 24:00 </div>
                                            <div class="room-price-info"><i class="bi bi-moon"></i> <%= formattedPrice%></div>
                                        </div>
                                        <% } %>

                                    </div>
                                </div>
                            </div>

                            <%
                                }
                            %>

                        </div> 
                    </div> 
                </div> 
            </div> 

            <%
                    floorCounter++;
                }
            %>

        </div> 

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>