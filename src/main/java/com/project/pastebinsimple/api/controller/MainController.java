package com.project.pastebinsimple.api.controller;

import com.project.pastebinsimple.api.dto.request.PasteRequest;
import com.project.pastebinsimple.api.dto.response.PasteResponse;
import com.project.pastebinsimple.api.dto.response.UserResponse;
import com.project.pastebinsimple.feature.service.PasteService;
import com.project.pastebinsimple.feature.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final PasteService pasteService;

    @GetMapping("/home")
    public String getMainPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        UserResponse user = userService.getUserResponseByOauth2Id(principal.getName());
        List<PasteResponse> pasteList = pasteService.getPastesByUserOauth2Id(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("pastes", pasteList);
        model.addAttribute("pasteRequest", new PasteRequest());

        return "main";
    }

    @PostMapping("/create-paste")
    public String createPaste(@ModelAttribute PasteRequest pasteRequest, @AuthenticationPrincipal OAuth2User principal) {
        pasteService.savePasteAsync(pasteRequest, principal);

        return "redirect:/api/main/home";
    }

    @PostMapping("/delete-paste")
    public String deletePaste(@RequestParam String url, @AuthenticationPrincipal OAuth2User principal) {

        System.out.println();

        pasteService.deletePaste(url, principal);
        return "redirect:/api/main/home";
    }

}
