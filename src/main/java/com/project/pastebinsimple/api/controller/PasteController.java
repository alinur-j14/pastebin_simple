package com.project.pastebinsimple.api.controller;

import com.project.pastebinsimple.feature.service.PasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/pastes")
@RequiredArgsConstructor
public class PasteController {
    private final PasteService pasteService;

    @GetMapping("/get-paste/{hash}")
    public String getPaste(@PathVariable String hash, Model model) {
        String url = pasteService.getPasteUrl(hash);
        String text = pasteService.getText(url);

        model.addAttribute("text", text);

        return "paste";
    }

}
