package dao;

import dto.BookingService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */
public class BookingServiceDAO {

    // =============================================================
    // ================= GET ALL BOOKING SERVICE ===================
    // =============================================================
    public ArrayList<BookingService> getAllBookingService() {
        ArrayList<BookingService> result = new ArrayList<>();

        String sql = "SELECT  [Booking_Service_ID],[BookingID],[ServiceID],[Quantity] ,[ServiceDate], [Status] FROM [HotelManagement].[dbo].[BOOKING_SERVICE]";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    int bookingServiceId = rs.getInt("Booking_Service_ID");
                    int bookingId = rs.getInt("BookingID");
                    int serviceId = rs.getInt("ServiceID");
                    int quantity = rs.getInt("Quantity");
                    java.time.LocalDate serviceDate = rs.getObject("ServiceDate", java.time.LocalDate.class);
                    int status = rs.getInt("Status");

                    BookingService bookingService = new BookingService(bookingServiceId, bookingId, serviceId, quantity, serviceDate, status);
                    result.add(bookingService);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============ GET BOOKING SERVICE BY BOOKING ID ==============
    // =============================================================
    public ArrayList<BookingService> getBookingServiceByBookingId(int bookingId) {

        ArrayList<BookingService> result = new ArrayList<>();
        String sql = "SELECT  [Booking_Service_ID],[BookingID],[ServiceID],[Quantity] ,[ServiceDate], [Status] FROM [HotelManagement].[dbo].[BOOKING_SERVICE] where BookingID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int bookingServiceId = rs.getInt("Booking_Service_ID");
                    int serviceId = rs.getInt("ServiceID");
                    int quantity = rs.getInt("Quantity");
                    java.time.LocalDate serviceDate = rs.getObject("ServiceDate", java.time.LocalDate.class);
                    int status = rs.getInt("Status");

                    BookingService bookingService = new BookingService(bookingServiceId, bookingId, serviceId, quantity, serviceDate, status);
                    result.add(bookingService);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================== ADD BOOKING SERVICE ======================
    // =============================================================
    public boolean addBookingService(BookingService bookingService) {
        String sql = "INSERT INTO [dbo].[BOOKING_SERVICE] (BookingID, ServiceID, Quantity, ServiceDate, Status) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingService.getBookingId());
            ps.setInt(2, bookingService.getServiceId());
            ps.setInt(3, bookingService.getQuantity());
            ps.setObject(4, bookingService.getServiceDate());
            ps.setInt(5, bookingService.getStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =============================================================
    // =========== DELETE BOOKING SERVICE BY BOOKING ID ============
    // =============================================================
    public boolean deleteByBookingId(int bookingId) {
        boolean result = false;
        String sql = "DELETE FROM [dbo].[BOOKING_SERVICE] WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ======== GET BOOKING SERVICES BY BOOKING ID (UPDATED) ======
    // =============================================================
    public ArrayList<BookingService> getBookingServicesByBookingId(int bookingId) {
        ArrayList<BookingService> list = new ArrayList<>();
        String sql = "SELECT [Booking_Service_ID], [BookingID], [ServiceID], [Quantity], [ServiceDate], [Status] "
                + "FROM [HotelManagement].[dbo].[BOOKING_SERVICE] "
                + "WHERE [BookingID] = ? "
                + "ORDER BY [ServiceDate]";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookingServiceId = rs.getInt("Booking_Service_ID");
                int serviceId = rs.getInt("ServiceID");
                int quantity = rs.getInt("Quantity");
                java.time.LocalDate serviceDate = rs.getObject("ServiceDate", java.time.LocalDate.class);
                int status = rs.getInt("Status");

                BookingService bs = new BookingService(bookingServiceId, bookingId, serviceId, quantity, serviceDate, status);
                list.add(bs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =============================================================
    // ====== DELETE BOOKING SERVICES BY BOOKING ID (UPDATED) =====
    // =============================================================
    public boolean deleteBookingServicesByBookingId(int bookingId) {
        String sql = "DELETE FROM [HotelManagement].[dbo].[BOOKING_SERVICE] WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =============================================================
    // ======= DELETE BOOKING SERVICE BY BOOKING ID & STATUS =======
    // =============================================================
    public boolean deleteByBookingIdAndStatus(int bookingId, int status) {
        String sql = "DELETE FROM [HotelManagement].[dbo].[BOOKING_SERVICE] "
                + "WHERE [BookingID] = ? AND [Status] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.setInt(2, status);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected >= 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
