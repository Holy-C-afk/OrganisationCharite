package com.charite.controller;

import com.charite.model.Donation;
import com.charite.model.User;
import com.charite.service.DonationService;
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

    @GetMapping
    public String getDonationsPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("donations", donationService.getUserDonations(user));
        model.addAttribute("totalAmount", donationService.getTotalDonationsAmount(user));
        return "donations";
    }

    @PostMapping("/create")
    public String createDonation(@RequestParam("amount") String amountStr,
                               @RequestParam("category") String categoryStr,
                               @RequestParam(value = "message", required = false) String message,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            Donation.DonationCategory category = Donation.DonationCategory.valueOf(categoryStr);
            
            donationService.createDonation(amount, category, message, user);
            redirectAttributes.addFlashAttribute("success", "Donation created successfully!");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid amount format");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid category");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create donation: " + e.getMessage());
        }
        return "redirect:/donations";
    }
} 