package io.stallion.starter.examples;

import io.stallion.asyncTasks.AsyncTaskHandlerBase;
import io.stallion.services.Log;
import io.stallion.users.IUser;
import io.stallion.users.UserController;


public class ReminderEmailScheduledTask extends AsyncTaskHandlerBase {
    private Long todoId;

    @Override
    public void process() {
        Log.info("Run reminder scheduled task for todo id {0}", todoId);
        Todo todo = TodoController.instance().forId(todoId);
        if (todo == null) {
            return;
        }
        if (todo.isDone()) {
            return;
        }
        IUser user = UserController.instance().forId(todo.getUserId());
        new ReminderEmailer(user, todo).sendEmail();
    }

    public Long getTodoId() {
        return todoId;
    }

    public ReminderEmailScheduledTask setTodoId(Long todoId) {
        this.todoId = todoId;
        return this;
    }
}
