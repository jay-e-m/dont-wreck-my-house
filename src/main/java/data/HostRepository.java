package data;

import models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    Host findHostByName(String name);

    Host findHostByState(String state);

    Host findHostByID(String id);

    Host add(Host host) throws DataException;

    boolean update(Host host) throws DataException;

    boolean delete(Host host) throws DataException;
}

