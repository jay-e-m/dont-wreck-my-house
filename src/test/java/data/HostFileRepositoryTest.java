package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {
    HostRepository repository = new HostRepositoryDouble();

    @Test
    void shouldFindAllHosts() {
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void shouldFindHostByName() {
        assertNotNull(repository.findHostByName("Test Host 1"));
        assertNull(repository.findHostByName("Nonexistent Host"));
    }

    @Test
    void shouldFindHostByState() {
        assertNotNull(repository.findHostByState("TX"));
        assertNull(repository.findHostByState("Nonexistent State"));
    }

    @Test
    void shouldFindHostByID() {
        assertNotNull(repository.findHostByID("1"));
        assertNull(repository.findHostByID("Nonexistent ID"));
    }

}

