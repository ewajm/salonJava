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
    testClient = new Client("Testy McTest");
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
  public void equals_returnsTrueWhenPropertiesAreTheSame_true() {
    Client testClient2 = new Client("Testy McTest");
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
    Client testClient2 = new Client("Testina McTest");
    testClient2.save();
    testClient.save();
    
  }
}
