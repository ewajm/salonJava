import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Request;
import static spark.Spark.*;

public class App {
  private static final String FLASH_MESSAGE_KEY = "flash_message";

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    //cookies for usernames
    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("index", true);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("flashMessage", captureFlashMessage(request));
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists", (request, response) -> {
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
      setFlashMessage(request, stylist.getName()+" added!");
      response.redirect("/stylists");
      return null;
    });

    get("/stylists/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/stylist-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      model.put("stylist", stylist);
      model.put("template", "templates/stylist.vtl");
      model.put("flashMessage", captureFlashMessage(request));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id", (request, response) -> {
      String name = request.queryParams("name");
      int stylistId = Integer.parseInt(request.queryParams("stylist_id"));
      String email = request.queryParams("email");
      String phone = request.queryParams("phone");
      Client client = new Client(name, stylistId, phone, email);
      client.save();
      String url = "/stylists/" + stylistId;
      setFlashMessage(request, client.getName()+" deleted!");
      response.redirect(url);
      return null;
    });

    get("/stylists/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      model.put("toDelete", stylist);
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylist-delete.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/delete", (request, response) -> {
      Set<String> allParams = request.queryParams();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      for(String param: allParams){
        Client client = Client.find(Integer.parseInt(param));
        String newStylistId = request.queryParams(param);
        client.update("stylist_id", newStylistId);
      }
      stylist.delete();
      setFlashMessage(request, stylist.getName()+" deleted :(");
      response.redirect("/stylists");
      return null;
    });

    get("/stylists/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      model.put("toUpdate", stylist);
      model.put("template", "templates/stylist-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/update", (request, response) -> {
      Set<String> allParams = request.queryParams();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("id")));
      for(String param: allParams){
        if(!request.queryParams(param).isEmpty()){
          if(param.equals("accepting")){
            stylist.toggleAccepting();
          } else {
            stylist.update(param, request.queryParams(param));
          }
        }
      }
      String url = "/stylists/" + stylist.getId();
      setFlashMessage(request, stylist.getName()+" updated");
      response.redirect(url);
      return null;
    });

    get("/stylists/:id/clients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int stylistId = Integer.parseInt(request.params("id"));
      model.put("post-url", "/stylists/" + stylistId);
      model.put("stylist", stylistId);
      model.put("template", "templates/client-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/delete", (request, response) -> {
      Client client = Client.find(Integer.parseInt(request.queryParams("client_id")));
      client.delete();
      setFlashMessage(request, client.getName()+" deleted :(");
      response.redirect("/clients");
      return null;
    });

    get("/stylists/:stylist_id/clients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("stylist_id")));
      Client client = Client.find(Integer.parseInt(request.params("id")));
      model.put("flashMessage", captureFlashMessage(request));
      model.put("client", client);
      model.put("stylist", stylist);
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:stylist_id/clients/:id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("stylist_id")));
      Client client = Client.find(Integer.parseInt(request.params("id")));
      model.put("post-url", "/stylists/" + stylist.getId() + "/clients/" + client.getId() + "/update");
      model.put("toUpdate", client);
      model.put("currentStylist", stylist);
      model.put("stylists", Stylist.all());
      model.put("template", "templates/client-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:stylist_id/clients/:id/update", (request, response) -> {
      Set<String> allParams = request.queryParams();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params("stylist_id")));
      Client client = Client.find(Integer.parseInt(request.params("id")));
      for(String param: allParams){
        if(!request.queryParams(param).isEmpty()){
            client.update(param, request.queryParams(param));
        }
      }
      //need to get new stylist id for the redirect
      client = Client.find(Integer.parseInt(request.params("id")));
      String url = "/stylists/" + client.getStylistId() + "/clients/" + client.getId();
      setFlashMessage(request, client.getName() + " updated!");
      response.redirect(url);
      return null;
    });

    get("/clients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("flashMessage", captureFlashMessage(request));
      model.put("clients", Client.all());
      model.put("template", "templates/clients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients", (request, response) -> {
      String name = request.queryParams("name");
      int stylistId = Integer.parseInt(request.queryParams("stylist_id"));
      String email = request.queryParams("email");
      String phone = request.queryParams("phone");
      Client client = new Client(name, stylistId, phone, email);
      client.save();
      setFlashMessage(request, name+" added!");
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

    //exception handling
    exception(NumberFormatException.class, (exc, req, res) -> {
      res.status(500);
      VelocityTemplateEngine engine = new VelocityTemplateEngine();
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/notfound.vtl");
      String html = engine.render(new ModelAndView(model, layout));
      res.body(html);
    });

    exception(NotFoundException.class, (exc, req, res) -> {
      res.status(404);
      VelocityTemplateEngine engine = new VelocityTemplateEngine();
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/notfound.vtl");
      String html = engine.render(new ModelAndView(model, layout));
      res.body(html);
    });
  }

  //flash message implementation
  private static void setFlashMessage(Request req, String message){
    req.session().attribute(FLASH_MESSAGE_KEY, message);
  }

  private static String getFlashMessage(Request req){
    if(req.session(false) == null){
      return null;
    }
    if(!req.session().attributes().contains(FLASH_MESSAGE_KEY)){
      return null;
    }
    return (String) req.session().attribute(FLASH_MESSAGE_KEY);
  }

  private static String captureFlashMessage(Request req){
    String message = getFlashMessage(req);
    if(message!=null){
      req.session().removeAttribute(FLASH_MESSAGE_KEY);
    }
    return message;
  }
}
