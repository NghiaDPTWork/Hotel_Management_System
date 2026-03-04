package dto;


/**
 *
 * @author TR_NGHIA
 */

public class Room {
    private int roomId;
    private String roomNumber;
    private int roomTypeId; // Chỉ giữ roomTypeId
    private String status;
    private String description;

    // Constructors
    public Room() {}

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Room(int roomId, String roomNumber, int roomTypeId, String status, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getRoomTypeId() { return roomTypeId; }
    public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomTypeId=" + roomTypeId +
                ", status='" + status + '\'' +
                '}';
    }
}