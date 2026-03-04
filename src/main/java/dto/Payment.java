package dto;

import java.time.LocalDate;


/**
 *
 * @author TR_NGHIA
 */

public class Payment {
    private int paymentId;
    private int bookingId; 
    private LocalDate paymentDate;
    private double amount;
    private String paymentMethod;
    private String status;

    // Constructors
    public Payment() {}

    public Payment(int bookingId, LocalDate paymentDate, double amount, String paymentMethod, String status) {
        this.bookingId = bookingId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Payment(int bookingId, double amount, String paymentMethod, String status) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public Payment(int paymentId, int bookingId, LocalDate paymentDate, double amount, String paymentMethod, String status) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Payment{" + "paymentId=" + paymentId + ", bookingId=" + bookingId + ", amount=" + amount + ", status='" + status + '\'' + '}';
    }
}