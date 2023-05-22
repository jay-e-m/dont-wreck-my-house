package data;

import admin.data.DataException;
import admin.data.HostRepository;
import admin.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        Host host1 = new Host(
                "3edda6bc-ab95-49a8-8962-d50b53f84b15",
                "Yearnes",
                "eyearnes0@sfgate.com",
                "(806) 1783815",
                "3 Nova Trail",
                "Amarillo",
                "TX",
                "79182",
                new BigDecimal(340),
                new BigDecimal(425)
        );
        Host host2 = new Host(
                "a0d911e7-4fde-4e4a-bdb7-f047f15615e8",
                "Rhodes",
                "krhodes1@posterous.com",
                "(478) 7475991",
                "7262 Morning Avenue",
                "Macon",
                "GA",
                "31296",
                new BigDecimal(295),
                new BigDecimal(368.75)
        );
        Host host3 = new Host(
                "b4f38829-c663-48fc-8bf3-7fca47a7ae70",
                "Fader",
                "mfader2@amazon.co.jp",
                "(501) 2490895",
                "99208 Morning Parkway",
                "North Little Rock",
                "AR",
                "72118",
                new BigDecimal(451),
                new BigDecimal(563.75)
        );
        Host host4 = new Host(
                "01f36661-cf18-49e7-a574-b72feb502ed0",
                "Test",
                "testtest@yahoo.com",
                "(555) 9990123",
                "123 Help Me Lane",
                "Hope",
                "NE",
                "68001",
                new BigDecimal(395),
                new BigDecimal(493.75)
        );

        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        hosts.add(host4);
    }



    @Override
    public List<Host> findAll() {
        return new ArrayList<>(hosts);
    }

    @Override
    public Host findHostByName(String name) {
        return hosts.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Host> findHostsByState(String state) {
        return findAll().stream()
                .filter(h -> h.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    @Override
    public Host findHostByID(String id) {
        return hosts.stream()
                .filter(h -> h.getHostUUID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host add(Host host) throws DataException {
        hosts.add(host);
        return host;
    }

    @Override
    public boolean update(Host host) throws DataException {
        for (int i = 0; i < hosts.size(); i++) {
            if (hosts.get(i).getHostUUID().equals(host.getHostUUID())) {
                hosts.set(i, host);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataException {
        return hosts.removeIf(h -> h.getHostUUID().equals(host.getHostUUID()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostRepositoryDouble that)) return false;
        return Objects.equals(hosts, that.hosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hosts);
    }
}

