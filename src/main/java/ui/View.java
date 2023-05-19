package ui;

import domain.ReservationService;
import domain.GuestService;
import models.Guest;
import models.Reservation;
import models.Guest;
import org.springframework.stereotype.Component;
import ui.ConsoleIO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            min = Math.min(min, option.getOptionNumber());
            max = Math.max(max, option.getOptionNumber());
        }

        String message = String.format("Select [%s-%s]: ", min, max - 1);
        return MainMenuOption.fromOptionNumber(io.readInt(message, min, max - 1));
    }

    public String getReservationId() {
        displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_ID.getDescription());
        return io.readString("Select a date [MM/dd/yyyy]: ");
    }

    public String getGuestNamePrefix() {
        return io.readRequiredString("Guest last name starts with: ");
    }

    public Guest chooseGuest(List<Guest> guests) {
        if (guests.size() == 0) {
            io.println("No guests found");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, guest.getFirstName(), guest.getLastName());
        }
        index--;

        if (guests.size() > 25) {
            io.println("More than 25 guests found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }

    public Reservation makeReservation(Guest guest) {
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("End date [MM/dd/yyyy]: "));
        return reservation;
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    // display only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations) {
            io.printf("%s %s - Start Date: %s - End Date: %s%n",
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getStartDate(),
                    reservation.getEndDate()
            );
        }
    }

    public void displayGuests(List<Guest> guests) {

        if (guests.size() == 0) {
            io.println("No guests found");
        }

        for (Guest guest : guests) {
            io.printf("%s: %s %s, Email: %s, Phone: %s%n",
                    guest.getId(),
                    guest.getFirstName(),
                    guest.getLastName(),
                    guest.getEmail(),
                    guest.getPhoneNumber());
        }
    }
}


