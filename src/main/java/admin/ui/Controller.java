package admin.ui;

import admin.data.DataException;
import admin.domain.*;
import admin.models.Guest;
import admin.models.Host;
import admin.models.Reservation;
import admin.ui.menus.FindGuestOption;
import admin.ui.menus.FindHostOption;
import admin.ui.menus.MainMenuOption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Controller {

    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to the Reservation System");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_BY_ID:
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    addReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
                case EXIT:
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }
    private void viewReservations() {
        String id = view.getReservationId();
        List<Reservation> reservations = reservationService.findReservationsForHost(id);
        view.displayReservations(reservations);
    }

    private void viewReservationsForChosenHost(String id) {
        List<Reservation> reservations = reservationService.findReservationsForHost(id);
        view.displayReservations(reservations);
    }


    private void addReservation() {

        Host host = selectHost();
        if (host == null) {
            return;
        }

        view.displayActiveReservationsHeader(host);
        viewReservationsForChosenHost(host.getHostUUID());

        // GUEST SELECTION
        FindGuestOption guestOption;
        List<Guest> guests;
        Guest guest;
        do {
            guestOption = view.selectGuestMenuOption();
            switch (guestOption) {
                case BY_LAST_NAME_INITIAL:
                    char initial = view.getLastNameInitial();
                    guests = guestService.findGuestsByLastNameInitial(initial);
                    if (guests.isEmpty()) {
                        view.displayStatus(false, "No guests found with last name beginning with " + initial);
                    }
                    break;
                case BY_EMAIL:
                    String email = view.getGuestEmail();
                    Guest guestByEmail = guestService.findGuestByEmail(email);
                    if (guestByEmail == null) {
                        view.displayStatus(false, "No guest found with email: " + email);
                        return;
                    }
                    guests = Collections.singletonList(guestByEmail);
                    break;
                case BY_ID:
                    String id = view.getGuestId();
                    Guest guestById = guestService.findGuestById(id);
                    if (guestById == null) {
                        view.displayStatus(false, "No guest found with ID: " + id);
                        return;
                    }
                    guests = Collections.singletonList(guestById);
                    break;
                case BACK:
                    return;
                default:
                    guests = guestService.findAll();
                    break;
            }
            guest = view.chooseGuest(guests);
        } while (guest == null);

        do {
            //create reservation based on the guest and host chosen
            Reservation reservation = view.makeReservation(guest, host);

            boolean dateOverlap = reservationService.validateNoDateOverlap(host, reservation.getStartDate(), reservation.getEndDate());

            if (dateOverlap) {
                // Prepare summary and ask for user confirmation
                String summary = view.prepareSummary(reservation);
                System.out.println(summary);
                boolean isConfirmed = view.confirm("Do you confirm the reservation? (y/n)");

                if(isConfirmed){
                    Result<Reservation> result = reservationService.add(reservation);

                    if (result.isSuccess()) {
                        view.displayStatus(true, "Reservation added successfully!");
                        return;
                    } else {
                        view.displayStatus(false, "Failed to add the reservation:");
                        List<String> errorMessages = result.getErrorMessages();
                        view.displayStatus(false, errorMessages);
                    }
                } else {
                    view.displayStatus(false, "Reservation cancelled.");
                }
            } else {
                view.displayStatus(false, "The reservation dates overlap with an existing reservation. Please try again with different dates.");
            }

        } while (true);
    }

    private void editReservation() {
        // Select the host
        Host host = selectHost();
        if (host == null) {
            view.displayStatus(false, "No host found.");
            return;
        }

        // Get guest ID and find the guest
        String guestId = view.getGuestId();
        Guest guest = guestService.findGuestById(guestId);
        if (guest == null) {
            view.displayStatus(false, "No guest found with ID: " + guestId);
            return;
        }

        // Find the specific reservation of the guest for the selected host
        Reservation reservation = reservationService.findSpecificReservationByGuestId(host.getHostUUID(), guestId);
        if (reservation == null) {
            view.displayStatus(false, "No reservation found for guest with ID: " + guestId);
            return;
        }

        reservation.setGuest(guest);
        reservation.setHost(host);

        reservation.setStartDate(view.getNewStartDate(reservation.getStartDate()));
        reservation.setEndDate(view.getNewEndDate(reservation.getEndDate()));

        reservation.calculateTotal();

        // Prepare summary and ask for user confirmation
        String summary = view.prepareSummary(reservation);
        System.out.println(summary);
        boolean isConfirmed = view.confirm("Do you confirm the reservation update? (y/n)");

        if(isConfirmed) {
            Result<Reservation> result = reservationService.update(reservation);

            if (result.isSuccess()) {
                view.displayStatus(true, "Reservation updated successfully!");
            } else {
                view.displayStatus(false, String.join("\n", result.getErrorMessages()));
            }
        } else {
            view.displayStatus(false, "Reservation update cancelled.");
        }
    }

    private void cancelReservation() {
        // Select the host
        Host host = selectHost();
        if (host == null) {
            view.displayStatus(false, "No host found.");
            return;
        }

        // Get guest ID and find the guest
        String guestId = view.getGuestId();
        Guest guest = guestService.findGuestById(guestId);
        if (guest == null) {
            view.displayStatus(false, "No guest found with ID: " + guestId);
            return;
        }

        // Retrieve all future reservations for the chosen host for the specific guest
        List<Reservation> reservations = reservationService.filterFutureReservations(host.getHostUUID());

        // If there are no future reservations for the guest, return
        if (reservations.isEmpty()) {
            view.displayStatus(false, "No future reservations found for guest with ID: " + guestId);
            return;
        }

        // Filter reservations for the specific guest
        List<Reservation> guestReservations = reservations.stream()
                .filter(r -> r.getGuest().getId().equals(guestId))
                .collect(Collectors.toList());

        // If there are no future reservations for the guest, return
        if (guestReservations.isEmpty()) {
            view.displayStatus(false, "No reservation found for guest with ID: " + guestId);
            return;
        }

        // Display these reservations to the user
        view.displayReservations(guestReservations);

        // Get reservation identifier from the user
        String reservationId = view.getSpecificReservationId();


        // Validate the input
        if (Integer.parseInt(reservationId) < 1 || Integer.parseInt(reservationId) > guestReservations.size()) {
            view.displayStatus(false, "Invalid reservation ID.");
            return;
        }

        // Fetch the selected reservation
        Reservation reservation = guestReservations.get(Integer.parseInt(reservationId) - 1);
        reservation.setGuest(guest);
        reservation.setHost(host);

        // Ask for user confirmation before cancellation
        boolean isConfirmed = view.confirm("Are you sure you want to cancel the reservation for guest with ID: " + guestId + " (y/n)");
        if (isConfirmed) {
            // Try to cancel the reservation
            Result<Reservation> result = reservationService.cancel(reservation);
            // Check the result and display the appropriate status
            if (result.isSuccess()) {
                view.displayStatus(true, "Reservation cancelled successfully!");
            } else {
                view.displayStatus(false, String.join("\n", result.getErrorMessages()));
            }
        } else {
            view.displayStatus(false, "Cancellation aborted.");
        }
    }




    private Host selectHost() {
        FindHostOption hostOption;
        List<Host> hosts;
        Host host = null;

        do {
            hostOption = view.selectHostMenuOption();
            switch (hostOption) {
                case BY_NAME:
                    String name = view.getHostName();
                    Host hostByName = hostService.findHostByName(name);
                    if (hostByName == null) {
                        view.displayStatus(false, "No host found with name: " + name);
                        return null;
                    }
                    hosts = Collections.singletonList(hostByName); // creates a list with a single entry
                    break;
                case BY_STATE:
                    String state = view.getHostState();
                    hosts = hostService.findHostByState(state);
                    if (hosts.isEmpty()) {
                        view.displayStatus(false, "No hosts found in state: " + state);
                    }
                    break;
                case BY_ID:
                    String hostId = view.getHostId();
                    Host hostById = hostService.findHostByID(hostId);
                    if (hostById == null) {
                        view.displayStatus(false, "No host found with ID: " + hostId);
                        return null;
                    }
                    hosts = Collections.singletonList(hostById);
                    break;
                case BACK:
                    return null;
                default:
                    hosts = hostService.findAll();
                    break;
            }
            host = view.chooseHost(hosts);
        } while (host == null);

        return host;
    }

}
