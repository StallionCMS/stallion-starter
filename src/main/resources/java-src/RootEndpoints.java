package io.stallion.starter.examples;

import io.stallion.Context;
import io.stallion.templating.TemplateRenderer;
import io.stallion.utils.Sanitize;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.Map;

import static io.stallion.utils.Literals.*;

public class RootEndpoints extends BaseEndpoints {

    @GET
    @Path("/")
    public String home() {
        Map ctx = map();

        Map appContext = map();
        appContext.put("user", Context.getUser());
        ctx.put("appContextJson", Sanitize.htmlSafeJson(appContext));

        return TemplateRenderer.instance().renderTemplate("$APP_NAME$:/app.jinja", ctx);
    }

    @GET
    @Path("/hello-world")
    public String helloWorld(@QueryParam("name") String name) {
        if (empty(name)) {
            name = "world";
        }
        return "Hello, " + name;
    }

    @GET
    @Path("/hello-world/:name")
    public String helloWorldFromPath(@PathParam("name") String name) {
        return helloWorld(name);
    }
}