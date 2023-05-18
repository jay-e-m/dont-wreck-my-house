package data;

import models.Guest;
import models.Host;
import models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    final LocalDate startDate = LocalDate.of(2023, 5, 15);
    final LocalDate endDate = LocalDate.of(2023, 5, 18);

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        Guest guest = new Guest();
        guest.setId("10");

        Host host = new Host();
        host.setId("01f36661-cf18-49e7-a574-b72feb502ed0");

        Reservation reservation = new Reservation();
        reservation.setId("01f36661-cf18-49e7-a574-b72feb502ed0");
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setTotal(new BigDecimal(String.valueOf(reservation.getTotalCost())));
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByHostID(String id) {
        return reservations.stream()
                .filter(i -> i.getHost().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservation.setId(java.util.UUID.randomUUID().toString());
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return false;
    }

    @Override
    public boolean cancel(Reservation reservation) throws DataException {
        return false;
    }
}

