package dao;

import dto.SystemConfig;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author TR_NGHIA
 */
public class SystemConfigDAO {

    // =============================================================
    // ================= GET ALL SYSTEM CONFIG =====================
    // =============================================================
    public ArrayList<SystemConfig> getAllSystemConfig() {
        ArrayList<SystemConfig> result = new ArrayList<>();
        String sql = "SELECT [ConfigID], [ConfigName], [ConfigValue], [Status] FROM [HotelManagement].[dbo].[SYSTEM_CONFIG]";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int configId = rs.getInt("ConfigID");
                String configName = rs.getString("ConfigName");
                String configValue = rs.getString("ConfigValue");
                boolean status = rs.getBoolean("Status");
                
                SystemConfig systemConfig = new SystemConfig(configId, configName, configValue, status);
                result.add(systemConfig);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getAllSystemConfig: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getAllSystemConfig: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // =============== GET SYSTEM CONFIG BY ID =====================
    // =============================================================
    public SystemConfig getSystemConfigById(int configId) {
        SystemConfig result = null;
        String sql = "SELECT [ConfigID], [ConfigName], [ConfigValue], [Status] FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [ConfigID] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, configId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String configName = rs.getString("ConfigName");
                    String configValue = rs.getString("ConfigValue");
                    boolean status = rs.getBoolean("Status");
                    
                    result = new SystemConfig(configId, configName, configValue, status);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getSystemConfigById: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getSystemConfigById: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // =========== GET SYSTEM CONFIG BY CONFIG NAME ================
    // =============================================================
    public SystemConfig getSystemConfigByName(String configName) {
        SystemConfig result = null;
        String sql = "SELECT [ConfigID], [ConfigName], [ConfigValue], [Status] FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [ConfigName] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, configName);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int configId = rs.getInt("ConfigID");
                    String configValue = rs.getString("ConfigValue");
                    boolean status = rs.getBoolean("Status");
                    
                    result = new SystemConfig(configId, configName, configValue, status);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in getSystemConfigByName: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getSystemConfigByName: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============== GET ACTIVE SYSTEM CONFIGS ====================
    // =============================================================
    public ArrayList<SystemConfig> getActiveSystemConfigs() {
        ArrayList<SystemConfig> result = new ArrayList<>();
        String sql = "SELECT [ConfigID], [ConfigName], [ConfigValue], [Status] FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [Status] = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int configId = rs.getInt("ConfigID");
                String configName = rs.getString("ConfigName");
                String configValue = rs.getString("ConfigValue");
                boolean status = rs.getBoolean("Status");
                
                SystemConfig systemConfig = new SystemConfig(configId, configName, configValue, status);
                result.add(systemConfig);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getActiveSystemConfigs: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in getActiveSystemConfigs: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================= UPDATE CONFIG VALUE =======================
    // =============================================================
    public boolean updateConfigValue(SystemConfig systemConfig) {
        boolean result = false;
        String sql = "UPDATE [HotelManagement].[dbo].[SYSTEM_CONFIG] SET [ConfigValue] = ? WHERE [ConfigID] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, systemConfig.getConfigValue());
            ps.setInt(2, systemConfig.getConfigId());
            
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in updateConfigValue: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateConfigValue: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================= UPDATE CONFIG STATUS ======================
    // =============================================================
    public boolean updateConfigStatus(int configId, boolean status) {
        boolean result = false;
        String sql = "UPDATE [HotelManagement].[dbo].[SYSTEM_CONFIG] SET [Status] = ? WHERE [ConfigID] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, status);
            ps.setInt(2, configId);
            
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in updateConfigStatus: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateConfigStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============= UPDATE FULL SYSTEM CONFIG =====================
    // =============================================================
    public boolean updateSystemConfig(SystemConfig systemConfig) {
        boolean result = false;
        String sql = "UPDATE [HotelManagement].[dbo].[SYSTEM_CONFIG] SET [ConfigName] = ?, [ConfigValue] = ?, [Status] = ? WHERE [ConfigID] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, systemConfig.getConfigName());
            ps.setString(2, systemConfig.getConfigValue());
            ps.setBoolean(3, systemConfig.isStatus());
            ps.setInt(4, systemConfig.getConfigId());
            
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in updateSystemConfig: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in updateSystemConfig: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ================ ADD SYSTEM CONFIG ==========================
    // =============================================================
    public boolean addSystemConfig(SystemConfig systemConfig) {
        boolean result = false;
        String sql = "INSERT INTO [HotelManagement].[dbo].[SYSTEM_CONFIG] ([ConfigName], [ConfigValue], [Status]) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, systemConfig.getConfigName());
            ps.setString(2, systemConfig.getConfigValue());
            ps.setBoolean(3, systemConfig.isStatus());
            
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in addSystemConfig: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in addSystemConfig: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // =============== DELETE SYSTEM CONFIG ========================
    // =============================================================
    public boolean deleteSystemConfig(int configId) {
        boolean result = false;
        String sql = "DELETE FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [ConfigID] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, configId);
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Database error in deleteSystemConfig: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in deleteSystemConfig: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============ CHECK IF CONFIG NAME EXISTS ====================
    // =============================================================
    public boolean isConfigNameExist(String configName) {
        boolean result = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [ConfigName] = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, configName);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    result = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in isConfigNameExist: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in isConfigNameExist: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // =============================================================
    // ============ CHECK IF CONFIG NAME EXISTS (EXCLUDE ID) =======
    // =============================================================
    public boolean isConfigNameExistExcludingId(String configName, int excludeConfigId) {
        boolean result = false;
        String sql = "SELECT COUNT(*) AS count FROM [HotelManagement].[dbo].[SYSTEM_CONFIG] WHERE [ConfigName] = ? AND [ConfigID] != ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, configName);
            ps.setInt(2, excludeConfigId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    result = count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in isConfigNameExistExcludingId: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error in isConfigNameExistExcludingId: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}