package dao;

import dto.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */

public class GuestDAO {

    // =============================================================
    // ====================== GET ALL GUEST ========================
    // =============================================================
    public ArrayList<Guest> getAllGuest() {
        ArrayList<Guest> result = new ArrayList<>();
        String sql = "SELECT [GuestID], [FullName], [Phone], [Email], [PasswordHash], [Address], [IDNumber], [DateOfBirth] FROM [HotelManagement].[dbo].[GUEST]";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs != null) {
                while (rs.next()) {
                    int guestId = rs.getInt("GuestID");
                    String fullName = rs.getString("FullName");
                    String phone = rs.getString("Phone");
                    String email = rs.getString("Email");
                    String passwordHash = rs.getString("PasswordHash");
                    String address = rs.getString("Address");
                    String idNumber = rs.getString("IDNumber");
                    String dateOfBirth = rs.getString("DateOfBirth");

                    Guest guest = new Guest(guestId, fullName, phone, email, address, idNumber, dateOfBirth, passwordHash);
                    result.add(guest);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getAllGuest: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getAllGuest: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ========= GET GUEST BY USERNAME AND PASSWORD ================
    // =============================================================
    public Guest getGuestByUsernameAndPassword(String username, String password) {
        Guest guest = null;
        String sql = "SELECT * FROM [HotelManagement].[dbo].[GUEST] WHERE [IDNumber] = ? AND [PasswordHash] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.next()) {
                    int guestId = rs.getInt("GuestID");
                    String fullName = rs.getString("FullName");
                    String phone = rs.getString("Phone");
                    String email = rs.getString("Email");
                    String passwordHash = rs.getString("PasswordHash");
                    String address = rs.getString("Address");
                    String idNumber = rs.getString("IDNumber");
                    String dateOfBirth = rs.getString("DateOfBirth");

                    guest = new Guest(guestId, fullName, phone, email, address, idNumber, dateOfBirth, passwordHash);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getGuestByUsernameAndPassword: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getGuestByUsernameAndPassword: " + e.getMessage());
            e.printStackTrace();
        }
        return guest;
    }

    // =============================================================
    // =================== GET GUEST BY GUEST ID =========================
    // =============================================================
    public Guest getGuestById(int guestId) {
        Guest guest = null;
        String sql = "SELECT * FROM [HotelManagement].[dbo].[GUEST] WHERE GuestID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, guestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.next()) {
                    String fullName = rs.getString("FullName");
                    String phone = rs.getString("Phone");
                    String email = rs.getString("Email");
                    String address = rs.getString("Address");
                    String idNumber = rs.getString("IDNumber");
                    String dateOfBirth = rs.getString("DateOfBirth");

                    guest = new Guest(guestId, fullName, phone, email, address, idNumber, dateOfBirth, "null");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getGuestById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getGuestById: " + e.getMessage());
            e.printStackTrace();
        }
        return guest;
    }

    // =============================================================
    // ============== CHECK DUPLICATE ID NUMBER ====================
    // =============================================================
    public boolean checkDuplicateIdNumber(String idNumber) {
        boolean isDuplicate = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[GUEST] WHERE [IDNumber] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    isDuplicate = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in checkDuplicateIdNumber: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in checkDuplicateIdNumber: " + e.getMessage());
            e.printStackTrace();
        }
        return isDuplicate;
    }

    // =============================================================
    // ================ CHECK DUPLICATE EMAIL ======================
    // =============================================================
    public boolean checkDuplicateEmail(String email) {
        boolean isDuplicate = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[GUEST] WHERE [Email] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    isDuplicate = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in checkDuplicateEmail: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in checkDuplicateEmail: " + e.getMessage());
            e.printStackTrace();
        }
        return isDuplicate;
    }

    // =============================================================
    // ====================== ADD GUEST ============================
    // =============================================================
    public boolean addGuest(Guest guest) {
        boolean result = false;
        String sql = "INSERT INTO [HotelManagement].[dbo].[GUEST] ([FullName], [Phone], [Email], [PasswordHash], [Address], [IDNumber], [DateOfBirth]) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Fullname: " + guest.getFullName());
            System.out.println("Address: " + guest.getAddress());

            ps.setString(1, guest.getFullName());
            ps.setString(2, guest.getPhone());
            ps.setString(3, guest.getEmail());
            ps.setString(4, guest.getPasswordHash());
            ps.setString(5, guest.getAddress());
            ps.setString(6, guest.getIdNumber());
            ps.setObject(7, LocalDate.parse(guest.getDateOfBirth()));

            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error in addGuest: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in addGuest: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ==================== DELETE GUEST | STAFF ===========================
    // =============================================================
    public boolean deleteStaff(int id) {
        boolean result = false;
        String sql = "DELETE FROM [HotelManagement].[dbo].[GUEST] WHERE [GuestID] = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error in deleteStaff: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in deleteStaff: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}