package util;

import java.time.format.DateTimeFormatter;

/**
 *
 * @author TR_NGHIA
 */
public interface IConstant {

    //==================================================================
    // COMMON CONSTANTS
    //==================================================================
    public static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //==================================================================
    // SERVLET & ACTIONS (Used for Front Controller)
    //==================================================================
    // --- Actions (Controller URL Mapping) ---
    // --------------- GUEST ---------------
    public static final String ACTION_HOME = "HomeController";
    public static final String ACTION_LOGIN = "./login";
    public static final String ACTION_LOGOUT = "./logout";
    public static final String ACTION_REGISTER = "./register";
    public static final String ACTION_SEARCH_ROOMS = "./search";
    public static final String ACTION_VIEW_BOOKINGS = "./viewBookings";
    public static final String ACTION_BOOKING = "./booking";
    public static final String ACTION_PRE_BOOKING = "./pre_booking";
    public static final String ACTION_SUF_BOOKING = "./suf_booking";
    public static final String ACTION_EDIT_BOOKING = "./editBooking";
    public static final String ACTION_PRE_EDIT_BOOKING = "./pre_editBooking";
    public static final String ACTION_VIEW_BOOKING = "./viewBooking";
    public static final String ACTION_PRE_CHECKOUT_BOOKING = "./pre_checkout";
    public static final String ACTION_CHECKOUT_BOOKING = "./checkout";
    public static final String ACTION_PAYMENT_CHECKOUT_BOOKING = "./takePayment_checkout";

    // --------------- ADMIN ---------------
    public static final String ACTION_HOME_FOR_ADMIN = "../HomeController";
    public static final String ACTION_LOGOUT_FOR_ADMIN = "../logout";
    public static final String ACTION_ADMIN_FILTER = "/admin";
    public static final String ACTION_ADMIN_HOME = "./admin";

    public static final String ACTION_MANAGER_ROOMS = "./managerRooms";

    public static final String ACTION_MANAGER_STAFFS = "./managerStaffs";
    public static final String ACTION_SEARCH_STAFFS = "./searchStaffs";
    public static final String ACTION_EDIT_STAFFS = "./editStaffs";
    public static final String ACTION_PRE_EDIT_STAFF = "./pre_editStaff";
    public static final String ACTION_EDIT_STAFF = "./editStaff";
    public static final String ACTION_DELETE_STAFF = "./deleteStaff";
    public static final String ACTION_PRE_ADD_STAFF = "./pre_addStaff";
    public static final String ACTION_ADD_STAFF = "./addStaff";

    public static final String ACTION_MANAGER_SYSTEM = "./managerSystem";
    public static final String ACTION_VIEW_EDIT_SYSTEM = "./viewEditSystem";
    public static final String ACTION_PRE_EDIT_SYSTEM = "./pre_editSystem";
    public static final String ACTION_EDIT_SYSTEM = "./editSystem";
    public static final String ACTION_PRE_ADD_SYSTEM = "./pre_addSystem";
    public static final String ACTION_ADD_SYSTEM = "./addSystem";
    public static final String ACTION_DELETE_SYSTEM = "./deleteSystem";

    public static final String ACTION_VIEW_EDIT_SERVICE = "./viewEditService";
    public static final String ACTION_PRE_ADD_SERVICE = "./pre_addService";
    public static final String ACTION_ADD_SERVICE = "./addService";

    // --- Main Action Parameters (Value of 'action' parameter or button name) ---
    public static final String LOGIN = "Login";
    public static final String LOGOUT = "Logout";
    public static final String REGISTER = "Sign up";
    public static final String BOOKING = "Booking";
    public static final String VIEW_ROOMS_BOOKING = "viewRoomsBooking";
    public static final String VIEW_PROFILE = "viewProfile";
    public static final String VIEW_HISTORY_ROOM_BOOKING = "viewHistoryRoomBooking";

    //==================================================================
    // MAIN LAYOUTS (Paths to .jsp files)
    //==================================================================
    public static final String PAGE_HOME = "/home.jsp";
    public static final String PAGE_ERROR = "/error.jsp";

    // --- Layouts by Role ---
    public static final String PAGE_ADMIN = "/WEB-INF/views/layout/Admin/AdminDashboard.jsp";
    public static final String PAGE_RECEPTIONIST = "#";
    public static final String PAGE_MANAGER = "#";
    public static final String PAGE_SERVICE_STAFF = "#";
    public static final String PAGE_HOUSEKEEPING = "#";

    //==================================================================
    // MAIN FUNCTION PAGES (Specific .jsp Views)
    //==================================================================
    // --------------- GUEST ---------------
    // --- Login / Register ---
    public static final String PAGE_LOGIN = "/WEB-INF/views/features/login/login.jsp";
    public static final String PAGE_SIGN_UP = "/WEB-INF/views/features/login/sign-up.jsp";

    public static final String PAGE_CHECK_SCORE = "/WEB-INF/views/score/grade.jsp";

    // --- Booking & Search ---
    public static final String PAGE_BOOKING = "/WEB-INF/views/layout/BookingDashboard.jsp";
    public static final String PAGE_BOOKING_RESULT = "/WEB-INF/views/layout/BookingResult.jsp";
    public static final String PAGE_SEARCH_RESULT = "/WEB-INF/views/layout/SearchRoomsResult.jsp";
    public static final String PAGE_ROOMS_BOOKING = "/WEB-INF/views/layout/ViewBookings.jsp";
    public static final String PAGE_ROOM_BOOKING = "/WEB-INF/views/layout/ViewBooking.jsp";
    public static final String PAGE_EDIT_BOOKING = "/WEB-INF/views/layout/EditBooking.jsp";
    public static final String PAGE_CHECKOUT = "/WEB-INF/views/layout/Checkout.jsp";
    public static final String PAGE_EDIT_BOOKING_RESULT = "/WEB-INF/views/layout/EditBookingResult.jsp";

    // --------------- ADMIN ---------------
    // --- Rooms ---
    public static final String PAGE_ROOMS_MANAGER = "/WEB-INF/views/layout/Admin/RoomsManager.jsp";

    // --- Staffs ---
    public static final String PAGE_STAFFS_MANAGER = "/WEB-INF/views/layout/Admin/StaffsManager.jsp";
    public static final String PAGE_STAFF_PRE_EDITION = "/WEB-INF/views/layout/Admin/EditStaff.jsp";
    public static final String PAGE_STAFFS_EDITION = "/WEB-INF/views/layout/Admin/StaffsEdition.jsp";
    public static final String PAGE_STAFF_RESULT_EDITION = "/WEB-INF/views/layout/Admin/ResultEditStaff.jsp";
    public static final String PAGE_STAFF_ADD = "/WEB-INF/views/layout/Admin/AddStaff.jsp";
    public static final String PAGE_STAFF_RESULT_ADD = "/WEB-INF/views/layout/Admin/ResultAddStaff.jsp";

    // --- Systems ---
    public static final String PAGE_SYSTEM_MANAGER = "/WEB-INF/views/layout/Admin/SystemManager.jsp";
    public static final String PAGE_SYSTEM_VIEW_EDIT = "/WEB-INF/views/layout/Admin/ViewEditSystem.jsp";
    public static final String PAGE_SYSTEM_EDIT = "/WEB-INF/views/layout/Admin/EditSystem.jsp";
    public static final String PAGE_SYSTEM_EDIT_RESULT = "/WEB-INF/views/layout/Admin/ResultEditSystem.jsp";
    public static final String PAGE_SYSTEM_ADD = "/WEB-INF/views/layout/Admin/AddSystem.jsp";
    public static final String PAGE_SYSTEM_ADD_RESULT = "/WEB-INF/views/layout/Admin/ResultAddSystem.jsp";

    // --- Service ---
    public static final String PAGE_SERVICE_VIEW_EDIT = "/WEB-INF/views/layout/Admin/ViewEditService.jsp";
    public static final String PAGE_SERVICE_ADD = "/WEB-INF/views/layout/Admin/AddService.jsp";
    public static final String PAGE_SERVICE_ADD_RESULT = "/WEB-INF/views/layout/Admin/ResultAddService.jsp";

}
