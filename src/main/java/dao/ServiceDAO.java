package dao;

import dto.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DBConnection;

/**
 *
 * @author TR_NGHIA
 */
public class ServiceDAO {

    // =============================================================
    // ==================== GET ALL SERVICE ========================
    // =============================================================
    public ArrayList<Service> getAllService() {
        ArrayList<Service> result = new ArrayList<>();
        String sql = "SELECT [ServiceID] ,[ServiceName] ,[ServiceType] ,[Price] FROM [HotelManagement].[dbo].[SERVICE]";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int serviceId = rs.getInt("ServiceID");
                    String serviceName = rs.getString("ServiceName");
                    String serviceType = rs.getString("ServiceType");
                    java.math.BigDecimal price = rs.getBigDecimal("Price");
                    Service service = new Service(serviceId, serviceName, serviceType, price);
                    result.add(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    // =============================================================
    // =================== GET SERVICE BY SERVICE ID ===============
    // =============================================================
    public Service getServiceById(int serviceId) {
        Service service = null;
        String sql = "SELECT [ServiceID] ,[ServiceName] ,[ServiceType] ,[Price] FROM [HotelManagement].[dbo].[SERVICE] where ServiceID = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    String serviceName = rs.getString("ServiceName");
                    String serviceType = rs.getString("ServiceType");
                    java.math.BigDecimal price = rs.getBigDecimal("Price");
                    service = new Service(serviceId, serviceName, serviceType, price);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return service;
    }

    // =============================================================
    // ==================== ADD NEW SERVICE ========================
    // =============================================================
    public boolean addService(Service service) {
        boolean result = false;
        String sql = "INSERT INTO [HotelManagement].[dbo].[SERVICE] "
                + "([ServiceName], [ServiceType], [Price]) "
                + "VALUES (?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getServiceType());
            ps.setBigDecimal(3, service.getPrice());

            int rowsAffected = ps.executeUpdate();
            result = (rowsAffected > 0);

            if (result) {
                System.out.println("Service added successfully: " + service.getServiceName());
            }

        } catch (Exception e) {
            System.err.println("Error adding service: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // =============================================================
    // ============ CHECK IF SERVICE NAME EXISTS ===================
    // =============================================================
    public boolean isServiceNameExists(String serviceName) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM [HotelManagement].[dbo].[SERVICE] WHERE LOWER(ServiceName) = LOWER(?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, serviceName.trim());
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (Exception e) {
            System.err.println("Error checking service name exists: " + e.getMessage());
            e.printStackTrace();
        }
        return exists;
    }
}
