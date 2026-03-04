package dto;

import java.time.LocalDate;


/**
 *
 * @author TR_NGHIA
 */

public class ChoosenService {
    private int serviceId;
    private int quantity;
    private LocalDate serviceDate;

    // Constructors
    public ChoosenService() {
    }

    public ChoosenService(int serviceId, int quantity, LocalDate serviceDate) {
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.serviceDate = serviceDate;
    }

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    @Override
    public String toString() {
        return "ChosenServiceDTO{" +
                "serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", serviceDate=" + serviceDate +
                '}';
    }
}