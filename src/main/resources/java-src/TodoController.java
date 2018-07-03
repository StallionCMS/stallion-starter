package io.stallion.starter.examples;

import io.stallion.asyncTasks.AsyncCoordinator;
import io.stallion.dataAccess.DataAccessRegistry;
import io.stallion.dataAccess.LocalMemoryStash;
import io.stallion.dataAccess.StandardModelController;

import java.time.ZonedDateTime;

import static io.stallion.utils.DateUtils.utcNow;


public class TodoController extends StandardModelController<Todo> {
    public static TodoController instance() {
        return (TodoController) DataAccessRegistry.instance().get("todos");
    }

    public static void register() {
        DataAccessRegistry.instance().registerDbOrFileModel(Todo.class, TodoController.class, "todos");
        //DataAccessRegistry.instance().registerDbModel(Todo.class, TodoController.class, LocalMemoryStash.class);
    }

    @Override
    public void onPostSave(Todo todo) {

        // Todo is not done, and due more than 12 hours from now
        if (!todo.isDone() && todo.getDueAt().minusHours(12).isAfter(utcNow())) {
            String taskKey = "todo-reminder-" + todo.getId();
            ZonedDateTime remindAt = todo.getDueAt().minusHours(12);
            // Will update the schedule if a task already exists.
            AsyncCoordinator.instance().enqueue(
                    new ReminderEmailScheduledTask(),
                    taskKey,
                    remindAt.toInstant().toEpochMilli()
            );
        }
    }
}
