package io.stallion.starter.examples;

import io.stallion.email.ContactableEmailer;
import io.stallion.users.IUser;

import static io.stallion.utils.Literals.*;


public class ReminderEmailer extends ContactableEmailer<IUser> {

    private Todo todo;

    public ReminderEmailer(IUser user, Todo todo) {
        super(user);
        this.todo = todo;
        // Add the todo to the template context.
        put("todo", todo);
    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public String getTemplate() {
        return "$APP_NAME$:reminder-email.jinja";
    }

    @Override
    public String getSubject() {
        return "To-do due soon!";
    }
}
