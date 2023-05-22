package data;

import admin.data.DataException;
import admin.data.HostFileRepository;
import admin.data.ReservationFileRepository;
import admin.models.Reservation;
import admin.models.Guest;
import admin.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/data_test/reservations/01f36661-cf18-49e7-a574-b72feb502ed0.csv";
    static final String TEST_FILE_PATH = "./data/data_test/reservations/reservation_test_file/01f36661-cf18-49e7-a574-b72feb502ed0.csv";
    static final String TEST_DIR_PATH = "./data/data_test/reservations/reservation_test_file";
    static final String TEST_HOST_DIR_PATH = "./data/data_test/hosts";
    static final int RESERVATION_COUNT = 15;

    final String hostID = "01f36661-cf18-49e7-a574-b72feb502ed0";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByID() {
        List<Reservation> reservations = repository.findByHostID(hostID);
        assertEquals(RESERVATION_COUNT, reservations.size());
    }

    @Test
    void shouldAdd() throws DataException, IOException {
        Reservation reservation = new Reservation();

        HostFileRepository hostRepository = new HostFileRepository(TEST_HOST_DIR_PATH);

        Host host = hostRepository.findHostByName("Test");
        assertNotNull(host);

        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2021, 5, 16));
        reservation.setEndDate(LocalDate.of(2021, 5, 20));

        Guest guest = new Guest();
        guest.setId("10");
        reservation.setGuest(guest);

        reservation = repository.add(reservation);

        BigDecimal expectedTotal = new BigDecimal("1580.00");
        BigDecimal actualTotal = reservation.getTotal();
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation reservation = new Reservation();
        Host host = new Host();

        host.setId(hostID);
        host.setStandardRate(new BigDecimal(395));
        host.setWeekendRate(new BigDecimal(493.75));


        reservation.setHost(host);
        reservation.setId("1");
        reservation.setStartDate(LocalDate.of(2023, 5, 25));
        reservation.setEndDate(LocalDate.of(2023, 5, 30));

        Guest guest = new Guest();
        guest.setId("10");
        reservation.setGuest(guest);

        boolean success = repository.update(reservation);

        assertTrue(success);

        List<Reservation> reservations = repository.findByHostID(hostID);
        assertNotNull(reservations);
        assertEquals(15, reservations.size());
        assertEquals("1", reservations.get(0).getReservationIdForGuest());
        assertEquals(LocalDate.of(2023, 5, 25), reservations.get(0).getStartDate());
        assertEquals(LocalDate.of(2023, 5, 30), reservations.get(0).getEndDate());
        assertEquals("10", reservations.get(0).getGuest().getId());
    }

    @Test
    void shouldCancel() throws DataException {
        Reservation reservation = new Reservation();
        Host host = new Host();

        host.setId(hostID);
        reservation.setHost(host);
        reservation.setId("1");

        reservation.setStartDate(LocalDate.of(2023, 5, 25));
        reservation.setEndDate(LocalDate.of(2023, 5, 30));

        boolean success = repository.cancel(reservation);

        assertTrue(success);

        List<Reservation> reservations = repository.findByHostID(hostID);

        assertNotNull(reservations);
        assertEquals(14, reservations.size());
        assertNotEquals("1", reservations.get(0).getReservationIdForGuest());
    }

    @Test
    void shouldNotCancelInPast() {
        Reservation reservation = new Reservation();
        Host host = new Host();

        host.setId(hostID);
        reservation.setHost(host);
        reservation.setId("2");

        //use dates in past
        reservation.setStartDate(LocalDate.of(2022, 4, 25));
        reservation.setEndDate(LocalDate.of(2022, 4, 30));

        boolean exceptionThrown = false;
        try {
            repository.cancel(reservation);
        } catch (DataException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        List<Reservation> reservations = repository.findByHostID(hostID);

        assertNotNull(reservations);
        assertEquals(15, reservations.size());
        assertEquals("1", reservations.get(0).getReservationIdForGuest());
    }



}
