package admin.domain;

import admin.data.DataException;
import admin.data.GuestRepository;
import admin.data.HostRepository;
import admin.data.ReservationRepository;
import admin.models.Guest;
import admin.models.Host;
import admin.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
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

        if (!validateNoDateOverlap(reservation.getHost(), reservation.getStartDate(), reservation.getEndDate())) {
            result.addErrorMessage("The provided dates overlap with an existing reservation.");
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

    public Result<Reservation> edit(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();

        // Recalculate the total based on the new dates
        reservation.setTotal(reservation.getTotal());

        boolean success = reservationRepository.update(reservation);
        if (!success) {
            result.addErrorMessage("Could not update the reservation.");
        }

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
        } else if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Start date cannot be in the past.");
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

    public Result<Reservation> cancel(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();

        boolean success = reservationRepository.cancel(reservation);
        if (!success) {
            result.addErrorMessage("Could not cancel the reservation.");
        }

        return result;
    }


    public boolean validateNoDateOverlap(Host host, LocalDate startDate, LocalDate endDate) {
        List<Reservation> existingReservations = this.findReservationsForHost(host.getHostUUID());

        for (Reservation existingReservation : existingReservations) {
            if ((startDate.isEqual(existingReservation.getStartDate()) || startDate.isAfter(existingReservation.getStartDate())) && startDate.isBefore(existingReservation.getEndDate())
                    || (endDate.isAfter(existingReservation.getStartDate()) && endDate.isBefore(existingReservation.getEndDate()))
                    || (startDate.isBefore(existingReservation.getStartDate()) && endDate.isAfter(existingReservation.getEndDate()))) {
                return false;
            }
        }
        return true;
    }
}
