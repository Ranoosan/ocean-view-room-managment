package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Room {
    private int roomId;
    private String roomNumber;
    private int categoryId;
    private int typeId;
    private String status;
    private Integer floorNumber;
    private Timestamp createdAt;
    
    // Additional fields for joined queries
    private String categoryName;
    private String typeName;
    private BigDecimal price;
    private Integer maxOccupancy;
    private String amenities;
    private String description;
    
    // Constructors
    public Room() {}
    
    public Room(String roomNumber, int categoryId, int typeId, String status) {
        this.roomNumber = roomNumber;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.status = status;
    }
    
    public Room(String roomNumber, int categoryId, int typeId, String status, int floorNumber) {
        this.roomNumber = roomNumber;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.status = status;
        this.floorNumber = floorNumber;
    }
    
    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public int getTypeId() {
        return typeId;
    }
    
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getFloorNumber() {
        return floorNumber;
    }
    
    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }
    
    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Business logic methods
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status);
    }
    
    public boolean isOccupied() {
        return "OCCUPIED".equalsIgnoreCase(status);
    }
    
    public boolean isReserved() {
        return "RESERVED".equalsIgnoreCase(status);
    }
    
    public boolean isUnderMaintenance() {
        return "MAINTENANCE".equalsIgnoreCase(status);
    }
    
    public String getStatusDisplay() {
        switch(status != null ? status.toUpperCase() : "") {
            case "AVAILABLE":
                return "Available";
            case "OCCUPIED":
                return "Occupied";
            case "RESERVED":
                return "Reserved";
            case "MAINTENANCE":
                return "Under Maintenance";
            default:
                return status;
        }
    }
    
    public String getStatusColor() {
        switch(status != null ? status.toUpperCase() : "") {
            case "AVAILABLE":
                return "green";
            case "OCCUPIED":
                return "red";
            case "RESERVED":
                return "orange";
            case "MAINTENANCE":
                return "gray";
            default:
                return "blue";
        }
    }
    
    public String getFullRoomInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Room ").append(roomNumber);
        if (categoryName != null) {
            info.append(" (").append(categoryName);
            if (typeName != null) {
                info.append(" - ").append(typeName);
            }
            info.append(")");
        }
        return info.toString();
    }
    
    public boolean canAccommodate(int numberOfGuests) {
        return maxOccupancy != null && numberOfGuests <= maxOccupancy;
    }
    
    public BigDecimal getPricePerNight() {
        return price != null ? price : BigDecimal.ZERO;
    }
    
    public BigDecimal calculateTotalPrice(int nights) {
        return getPricePerNight().multiply(new BigDecimal(nights));
    }
    
    @Override
    public String toString() {
        return String.format("Room{id=%d, number='%s', category='%s', type='%s', status='%s', price=%s}",
                roomId, roomNumber, categoryName, typeName, status, price);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Room room = (Room) o;
        return roomId == room.roomId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(roomId);
    }
    
    // Builder pattern for easy object creation
    public static class Builder {
        private Room room;
        
        public Builder() {
            room = new Room();
        }
        
        public Builder withRoomNumber(String roomNumber) {
            room.setRoomNumber(roomNumber);
            return this;
        }
        
        public Builder withCategoryId(int categoryId) {
            room.setCategoryId(categoryId);
            return this;
        }
        
        public Builder withTypeId(int typeId) {
            room.setTypeId(typeId);
            return this;
        }
        
        public Builder withStatus(String status) {
            room.setStatus(status);
            return this;
        }
        
        public Builder withFloorNumber(int floorNumber) {
            room.setFloorNumber(floorNumber);
            return this;
        }
        
        public Builder withCategoryName(String categoryName) {
            room.setCategoryName(categoryName);
            return this;
        }
        
        public Builder withTypeName(String typeName) {
            room.setTypeName(typeName);
            return this;
        }
        
        public Builder withPrice(BigDecimal price) {
            room.setPrice(price);
            return this;
        }
        
        public Builder withMaxOccupancy(int maxOccupancy) {
            room.setMaxOccupancy(maxOccupancy);
            return this;
        }
        
        public Builder withAmenities(String amenities) {
            room.setAmenities(amenities);
            return this;
        }
        
        public Room build() {
            return room;
        }
    }
}