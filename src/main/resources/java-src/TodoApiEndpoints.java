package io.stallion.starter.examples;

import com.fasterxml.jackson.annotation.JsonView;
import io.stallion.Context;
import io.stallion.dataAccess.filtering.FilterChain;
import io.stallion.dataAccess.filtering.Pager;
import io.stallion.exceptions.ClientException;
import io.stallion.requests.validators.SafeMerger;
import io.stallion.restfulEndpoints.MinRole;
import io.stallion.restfulEndpoints.ObjectParam;
import io.stallion.restfulEndpoints.QueryToPager;
import io.stallion.users.Role;
import io.stallion.utils.json.RestrictedViews;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Map;

import static io.stallion.utils.Literals.map;
import static io.stallion.utils.Literals.val;

@Path("/api/v1")
@MinRole(Role.MEMBER)
public class TodoApiEndpoints extends BaseEndpoints {

    @Path("/users/me")
    @GET
    @JsonView(RestrictedViews.Owner.class)
    public Object getMe() {
        return Context.getUser();
    }

    @Path("/todos/find-my-todos")
    @GET
    @JsonView(RestrictedViews.Owner.class)
    public Object findTodos() {
        Map ctx = map();

        FilterChain<Todo> chain = TodoController
                .instance()
                .filter("userId", Context.getUser().getId());

        Pager<Todo> pager = new QueryToPager<Todo>(
                Context.getRequest(),
                TodoController.instance(),
                chain
        )
                .allowedFilters("dueAt")
                .allowedSortable("name", "dueAt")
                .pager();

        ctx.put("pager", pager);

        return ctx;
    }


    @Path("/todos/create")
    @POST
    @JsonView(RestrictedViews.Owner.class)
    public Object createTodo(@ObjectParam Todo todo) {
        todo.setUserId(Context.getUser().getId());
        todo.setId(null);
        TodoController.instance().save(todo);
        return todo;
    }

    @Path("/todos/update/:todoId")
    @POST
    @JsonView(RestrictedViews.Owner.class)
    public Object updateTodo(@PathParam("todoId") Long todoId, @ObjectParam Todo updatedTodo) {
        Todo todo = TodoController.instance()
                .filter("userId", Context.getUser().getId())
                .filter("id", todoId)
                .first();
        if (todo == null) {
            throw new ClientException("Could not find Todo for ID " + todoId, 404);
        }

        // Only merge in the fields the user is allowed to update.
        todo = new SafeMerger()
                .optional("dueAt", "name", "done")
                .merge(updatedTodo, todo);

        TodoController.instance().save(todo);
        return todo;
    }


    @Path("/todos/delete/:todoId")
    @POST
    @JsonView(RestrictedViews.Owner.class)
    public Object deleteTodo(@PathParam("todoId") Long todoId) {
        Todo todo = TodoController.instance()
                .filter("userId", Context.getUser().getId())
                .filter("id", todoId)
                .first();
        TodoController.instance().softDelete(todo);
        return map(val("succeeded", true));
    }

}