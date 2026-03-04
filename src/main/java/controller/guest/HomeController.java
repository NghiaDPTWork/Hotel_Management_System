package controller.guest;

import dao.RoomDAO;
import dao.RoomTypeDAO;
import dto.Guest;
import dto.Room;
import dto.RoomType;
import dto.Staff;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ===============================================
        // ========== SESSION DATA RETRIEVAL =============
        // ===============================================
        Boolean isLogin = (Boolean) req.getSession().getAttribute("IS_LOGIN");
        Staff userStaff = (Staff) req.getSession().getAttribute("USER_STAFF");
        Guest userGuest = (Guest) req.getSession().getAttribute("USER_GUEST");

        // ===============================================
        // ========== USER SESSION ATTRIBUTES ============
        // ===============================================
        req.setAttribute("IS_LOGIN", isLogin != null ? isLogin : false);
        req.setAttribute("USER_STAFF", userStaff);
        req.setAttribute("USER_GUEST", userGuest);

        // ===============================================
        // ======== ROOM AND ROOM TYPE RETRIEVAL =========
        // ===============================================
        ArrayList<Room> roomList = roomDAO.getAllRoom();
        ArrayList<RoomType> roomTypeList = roomTypeDAO.getAllRoomType();

        req.setAttribute("ROOM_LIST", roomList);
        req.setAttribute("ROOM_TYPE_LIST", roomTypeList);

        // ===============================================
        // ============ FORWARD TO HOME PAGE =============
        // ===============================================
        req.getRequestDispatcher(IConstant.PAGE_HOME).forward(req, resp);
    }
}
