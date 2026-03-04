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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/admin/managerSystem")
public class SystemManagerController extends HttpServlet {

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
            ArrayList<Room> rooms = roomDAO.getAllRoom();

            // ===============================================
            // ======= RETRIEVE ROOM TYPES FROM DATABASE =====
            // ===============================================
            ArrayList<RoomType> roomTypes = roomTypeDAO.getAllRoomType();

            // ===============================================
            // ====== CREATE ROOM TYPE MAP FOR LOOKUP ========
            // ===============================================
            Map<Integer, RoomType> roomTypeMap = new HashMap<>();
            for (RoomType roomType : roomTypes) {
                roomTypeMap.put(roomType.getRoomTypeId(), roomType);
            }

            // ===============================================
            // ========= SET ATTRIBUTES FOR JSP ==============
            // ===============================================
            request.setAttribute("ROOM_LIST", rooms);
            request.setAttribute("ROOM_TYPE_LIST", roomTypeMap);

            // ===============================================
            // ============== LOGGING DATA ===================
            // ===============================================
            System.out.println("System manager data loaded:");
            System.out.println("- Rooms: " + (rooms != null ? rooms.size() : 0));
            System.out.println("- Room types: " + (roomTypes != null ? roomTypes.size() : 0));

            // ===============================================
            // ===== FORWARD TO SYSTEM MANAGER PAGE ==========
            // ===============================================
            request.getRequestDispatcher(IConstant.PAGE_SYSTEM_MANAGER).forward(request, response);

        } catch (Exception e) {
            // ===============================================
            // ============ EXCEPTION HANDLING ===============
            // ===============================================
            System.err.println("Error in SystemManagerController: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred while loading system data");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "System Manager Controller - Manages room and room type configuration";
    }
}