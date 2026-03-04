package dao;

import dto.Booking;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */
public class BookingDAO {

    // =============================================================
    // ===================== GET ALL BOOKINGS ======================
    // =============================================================
    public ArrayList<Booking> getAllBookings() {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT TOP (1000) [BookingID], [GuestID], [RoomID], [CheckInDate], [CheckOutDate], [BookingDate], [Status] FROM [HotelManagement].[dbo].[BOOKING]";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            if (rs != null) {
                while (rs.next()) {
                    int bookingId = rs.getInt("BookingID");
                    int guestId = rs.getInt("GuestID");
                    int roomId = rs.getInt("RoomID");
                    String status = rs.getString("Status");

                    LocalDateTime checkInDate = rs.getObject("CheckInDate", LocalDateTime.class);
                    LocalDateTime checkOutDate = rs.getObject("CheckOutDate", LocalDateTime.class);
                    LocalDate bookingDate = rs.getObject("BookingDate", LocalDate.class);

                    Booking booking = new Booking();
                    booking.setBookingId(bookingId);
                    booking.setGuestId(guestId);
                    booking.setRoomId(roomId);
                    booking.setStatus(status);
                    booking.setCheckInDate(checkInDate);
                    booking.setCheckOutDate(checkOutDate);
                    booking.setBookingDate(bookingDate);

                    result.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getAllBookings: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getAllBookings: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================ ADD BOOKING AND GET BOOKING ID =============
    // =============================================================
    public int addBooking(Booking booking) {
        int generatedBookingId = -1;
        String sql = "INSERT INTO [dbo].[BOOKING] (GuestID, RoomID, CheckInDate, CheckOutDate, BookingDate, Status) VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getGuestId());
            ps.setInt(2, booking.getRoomId());
            ps.setObject(3, booking.getCheckInDate());
            ps.setObject(4, booking.getCheckOutDate());
            ps.setObject(5, booking.getBookingDate());
            ps.setString(6, booking.getStatus());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try ( ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedBookingId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in addBooking: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in addBooking: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedBookingId;
    }

    // =============================================================
    // ================== GET BOOKING BY BOOKINGID =================
    // =============================================================
    public Booking getBookingById(int bookingId) {
        Booking result = null;
        String sql = "SELECT [BookingID], [GuestID], [RoomID], [CheckInDate], [CheckOutDate], [BookingDate], [Status] FROM [HotelManagement].[dbo].[BOOKING] WHERE BookingID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.next()) {
                    int guestId = rs.getInt("GuestID");
                    int roomId = rs.getInt("RoomID");
                    String status = rs.getString("Status");
                    LocalDateTime checkInDate = rs.getObject("CheckInDate", LocalDateTime.class);
                    LocalDateTime checkOutDate = rs.getObject("CheckOutDate", LocalDateTime.class);
                    LocalDate bookingDate = rs.getObject("BookingDate", LocalDate.class);

                    result = new Booking(bookingId, guestId, roomId, checkInDate, checkOutDate, bookingDate, status);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getBookingById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getBookingById: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============== GET BOOKINGS BY GUEST ID =====================
    // =============================================================
    public ArrayList<Booking> getBookingByGuestId(int guestId) {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT [BookingID], [GuestID], [RoomID], [CheckInDate], [CheckOutDate], [BookingDate], [Status] FROM [HotelManagement].[dbo].[BOOKING] WHERE GuestID = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, guestId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        int bookingId = rs.getInt("BookingID");
                        int roomId = rs.getInt("RoomID");
                        String status = rs.getString("Status");
                        LocalDateTime checkInDate = rs.getObject("CheckInDate", LocalDateTime.class);
                        LocalDateTime checkOutDate = rs.getObject("CheckOutDate", LocalDateTime.class);
                        LocalDate bookingDate = rs.getObject("BookingDate", LocalDate.class);

                        Booking booking = new Booking();
                        booking.setBookingId(bookingId);
                        booking.setGuestId(guestId);
                        booking.setRoomId(roomId);
                        booking.setStatus(status);
                        booking.setCheckInDate(checkInDate);
                        booking.setCheckOutDate(checkOutDate);
                        booking.setBookingDate(bookingDate);

                        result.add(booking);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getBookingByGuestId: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getBookingByGuestId: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ======= GET BOOKINGS BY DATE RANGE (OVERLAP CHECK) ==========
    // =============================================================
    public ArrayList<Booking> getBookingByCheckInCheckOutDate(LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT [BookingID], [GuestID], [RoomID], [CheckInDate], [CheckOutDate], [BookingDate], [Status] "
                + "FROM [HotelManagement].[dbo].[BOOKING] "
                + "WHERE [Status] NOT IN ('Canceled', 'Checked-out') "
                + "AND ([CheckInDate] < ? AND [CheckOutDate] > ?)";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(checkOutDate));
            ps.setTimestamp(2, Timestamp.valueOf(checkInDate));

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        int bookingId = rs.getInt("BookingID");
                        int guestId = rs.getInt("GuestID");
                        int roomId = rs.getInt("RoomID");
                        String status = rs.getString("Status");
                        LocalDateTime dbCheckInDate = rs.getObject("CheckInDate", LocalDateTime.class);
                        LocalDateTime dbCheckOutDate = rs.getObject("CheckOutDate", LocalDateTime.class);
                        LocalDate bookingDate = rs.getObject("BookingDate", LocalDate.class);

                        Booking booking = new Booking();
                        booking.setBookingId(bookingId);
                        booking.setGuestId(guestId);
                        booking.setRoomId(roomId);
                        booking.setStatus(status);
                        booking.setCheckInDate(dbCheckInDate);
                        booking.setCheckOutDate(dbCheckOutDate);
                        booking.setBookingDate(bookingDate);

                        result.add(booking);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getBookingByCheckInCheckOutDate: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getBookingByCheckInCheckOutDate: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================ UPDATE BOOKING STATUS ======================
    // =============================================================
    public boolean updateBookingStatus(int bookingId, String status) {
        boolean result = false;
        String sql = "UPDATE [dbo].[BOOKING] SET Status = ? WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);
            result = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error in updateBookingStatus: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateBookingStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================= DELETE BOOKING BY ID ======================
    // =============================================================
    public boolean deleteBooking(int bookingId) {
        boolean result = false;
        String sql = "DELETE FROM [dbo].[BOOKING] WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            result = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error in deleteBooking: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in deleteBooking: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // =================== UPDATE BOOKING ==========================
    // =============================================================
    public boolean updateBooking(Booking booking) {
        boolean result = false;
        String sql = "UPDATE [dbo].[BOOKING] SET [GuestID] = ?, [RoomID] = ?, [CheckInDate] = ?, [CheckOutDate] = ?, [BookingDate] = ?, [Status] = ? WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, booking.getGuestId());
            ps.setInt(2, booking.getRoomId());
            ps.setObject(3, booking.getCheckInDate());
            ps.setObject(4, booking.getCheckOutDate());
            ps.setObject(5, booking.getBookingDate());
            ps.setString(6, booking.getStatus());
            ps.setInt(7, booking.getBookingId());

            int rowsAffected = ps.executeUpdate();
            result = (rowsAffected > 0);

            if (result) {
                System.out.println("? Booking updated successfully: ID=" + booking.getBookingId());
            } else {
                System.out.println("? No booking found with ID: " + booking.getBookingId());
            }

        } catch (SQLException e) {
            System.err.println("Database error in updateBooking: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateBooking: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
