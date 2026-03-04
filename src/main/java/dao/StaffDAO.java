package dao;

import dto.Staff;
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
public class StaffDAO extends BaseDAO {

    // =============================================================
    // ===================== GET ALL STAFF =========================
    // =============================================================
    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> result = new ArrayList<>();
        String sql = "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] FROM [HotelManagement].[dbo].[STAFF]";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("StaffID");
                String fullName = rs.getString("FullName");
                String role = rs.getString("Role");
                String username = rs.getString("Username");
                String passwordHash = rs.getString("PasswordHash");
                String phone = rs.getString("Phone");
                String email = rs.getString("Email");
                Boolean status = rs.getBoolean("Status");

                Staff staff = new Staff(id, fullName, role, username, passwordHash, phone, email, status);
                result.add(staff);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getAllStaff: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getAllStaff: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // =========== GET STAFF BY USERNAME AND PASSWORD ==============
    // =============================================================
    public Staff getStaffByUsernameAndPassword(String username, String password) {
        Staff staff = null;
        String sql = "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] "
                + "FROM [HotelManagement].[dbo].[STAFF] "
                + "WHERE Username = ? AND [PasswordHash] = ? AND [Status] = 1";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    staff = new Staff();
                    staff.setStaffId(rs.getInt("StaffID"));
                    staff.setFullName(rs.getString("FullName"));
                    staff.setRole(rs.getString("Role"));
                    staff.setUsername(rs.getString("Username"));
                    staff.setPasswordHash(rs.getString("PasswordHash"));
                    staff.setPhone(rs.getString("Phone"));
                    staff.setEmail(rs.getString("Email"));
                    staff.setStatus(rs.getBoolean("Status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getStaffByUsernameAndPassword: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getStaffByUsernameAndPassword: " + e.getMessage());
            e.printStackTrace();
        }
        return staff;
    }

    // =============================================================
    // ====================== ADD STAFF ============================
    // =============================================================
    public boolean addStaff(Staff staff) {
        boolean result = false;
        String sql = "INSERT INTO [HotelManagement].[dbo].[STAFF] ([FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status]) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getRole());
            ps.setString(3, staff.getUsername());
            ps.setString(4, staff.getPasswordHash());
            ps.setString(5, staff.getPhone());
            ps.setString(6, staff.getEmail());
            ps.setBoolean(7, staff.isStatus());

            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in addStaff: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in addStaff: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ===================== UPDATE STAFF ==========================
    // =============================================================
    public boolean updateStaff(Staff staff) {
        boolean result = false;
        String sql = "UPDATE [HotelManagement].[dbo].[STAFF] SET [FullName] = ?, [Role] = ?, [Username] = ?, [PasswordHash] = ?, [Phone] = ?, [Email] = ?, [Status] = ? WHERE [StaffID] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getRole());
            ps.setString(3, staff.getUsername());
            ps.setString(4, staff.getPasswordHash());
            ps.setString(5, staff.getPhone());
            ps.setString(6, staff.getEmail());
            ps.setBoolean(7, staff.isStatus());
            ps.setInt(8, staff.getStaffId());

            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in updateStaff: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateStaff: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ===================== DELETE STAFF ==========================
    // =============================================================
    public boolean deleteStaff(int staffId) {
        boolean result = false;
        String sql = "DELETE FROM [HotelManagement].[dbo].[STAFF] WHERE [StaffID] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, staffId);
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

    // =============================================================
    // ================== IS USERNAME EXIST ========================
    // =============================================================
    public boolean isUsernameExist(String username) {
        boolean result = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[STAFF] WHERE [Username] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    result = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in isUsernameExist: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in isUsernameExist: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================== GET STAFFS BY ROLE =======================
    // =============================================================
    public ArrayList<Staff> getStaffsByRole(String role) {
        ArrayList<Staff> result = new ArrayList<>();
        String sql = "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] FROM [HotelManagement].[dbo].[STAFF] WHERE [Role] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("StaffID");
                    String fullName = rs.getString("FullName");
                    String staffRole = rs.getString("Role");
                    String username = rs.getString("Username");
                    String passwordHash = rs.getString("PasswordHash");
                    String phone = rs.getString("Phone");
                    String email = rs.getString("Email");
                    Boolean status = rs.getBoolean("Status");

                    Staff staff = new Staff(id, fullName, staffRole, username, passwordHash, phone, email, status);
                    result.add(staff);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getStaffsByRole: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getStaffsByRole: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================== SEARCH STAFFS BY KEYWORD =================
    // =============================================================
    public ArrayList<Staff> searchStaffByKeyword(String searchQuery) {
        ArrayList<Staff> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] "
                + "FROM [HotelManagement].[dbo].[STAFF] WHERE 1=1"
        );

        boolean hasSearchQuery = searchQuery != null && !searchQuery.trim().isEmpty();
        if (hasSearchQuery) {
            sql.append(" AND ([FullName] LIKE ? OR [Phone] LIKE ? OR [Email] LIKE ?)");
        }

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (hasSearchQuery) {
                String query = "%" + searchQuery.trim() + "%";
                ps.setString(1, query);
                ps.setString(2, query);
                ps.setString(3, query);
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Staff staff = new Staff(
                            rs.getInt("StaffID"),
                            rs.getString("FullName"),
                            rs.getString("Role"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getBoolean("Status")
                    );
                    result.add(staff);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in searchStaffByKeyword: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in searchStaffByKeyword: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ========== SEARCH STAFFS BY KEYWORD AND STATUS ==============
    // =============================================================
    public ArrayList<Staff> searchStaffByKeywordAndStatus(String searchQuery, int status) {
        ArrayList<Staff> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] "
                + "FROM [HotelManagement].[dbo].[STAFF] WHERE [Status] = ?"
        );

        boolean hasSearchQuery = searchQuery != null && !searchQuery.trim().isEmpty();
        if (hasSearchQuery) {
            sql.append(" AND ([FullName] LIKE ? OR [Phone] LIKE ? OR [Email] LIKE ?)");
        }

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setInt(1, status);

            if (hasSearchQuery) {
                String query = "%" + searchQuery.trim() + "%";
                ps.setString(2, query);
                ps.setString(3, query);
                ps.setString(4, query);
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Staff staff = new Staff(
                            rs.getInt("StaffID"),
                            rs.getString("FullName"),
                            rs.getString("Role"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getBoolean("Status")
                    );
                    result.add(staff);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in searchStaffByKeywordAndStatus: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in searchStaffByKeywordAndStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
// ================= GET STAFF BY ID ===========================
// =============================================================
    public Staff getStaffById(int staffId) {
        Staff staff = null;
        String sql = "SELECT [StaffID], [FullName], [Role], [Username], [PasswordHash], [Phone], [Email], [Status] "
                + "FROM [HotelManagement].[dbo].[STAFF] "
                + "WHERE [StaffID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    staff = new Staff(
                            rs.getInt("StaffID"),
                            rs.getString("FullName"),
                            rs.getString("Role"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getBoolean("Status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getStaffById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getStaffById: " + e.getMessage());
            e.printStackTrace();
        }
        return staff;
    }

    // =============================================================
    // ================== IS EMAIL EXIST ===========================
    // =============================================================
    public boolean isEmailExist(String email) {
        boolean result = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[STAFF] WHERE [Email] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    result = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in isEmailExist: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in isEmailExist: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================== IS PHONE EXIST ===========================
    // =============================================================
    public boolean isPhoneExist(String phone) {
        boolean result = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[STAFF] WHERE [Phone] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phone);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    result = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in isPhoneExist: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in isPhoneExist: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
