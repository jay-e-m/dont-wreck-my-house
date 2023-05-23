package admin.data;

import admin.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    Host findHostByName(String name);

    List<Host> findHostsByState(String state);

    Host findHostByID(String id);

}

