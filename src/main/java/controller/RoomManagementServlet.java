package controller;

import model.RoomDAO;
import model.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/RoomManagementServlet")

public class RoomManagementServlet extends HttpServlet{

protected void doPost(HttpServletRequest request,HttpServletResponse response)
throws ServletException,IOException{

String category=request.getParameter("category");
String type=request.getParameter("type");
String price=request.getParameter("price");
int count=Integer.parseInt(request.getParameter("count"));

RoomDAO.addCategory(category);

int categoryId=RoomDAO.getCategoryId(category);

try{

Connection con = DBConnection.getConnection();

String typeSql="INSERT INTO room_types(category_id,type_name,price) VALUES(?,?,?)";

PreparedStatement ps=con.prepareStatement(typeSql);

ps.setInt(1,categoryId);
ps.setString(2,type);
ps.setDouble(3,Double.parseDouble(price));

ps.executeUpdate();

String getType="SELECT type_id FROM room_types WHERE type_name=?";

PreparedStatement ps2=con.prepareStatement(getType);

ps2.setString(1,type);

var rs=ps2.executeQuery();

int typeId=0;

if(rs.next())
typeId=rs.getInt(1);

/* Generate Rooms */

String prefix=category.substring(0,1).toUpperCase();

for(int i=1;i<=count;i++){

String roomNo=prefix+ (100+i);

String roomSql="INSERT INTO rooms(room_number,category_id,type_id) VALUES(?,?,?)";

PreparedStatement ps3=con.prepareStatement(roomSql);

ps3.setString(1,roomNo);
ps3.setInt(2,categoryId);
ps3.setInt(3,typeId);

ps3.executeUpdate();

}

response.sendRedirect("roomSuccess.jsp");

}
catch(Exception e){
e.printStackTrace();
}

}

}