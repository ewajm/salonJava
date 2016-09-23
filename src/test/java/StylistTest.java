import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
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
}
