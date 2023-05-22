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
    void shouldAddValidHost() throws DataException {
        Host host = new Host();
        host.setId(UUID.randomUUID().toString());
        host.setName("John Doe");
        host.setEmail("johndoe@gmail.com");
        host.setPhoneNumber("(555) 5551234");
        host.setAddress("123 Main St");
        host.setCity("Somewhere");
        host.setState("NY");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal("300"));
        host.setWeekendRate(new BigDecimal("400"));

        Result<Host> result = service.add(host);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(host, result.getPayload());
    }

    @Test
    void shouldNotAddInvalidHost() throws DataException {
        Host host = new Host();
        // no name/email

        host.setId(UUID.randomUUID().toString());
        host.setPhoneNumber("(555) 5551234");
        host.setAddress("123 Main St");
        host.setCity("Somewhere");
        host.setState("NY");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal("300"));
        host.setWeekendRate(new BigDecimal("400"));

        Result<Host> result = service.add(host);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("Host name is required."));
        assertTrue(result.getErrorMessages().contains("Email is required."));
    }

    @Test
    void shouldUpdateExistingHost() throws DataException {
        Host host = new Host();

        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setName("Jane Doe");
        host.setEmail("janedoe@gmail.com");
        host.setPhoneNumber("(555) 5551234");
        host.setAddress("123 Main St");
        host.setCity("Somewhere");
        host.setState("NY");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal("300"));
        host.setWeekendRate(new BigDecimal("400"));

        Result<Host> result = service.update(host);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(host, result.getPayload());
    }

    @Test
    void shouldNotUpdateNonExistingHost() throws DataException {
        Host host = new Host();
        // fake host data

        host.setId("non-existing-id");
        host.setName("Jane Doe");
        host.setEmail("janedoe@gmail.com");
        host.setPhoneNumber("(555) 5551234");
        host.setAddress("123 Main St");
        host.setCity("Somewhere");
        host.setState("NY");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal("300"));
        host.setWeekendRate(new BigDecimal("400"));

        Result<Host> result = service.update(host);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("Could not find host to update."));
    }
}

