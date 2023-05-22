package data;

import admin.data.HostRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    HostRepository repository = new HostRepositoryDouble();

    @Test
    void shouldFindAllHosts() {
        assertEquals(4, repository.findAll().size());
    }

    @Test
    void shouldFindHostByName() {
        assertNotNull(repository.findHostByName("Yearnes"));
        assertNull(repository.findHostByName("Nonexistent Host"));
    }

    @Test
    void shouldFindHostByState() {
        assertFalse(repository.findHostsByState("TX").isEmpty());
        assertTrue(repository.findHostsByState("Nonexistent State").isEmpty());
    }

    @Test
    void shouldFindHostByID() {
        assertNotNull(repository.findHostByID("a0d911e7-4fde-4e4a-bdb7-f047f15615e8"));
        assertNull(repository.findHostByID("Nonexistent ID"));
    }

}

