package dto;

/**
 *
 * @author TR_NGHIA
 */

public class RoomTask {
    private int roomTaskID;
    private int roomID;
    private Integer staffID; 
    private String startTime; 
    private String endTime; 
    private String statusClean;
    private String notes;

    public RoomTask() {
    }

    public RoomTask(int roomTaskID, int roomID, String statusClean) {
        this.roomTaskID = roomTaskID;
        this.roomID = roomID;
        this.statusClean = statusClean;
    }

    public RoomTask(int roomTaskID, int roomID, Integer staffID, String startTime, String endTime, String statusClean, String notes) {
        this.roomTaskID = roomTaskID;
        this.roomID = roomID;
        this.staffID = staffID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.statusClean = statusClean;
        this.notes = notes;
    }

    public RoomTask(int roomID, String statusClean) {
        this.roomID = roomID;
        this.statusClean = statusClean;
    }

    public int getRoomTaskID() {
        return roomTaskID;
    }

    public void setRoomTaskID(int roomTaskID) {
        this.roomTaskID = roomTaskID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusClean() {
        return statusClean;
    }

    public void setStatusClean(String statusClean) {
        this.statusClean = statusClean;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "RoomTask{" +
                "roomTaskID=" + roomTaskID +
                ", roomID=" + roomID +
                ", staffID=" + staffID +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", statusClean='" + statusClean + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}