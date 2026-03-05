package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomDAO {

    public static boolean addCategory(String categoryName){

        boolean status=false;

        try{

            Connection con = DBConnection.getConnection();

            String sql="INSERT INTO room_categories(category_name) VALUES(?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,categoryName);

            int i=ps.executeUpdate();

            if(i>0)
                status=true;

        }catch(Exception e){ e.printStackTrace(); }

        return status;
    }

    public static int getCategoryId(String categoryName){

        int id=0;

        try{

            Connection con = DBConnection.getConnection();

            String sql="SELECT category_id FROM room_categories WHERE category_name=?";

            PreparedStatement ps=con.prepareStatement(sql);

            ps.setString(1,categoryName);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
                id=rs.getInt(1);

        }catch(Exception e){ e.printStackTrace(); }

        return id;
    }

}