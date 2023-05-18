package data;

import models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {

    private final String directory;

    public GuestFileRepository(@Value("${guests.path}") String directory) {
        this.directory = directory;
    }

    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    Guest guest = deserialize(fields);
                    result.add(guest);
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public Guest findGuestByEmail(String email) {
        return findAll().stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Guest findGuestById(String id) {
        return findAll().stream()
                .filter(g -> g.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        return null;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        return false;
    }

    private String getFilePath() {
        return Paths.get(directory, "guests.csv").toString();
    }

    private Guest deserialize(String[] fields) {
        Guest guest = new Guest();
        guest.setId(fields[0]);
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhoneNumber(fields[4]);
        guest.setState(fields[5]);
        return guest;
    }
}
