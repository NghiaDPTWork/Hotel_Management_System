package controller.admin;

import dao.RoomDAO;
import dao.RoomTypeDAO;
import dto.Room;
import dto.RoomType;
import util.IConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/managerRooms")
public class RoomsManagerController extends HttpServlet {

    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            // ===============================================
            // ========= RETRIEVE ROOMS FROM DATABASE ========
            // ===============================================
            ArrayList<Room> allRooms = roomDAO.getAllRoom();

            // ===============================================
            // ======= RETRIEVE ROOM TYPES FROM DATABASE =====
            // ===============================================
            ArrayList<RoomType> allRoomTypes = roomTypeDAO.getAllRoomType();

            // ===============================================
            // ========= SET ATTRIBUTES FOR JSP ==============
            // ===============================================
            request.setAttribute("ALL_ROOMS_LIST", allRooms);
            request.setAttribute("ALL_ROOM_TYPES_LIST", allRoomTypes);

            // ===============================================
            // ============== LOGGING DATA ===================
            // ===============================================
            System.out.println("Rooms loaded: " + (allRooms != null ? allRooms.size() : 0) + " rooms");
            System.out.println("Room types loaded: " + (allRoomTypes != null ? allRoomTypes.size() : 0) + " types");

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in RoomsManagerController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("ERROR_MESSAGE", "An error occurred while loading rooms data!");
        }

        // ===============================================
        // ====== FORWARD TO ROOMS MANAGER PAGE ==========
        // ===============================================
        request.getRequestDispatcher(IConstant.PAGE_ROOMS_MANAGER).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Rooms Manager Controller - Handles room and room type management display";
    }
}