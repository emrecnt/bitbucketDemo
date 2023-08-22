// junit4 vs junit5 https://www.softwaretestinghelp.com/junit-annotations-tutorial/

//import org.junit.jupiter.api.Assertions; //junit 5
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test; //junit:junit.jupiter oldu mu 5

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.management.DescriptorKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

//@ExtendWith(MockitoExtension.class) // juint5 ile çalışabilmesi için (junit4 kullanıldığından aktif değil)

@RunWith(PowerMockRunner.class)
@PrepareForTest({BookingManager.class,HotelDao.class,MyFinalClass.class})
public class BookingManagerTest {

    @Test
    public void doAnswerTest(){

        HotelDao dao = mock(HotelDao.class);
        doAnswer(invocationOnMock -> {
            Object[] arg = invocationOnMock.getArguments();
                 String s = (String) arg[0];
                    System.out.println("Method çağırıldı. Argüman: " + s);
                    return null;
                }
                ).when(dao).processData(anyString());

        dao.processData("DENENMEE");
    }

    private BookingManager bManager = new BookingManager();

    @Test
    public void beklenen555() throws Exception {
        HotelDao hotelDao = mock(HotelDao.class);
        when(hotelDao.method1()).thenReturn(555);
        PowerMockito.whenNew(HotelDao.class).withAnyArguments().thenReturn(hotelDao);

        System.out.println(bManager.newAnahtarliDeneme());
    }

    @Test
    public void privateTest() throws Exception{
        ClassWithPrivateMethod instance = spy(new ClassWithPrivateMethod());
        PowerMockito.doReturn("private ici degistirildi").when(instance,"myPrivateMethod");

    }

    @Test
    public void finalClassTest(){

        MyFinalClass myFinalClass = PowerMockito.mock(MyFinalClass.class);
        when(myFinalClass.methodInFinalClass()).thenReturn("Mocked final class");
        System.out.println(myFinalClass.methodInFinalClass());
    }

    @Mock
    HotelDao mockHotelDao;


    @Test
    public void staticMockTest(){
        mockStatic(HotelDao.class);
        when(HotelDao.myStaticMethod()).thenReturn("value degistirildi.");
        System.out.println(HotelDao.myStaticMethod());
    }

    @Test
    public void checkRoomAvailability_StringAA_ShouldReturnTrue() throws SQLException {
        List<String> availableRooms = Arrays.asList("AA", "BA", "CA");

        when(mockHotelDao.fetchAvailableRooms()).thenReturn(availableRooms);
        List<String> deneme = mockHotelDao.fetchAvailableRooms();
        verify(mockHotelDao).fetchAvailableRooms(); //çalıştı mı çalışmadı mı. geçer, çalışmadıysa test failler.

        bManager = new BookingManager(mockHotelDao);

        assertTrue(bManager.checkRoomAvailability("AA"));

        List<String> mockList = mock(List.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                String arg = (String) arguments[0];
                if (arg == "return false")
                    return false;

                return true;
            }
        }).when(mockList).add(anyString());

        assertEquals(true, mockList.add("anything"));
        assertEquals(false, mockList.add("return false"));
    }

    @Test
    public void HotelDao_someMethod_3String_ShouldConcat() {
        //when(mockHotelDao.someMethod(anyString(),anyString(),eq("fvf"))).thenReturn(anyString()+"-"+anyString()+eq("_fvf"));
        //verify(mockHotelDao).someMethod(eq("Se"),eq("SE"),eq("_fvf"));  //Argument matchers kullanıyosan her şeyi onunla vericen. anyString() falan genelde null obje döndürür dolayısıyla stub ve verifyda kullan sadece. ( stub = when)

        List mockedList = mock(List.class);

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        verify(mockedList, times(3)).add("three times"); // eğer yukarıda 3 kere "three times" olmasa test failler.

        verify(mockedList, never()).add("never happened"); //"never happend" hiç olmayacak.

        mockedList.add("five times");
        mockedList.add("five times");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times"); //
        verify(mockedList, atLeast(2)).add("five times");
        verify(mockedList, atMost(5)).add("three times");

        mockedList.add("once");
        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");  //times(1) is default.


        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        List mockThree = mock(List.class);
        //using mocks - only mockOne is interacted
        mockOne.add("one");

        //ordinary verification
        verify(mockOne).add("one");

        //verify that method was never called on a mock
        verify(mockOne, never()).add("two");

        //verify that other mocks were not interacted
        verifyZeroInteractions(mockTwo, mockThree);
    }

//    @Test
//    public void thenReturn_thenThrow_kullanimi(){ //ThenThrow'a Bak.
//        HotelDao mock=mock(HotelDao.class);
//        when(mock.someMethod(eq("throw"),anyString(),anyString())
//                .thenThrow(new RuntimeException())
//                .thenReturn("foo"));
//        //First call: throws runtime exception:
//        mock.someMethod("some arg","se","c");
//        //Second call: prints "foo"
//        System.out.println(mock.someMethod("some arg","a","v"));
//        //Any consecutive call: prints "foo" as well (last stubbing wins).
//        System.out.println(mock.someMethod("some arg","s,","d"));
//    }

    @Test
    public void thenAnswer_kullanimi() { //Kullanım çok önerilmiyor. thenReturn thenThrow bil yetr.
        HotelDao mock = mock(HotelDao.class);
        when(mock.someMethod(anyString(), anyString(), anyString())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                return "called with arguments: " + Arrays.toString(args);
            }
        });

        //Following prints "called with arguments: [foo]"
        System.out.println(mock.someMethod("selma ", "le", "foo"));
        assertEquals("called with arguments: selma lefoo", mock.someMethod("selma ", "le", "foo"));
    }

    @Test
    public void voidMethodlardaThenReturnKullanilmiyo_NeYapicaz() {
        List mockedList = mock(List.class);

        //Use doThrow() when you want to stub a void method with an exception
        doThrow(new RuntimeException()).when(mockedList).clear();
        //following throws RuntimeException:
        mockedList.clear();
    }

    @Test
    public void spy_GercekNesneleriSarmalayan_KismiTaklitci() {
        // Spy, gerçek bir nesneyi sarmalayan ve onun tüm davranışlarını ve yöntemlerini koruyan bir objedir. Yani spy, nesnenin asıl davranışlarını kısmen taklit eder ve gerçek nesneyle etkileşim sağlar. Kısacası, spy biraz daha "gerçek" bir yedek nesnedir ve bazı durumlarda mock'a göre daha uygun olabilir.
        // Örneğin, gerçek bir veritabanı bağlantısını test etmek istediğinizde, veritabanı bağlantısı üzerindeki gerçek işlemleri yapmak yerine, bir spy kullanarak gerçek bağlantıya izin verir ve sonuçları inceleyebilirsiniz.
        // Gerçek nesne oluşturma ve spy kullanımı
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);

        // Gerçek davranışı korur, ancak davranışı belirleyebilirsiniz
        when(spyList.size()).thenReturn(10);
        //  when(spyList.get(45)).thenReturn(String.valueOf(10)); //bir üst satırda sorun yokken neden bunda var

        int size = spyList.size(); // Gerçek listenin boyutu alınır, ancak 10 dönecektir
        System.out.println(spyList.size());
        // System.out.println(spyList.get(45)); //çalışmıyor??  -> spy ile çalışması için doReturn kullanılabilir.

    }

    @Test
    public void doReturnDenemesi() {
//        List list = new LinkedList();
//        list=mock(LinkedList.class);
//        when(list.get(0)).thenReturn("foo");
//        System.out.println(list.get(0));   //bu şekildeyken çalışıyor

        List list = new LinkedList();

        List spyy = spy(list);

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                         return "null";
                     }
                 }

        ).when(spyy).get(0);
        System.out.println(spyy.get(0));  //bu çalışmıyoer indexOutOfBoundExc. veriyor. çünkü list boş.

//        List list = new LinkedList();
//        List spy = spy(list);
        doReturn("foo").when(spyy).get(0);
//        System.out.println(spy.get(0));  // Bu çalışıyor
    }
}