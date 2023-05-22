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
                    System.out.println("NOT IMPLEMENTED");
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
        // HOST SELECTION
        FindHostOption hostOption;
        List<Host> hosts;
        Host host;
        do {
            hostOption = view.selectHostMenuOption();
            switch (hostOption) {
                case BY_NAME:
                    String name = view.getHostName();
                    Host hostByName = hostService.findHostByName(name);
                    if (hostByName == null) {
                        view.displayStatus(false, "No host found with name: " + name);
                        return;
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
                        return;
                    }
                    hosts = Collections.singletonList(hostById);
                    break;
                case BACK:
                    return;
                default:
                    hosts = hostService.findAll();
                    break;
            }
            host = view.chooseHost(hosts);
        } while (host == null);


        System.out.println("\nACTIVE RESERVATIONS FOR: " + host.getName() + " , " + host.getState());
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


    private void editReservation() throws DataException {
        // Select the reservation to edit
        String reservationId = view.getReservationId();
        List<Reservation> reservation = reservationService.findReservationsForHost(reservationId);


        if (reservation == null) {
            view.displayStatus(false, "No reservation found with ID: " + reservationId);
            return;
        }

        // Edit the dates of the reservation
        Reservation updatedReservation = view.editReservationDates((Reservation) reservation);

        // Check for date overlap
        boolean dateOverlap = reservationService.validateNoDateOverlap(((Reservation) reservation).getHost(), updatedReservation.getStartDate(), updatedReservation.getEndDate());

        if (!dateOverlap) {
            // Prepare summary and ask for user confirmation
            String summary = view.prepareSummary(updatedReservation);
            System.out.println(summary);
            boolean isConfirmed = view.confirm("Do you confirm the changes? (y/n)");

            if (isConfirmed) {
                // Save the updated reservation
                Result<Reservation> result = reservationService.edit(updatedReservation);

                if (result.isSuccess()) {
                    view.displayStatus(true, "Reservation edited successfully!");
                } else {
                    view.displayStatus(false, "Failed to edit the reservation:");
                    List<String> errorMessages = result.getErrorMessages();
                    view.displayStatus(false, errorMessages);
                }
            } else {
                view.displayStatus(false, "Edit cancelled.");
            }
        } else {
            view.displayStatus(false, "The new reservation dates overlap with an existing reservation. Please try again with different dates.");
        }
    }


//    private void cancelReservation() throws DataException {
//        // Get reservation ID from the user
//        String reservationId = view.getReservationId();
//        // Find the reservation
//        Reservation reservation = reservationService.findReservationById(reservationId);
//
//        if (reservation == null) {
//            view.displayStatus(false, "No reservation found with ID: " + reservationId);
//            return;
//        }
//
//        // Check if the reservation start date is in the future
//        if (reservation.getStartDate().isBefore(LocalDate.now())) {
//            view.displayStatus(false, "You cannot cancel a reservation that's in the past.");
//            return;
//        }
//
//        // Cancel the reservation
//        Result<Reservation> result = reservationService.cancel(reservation);
//
//        if (result.isSuccess()) {
//            view.displayStatus(true, "Reservation cancelled successfully!");
//        } else {
//            view.displayStatus(false, "Failed to cancel the reservation:");
//            view.displayStatus(false, result.getErrorMessages());
//        }
//    }


    private void viewGuests() {
        List<Guest> guests = guestService.findAll();
        view.displayGuests(guests);
    }


}
