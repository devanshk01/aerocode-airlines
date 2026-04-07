package com.aerocode.airlines.service;

import com.aerocode.airlines.model.Booking;
import com.aerocode.airlines.model.Flight;
import com.aerocode.airlines.model.Passenger;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Generates formatted ticket text for export/download.
 * Based on the original console ticket display format.
 */
@Service
public class TicketExportService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public String generateTicketText(Booking booking) {
        Passenger p = booking.getPassenger();
        Flight f = booking.getFlight();

        StringBuilder sb = new StringBuilder();

        String line = "=".repeat(72);
        String thinLine = "-".repeat(72);

        sb.append(line).append("\n");
        sb.append(centerText("✈  AEROCODE AIRLINES", 72)).append("\n");
        sb.append(centerText("E-TICKET / BOARDING PASS", 72)).append("\n");
        sb.append(line).append("\n\n");

        // Booking Info
        sb.append("  BOOKING REFERENCE  : #").append(String.format("%06d", booking.getId())).append("\n");
        sb.append("  BOOKING DATE       : ").append(booking.getBookingTime().format(FORMATTER)).append("\n");
        sb.append("  STATUS             : ").append(booking.getStatus()).append("\n");
        sb.append("\n").append(thinLine).append("\n");

        // Passenger Details
        sb.append("  PASSENGER DETAILS\n");
        sb.append(thinLine).append("\n");
        sb.append("  Name               : ").append(p.getName()).append("\n");
        sb.append("  Age                : ").append(p.getAge()).append("\n");
        sb.append("  Gender             : ").append(p.getGender()).append("\n");
        sb.append("  Mobile Number      : ").append(p.getMobileNum()).append("\n");
        sb.append("  Email              : ").append(p.getEmail()).append("\n");

        if (f.getType().equalsIgnoreCase("International") && p.getPassportNum() != 0) {
            sb.append("  Passport Number    : ").append(p.getPassportNum()).append("\n");
        }

        sb.append("\n").append(thinLine).append("\n");

        // Flight Details
        sb.append("  FLIGHT DETAILS\n");
        sb.append(thinLine).append("\n");
        sb.append("  Flight Number      : ").append(f.getFlightNumber()).append("\n");
        sb.append("  Date               : ").append(f.getDate()).append("\n");
        sb.append("  From               : ").append(f.getFromCity()).append("\n");
        sb.append("  To                 : ").append(f.getToCity()).append("\n");
        sb.append("  Departure Time     : ").append(f.getDeparture()).append("\n");
        sb.append("  Arrival Time       : ").append(f.getArrival()).append("\n");
        sb.append("  Aircraft           : ").append(f.getAircraft()).append("\n");
        sb.append("  Flight Type        : ").append(f.getType()).append("\n");
        sb.append("  Distance           : ").append(String.format("%.0f", f.getDistance())).append(" kms\n");
        sb.append("  Flight Duration    : ").append(f.getFlightDuration()).append("\n");

        sb.append("\n").append(thinLine).append("\n");

        // Payment
        sb.append("  PAYMENT DETAILS\n");
        sb.append(thinLine).append("\n");
        sb.append("  Ticket Price       : Rs. ").append(String.format("%.2f", booking.getTicketPrice())).append("\n");
        sb.append("  Payment Status     : Successful\n");
        sb.append("  Ticket Status      : ").append(booking.getStatus()).append("\n");

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            sb.append("  Cancellation Fee   : Rs. ").append(String.format("%.2f", booking.getTicketPrice() * 0.4)).append("\n");
            sb.append("  Refund Amount      : Rs. ").append(String.format("%.2f", booking.getRefundAmount())).append("\n");
            sb.append("  Cancelled On       : ").append(booking.getCancellationTime().format(FORMATTER)).append("\n");
        }

        sb.append("\n").append(line).append("\n");
        sb.append(centerText("Thank you for choosing AeroCode Airlines!", 72)).append("\n");
        sb.append(centerText("Have a safe and pleasant journey.", 72)).append("\n");
        sb.append(line).append("\n");

        return sb.toString();
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
