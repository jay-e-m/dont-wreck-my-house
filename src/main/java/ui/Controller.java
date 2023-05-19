package ui;

import data.DataException;
import domain.Result;
import domain.HostService;
import domain.GuestService;
import domain.ReservationService;
import models.Host;
import models.Guest;
import models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
                    System.out.println("NOT IMPLEMENTED");
                    break;
                case CANCEL_RESERVATION:
                    System.out.println("NOT IMPLEMENTED");
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }
    private void viewReservations() {
        String id = view.getReservationId();
        List<Reservation> reservations = reservationService.findReservationsForHost(id);
        view.displayReservations(reservations);
    }

    private void addReservation() {
        List<Guest> guests = guestService.findAll();
        Guest guest = view.chooseGuest(guests);
        Reservation reservation = view.makeReservation(guest);
        reservationService.add(reservation);
    }


//    private void updateReservation() {
//        Reservation reservation = view
//        if (reservation != null) {
//            reservation = view.updateReservation(reservation);
//            reservationService.update(reservation);
//        }
//    }

    private void cancelReservation() {
    }

    private void viewGuests() {
        List<Guest> guests = guestService.findAll();
        view.displayGuests(guests);
    }
}
