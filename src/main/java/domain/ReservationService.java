package service;

import data.DataException;
import data.GuestRepository;
import data.HostRepository;
import data.ReservationRepository;
import domain.Result;
import models.Guest;
import models.Host;
import models.Reservation;

import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findReservationsForHost(String hostId) {
        return reservationRepository.findByHostID(hostId);
    }

    public Result<Reservation> add(Reservation reservation) {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        reservation.calculateTotal();

        try {
            reservation = reservationRepository.add(reservation);
        } catch (DataException ex) {
            result.addErrorMessage("Could not add the reservation: " + ex.getMessage());
            return result;
        }

        result.setPayload(reservation);
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            result.addErrorMessage("Dates must be provided.");
        } else if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Start date must be before end date.");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest is required.");
        } else {
            Guest guestFromRepo = guestRepository.findGuestById(reservation.getGuest().getId());
            if (guestFromRepo == null) {
                result.addErrorMessage("Guest not found.");
            } else {
                reservation.setGuest(guestFromRepo);
            }
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host is required.");
        } else {
            Host hostFromRepo = hostRepository.findHostByID(reservation.getHost().getHostUUID());
            if (hostFromRepo == null) {
                result.addErrorMessage("Host not found.");
            } else {
                reservation.setHost(hostFromRepo);
            }
        }

        return result;
    }
}
