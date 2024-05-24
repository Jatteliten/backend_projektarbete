package com.example.backend.configuration;

import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class TemplateSeeder {
    private final ThymeleafTemplateRepo thymeleafTemplateRepo;

    public TemplateSeeder(ThymeleafTemplateRepo thymeleafTemplateRepo) {
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
    }

    @Bean
    public CommandLineRunner templateInitializer() {
        return args -> {

            Optional<ThymeLeafTemplates> templateOptional = thymeleafTemplateRepo.findByTitle("confirmBookingEmailTemplate");

            if (templateOptional.isEmpty()) {
                ThymeLeafTemplates template = new ThymeLeafTemplates("confirmBookingEmailTemplate",
                        "<div class=\"container\"><a href=\"/\" style=\"text-decoration: none; color: #333333\"><h1>Booking ASMADALI</h1></a><br><div style=\"border: 1px solid #000; padding: 10px;\"><table class=\"table\"><thead class=\"thead-dark\"><th><h1>BOOKING DETAILS</h1></th><th></th></thead><tbody><tr><td class=\"label-col\"><label>Checkin:</label></td><td><label style=\"text-align: left;\" th:text=\"${start}\"></label></td></tr><tr><td class=\"label-col\"><label>Checkout:</label></td><td><label style=\"text-align: left;\" th:text=\"${end}\"></label></td></tr></tbody></table><table class=\"table\"><thead class=\"thead-dark\"><th><h1>CUSTOMER DETAILS</h1></th><th></th></thead><tbody><tr><td class=\"label-col\"><label>Customer name:</label></td><td><label style=\"text-align: left;\" th:text=\"${customerName}\"></label></td></tr><tr><td><label>Customer Email:</label></td><td><label style=\"text-align: left;\" th:text=\"${email}\"></label></td></tr><tr><td><label>Customer Phone number:</label></td><td><label style=\"text-align: left;\" th:text=\"${customerPhone}\"></label></td></tr></tbody></table><table class=\"table\"><thead class=\"thead-dark\"><th><h1>ROOM DETAILS</h1></th><th></th></thead><tbody><tr><td class=\"label-col\"><label>Room number:</label></td><td><label style=\"text-align: left;\" th:text=\"'Room nr '+${roomId}\"></label></td></tr><tr><td><label>Room price per night:</label></td><td><label style=\"text-align: left;\" th:text=\"${pricePerNight}+' :-'\"></label></td></tr><tr><td><label>Room size:</label></td><td><label style=\"text-align: left;\" th:text=\"'Size: ' + ${roomSize}\"></label></td></tr><tr><td><label>Number of nights:</label></td><td><label style=\"text-align: left;\" th:text=\"${amountOfNights} + ' Nights'\"></label></td></tr></tbody></table><table class=\"table\"><thead class=\"thead-dark\"><th><h1>DISCOUNT DETAILS</h1></th><th></th></thead><tr><td class=\"label-col\"><label>Sunday Discount:</label></td><td><label style=\"text-align: left;\" th:text=\"'Discount amount: ' + ${sundayDiscount}+' :-'\"></label></td></tr><tr><td><label>Longstay Discount:</label></td><td><label style=\"text-align: left;\" th:text=\"'Discount amount: ' + ${longStayDiscount}+' :-'\"></label></td></tr><tr><td><label>Regular Customer Discount:</label></td><td><label style=\"text-align: left;\" th:text=\"'Discount amount: ' + ${tenDayDiscount}+' :-'\"></label></td></tr><tr><td><label>Full price:</label></td><td><label style=\"text-align: left;\" th:text=\"${fullPrice}+' :-'\"></label></td></tr><tr><td><label>Your Discounted price:</label></td><td><label style=\"text-align: left;\" th:text=\"${discountedPrice}+' :-'\"></label></td></tr></table><table class=\"table\"><thead class=\"thead-dark\"><th><h1>ASMADALI BOOKING </h1></th><th></th></thead><tbody><tr><td class=\"label-col\"><label>Thank you for your booking! We look forward to your stay.</label></td></tr></tbody></table></div></div>");
                thymeleafTemplateRepo.save(template);
            }
        };
    }
}
