import java.sql.SQLException;
import java.util.List;

public class BookingManager {
    public BookingManager(){

    }
    public String newAnahtarliDeneme(){
        HotelDao hotelDao = new HotelDao();
        if(hotelDao.method1()==777) {
            return "777'ye esit";

        }
        if(hotelDao.method1()==555){
            return "555'e esit";
        }
        else return "-1";
    }

    private HotelDao dao;
    public BookingManager(HotelDao dao) {
        this.dao = dao;
    }




    public boolean checkRoomAvailability(String roomName) throws SQLException {

        List<String> roomsAvailable = dao.fetchAvailableRooms();
        return roomsAvailable.contains(roomName);

    }


}
