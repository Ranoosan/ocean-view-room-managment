package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private int reservationId;
    private String reservationNumber;
    private int guestId;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate expectedCheckIn;
    private LocalDate expectedCheckOut;
    private int adultsCount;
    private int childrenCount;
    private String bookingStatus;
    private String paymentStatus;
    private String specialRequests;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for display
    private String guestName;
    private String roomNumber;
    private BigDecimal roomPrice;
    
    public Reservation() {}
    
    // Calculate actual nights stayed
    public long getActualNightsStayed() {
        if (checkInDate != null && checkOutDate != null) {
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }
    
    // Calculate expected nights
    public long getExpectedNights() {
        if (expectedCheckIn != null && expectedCheckOut != null) {
            return ChronoUnit.DAYS.between(expectedCheckIn, expectedCheckOut);
        }
        return 0;
    }
    
    // Check if early checkout
    public boolean isEarlyCheckout() {
        return getActualNightsStayed() < getExpectedNights();
    }
    
    // Check if late checkout
    public boolean isLateCheckout() {
        return checkOutDate != null && expectedCheckOut != null && 
               checkOutDate.isAfter(expectedCheckOut);
    }
    
    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public String getReservationNumber() { return reservationNumber; }
    public void setReservationNumber(String reservationNumber) { this.reservationNumber = reservationNumber; }
    
    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }
    
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    
    public LocalDate getExpectedCheckIn() { return expectedCheckIn; }
    public void setExpectedCheckIn(LocalDate expectedCheckIn) { this.expectedCheckIn = expectedCheckIn; }
    
    public LocalDate getExpectedCheckOut() { return expectedCheckOut; }
    public void setExpectedCheckOut(LocalDate expectedCheckOut) { this.expectedCheckOut = expectedCheckOut; }
    
    public int getAdultsCount() { return adultsCount; }
    public void setAdultsCount(int adultsCount) { this.adultsCount = adultsCount; }
    
    public int getChildrenCount() { return childrenCount; }
    public void setChildrenCount(int childrenCount) { this.childrenCount = childrenCount; }
    
    public int getTotalGuests() { return adultsCount + childrenCount; }
    
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public BigDecimal getRoomPrice() { return roomPrice; }
    public void setRoomPrice(BigDecimal roomPrice) { this.roomPrice = roomPrice; }
}