package dto;

import java.math.BigDecimal;

/**
 *
 * @author TR_NGHIA
 */

public class RoomType {
    private int roomTypeId;
    private String typeName;
    private int capacity;
    private BigDecimal pricePerNight;

    // Constructors
    public RoomType() {}

    public RoomType(String typeName) {
        this.typeName = typeName;
    }

    public RoomType(String typeName, int capacity, BigDecimal pricePerNight) {
        this.typeName = typeName;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    public RoomType(int roomTypeId, String typeName, int capacity, BigDecimal pricePerNight) {
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    // Getters and Setters
    public int getRoomTypeId() { return roomTypeId; }
    public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    @Override
    public String toString() {
        return "RoomType{" + "roomTypeId=" + roomTypeId + ", typeName='" + typeName + '\'' + '}';
    }
}
