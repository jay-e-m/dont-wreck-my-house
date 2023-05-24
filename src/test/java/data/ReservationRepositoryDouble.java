package data;

import admin.data.DataException;
import admin.data.ReservationRepository;
import admin.models.Guest;
import admin.models.Host;
import admin.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        host.setStandardRate(new BigDecimal("395.00"));
        host.setWeekendRate(new BigDecimal("493.75"));

        Reservation reservation = new Reservation();
        reservation.setId("01f36661-cf18-49e7-a574-b72feb502ed0");
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setGuest(guest);
        reservation.setHost(host);

        reservation.calculateTotal();

        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByHostID(String id) {
        return reservations.stream()
                .filter(i -> i.getHost().getHostUUID().equals(id))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationRepositoryDouble that)) return false;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, reservations);
    }
}

