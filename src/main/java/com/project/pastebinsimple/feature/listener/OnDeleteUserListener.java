package com.project.pastebinsimple.feature.listener;

import com.project.pastebinsimple.feature.event.OnDeleteUserEvent;
import com.project.pastebinsimple.feature.service.StorageService;
import com.project.pastebinsimple.store.model.Paste;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OnDeleteUserListener {

    private final StorageService storageService;

    @EventListener
    public void handleDeleteUser(OnDeleteUserEvent event) {
        List<String> pastes = event.getPastes();
        storageService.deleteFiles(pastes);
    }

}