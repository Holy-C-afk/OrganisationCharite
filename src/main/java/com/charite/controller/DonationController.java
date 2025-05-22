package com.charite.controller;

import com.charite.model.Donation;
import com.charite.model.User;
import com.charite.service.DonationService;
import com.charite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getDonationsPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("donations", donationService.getUserDonations(user));
        model.addAttribute("totalAmount", donationService.getTotalDonationsAmount(user));
        return "donations";
    }

    @PostMapping("/create")
    public String createDonation(@RequestParam("amount") String amountStr,
                               @RequestParam("category") String categoryStr,
                               @RequestParam(value = "message", required = false) String message,
                               @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vous devez être connecté pour faire un don.");
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getUsername()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable.");
            return "redirect:/login";
        }
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            Donation.DonationCategory category = Donation.DonationCategory.valueOf(categoryStr);
            
            donationService.createDonation(amount, category, message, user);
            redirectAttributes.addFlashAttribute("success", "Donation créée avec succès !");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Format de montant invalide");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Catégorie invalide");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Échec de la création du don : " + e.getMessage());
        }
        return "redirect:/donations";
    }
} 