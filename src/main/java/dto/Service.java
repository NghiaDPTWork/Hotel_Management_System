package dto;

import java.math.BigDecimal;


/**
 *
 * @author TR_NGHIA
 */

public class Service {
    private int serviceId;
    private String serviceName;
    private String serviceType;
    private BigDecimal price;

    // Constructors
    public Service() {}

    public Service(int serviceId, String serviceName, String serviceType, BigDecimal price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
    }

    // Getters and Setters
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Override
    public String toString() {
        return "Service{" + "serviceId=" + serviceId + ", serviceName='" + serviceName + '\'' + '}';
    }
}
