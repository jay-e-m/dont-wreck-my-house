package domain;

import admin.data.DataException;
import admin.domain.HostService;
import admin.domain.Result;
import data.HostRepositoryDouble;
import admin.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class HostServiceTest {

    HostService service;
    HostRepositoryDouble repository;

    @BeforeEach
    void setup() {
        repository = new HostRepositoryDouble();
        service = new HostService(repository);
    }

    @Test
    void shouldFindAll() {
        assertEquals(4, service.findAll().size());
    }

    @Test
    void shouldFindByName() {
        Host host = service.findHostByName("Yearnes");
        assertNotNull(host);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", host.getHostUUID());
    }

    @Test
    void shouldNotFindMissingName() {
        Host host = service.findHostByName("Nonexistent");
        assertNull(host);
    }

    @Test
    void shouldFindByState() {
        assertEquals(1, service.findHostByState("TX").size());
        assertEquals(1, service.findHostByState("GA").size());
        assertEquals(1, service.findHostByState("AR").size());
    }

    @Test
    void shouldNotFindByMissingState() {
        assertEquals(0, service.findHostByState("Nonexistent").size());
    }

    @Test
    void shouldFindById() {
        Host host = service.findHostByID("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(host);
        assertEquals("Yearnes", host.getName());
    }

    @Test
    void shouldNotFindMissingId() {
        Host host = service.findHostByID(UUID.randomUUID().toString());
        assertNull(host);
    }


}

