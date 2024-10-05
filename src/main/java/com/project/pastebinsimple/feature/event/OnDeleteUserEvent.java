package com.project.pastebinsimple.feature.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class OnDeleteUserEvent extends ApplicationEvent {

    private final List<String> pastes;

    public OnDeleteUserEvent(List<String> pastes) {
        super(pastes);
        this.pastes = pastes;
    }

    public List<String> getPastes() {
        return this.pastes;
    }

}
