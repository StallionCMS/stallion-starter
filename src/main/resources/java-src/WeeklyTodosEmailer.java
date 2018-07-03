package io.stallion.starter.examples;

import io.stallion.email.ContactableEmailer;
import io.stallion.users.IUser;

import java.util.List;

import static io.stallion.utils.Literals.*;


public class WeeklyTodosEmailer extends ContactableEmailer<IUser> {
    public WeeklyTodosEmailer(IUser user, List<Todo> todos) {
        super(user);
        put("todos", todos);

    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public String getTemplate() {
        return "$APP_NAME$:weekly-todos-email.jinja";
    }

    @Override
    public String getSubject() {
        return "Your Todos This Week";
    }
}
