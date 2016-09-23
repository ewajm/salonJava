import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class ClientTest{
  Client testClient;
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Before
  public void setUp(){
    testClient = new Client("Testy McTest", 1, "555-5555", "testy@testy.com");
  }

  @Test
  public void client_instantiatesCorrectly_true() {
    assertTrue(testClient instanceof Client);
    //assertEquals(expected-output, methodBeingTested(arguments));
  }

  @Test
  public void getName_returnsNameSetByConstructor_String() {
    assertEquals("Testy McTest", testClient.getName());
  }

  @Test
  public void getPhone_returnsPhoneSetByConstructor_String() {
    assertEquals("555-5555", testClient.getPhone());
  }

  @Test
  public void getEmail_returnsEmailSetByConstructor_String() {
    assertEquals("testy@testy.com", testClient.getEmail());
  }

  @Test
  public void equals_returnsTrueWhenPropertiesAreTheSame_true() {
    Client testClient2 = new Client("Testy McTest", 1, "555-5555", "testy@testy.com");
    assertTrue(testClient.equals(testClient2));
  }

  @Test
  public void save_savesToDatabase_true() {
    testClient.save();
    Client testClient2 = null;
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM clients WHERE name = 'Testy McTest'";
      testClient2 = con.createQuery(sql).executeAndFetchFirst(Client.class);
    }
    assertTrue(testClient.equals(testClient2));
  }

  @Test
  public void getId_returnsIdSetBySave_String() {
    testClient.save();
    assertTrue(testClient.getId() > 0);
  }

  @Test
  public void all_returnsAllInstancesOfclass_true(){
    Client testClient2 = new Client("Testina McTest", 1,"555-5556", "testina@testy.com");
    testClient2.save();
    testClient.save();
    assertTrue(Client.all().contains(testClient));
    assertTrue(Client.all().contains(testClient2));
  }

  @Test
  public void find_returnsClientWithMatchingId_true(){
    Client testClient2 = new Client("Testina McTest", 1, "555-5556", "testina@testy.com");
    testClient2.save();
    testClient.save();
    assertEquals(Client.find(testClient2.getId()), testClient2);
  }

  @Test
  public void getStylistId_returnsStylistId_int(){
    assertEquals(1, testClient.getStylistId());
  }

  @Test
  public void delete_deletesFromTable_true(){
    testClient.save();
    testClient.delete();
    assertEquals(null, Client.find(testClient.getId()));
  }

  @Test
  public void update_updatesPassedInProperty_true(){
    testClient.save();
    testClient.update("phone", "666-6666");
    testClient.update("stylist_id", "3");
    assertEquals("666-6666", Client.find(testClient.getId()).getPhone());
    assertEquals(3, Client.find(testClient.getId()).getStylistId());
  }
}
