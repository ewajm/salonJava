import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name= request.queryParams("name");
      String specialty = request.queryParams("specialty");
      int experience = Integer.parseInt(request.queryParams("experience"));
      String[] days = request.queryParamsValues("days");
      StringBuilder builder = new StringBuilder();
      for(String day : days) {
          builder.append(day);
      }
      builder.append(" " + request.queryParams("from-time") + " - ");
      builder.append(request.queryParams("to-time"));
      String hoursString = builder.toString();
      Stylist stylist = new Stylist(name, specialty, experience, hoursString);
      stylist.save();
      model.put("success", stylist.getName());
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/stylist-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //unfinished
    get("/stylists/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/stylist-delete.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      model.put("stylist", stylist);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id", (request, response) -> {
      String name = request.queryParams("client-name");
      int stylistId = Integer.parseInt(request.queryParams("stylist_id"));
      String email = request.queryParams("client-email");
      String phone = request.queryParams("client-phone");
      Client client = new Client(name, stylistId, email, phone);
      client.save();
      String url = "/stylists/" + stylistId;
      response.redirect(url);
      return null;
    });

    get("/clients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("clients", Client.all());
      model.put("template", "templates/clients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients", (request, response) -> {
      String name = request.queryParams("client-name");
      int stylistId = Integer.parseInt(request.queryParams("stylist_id"));
      String email = request.queryParams("client-email");
      String phone = request.queryParams("client-phone");
      Client client = new Client(name, stylistId, email, phone);
      client.save();
      response.redirect("/clients");
      return null;
    });

    get("/clients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("post-url", "/clients");
      model.put("stylists", Stylist.all());
      model.put("template", "templates/client-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id/clients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int stylistId = Integer.parseInt(request.params("id"));
      model.put("post-url", "/stylists/" + stylistId);
      model.put("stylist", stylistId);
      model.put("template", "templates/client-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:stylist_id/clients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("stylist_id")));
      Client client = Client.find(Integer.parseInt(request.params("id")));
      model.put("client", client);
      model.put("stylist", stylist);
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
