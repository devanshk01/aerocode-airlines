package com.aerocode.airlines.controller;

import com.aerocode.airlines.model.*;
import com.aerocode.airlines.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TicketExportController {

    @Autowired private TicketExportService ticketExportService;
    @Autowired private BookingService bookingService;
    @Autowired private UserService userService;

    @GetMapping("/export/ticket/{bookingId}")
    public ResponseEntity<byte[]> exportTicket(@PathVariable Long bookingId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Security: only owner or admin
        if (user.getRole() != Role.ADMIN &&
            !booking.getPassenger().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        String ticketText = ticketExportService.generateTicketText(booking);
        byte[] content = ticketText.getBytes();

        String filename = "AeroCode_Ticket_" + booking.getFlight().getFlightNumber() + "_" +
                           String.format("%06d", booking.getId()) + ".txt";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
