import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.sql2o.*;

public class StylistTest{
  Stylist testStylist;
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Before
  public void setUp(){
    testStylist = new Stylist("Styles McTesty", "Some hair thing", 1, "MTh 1pm-5pm");
  }

  @Test
  public void methodBeingTested_behaviorBeingTested_ExpectedReturnType() {
    assertTrue(testStylist instanceof Stylist);
  }

  @Test
  public void getName_returnsNameSetByConstructor_String() {
    assertEquals("Styles McTesty", testStylist.getName());
  }

  @Test
  public void getSpecialty_returnsSpecialtySetByConstructor_String() {
    assertEquals("Some hair thing", testStylist.getSpecialty());
  }

  @Test
  public void getHours_returnsHoursSetByConstructor_String() {
    assertEquals("MTh 1pm-5pm", testStylist.getHours());
  }

  @Test
  public void getExperience_returnsExperienceSetByConstructor_int() {
    assertEquals(1, testStylist.getExperience());
  }

  @Test
  public void isAcceptingClients_returnsAcceptingClientsSetByConstructor_boolean() {
    assertEquals(true, testStylist.isAcceptingClients());
  }

  @Test
  public void isAcceptingClients_returnsNewValueWhenToggled_boolean() {
    testStylist.toggleAccepting();
    assertEquals(false, testStylist.isAcceptingClients());
  }

  @Test
  public void equals_returnsTrueWhenPropertiesAreTheSame_true() {
    Stylist testStylist2 = new Stylist("Styles McTesty", "Some hair thing", 1, "MTh 1pm-5pm");
    assertTrue(testStylist.equals(testStylist2));
  }

  @Test
  public void save_savesToDatabase_true() {
    testStylist.save();
    Stylist testStylist2 = null;
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM stylists WHERE name = 'Styles McTesty'";
      testStylist2 = con.createQuery(sql).executeAndFetchFirst(Stylist.class);
    }
    assertTrue(testStylist.equals(testStylist2));
  }

  @Test
  public void getId_returnsIdSetBySave_String() {
    testStylist.save();
    assertTrue(testStylist.getId() > 0);
  }

  @Test
  public void all_returnsAllInstancesOfclass_true(){
    Stylist testStylist2 = new Stylist("Testy McStyles", "Some other hair thing", 2, "TF 8am-12pm");
    testStylist2.save();
    testStylist.save();
    assertTrue(Stylist.all().contains(testStylist));
    assertTrue(Stylist.all().contains(testStylist2));
  }

  @Test
  public void find_returnsStylistWithMatchingId_true(){
    Stylist testStylist2 = new Stylist("Testy McStyles", "Some other hair thing", 2, "TF 8am-12pm");
    testStylist2.save();
    testStylist.save();
    assertEquals(Stylist.find(testStylist2.getId()), testStylist2);
  }

  @Test
  public void getClients_returnsAllClientsOfStylist_true(){
    testStylist.save();
    Client testClient = new Client("Testy McTest", testStylist.getId(), "555-5555", "test@test.com");
    testClient.save();
    Client testClient2 = new Client("Testina McTest", testStylist.getId(), "655-5555", "testina@test.com");
    testClient2.save();
    Client[] clients = new Client[] {testClient, testClient2};
    assertTrue(testStylist.getClients().containsAll(Arrays.asList(clients)));
  }

  @Test
  public void delete_deletesFromTable_true(){
    testStylist.save();
    testStylist.delete();
    assertEquals(0, Stylist.all().size());
  }

  @Test
  public void update_updatesPassedInProperty_true(){
    testStylist.save();
    testStylist.update("specialty", "coloring");
    testStylist.update("experience", "3");
    assertEquals("coloring", Stylist.find(testStylist.getId()).getSpecialty());
    assertEquals(3, Stylist.find(testStylist.getId()).getExperience());
  }
}
