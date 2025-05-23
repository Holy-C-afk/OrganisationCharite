package com.charite.controller;

import com.charite.model.User;
import com.charite.model.Organization;
import com.charite.model.CharityAction;
import com.charite.model.Donation;
import com.charite.service.UserService;
import com.charite.service.OrganizationService;
import com.charite.service.CharityActionService;
import com.charite.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CharityActionService charityActionService;

    @Autowired
    private DonationService donationService;

    @GetMapping
    public String adminDashboard(Model model) {
        logger.debug("Accessing admin dashboard");
        try {
            // Statistiques utilisateurs
            Map<String, Object> userStats = userService.getUserStatistics();
            model.addAttribute("userStats", userStats);

            // Statistiques organisations
            Map<String, Object> orgStats = organizationService.getOrganizationStatistics();
            model.addAttribute("orgStats", orgStats);

            // Statistiques actions caritatives
            Map<String, Object> actionStats = charityActionService.getActionStatistics();
            model.addAttribute("actionStats", actionStats);

            // Statistiques dons
            Map<String, Object> donationStats = donationService.getDonationStatistics();
            model.addAttribute("donationStats", donationStats);

            // Activités récentes
            model.addAttribute("recentUsers", userService.getRecentUsers(5));
            model.addAttribute("recentOrganizations", organizationService.getRecentOrganizations(5));
            model.addAttribute("recentCharityActions", charityActionService.getRecentActions(5));
            model.addAttribute("recentDonations", donationService.getRecentDonations(5));

            logger.debug("Admin dashboard data loaded successfully");
            return "admin/dashboard";
        } catch (Exception e) {
            logger.error("Error loading admin dashboard data", e);
            model.addAttribute("error", "Une erreur s'est produite lors du chargement du tableau de bord");
            return "error";
        }
    }

    // Gestion des utilisateurs
    @GetMapping("/users")
    public String userManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/update-role")
    public String updateUserRole(@PathVariable Long id, @RequestParam User.UserRole role, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(id, role);
            redirectAttributes.addFlashAttribute("success", "Rôle utilisateur mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du rôle: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Gestion des organisations
    @GetMapping("/organizations")
    public String organizationManagement(Model model) {
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        return "admin/organizations";
    }

    @PostMapping("/organizations/{id}/update-status")
    public String updateOrganizationStatus(@PathVariable Long id, @RequestParam Organization.OrganizationStatus status, RedirectAttributes redirectAttributes) {
        try {
            organizationService.updateOrganizationStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Statut de l'organisation mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
        return "redirect:/admin/organizations";
    }

    // Gestion des actions caritatives
    @GetMapping("/charity-actions")
    public String charityActionManagement(Model model) {
        model.addAttribute("charityActions", charityActionService.getAllActions());
        return "admin/charity-actions";
    }

    @PostMapping("/charity-actions/{id}/update-status")
    public String updateCharityActionStatus(@PathVariable Long id, @RequestParam CharityAction.ActionStatus status, RedirectAttributes redirectAttributes) {
        try {
            charityActionService.updateActionStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Statut de l'action caritative mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
        return "redirect:/admin/charity-actions";
    }

    // Gestion des dons
    @GetMapping("/donations")
    public String donationManagement(Model model) {
        model.addAttribute("donations", donationService.getAllDonations());
        return "admin/donations";
    }

    @PostMapping("/donations/{id}/update-status")
    public String updateDonationStatus(@PathVariable Long id, @RequestParam Donation.Status status, RedirectAttributes redirectAttributes) {
        try {
            donationService.updateDonationStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Statut du don mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
        return "redirect:/admin/donations";
    }

    // Rapports et statistiques
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("donationStats", donationService.getDonationStatistics());
        model.addAttribute("userStats", userService.getUserStatistics());
        model.addAttribute("charityActionStats", charityActionService.getActionStatistics());
        return "admin/reports";
    }
} 