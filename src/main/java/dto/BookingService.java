package dto;

import java.time.LocalDate; // THAY �?ỔI: Thêm import cho LocalDate


/**
 *
 * @author TR_NGHIA
 */

public class BookingService {
    private int bookingServiceId;
    private int bookingId;
    private int serviceId;
    private int quantity;
    private LocalDate serviceDate; 
    private int status;

    public BookingService() {}

    public BookingService(int bookingId, int serviceId, int quantity, LocalDate serviceDate, int status) {
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.serviceDate = serviceDate;
        this.status = status;
    }

    public BookingService(int bookingServiceId, int bookingId, int serviceId, int quantity, LocalDate serviceDate, int status) { // THAY �?ỔI
        this.bookingServiceId = bookingServiceId;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.serviceDate = serviceDate;
        this.status = status;
    }

    // --- Getters and Setters được cập nhật ---
    public int getBookingServiceId() { return bookingServiceId; }
    public void setBookingServiceId(int bookingServiceId) { this.bookingServiceId = bookingServiceId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getServiceDate() { return serviceDate; } // THAY �?ỔI
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; } // THAY �?ỔI
    
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    // --- THAY �?ỔI: toString() được cập nhật ---
    @Override
    public String toString() {
        return "BookingService{" +
                "bookingServiceId=" + bookingServiceId +
                ", bookingId=" + bookingId +
                ", serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", serviceDate=" + serviceDate + // B�? dấu nháy đơn, sẽ tự g�?i .toString() của LocalDate
                ", status=" + status +
                '}';
    }
}