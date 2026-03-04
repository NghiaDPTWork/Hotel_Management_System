package dto;


/**
 *
 * @author TR_NGHIA
 */

public class SystemConfig {
    private int ConfigId;
    private String ConfigName;
    private String ConfigValue;
    private boolean Status;

    public SystemConfig() {
    }

    public SystemConfig(int ConfigId, String ConfigName, String ConfigValue, boolean Status) {
        this.ConfigId = ConfigId;
        this.ConfigName = ConfigName;
        this.ConfigValue = ConfigValue;
        this.Status = Status;
    }

    public int getConfigId() {
        return ConfigId;
    }

    public void setConfigId(int ConfigId) {
        this.ConfigId = ConfigId;
    }

    public String getConfigName() {
        return ConfigName;
    }

    public void setConfigName(String ConfigName) {
        this.ConfigName = ConfigName;
    }

    public String getConfigValue() {
        return ConfigValue;
    }

    public void setConfigValue(String ConfigValue) {
        this.ConfigValue = ConfigValue;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    
}
