import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

//TODO: abstract specialties to it's own class/table of offered specialties?

public class Stylist{
  private int id;
  private String name;
  private String specialty;
  private boolean accepting_clients;
  private int experience;
  private String hours;

  public Stylist(String name, String specialty, int experience, String hours){
    this.name = name;
    this.specialty = specialty;
    this.experience = experience;
    this.hours = hours;
    accepting_clients = true;
  }

  public String getName(){
    return name;
  }

  public String getSpecialty(){
    return specialty;
  }

  public int getExperience(){
    return experience;
  }

  public String getHours(){
    return hours;
  }

  public boolean isAcceptingClients(){
    return accepting_clients;
  }

  public void toggleAccepting(){
    accepting_clients = !accepting_clients;
  }

  @Override
  public boolean equals(Object toCompare){
    if(!(toCompare instanceof Stylist)){
      return false;
    } else {
      Stylist stylistCompare = (Stylist) toCompare;
      return this.name.equals(stylistCompare.name) && this.specialty.equals(stylistCompare.specialty) && this.experience == stylistCompare.experience && this.hours.equals(stylistCompare.hours) && this.accepting_clients == stylistCompare.accepting_clients;
    }
  }

  public void save(){
    String sql = "INSERT INTO stylists (name, specialty, experience, accepting_clients, hours) VALUES (:name, :specialty, :experience, :accepting_clients, :hours)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql, true).addParameter("name", this.name)
      .addParameter("specialty", this.specialty)
      .addParameter("experience", this.experience)
      .addParameter("accepting_clients", this.accepting_clients)
      .addParameter("hours", this.hours)
      .executeUpdate().getKey();
    }
  }

  public int getId(){
    return id;
  }

  public List<Client> getClients(){
    String sql = "SELECT id, name, stylist_id FROM clients WHERE stylist_id=:id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id", id).executeAndFetch(Client.class);
    }
  }

  public static List<Stylist> all(){
    String sql = "SELECT id, name, specialty, experience, accepting_clients, hours FROM stylists";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  public static Stylist find(int id){
    String sql = "SELECT id, name, specialty, experience, accepting_clients, hours FROM stylists WHERE id=:id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Stylist.class);
    }
  }
}
