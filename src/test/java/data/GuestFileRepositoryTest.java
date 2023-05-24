package data;

import admin.data.DataException;
import admin.data.GuestRepository;
import admin.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestFileRepositoryTest {

    private GuestRepository repository;

    @BeforeEach
    void setUp() {
        repository = new GuestRepositoryDouble();
    }

    @Test
    void shouldFindAll() {
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFindGuestByEmail() {
        Guest guest = repository.findGuestByEmail("ncage@museumonh.org");
        assertNotNull(guest);
        assertEquals("10", guest.getId());
        assertEquals("Nicolas", guest.getFirstName());
        assertEquals("Cage", guest.getLastName());
    }

    @Test
    void shouldNotFindMissingGuestByEmail() {
        Guest guest = repository.findGuestByEmail("notarealemail@mail.com");
        assertNull(guest);
    }

    @Test
    void shouldFindGuestById() {
        Guest guest = repository.findGuestById("10");
        assertNotNull(guest);
        assertEquals("ncage@museumonh.org", guest.getEmail());
        assertEquals("Nicolas", guest.getFirstName());
        assertEquals("Cage", guest.getLastName());
    }

    @Test
    void shouldNotFindMissingGuestById() {
        Guest guest = repository.findGuestById("999");
        assertNull(guest);
    }

}

