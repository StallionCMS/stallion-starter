package io.stallion.starter.examples;

import io.stallion.dataAccess.filtering.FilterOperator;
import io.stallion.jobs.JobComplete;
import io.stallion.jobs.Schedule;
import io.stallion.services.Log;
import io.stallion.users.IUser;
import io.stallion.users.UserController;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static io.stallion.utils.DateUtils.utcNow;
import static io.stallion.utils.Literals.list;
import static io.stallion.utils.Literals.map;

/**
 * Job to find all todos due in the next 7 days.
 */
public class WeeklyTodosJob extends JobComplete {
    @Override
    public String getName() {
        return "weekly-todos-job";
    }

    /**
     * Job is considered overdue if at hasn't run in these many minutes. Make
     * this a bit longer than the longest amount of time between scheduled runs.
     *
     * @return
     */
    @Override
    public int getAlertThresholdMinutes() {
        return 10 * 24 * 60;
    }

    @Override
    public Schedule getSchedule() {
        return new Schedule()
                .everyMonth()
                .daysOfWeek(DayOfWeek.MONDAY)
                .hours(6)
                .minutes(30)
                .verify();
    }

    @Override
    public void execute() {
        Log.info("Start WeeklyTodosJob");
        ZonedDateTime dt = utcNow()
                .plusDays(7)
                ;
        List<Todo> todos = TodoController.instance().filterBy("dueAt", dt, FilterOperator.LESS_THAN).all();
        Map<Long, List<Todo>> todosByUserId = map();
        for(Todo todo: todos) {
            if (!todosByUserId.containsKey(todo.getUserId())) {
                todosByUserId.put(todo.getUserId(), list());
            }
            todosByUserId.get(todo.getUserId()).add(todo);
        }



        for(Map.Entry<Long, List<Todo>> userTodos: todosByUserId.entrySet()) {
            IUser user = UserController.instance().forId(userTodos.getKey());
            if (user != null) {
                Log.info("Email user {0} with reminder for {1} todos", user.getEmail(), userTodos.getValue().size());
                new WeeklyTodosEmailer(user, userTodos.getValue())
                        .sendEmail();
            }
        }
    }
}