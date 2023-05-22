package admin.data;

import admin.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HostFileRepository implements HostRepository {

    private final String directory;

    public HostFileRepository(@Value("${hosts.path}")String directory) {
        this.directory = directory;
    }


    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    Host host = deserialize(fields);
                    result.add(host);
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public Host findHostByName(String name) {
        return findAll().stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Host> findHostsByState(String state) {
        return findAll().stream()
                .filter(h -> h.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }


    public Host findHostByID(String id) {
        return findAll().stream()
                .filter(h -> h.getHostUUID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host add(Host host) throws DataException {
        return null;
    }

    @Override
    public boolean update(Host host) throws DataException {
        return false;
    }

    @Override
    public boolean delete(Host host) throws DataException {
        return false;
    }

    private String getFilePath() {
        return Paths.get(directory).toString();
    }

    private String serialize(Host item) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                item.getHostUUID(),
                item.getName(),
                item.getEmail(),
                item.getPhoneNumber(),
                item.getAddress(),
                item.getCity(),
                item.getState(),
                item.getPostalCode(),
                item.getStandardRate(),
                item.getWeekendRate());
    }

    private Host deserialize(String[] fields) {
        return new Host(fields[0],
                fields[1],
                fields[2],
                fields[3],
                fields[4],
                fields[5],
                fields[6],
                fields[7],
                new BigDecimal(fields[8]),
                new BigDecimal(fields[9])
        );
    }

}
