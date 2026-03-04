package dao;

import dto.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */
public class RoomDAO extends BaseDAO {

    // =============================================================
    // ===================== GET ALL ROOMS =========================
    // =============================================================
    public ArrayList<Room> getAllRoom() {
        ArrayList<Room> result = new ArrayList<Room>();
        String sql = "SELECT [RoomID], [RoomNumber], [RoomTypeID], [Description], [Status] FROM [HotelManagement].[dbo].[ROOM]";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs != null) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("RoomID"));
                    room.setRoomNumber(rs.getString("RoomNumber"));
                    room.setRoomTypeId(rs.getInt("RoomTypeID"));
                    room.setDescription(rs.getString("Description"));
                    room.setStatus(rs.getString("Status"));

                    result.add(room);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in getAllRoom: " + e.getMessage());
        }
        return result;
    }

    // =============================================================
    // ==================== GET ROOM BY ROOM ID ====================
    // =============================================================
    public Room getRoomById(int roomId) {
        Room room = null;
        String sql = "SELECT [RoomID], [RoomNumber], [RoomTypeID], [Description], [Status] FROM [HotelManagement].[dbo].[ROOM] WHERE [RoomID] = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.next()) {
                    int id = rs.getInt("RoomID");
                    String roomNumber = rs.getString("RoomNumber");
                    int roomTypeId = rs.getInt("RoomTypeID");
                    String description = rs.getString("Description");
                    String status = rs.getString("Status");

                    room = new Room(id, roomNumber, roomTypeId, description, status);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in getRoomById: " + e.getMessage());
        }
        return room;
    }

    // =============================================================
    // ========= FIND AVAILABLE ROOMS (DATE TIME) ==================
    // =============================================================
    public List<Room> findAvailableRooms(LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime, int roomTypeId) {
        List<Room> availableRooms = new ArrayList<>();
        String sql = "SELECT r.* FROM ROOM r "
                + "WHERE (r.RoomTypeID = ?) "
                + "AND r.RoomID NOT IN ("
                + "    SELECT b.RoomID FROM BOOKING b "
                + "    WHERE b.Status NOT IN ('Canceled', 'Checked-out') "
                + "    AND (b.CheckInDate < ? AND b.CheckOutDate > ?) "
                + ")";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);
            ps.setTimestamp(2, Timestamp.valueOf(checkOutDateTime));
            ps.setTimestamp(3, Timestamp.valueOf(checkInDateTime));

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        Room room = new Room();
                        room.setRoomId(rs.getInt("RoomID"));
                        room.setRoomNumber(rs.getString("RoomNumber"));
                        room.setRoomTypeId(rs.getInt("RoomTypeID"));
                        room.setDescription(rs.getString("Description"));
                        room.setStatus(rs.getString("Status"));

                        availableRooms.add(room);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in findAvailableRooms: " + e.getMessage());
        }
        return availableRooms;
    }

    // =============================================================
    // =============== GET TOTAL ROOM COUNT ========================
    // =============================================================
    public int getTotalRoomCount() {
        int count = 0;
        String sql = "SELECT COUNT(RoomID) AS TotalRooms FROM [dbo].[ROOM]";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("TotalRooms");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in getTotalRoomCount: " + e.getMessage());
        }
        return count;
    }

    // =============================================================
    // ===================== UPDATE ROOM ===========================
    // =============================================================
    public boolean updateRoom(Room room) {
        boolean result = false;
        String sql = "UPDATE [dbo].[ROOM] SET [RoomNumber] = ?, [RoomTypeID] = ?, [Description] = ?, [Status] = ? WHERE [RoomID] = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setInt(2, room.getRoomTypeId());
            ps.setString(3, room.getDescription());
            ps.setString(4, room.getStatus());
            ps.setInt(5, room.getRoomId());

            int rowsAffected = ps.executeUpdate();
            result = (rowsAffected > 0);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error in updateRoom: " + e.getMessage());
        }
        return result;
    }
}

