package domain;

import data.DataException;
import data.GuestRepositoryDouble;
import data.HostRepositoryDouble;
import data.ReservationRepositoryDouble;
import models.Guest;
import models.Host;
import models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    service.ReservationService service;
    ReservationRepositoryDouble reservationRepository;
    GuestRepositoryDouble guestRepository;
    HostRepositoryDouble hostRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepositoryDouble();
        guestRepository = new GuestRepositoryDouble();
        hostRepository = new HostRepositoryDouble();

        service = new service.ReservationService(reservationRepository, guestRepository, hostRepository);
    }

    @Test
    void shouldFindByHostID() {
        List<Reservation> result = service.findReservationsForHost("01f36661-cf18-49e7-a574-b72feb502ed0");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        Guest guest = new Guest();
        guest.setId("10");
        reservation.setGuest(guest);
        Host host = new Host();
        host.setId("01f36661-cf18-49e7-a574-b72feb502ed0");
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2023, 5, 22));
        reservation.setEndDate(LocalDate.of(2023, 5, 25));
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(new BigDecimal("1185.00"), result.getPayload().getTotal());
    }


}
