package com.charite.controller;

import com.charite.model.CharityAction;
import com.charite.model.User;
import com.charite.service.CharityActionService;
import com.charite.service.UserService;
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

    @Autowired
    private UserService userService;

    @GetMapping
    public String getCharityActionsPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
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
                             @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                             RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour créer une action.");
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getUsername()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable.");
            return "redirect:/login";
        }
        try {
            charityActionService.createAction(title, description, category, location, date, image, user);
            redirectAttributes.addFlashAttribute("success", "Action caritative créée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Échec de la création de l'action caritative : " + e.getMessage());
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

    @PostMapping("/{id}/participate")
    public String participateInAction(@PathVariable Long id,
                                    @RequestParam String fullName,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String availability,
                                    @RequestParam(required = false) String message,
                                    RedirectAttributes redirectAttributes) {
        try {
            charityActionService.addParticipant(id, fullName, email, phone, availability, message);
            redirectAttributes.addFlashAttribute("success", "Votre participation a été enregistrée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement de votre participation : " + e.getMessage());
        }
        return "redirect:/charity-actions";
    }
} 