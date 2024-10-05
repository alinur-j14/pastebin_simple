package com.project.pastebinsimple.feature.listener;

import com.project.pastebinsimple.feature.event.OnDeleteCacheEvent;
import com.project.pastebinsimple.feature.service.PasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OnDeleteCacheListener {

    private final PasteService pasteService;

    @EventListener
    public void handleDeleteCache(OnDeleteCacheEvent event) {
        List<Long> ids = event.getIds();
        pasteService.deleteCache(ids);
    }

}
