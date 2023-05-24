package admin.ui;

import admin.models.Guest;
import admin.models.Host;
import admin.models.Reservation;
import admin.ui.menus.FindGuestOption;
import admin.ui.menus.FindHostOption;
import admin.ui.menus.MainMenuOption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            io.println(option.getOptionNumber() + ": " + option.getDescription());
            min = Math.min(min, option.getOptionNumber());
            max = Math.max(max, option.getOptionNumber());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromOptionNumber(io.readInt(message, min, max));

    }

    public String getReservationId() {
        displayHeader(MainMenuOption.VIEW_RESERVATIONS_BY_ID.getDescription());
        return io.readString("Enter unique host ID: ");
    }


    public Host chooseHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, host.getName(), host.getState());
        }
        index--;

        if (hosts.size() > 25) {
            io.println("More than 25 hosts found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
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

    public Reservation makeReservation(Guest guest, Host host) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("End date [MM/dd/yyyy]: "));
        return reservation;
    }

    public boolean confirm(String message) {
        return io.confirm(message);
    }

    public LocalDate getNewStartDate(LocalDate oldStartDate) {
        String oldDateString = oldStartDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String promptMessage = String.format("Current start date is %s. Enter a new start date [MM/dd/yyyy]: ", oldDateString);
        return io.readLocalDate(promptMessage);
    }

    public LocalDate getNewEndDate(LocalDate oldEndDate) {
        String oldDateString = oldEndDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String promptMessage = String.format("Current end date is %s. Enter a new end date [MM/dd/yyyy]: ", oldDateString);
        return io.readLocalDate(promptMessage);
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

        String header = " # | Start Date |  End Date  | Guest ID | Total";

        displayHeader(header);

        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            String formattedIndex;
            String formattedGuestId;

            if (i < 9) {  // for single digit numbers
                formattedIndex = " " + (i + 1); // Add an extra space
            } else {
                formattedIndex = Integer.toString(i + 1);
            }

            String guestId = reservation.getGuest().getId();
            switch (guestId.length()) {
                case 1:
                    formattedGuestId = "  " + guestId;
                    break;
                case 2:
                    formattedGuestId = " " + guestId;
                    break;
                default:
                    formattedGuestId = guestId;
            }

            io.printf("%s | %s | %s |   %s    | %s%n",
                    formattedIndex,
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    formattedGuestId,
                    reservation.getTotal()
            );
        }
    }

    public void displayActiveReservationsHeader(Host host) {
        io.printf("\nACTIVE RESERVATIONS FOR: %s , %s%n", host.getName(), host.getState());
    }

    public void displayFutureReservations(List<Reservation> reservations) {
        System.out.println("\nFUTURE RESERVATIONS:\n");
        for (Reservation reservation : reservations) {
            // Display reservation details
            System.out.println("Guest ID: " + reservation.getGuest().getId());
            System.out.println("Start Date: " + reservation.getStartDate());
            System.out.println("End Date: " + reservation.getEndDate());
            System.out.println("Total: " + reservation.getTotal());
            System.out.println("---------------------");
        }
    }

    public String prepareSummary(Reservation reservation) {
        reservation.calculateTotal();
        StringBuilder summary = new StringBuilder();
        summary.append("Reservation details:\n");
        summary.append("Host: ").append(reservation.getHost().getName()).append("\n");
        summary.append("Guest: ").append(reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName()).append("\n");
        summary.append("From: ").append(reservation.getStartDate()).append("\n");
        summary.append("To: ").append(reservation.getEndDate()).append("\n");
        summary.append("Total: ").append(reservation.getTotal()).append("\n");
        return summary.toString();
    }


    public FindHostOption selectHostMenuOption() {
        displayHeader("Host Search Options");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (FindHostOption option : FindHostOption.values()) {
            io.println(option.getValue() + ": " + option.getDescription());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return FindHostOption.fromValue(io.readInt(message, min, max));
    }


    public FindGuestOption selectGuestMenuOption() {
        displayHeader("Guest Search Options");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (FindGuestOption option : FindGuestOption.values()) {
            io.println(option.getValue() + ": " + option.getDescription());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return FindGuestOption.fromValue(io.readInt(message, min, max));
    }


    public String getHostName() {
        return io.readString("Enter the name of the host: ");
    }

    public String getHostState() {
        return io.readString("Enter the state of the host (2 letter abbreviation): ");
    }

    public String getHostId() {
        return io.readString("Enter the host ID: ");
    }

    public String getGuestEmail() {
        return io.readString("Enter the email of the guest: ");
    }

    public String getGuestId() {
        return io.readString("Enter the ID of the guest: ");
    }

    public String getSpecificReservationId() {return io.readString("Enter the # of the reservation from the list above: ");}

    public char getLastNameInitial() {
        return io.readChar("Enter the initial of the guest's last name: ");
    }



}


