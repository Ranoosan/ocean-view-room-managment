package model;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDAO {
    
    // Generate unique reservation number
    private String generateReservationNumber() {
        return "R" + System.currentTimeMillis() + 
               UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    // Create new reservation
    public Reservation createReservation(Reservation reservation, Guest guest, int roomId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // First insert guest
            String guestSql = "INSERT INTO guests (first_name, last_name, address, contact_number, email) " +
                             "VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(guestSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getAddress());
            pstmt.setString(4, guest.getContactNumber());
            pstmt.setString(5, guest.getEmail());
            pstmt.executeUpdate();
            
            rs = pstmt.getGeneratedKeys();
            int guestId = 0;
            if (rs.next()) {
                guestId = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            
            // Then insert reservation
            String reservationSql = "INSERT INTO reservations (reservation_number, guest_id, room_id, " +
                                   "expected_check_in, expected_check_out, adults_count, children_count, " +
                                   "special_requests, booking_status) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(reservationSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, generateReservationNumber());
            pstmt.setInt(2, guestId);
            pstmt.setInt(3, roomId);
            pstmt.setDate(4, Date.valueOf(reservation.getExpectedCheckIn()));
            pstmt.setDate(5, Date.valueOf(reservation.getExpectedCheckOut()));
            pstmt.setInt(6, reservation.getAdultsCount());
            pstmt.setInt(7, reservation.getChildrenCount());
            pstmt.setString(8, reservation.getSpecialRequests());
            pstmt.setString(9, "CONFIRMED");
            pstmt.executeUpdate();
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                reservation.setReservationId(rs.getInt(1));
                reservation.setReservationNumber(generateReservationNumber()); // Set the generated number
            }
            
            // Update room status
            String updateRoomSql = "UPDATE rooms SET status = 'RESERVED' WHERE room_id = ?";
            pstmt = conn.prepareStatement(updateRoomSql);
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
            
            conn.commit();
            
            // Set additional data
            reservation.setGuestId(guestId);
            reservation.setRoomId(roomId);
            reservation.setBookingStatus("CONFIRMED");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
        
        return reservation;
    }
    
    // Check room availability
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND booking_status IN ('CONFIRMED', 'CHECKED_IN') " +
                     "AND ((expected_check_in BETWEEN ? AND ?) OR (expected_check_out BETWEEN ? AND ?) " +
                     "OR (? BETWEEN expected_check_in AND expected_check_out))";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            pstmt.setDate(2, Date.valueOf(checkIn));
            pstmt.setDate(3, Date.valueOf(checkOut));
            pstmt.setDate(4, Date.valueOf(checkIn));
            pstmt.setDate(5, Date.valueOf(checkOut));
            pstmt.setDate(6, Date.valueOf(checkIn));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }
    
    // Get all available rooms for date range
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, String category) throws SQLException {
        List<Room> availableRooms = new ArrayList<>();
        
        String sql = "SELECT r.*, rc.category_name, rt.type_name, rt.base_price " +
                     "FROM rooms r " +
                     "JOIN room_categories rc ON r.category_id = rc.category_id " +
                     "JOIN room_types rt ON r.type_id = rt.type_id " +
                     "WHERE r.status = 'AVAILABLE' AND rc.category_name = ? " +
                     "AND r.room_id NOT IN (" +
                     "    SELECT room_id FROM reservations " +
                     "    WHERE booking_status IN ('CONFIRMED', 'CHECKED_IN') " +
                     "    AND ((expected_check_in BETWEEN ? AND ?) OR (expected_check_out BETWEEN ? AND ?) " +
                     "    OR (? BETWEEN expected_check_in AND expected_check_out))" +
                     ")";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            pstmt.setDate(2, Date.valueOf(checkIn));
            pstmt.setDate(3, Date.valueOf(checkOut));
            pstmt.setDate(4, Date.valueOf(checkIn));
            pstmt.setDate(5, Date.valueOf(checkOut));
            pstmt.setDate(6, Date.valueOf(checkIn));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setCategoryId(rs.getInt("category_id"));
                    room.setTypeId(rs.getInt("type_id"));
                    room.setCategoryName(rs.getString("category_name"));
                    room.setTypeName(rs.getString("type_name"));
                    room.setPrice(rs.getBigDecimal("base_price"));
                    room.setStatus(rs.getString("status"));
                    availableRooms.add(room);
                }
            }
        }
        return availableRooms;
    }
    
    // Check-in guest
    public void checkIn(int reservationId, int roomId) throws SQLException {
        String sql = "UPDATE reservations SET booking_status = 'CHECKED_IN', check_in_date = CURRENT_DATE " +
                     "WHERE reservation_id = ?";
        
        String updateRoomSql = "UPDATE rooms SET status = 'OCCUPIED' WHERE room_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            pstmt1 = conn.prepareStatement(sql);
            pstmt1.setInt(1, reservationId);
            pstmt1.executeUpdate();
            
            pstmt2 = conn.prepareStatement(updateRoomSql);
            pstmt2.setInt(1, roomId);
            pstmt2.executeUpdate();
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (pstmt1 != null) pstmt1.close();
            if (pstmt2 != null) pstmt2.close();
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
    }
    
    // Check-out guest and calculate bill
    public Bill checkOut(int reservationId, int roomId, LocalDate actualCheckOut) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Bill bill = new Bill();
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get reservation details
            String getResSql = "SELECT r.*, rt.base_price, rt.type_name, rc.category_name, " +
                              "g.first_name, g.last_name, g.contact_number, g.email " +
                              "FROM reservations r " +
                              "JOIN rooms rm ON r.room_id = rm.room_id " +
                              "JOIN room_types rt ON rm.type_id = rt.type_id " +
                              "JOIN room_categories rc ON rm.category_id = rc.category_id " +
                              "JOIN guests g ON r.guest_id = g.guest_id " +
                              "WHERE r.reservation_id = ?";
            
            pstmt = conn.prepareStatement(getResSql);
            pstmt.setInt(1, reservationId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                LocalDate expectedCheckIn = rs.getDate("expected_check_in").toLocalDate();
                LocalDate expectedCheckOut = rs.getDate("expected_check_out").toLocalDate();
                LocalDate actualCheckIn = rs.getDate("check_in_date") != null ? 
                                         rs.getDate("check_in_date").toLocalDate() : expectedCheckIn;
                
                // Calculate actual days stayed
                long actualDays = actualCheckIn.until(actualCheckOut).getDays();
                long expectedDays = expectedCheckIn.until(expectedCheckOut).getDays();
                
                // Create bill
                bill.setReservationId(reservationId);
                bill.setBillNumber("B" + System.currentTimeMillis());
                
                // Calculate room charges based on actual days
                BigDecimal dailyRate = rs.getBigDecimal("base_price");
                bill.calculateBill(dailyRate, actualDays);
                
                // Handle early checkout
                if (actualDays < expectedDays) {
                    bill.handleEarlyCheckout(dailyRate, expectedDays, actualDays);
                }
                
                // Update reservation
                String updateResSql = "UPDATE reservations SET booking_status = 'CHECKED_OUT', " +
                                     "check_out_date = ? WHERE reservation_id = ?";
                pstmt = conn.prepareStatement(updateResSql);
                pstmt.setDate(1, Date.valueOf(actualCheckOut));
                pstmt.setInt(2, reservationId);
                pstmt.executeUpdate();
                
                // Update room status
                String updateRoomSql = "UPDATE rooms SET status = 'AVAILABLE' WHERE room_id = ?";
                pstmt = conn.prepareStatement(updateRoomSql);
                pstmt.setInt(1, roomId);
                pstmt.executeUpdate();
                
                // Insert bill
                String insertBillSql = "INSERT INTO billing (reservation_id, bill_number, room_charges, " +
                                      "extra_charges, tax_amount, discount_amount, total_amount, paid_amount) " +
                                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertBillSql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, reservationId);
                pstmt.setString(2, bill.getBillNumber());
                pstmt.setBigDecimal(3, bill.getRoomCharges());
                pstmt.setBigDecimal(4, bill.getExtraCharges());
                pstmt.setBigDecimal(5, bill.getTaxAmount());
                pstmt.setBigDecimal(6, bill.getDiscountAmount());
                pstmt.setBigDecimal(7, bill.getTotalAmount());
                pstmt.setBigDecimal(8, bill.getPaidAmount());
                pstmt.executeUpdate();
                
                ResultSet billRs = pstmt.getGeneratedKeys();
                if (billRs.next()) {
                    bill.setBillId(billRs.getInt(1));
                }
                billRs.close();
            }
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) { conn.setAutoCommit(true); conn.close(); }
        }
        
        return bill;
    }
    
    // Get all reservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        
        String sql = "SELECT r.*, g.first_name, g.last_name, rm.room_number " +
                     "FROM reservations r " +
                     "JOIN guests g ON r.guest_id = g.guest_id " +
                     "JOIN rooms rm ON r.room_id = rm.room_id " +
                     "ORDER BY r.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setReservationId(rs.getInt("reservation_id"));
                res.setReservationNumber(rs.getString("reservation_number"));
                res.setGuestId(rs.getInt("guest_id"));
                res.setGuestName(rs.getString("first_name") + " " + rs.getString("last_name"));
                res.setRoomId(rs.getInt("room_id"));
                res.setRoomNumber(rs.getString("room_number"));
                
                Date expectedCheckIn = rs.getDate("expected_check_in");
                if (expectedCheckIn != null) {
                    res.setExpectedCheckIn(expectedCheckIn.toLocalDate());
                }
                
                Date expectedCheckOut = rs.getDate("expected_check_out");
                if (expectedCheckOut != null) {
                    res.setExpectedCheckOut(expectedCheckOut.toLocalDate());
                }
                
                Date actualCheckIn = rs.getDate("check_in_date");
                if (actualCheckIn != null) {
                    res.setCheckInDate(actualCheckIn.toLocalDate());
                }
                
                Date actualCheckOut = rs.getDate("check_out_date");
                if (actualCheckOut != null) {
                    res.setCheckOutDate(actualCheckOut.toLocalDate());
                }
                
                res.setAdultsCount(rs.getInt("adults_count"));
                res.setChildrenCount(rs.getInt("children_count"));
                res.setBookingStatus(rs.getString("booking_status"));
                res.setPaymentStatus(rs.getString("payment_status"));
                res.setSpecialRequests(rs.getString("special_requests"));
                res.setCreatedAt(rs.getTimestamp("created_at"));
                
                reservations.add(res);
            }
        }
        
        return reservations;
    }
    
    // Get reservation by ID
    public Reservation getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT r.*, g.first_name, g.last_name, g.contact_number, g.email, " +
                     "rm.room_number, rt.base_price " +
                     "FROM reservations r " +
                     "JOIN guests g ON r.guest_id = g.guest_id " +
                     "JOIN rooms rm ON r.room_id = rm.room_id " +
                     "JOIN room_types rt ON rm.type_id = rt.type_id " +
                     "WHERE r.reservation_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reservation res = new Reservation();
                    res.setReservationId(rs.getInt("reservation_id"));
                    res.setReservationNumber(rs.getString("reservation_number"));
                    res.setGuestId(rs.getInt("guest_id"));
                    res.setGuestName(rs.getString("first_name") + " " + rs.getString("last_name"));
                    res.setRoomId(rs.getInt("room_id"));
                    res.setRoomNumber(rs.getString("room_number"));
                    res.setRoomPrice(rs.getBigDecimal("base_price"));
                    
                    Date expectedCheckIn = rs.getDate("expected_check_in");
                    if (expectedCheckIn != null) {
                        res.setExpectedCheckIn(expectedCheckIn.toLocalDate());
                    }
                    
                    Date expectedCheckOut = rs.getDate("expected_check_out");
                    if (expectedCheckOut != null) {
                        res.setExpectedCheckOut(expectedCheckOut.toLocalDate());
                    }
                    
                    Date actualCheckIn = rs.getDate("check_in_date");
                    if (actualCheckIn != null) {
                        res.setCheckInDate(actualCheckIn.toLocalDate());
                    }
                    
                    Date actualCheckOut = rs.getDate("check_out_date");
                    if (actualCheckOut != null) {
                        res.setCheckOutDate(actualCheckOut.toLocalDate());
                    }
                    
                    res.setAdultsCount(rs.getInt("adults_count"));
                    res.setChildrenCount(rs.getInt("children_count"));
                    res.setBookingStatus(rs.getString("booking_status"));
                    res.setPaymentStatus(rs.getString("payment_status"));
                    res.setSpecialRequests(rs.getString("special_requests"));
                    res.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    return res;
                }
            }
        }
        return null;
    }
}