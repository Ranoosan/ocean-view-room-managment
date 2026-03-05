package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billId;
    private int reservationId;
    private String billNumber;
    private BigDecimal roomCharges;
    private BigDecimal extraCharges;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private Timestamp billingDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    
    private List<ExtraServiceItem> extraServices = new ArrayList<>();
    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% GST
    
    public Bill() {
        this.roomCharges = BigDecimal.ZERO;
        this.extraCharges = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.paidAmount = BigDecimal.ZERO;
    }
    
    public void calculateBill(BigDecimal dailyRate, long actualDays) {
        // Calculate room charges for actual days stayed
        this.roomCharges = dailyRate.multiply(new BigDecimal(actualDays))
                                     .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate tax
        this.taxAmount = roomCharges.multiply(TAX_RATE)
                                     .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate extra charges from services
        this.extraCharges = calculateExtraCharges();
        
        // Calculate total
        this.totalAmount = roomCharges.add(taxAmount)
                                       .add(extraCharges)
                                       .subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO)
                                       .setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateExtraCharges() {
        return extraServices.stream()
                .map(ExtraServiceItem::getChargeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    public void handleEarlyCheckout(BigDecimal dailyRate, long bookedDays, long actualDays) {
        // Handle early checkout logic here
        if (actualDays < bookedDays) {
            // You can implement early checkout policy here
            // For example, charge 50% for unused days
            BigDecimal unusedDays = new BigDecimal(bookedDays - actualDays);
            BigDecimal earlyCheckoutFee = dailyRate.multiply(unusedDays).multiply(new BigDecimal("0.5"));
            this.extraCharges = this.extraCharges.add(earlyCheckoutFee);
        }
    }
    
    // Getters and Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    
    public BigDecimal getRoomCharges() { return roomCharges; }
    public void setRoomCharges(BigDecimal roomCharges) { this.roomCharges = roomCharges; }
    
    public BigDecimal getExtraCharges() { return extraCharges; }
    public void setExtraCharges(BigDecimal extraCharges) { this.extraCharges = extraCharges; }
    
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    
    public BigDecimal getDueAmount() { 
        return totalAmount.subtract(paidAmount).setScale(2, RoundingMode.HALF_UP);
    }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public Timestamp getBillingDate() { return billingDate; }
    public void setBillingDate(Timestamp billingDate) { this.billingDate = billingDate; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public List<ExtraServiceItem> getExtraServices() { return extraServices; }
    public void setExtraServices(List<ExtraServiceItem> extraServices) { 
        this.extraServices = extraServices; 
    }
}

class ExtraServiceItem {
    private String serviceName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal chargeAmount;
    
    public ExtraServiceItem(String serviceName, int quantity, BigDecimal unitPrice) {
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.chargeAmount = unitPrice.multiply(new BigDecimal(quantity));
    }
    
    public String getServiceName() { return serviceName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getChargeAmount() { return chargeAmount; }
}