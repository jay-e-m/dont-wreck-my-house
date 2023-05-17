package data;

import data.DataException;
import data.ReservationFileRepository;
import models.Reservation;
import models.Guest;
import models.Host;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReservationsFileRepositoryTest {

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

        // Create a HostRepository to find a host by name
        HostFileRepository hostRepository = new HostFileRepository(TEST_HOST_DIR_PATH);

        // Find the host
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


}
