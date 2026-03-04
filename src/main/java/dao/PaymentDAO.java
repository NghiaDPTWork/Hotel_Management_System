package dao;

import dto.Payment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */

public class PaymentDAO {

    // =============================================================
    // =================== GET PAYMENT LIST ========================
    // =============================================================
    public ArrayList<Payment> getPaymentList() {
        ArrayList<Payment> result = new ArrayList<>();
        String sql = "SELECT [PaymentID], [BookingID], [PaymentDate], [Amount], [PaymentMethod], [Status] FROM [HotelManagement].[dbo].[PAYMENT]";
        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs != null) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("PaymentID"));
                    payment.setBookingId(rs.getInt("BookingID"));
                    payment.setPaymentDate(rs.getObject("PaymentDate", LocalDate.class));
                    payment.setAmount(rs.getDouble("Amount"));
                    payment.setPaymentMethod(rs.getString("PaymentMethod"));
                    payment.setStatus(rs.getString("Status"));
                    result.add(payment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =============================================================
    // ===================== ADD PAYMENT ===========================
    // =============================================================
    public boolean addPayment(Payment payment) {
        boolean result = false;
        String sql = "INSERT INTO [HotelManagement].[dbo].[PAYMENT] ([BookingID], [PaymentDate], [Amount], [PaymentMethod], [Status]) VALUES (?, ?, ?, ?, ?)";
        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, payment.getBookingId());
            ps.setObject(2, payment.getPaymentDate());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getStatus());
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =============================================================
    // ============ DELETE PAYMENT BY BOOKING ID ===================
    // =============================================================
    public boolean deleteByBookingId(int bookingId) {
        boolean result = false;
        String sql = "DELETE FROM [dbo].[PAYMENT] WHERE [BookingID] = ?";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =============================================================
    // ============== GET PAYMENT BY BOOKING ID ====================
    // =============================================================
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT [PaymentID], [BookingID], [Amount], [PaymentMethod], [PaymentDate], [Status] "
                + "FROM [HotelManagement].[dbo].[PAYMENT] "
                + "WHERE [BookingID] = ?";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("PaymentID"));
                payment.setBookingId(rs.getInt("BookingID"));
                payment.setAmount(rs.getDouble("Amount"));
                payment.setPaymentMethod(rs.getString("PaymentMethod"));
                payment.setStatus(rs.getString("Status"));

                java.sql.Timestamp paymentDate = rs.getTimestamp("PaymentDate");
                if (paymentDate != null) {
                    payment.setPaymentDate(paymentDate.toLocalDateTime().toLocalDate());
                }

                return payment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =============================================================
    // =========== GET ALL PAYMENTS BY BOOKING ID ==================
    // =============================================================
    public ArrayList<Payment> getPaymentsByBookingId(int bookingId) {
        ArrayList<Payment> result = new ArrayList<>();
        String sql = "SELECT [PaymentID], [BookingID], [PaymentDate], [Amount], [PaymentMethod], [Status] "
                + "FROM [HotelManagement].[dbo].[PAYMENT] "
                + "WHERE [BookingID] = ? "
                + "ORDER BY [PaymentDate] ASC";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("PaymentID"));
                    payment.setBookingId(rs.getInt("BookingID"));
                    payment.setPaymentDate(rs.getObject("PaymentDate", LocalDate.class));
                    payment.setAmount(rs.getDouble("Amount"));
                    payment.setPaymentMethod(rs.getString("PaymentMethod"));
                    payment.setStatus(rs.getString("Status"));
                    result.add(payment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =============================================================
    // ============ GET REVENUE BY DATE (For Dashboard) ============
    // =============================================================
    public double getRevenueByDate(LocalDate date) {
        double totalRevenue = 0;
        // Gi? s? 'Paid' là tr?ng thái thanh toán thành công
        String sql = "SELECT SUM(Amount) AS Total FROM [dbo].[PAYMENT] "
                + "WHERE CONVERT(date, PaymentDate) = ? AND Status != 'Failed'";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, date);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalRevenue = rs.getDouble("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    // =============================================================
    // =========== GET INVOICE COUNT BY DATE (For Dashboard) =======
    // =============================================================
    public int getInvoiceCountByDate(LocalDate date) {
        int count = 0;
        String sql = "SELECT COUNT(PaymentID) AS InvoiceCount FROM [dbo].[PAYMENT] "
                + "WHERE CONVERT(date, PaymentDate) = ? AND Status != 'Failed'";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, date);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("InvoiceCount");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // =============================================================
    // ========== GET RECENT PAYMENTS (For Activity Feed) ==========
    // =============================================================
    public ArrayList<Payment> getRecentPayments(int limit) {
        ArrayList<Payment> result = new ArrayList<>();
        String sql = "SELECT TOP (?) [PaymentID], [BookingID], [PaymentDate], [Amount], [PaymentMethod], [Status] "
                + "FROM [HotelManagement].[dbo].[PAYMENT] ORDER BY [PaymentDate] DESC";

        try ( Connection con = DBConnection.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment();
                    payment.setPaymentId(rs.getInt("PaymentID"));
                    payment.setBookingId(rs.getInt("BookingID"));
                    payment.setPaymentDate(rs.getObject("PaymentDate", LocalDate.class));
                    payment.setAmount(rs.getDouble("Amount"));
                    payment.setPaymentMethod(rs.getString("PaymentMethod"));
                    payment.setStatus(rs.getString("Status"));
                    result.add(payment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
