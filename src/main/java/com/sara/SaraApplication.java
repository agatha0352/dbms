package com.sara;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sara.entity.Resource;
import com.sara.entity.User;
import com.sara.repository.ResourceRepository;
import com.sara.repository.UserRepository;

@SpringBootApplication
public class SaraApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaraApplication.class, args);
    }

    @Bean
    CommandLineRunner seedUsersAndResources(UserRepository userRepository, ResourceRepository resourceRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Admin
            User admin = userRepository.findByUsername("admin").orElseGet(User::new);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("admin@sara.com");
            admin.setFullName("System Administrator");
            admin.setPhoneNumber("1234567890");
            admin.setAddress("123 Admin Street");
            admin.setCity("Washington");
            admin.setState("DC");
            admin.setZipCode("20001");
            admin.setRole(User.UserRole.ADMIN);
            admin.setEnabled(true);
            admin.setVerified(true);
            userRepository.save(admin);

            // Donor
            if (!userRepository.existsByUsername("donor1")) {
                User donor = new User();
                donor.setUsername("donor1");
                donor.setPassword(passwordEncoder.encode("password"));
                donor.setEmail("donor1@sara.com");
                donor.setFullName("John Donor");
                donor.setPhoneNumber("2345678910");
                donor.setAddress("456 Generous Ave");
                donor.setCity("New York");
                donor.setState("NY");
                donor.setZipCode("10001");
                donor.setRole(User.UserRole.DONOR);
                donor.setEnabled(true);
                donor.setVerified(true);
                userRepository.save(donor);
            }

            // Volunteer 1
            if (!userRepository.existsByUsername("volunteer1")) {
                User volunteer1 = new User();
                volunteer1.setUsername("volunteer1");
                volunteer1.setPassword(passwordEncoder.encode("password"));
                volunteer1.setEmail("volunteer1@sara.com");
                volunteer1.setFullName("Sarah Helper");
                volunteer1.setPhoneNumber("2345678920");
                volunteer1.setAddress("789 Service Road");
                volunteer1.setCity("Los Angeles");
                volunteer1.setState("CA");
                volunteer1.setZipCode("90001");
                volunteer1.setRole(User.UserRole.VOLUNTEER);
                volunteer1.setEnabled(true);
                volunteer1.setVerified(true);
                userRepository.save(volunteer1);
            }

            // Volunteer 2
            if (!userRepository.existsByUsername("volunteer2")) {
                User volunteer2 = new User();
                volunteer2.setUsername("volunteer2");
                volunteer2.setPassword(passwordEncoder.encode("password"));
                volunteer2.setEmail("volunteer2@sara.com");
                volunteer2.setFullName("David Volunteer");
                volunteer2.setPhoneNumber("2345678950");
                volunteer2.setAddress("987 Help Street");
                volunteer2.setCity("Seattle");
                volunteer2.setState("WA");
                volunteer2.setZipCode("98101");
                volunteer2.setRole(User.UserRole.VOLUNTEER);
                volunteer2.setEnabled(true);
                volunteer2.setVerified(true);
                userRepository.save(volunteer2);
            }

            // Needer
            if (!userRepository.existsByUsername("needer1")) {
                User needer = new User();
                needer.setUsername("needer1");
                needer.setPassword(passwordEncoder.encode("password"));
                needer.setEmail("needer1@sara.com");
                needer.setFullName("Mike Needhelp");
                needer.setPhoneNumber("2345678930");
                needer.setAddress("321 Need Help Blvd");
                needer.setCity("Miami");
                needer.setState("FL");
                needer.setZipCode("33101");
                needer.setRole(User.UserRole.NEEDER);
                needer.setEnabled(true);
                needer.setVerified(true);
                userRepository.save(needer);
            }

            // Resource 1 (Food, by donor)
            User donor = userRepository.findByUsername("donor1").orElse(null);
            if (donor != null && resourceRepository.findByUser(donor).stream().noneMatch(r -> r.getName().equals("Emergency Food Packages"))) {
                Resource foodResource = new Resource(
                    "Emergency Food Packages",
                    "Non-perishable food items for families in need",
                    Resource.ResourceType.FOOD,
                    50,
                    "New York Community Center, 456 Generous Ave, New York, NY",
                    "Contact: +1234567891",
                    donor
                );
                foodResource.setVerified(true);
                resourceRepository.save(foodResource);
            }

            // Resource 2 (Water, by needer)
            User needer = userRepository.findByUsername("needer1").orElse(null);
            if (needer != null && resourceRepository.findByUser(needer).stream().noneMatch(r -> r.getName().equals("Bottled Water Supply"))) {
                Resource waterResource = new Resource(
                    "Bottled Water Supply",
                    "Clean drinking water bottles",
                    Resource.ResourceType.WATER,
                    200,
                    "Miami Community Center, 321 Need Help Blvd, Miami, FL",
                    "Contact: +1234567892",
                    needer
                );
                waterResource.setVerified(true);
                resourceRepository.save(waterResource);
            }
        };
    }
}