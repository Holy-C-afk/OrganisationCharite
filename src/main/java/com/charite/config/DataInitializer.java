package com.charite.config;

import com.charite.model.User;
import com.charite.model.Donation;
import com.charite.model.CharityAction;
import com.charite.model.Organization;
import com.charite.repository.UserRepository;
import com.charite.repository.DonationRepository;
import com.charite.repository.CharityActionRepository;
import com.charite.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CharityActionRepository charityActionRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Créer un utilisateur de test
            User testUser = new User();
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setEmail("test@example.com");
            testUser.setPassword(passwordEncoder.encode("password"));
            testUser.setRole(User.UserRole.USER);
            testUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(testUser);

            // Créer une organisation de test
            Organization testOrg = new Organization();
            testOrg.setName("Test Organization");
            testOrg.setLegalRegistrationNumber("123456789");
            testOrg.setAddress("123 Test Street");
            testOrg.setContactEmail("org@example.com");
            testOrg.setContactPhone("0123456789");
            testOrg.setStatus(Organization.OrganizationStatus.APPROVED);
            testOrg.setCreatedAt(LocalDateTime.now());
            organizationRepository.save(testOrg);

            // Créer une action caritative de test
            CharityAction testAction = new CharityAction();
            testAction.setTitle("Distribution Alimentaire");
            testAction.setDescription("Distribution de nourriture aux personnes dans le besoin");
            testAction.setCategory(CharityAction.ActionCategory.FOOD);
            testAction.setLocation("Paris");
            testAction.setDate(LocalDate.now().plusDays(7));
            testAction.setStatus(CharityAction.ActionStatus.UPCOMING);
            testAction.setCreatedAt(LocalDateTime.now());
            testAction.setOrganizer(testUser);
            charityActionRepository.save(testAction);

            // Créer un don de test
            Donation testDonation = new Donation();
            testDonation.setAmount(new BigDecimal("100.00"));
            testDonation.setCategory(Donation.DonationCategory.FOOD);
            testDonation.setMessage("Don pour la distribution alimentaire");
            testDonation.setCreatedAt(LocalDateTime.now());
            testDonation.setUser(testUser);
            testDonation.setCharityAction(testAction);
            testDonation.setStatus(Donation.Status.COMPLETED);
            donationRepository.save(testDonation);
        };
    }
} 