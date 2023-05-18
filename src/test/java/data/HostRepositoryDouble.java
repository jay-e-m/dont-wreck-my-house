package data;

import models.Host;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        Host host1 = new Host("1", "Test Host 1", "test1@yahoo.com", "1234567890", "123 Fake Street", "Houston", "TX", "01010", new BigDecimal(100), new BigDecimal(150));
        Host host2 = new Host("2", "Test Host 2", "test2@jp.co.amzn", "1234567891", "456 Test Avenue", "Atlanta", "GA", "23232", new BigDecimal(200), new BigDecimal(250));
        Host host3 = new Host("3", "Test Host 3", "test3@fintech.org", "1234567892", "789 Dummy Boulevard", "Boston", "NE", "45454", new BigDecimal(300), new BigDecimal(350));

        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
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
    public Host findHostByState(String state) {
        return hosts.stream()
                .filter(h -> h.getState().equalsIgnoreCase(state))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findHostByID(String id) {
        return hosts.stream()
                .filter(h -> h.getId().equalsIgnoreCase(id))
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
            if (hosts.get(i).getId().equals(host.getId())) {
                hosts.set(i, host);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataException {
        return hosts.removeIf(h -> h.getId().equals(host.getId()));
    }
}

