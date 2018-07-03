package io.stallion.starter.examples;

import com.fasterxml.jackson.annotation.JsonView;
import io.stallion.dataAccess.ModelBase;
import io.stallion.utils.json.RestrictedViews;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Table(name="todos")
public class Todo extends ModelBase {
    private String name;
    private ZonedDateTime dueAt;
    private Long userId;
    private boolean done;
    private String internalSecret;

    @Column
    public String getName() {
        return name;
    }

    public Todo setName(String name) {
        this.name = name;
        return this;
    }

    @Column
    public ZonedDateTime getDueAt() {
        return dueAt;
    }

    public Todo setDueAt(ZonedDateTime dueAt) {
        this.dueAt = dueAt;
        return this;
    }

    @Column
    public Long getUserId() {
        return userId;
    }

    public Todo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @Column
    public boolean isDone() {
        return done;
    }

    public Todo setDone(boolean done) {
        this.done = done;
        return this;
    }

    @Column
    @JsonView(RestrictedViews.Internal.class)
    public String getInternalSecret() {
        return internalSecret;
    }

    public Todo setInternalSecret(String internalSecret) {
        this.internalSecret = internalSecret;
        return this;
    }
}