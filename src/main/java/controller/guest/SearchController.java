package controller.guest;

import dao.RoomDAO;
import dao.RoomTypeDAO;
import dto.Room;
import dto.RoomType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Locale;
import util.IConstant;

/**
 *
 * @author TR_NGHIA
 */

@WebServlet("/search")
public class SearchController extends HttpServlet {

    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        roomTypeDAO = new RoomTypeDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ===============================================
        // ============ ENCODING CONFIGURATION ===========
        // ===============================================
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // ===============================================
        // ========== SEARCH PARAMETERS RETRIEVAL ========
        // ===============================================
        String arrivalDateStr = req.getParameter("arrivalDate");
        String departureDateStr = req.getParameter("departureDate");
        String adultsStr = req.getParameter("adults");
        String childrenStr = req.getParameter("children");
        String roomTypeIdStr = req.getParameter("roomTypeId");

        System.out.println("Room Type ID: " + roomTypeIdStr);

        // ===============================================
        // ========== DATE AND GUEST PROCESSING ==========
        // ===============================================
        LocalDate checkInDate = LocalDate.parse(arrivalDateStr);
        LocalDate checkOutDate = LocalDate.parse(departureDateStr);

        if (childrenStr == null || childrenStr.trim().isEmpty()) {
            childrenStr = "0";
        }

        int numberOfGuests = Integer.parseInt(adultsStr) + Integer.parseInt(childrenStr);
        int roomTypeId = 0;

        if (roomTypeIdStr != null && !roomTypeIdStr.trim().isEmpty() && !roomTypeIdStr.equals("null")) {
            roomTypeId = Integer.parseInt(roomTypeIdStr);
        }

        LocalDateTime checkInDateTime = checkInDate.atStartOfDay();
        LocalDateTime checkOutDateTime = checkOutDate.plusDays(1).atStartOfDay();

        // ===============================================
        // ========== INITIALIZE DATA STRUCTURES =========
        // ===============================================
        List<Room> availableRooms = new ArrayList<>();
        List<RoomType> allRoomTypes = new ArrayList<>();
        String errorMessage = "";

        try {
            // ===============================================
            // ======== AVAILABLE ROOMS DATA RETRIEVAL =======
            // ===============================================
            availableRooms = roomDAO.findAvailableRooms(checkInDateTime, checkOutDateTime, roomTypeId);
            allRoomTypes = roomTypeDAO.getAllRoomType();

            // ===============================================
            // ========== ROOM TYPE MAP CONSTRUCTION =========
            // ===============================================
            Map<Integer, RoomType> roomTypeMap = new HashMap<>();
            for (RoomType rt : allRoomTypes) {
                roomTypeMap.put(rt.getRoomTypeId(), rt);
            }

            // ===============================================
            // ============ ROOM DATA PROCESSING =============
            // ===============================================
            List<Map<String, Object>> processedRooms = new ArrayList<>();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            for (int i = 0; i < availableRooms.size(); i++) {
                Room room = availableRooms.get(i);
                Map<String, Object> roomInfo = new HashMap<>();

                roomInfo.put("room", room);

                RoomType roomType = roomTypeMap.get(room.getRoomTypeId());
                if (roomType != null) {
                    roomInfo.put("typeName", roomType.getTypeName());
                    roomInfo.put("capacity", roomType.getCapacity());
                    roomInfo.put("priceFormatted", String.format("%,.2f VND", roomType.getPricePerNight()));
                } else {
                    roomInfo.put("typeName", "Unknown");
                    roomInfo.put("capacity", 0);
                    roomInfo.put("priceFormatted", "N/A");
                }

                int imageNumber = i + 1;
                String imageUrl = req.getContextPath() + "/public/img/room-" + imageNumber + ".jpg";
                String fallbackImageUrl = "https://images.unsplash.com/photo-1590490359854-dfba59392828?w=800";
                roomInfo.put("imageUrl", imageUrl);
                roomInfo.put("fallbackImageUrl", fallbackImageUrl);

                processedRooms.add(roomInfo);
            }

            // ===============================================
            // ======= SELECTED ROOM TYPE NAME HANDLING ======
            // ===============================================
            String selectedRoomTypeName = "All Types";
            if (roomTypeId != 0) {
                RoomType selectedType = roomTypeMap.get(roomTypeId);
                if (selectedType != null) {
                    selectedRoomTypeName = selectedType.getTypeName();
                } else {
                    selectedRoomTypeName = "Invalid Type";
                }
            }

            // ===============================================
            // ========= SET ATTRIBUTES FOR SUCCESS ==========
            // ===============================================
            req.setAttribute("PROCESSED_ROOMS", processedRooms);
            req.setAttribute("ROOM_COUNT", availableRooms.size());
            req.setAttribute("CHECK_IN", arrivalDateStr);
            req.setAttribute("CHECK_OUT", departureDateStr);
            req.setAttribute("GUESTS", numberOfGuests);
            req.setAttribute("SELECTED_ROOM_TYPE_NAME", selectedRoomTypeName);
            req.setAttribute("ROOM_TYPE_ID", roomTypeIdStr);

            // ===============================================
            // ======== FORWARD TO SEARCH RESULT PAGE ========
            // ===============================================
            req.getRequestDispatcher(IConstant.PAGE_SEARCH_RESULT).forward(req, resp);

        } catch (Exception e) {
            // ===============================================
            // ========== GENERAL ERROR HANDLING =============
            // ===============================================
            errorMessage = "An error occurred while searching for rooms. Please try again later.";
            System.out.println("Error during room search: " + e.getMessage());
            e.printStackTrace();

            req.setAttribute("ERROR_MESSAGE", errorMessage);
            req.setAttribute("ROOM_COUNT", 0);
            req.setAttribute("CHECK_IN", arrivalDateStr);
            req.setAttribute("CHECK_OUT", departureDateStr);
            req.setAttribute("GUESTS", numberOfGuests);
            req.setAttribute("SELECTED_ROOM_TYPE_NAME", "All Types");

            try {
                req.setAttribute("ROOM_TYPE_LIST", roomTypeDAO.getAllRoomType());
            } catch (Exception exp) {
                System.out.println("Error fetching room types: " + exp.getMessage());
            }

            req.getRequestDispatcher(IConstant.PAGE_SEARCH_RESULT).forward(req, resp);
        }
    }
}
