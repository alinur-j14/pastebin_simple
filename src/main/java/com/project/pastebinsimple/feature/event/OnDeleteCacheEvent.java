package com.project.pastebinsimple.feature.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class OnDeleteCacheEvent extends ApplicationEvent {

    private final List<Long> ids;

    public OnDeleteCacheEvent(List<Long> ids) {
        super(ids);
        this.ids = ids;
    }

    public List<Long> getIds() {
        return this.ids;
    }

}
