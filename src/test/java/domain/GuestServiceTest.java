package domain;

import admin.domain.GuestService;
import admin.domain.Result;
import data.GuestRepositoryDouble;
import admin.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService service;

    @BeforeEach
    void setUp() {
        service = new GuestService(new GuestRepositoryDouble());
    }

    @Test
    void shouldFindAll() {
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldFindGuestByEmail() {
        Guest guest = service.findGuestByEmail("ncage@museumonh.org");
        assertNotNull(guest);
        assertEquals("10", guest.getId());
    }

    @Test
    void shouldNotFindMissingGuestByEmail() {
        Guest guest = service.findGuestByEmail("missing@mail.com");
        assertNull(guest);
    }

    @Test
    void shouldFindGuestById() {
        Guest guest = service.findGuestById("10");
        assertNotNull(guest);
        assertEquals("ncage@museumonh.org", guest.getEmail());
    }

    @Test
    void shouldNotFindMissingGuestById() {
        Guest guest = service.findGuestById("999");
        assertNull(guest);
    }


}
