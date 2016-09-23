import org.sql2o.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Client {
  private int id;
  private String name;
  //private Date memberSince;
  private int stylist_id;
  private String phone;
  private String email;

  public Client(String name, int stylist_id, String phone, String email){
    this.name = name;
    this.stylist_id = stylist_id;
    this.phone = phone;
    this.email = email;
  }

  public String getName(){
    return name;
  }

  public String getPhone(){
    return phone;
  }

  public String getEmail(){
    return email;
  }

  public int getId(){
    return id;
  }

  public int getStylistId(){
    return stylist_id;
  }

  @Override
  public boolean equals(Object toCompare){
    if(!(toCompare instanceof Client)){
      return false;
    } else {
      Client clientCompare = (Client) toCompare;
      return this.name.equals(clientCompare.name) && this.id == clientCompare.id &&
      this.email.equals(clientCompare.email) &&
      this.phone.equals(clientCompare.phone) &&
      this.stylist_id == clientCompare.stylist_id;
    }
  }

  public void save(){
    String sql = "INSERT INTO clients (name, stylist_id, email, phone) VALUES (:name, :stylist_id, :email, :phone)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql, true).addParameter("name", name)
      .addParameter("stylist_id", stylist_id)
      .addParameter("phone", phone)
      .addParameter("email", email)
      .executeUpdate().getKey();
    }
  }

  public static List<Client> all(){
    String sql = "SELECT id, name, stylist_id, email, phone FROM clients";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

  public static Client find(int id){
    String sql = "SELECT id, name, stylist_id, phone, email FROM clients WHERE id=:id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Client.class);
    }
  }
}
