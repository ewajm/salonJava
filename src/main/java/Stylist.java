import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

//abstract specialties to it's own class/table of offered specialties?

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
}
