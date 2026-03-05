package model;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReservationDAO {

    public static boolean addReservation(Reservation r) {

        boolean status = false;

        try {

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO reservations "
                    + "(reservation_no, guest_name, nic, address, contact_number, room_category, room_type, check_in, check_out) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, r.getReservationNo());
            ps.setString(2, r.getGuestName());
            ps.setString(3, r.getNic());
            ps.setString(4, r.getAddress());
            ps.setString(5, r.getContactNumber());
            ps.setString(6, r.getRoomCategory());
            ps.setString(7, r.getRoomType());
            ps.setString(8, r.getCheckIn());
            ps.setString(9, r.getCheckOut());

            int i = ps.executeUpdate();

            if(i > 0)
                status = true;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}