package com.charite.controller;

import com.charite.model.CharityAction;
import com.charite.model.User;
import com.charite.service.CharityActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/charity-actions")
public class CharityActionController {

    @Autowired
    private CharityActionService charityActionService;

    @GetMapping
    public String getCharityActionsPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("activeActions", charityActionService.getActionsByStatus(CharityAction.ActionStatus.ACTIVE));
        model.addAttribute("upcomingActions", charityActionService.getActionsByStatus(CharityAction.ActionStatus.UPCOMING));
        model.addAttribute("completedActions", charityActionService.getActionsByStatus(CharityAction.ActionStatus.COMPLETED));
        model.addAttribute("userActions", charityActionService.getOrganizerActions(user));
        return "charity-actions";
    }

    @PostMapping("/create")
    public String createAction(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam CharityAction.ActionCategory category,
                             @RequestParam String location,
                             @RequestParam LocalDate date,
                             @RequestParam(required = false) MultipartFile image,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        try {
            charityActionService.createAction(title, description, category, location, date, image, user);
            redirectAttributes.addFlashAttribute("success", "Charity action created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create charity action: " + e.getMessage());
        }
        return "redirect:/charity-actions";
    }

    @PostMapping("/{id}/update-status")
    @ResponseBody
    public String updateActionStatus(@PathVariable Long id,
                                   @RequestParam CharityAction.ActionStatus status) {
        try {
            charityActionService.updateActionStatus(id, status);
            return "Status updated successfully";
        } catch (Exception e) {
            return "Failed to update status: " + e.getMessage();
        }
    }
} 