import org.sql2o.*;
import java.util.Date;

public class Client {
  private int id;
  private String name;
  //private Date memberSince;
  private int stylist_id;

  public Client(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public int getId(){
    return id;
  }

  @Override
  public boolean equals(Object toCompare){
    if(!(toCompare instanceof Client)){
      return false;
    } else {
      Client clientCompare = (Client) toCompare;
      return this.name.equals(clientCompare.name);
    }
  }

  public void save(){
    String sql = "INSERT INTO clients (name) VALUES (:name)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql, true).addParameter("name", name).executeUpdate().getKey();
    }
  }

  
}
