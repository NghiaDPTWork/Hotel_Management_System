package dao;

import dto.RoomType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */

public class RoomTypeDAO {

    // =============================================================
    // ================== GET ALL ROOM TYPE ========================
    // =============================================================
    public ArrayList<RoomType> getAllRoomType() {
        ArrayList<RoomType> result = new ArrayList<RoomType>();
        String sql = "SELECT [RoomTypeID], [TypeName], [Capacity], [PricePerNight] FROM [HotelManagement].[dbo].[ROOM_TYPE]";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("RoomTypeID");
                    String typeName = rs.getString("TypeName");
                    int capacity = rs.getInt("Capacity");
                    BigDecimal pricePerNight = rs.getBigDecimal("PricePerNight");

                    RoomType roomType = new RoomType(id, typeName, capacity, pricePerNight);
                    result.add(roomType);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getAllRoomType: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getAllRoomType: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================ GET ROOM TYPE BY ROOM TYPE ID ==============
    // =============================================================
    public RoomType getRoomTypeById(int roomTypeId) {
        RoomType roomType = null;
        String sql = "SELECT [RoomTypeID], [TypeName], [Capacity], [PricePerNight] FROM ROOM_TYPE WHERE [RoomTypeID] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.next()) {
                    int id = rs.getInt("RoomTypeID");
                    String typeName = rs.getString("TypeName");
                    int capacity = rs.getInt("Capacity");
                    BigDecimal pricePerNight = rs.getBigDecimal("PricePerNight");

                    roomType = new RoomType(id, typeName, capacity, pricePerNight);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getRoomTypeById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getRoomTypeById: " + e.getMessage());
            e.printStackTrace();
        }
        return roomType;
    }

}