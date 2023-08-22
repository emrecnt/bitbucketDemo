import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDao {

public String processData(String s){
    return s;
}
    public int method1(){
        return 777;
    }

    public static String myStaticMethod(){
        return "static method return value";
    }
    public List<String> fetchAvailableRooms() throws SQLException {
        List<String> availableRooms = new ArrayList<String>();
        Connection conn = DriverManager.getConnection("DB_URL");
        Statement statement = conn.createStatement();
        ResultSet rs;
        rs = statement.executeQuery("SELECT * FROM ROOMS WHERE AVAILABLE like '1'");
        while (rs.next()) {
            availableRooms.add(rs.getString(("Room name")));

        }
        return availableRooms;
    }

    public String someMethod(String a, String b, String c){
        if(a=="throw") throw new RuntimeException();

        return String.format("%s-%s_%s",a,b,c);
    }



}
