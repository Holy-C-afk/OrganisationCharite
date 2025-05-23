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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

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
            // Créer un utilisateur admin s'il n'existe pas
            if (!userRepository.existsByEmail("admin@charite.com")) {
                logger.info("Creating admin user");
                User adminUser = new User();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setEmail("admin@charite.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRole(User.UserRole.SUPER_ADMIN);
                adminUser.setCreatedAt(LocalDateTime.now());
                userRepository.save(adminUser);
                logger.info("Admin user created successfully");
            } else {
                logger.info("Admin user already exists");
            }

            // Créer un utilisateur de test s'il n'existe pas
            if (!userRepository.existsByEmail("test@example.com")) {
                logger.info("Creating test user");
                User testUser = new User();
                testUser.setFirstName("Test");
                testUser.setLastName("User");
                testUser.setEmail("test@example.com");
                testUser.setPassword(passwordEncoder.encode("password"));
                testUser.setRole(User.UserRole.USER);
                testUser.setCreatedAt(LocalDateTime.now());
                userRepository.save(testUser);
                logger.info("Test user created successfully");

                // Créer une organisation de test
                logger.info("Creating test organization");
                Organization testOrg = new Organization();
                testOrg.setName("Test Organization");
                testOrg.setLegalRegistrationNumber("123456789");
                testOrg.setAddress("123 Test Street");
                testOrg.setContactEmail("org@example.com");
                testOrg.setContactPhone("0123456789");
                testOrg.setStatus(Organization.OrganizationStatus.APPROVED);
                testOrg.setCreatedAt(LocalDateTime.now());
                organizationRepository.save(testOrg);
                logger.info("Test organization created successfully");

                // Créer une action caritative de test
                logger.info("Creating test charity action");
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
                logger.info("Test charity action created successfully");

                // Créer un don de test
                logger.info("Creating test donation");
                Donation testDonation = new Donation();
                testDonation.setAmount(new BigDecimal("100.00"));
                testDonation.setCategory(Donation.DonationCategory.FOOD);
                testDonation.setMessage("Don pour la distribution alimentaire");
                testDonation.setCreatedAt(LocalDateTime.now());
                testDonation.setUser(testUser);
                testDonation.setCharityAction(testAction);
                testDonation.setStatus(Donation.Status.COMPLETED);
                donationRepository.save(testDonation);
                logger.info("Test donation created successfully");
            } else {
                logger.info("Test user already exists, skipping test data creation");
            }
        };
    }
} 