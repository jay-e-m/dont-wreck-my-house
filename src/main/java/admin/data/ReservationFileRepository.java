package admin.data;

import admin.models.Guest;
import admin.models.Host;
import admin.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest,total";
    private final String directory;

    public ReservationFileRepository(@Value("${reservations.path}")String directory) {this.directory = directory;}

    @Override
    public List<Reservation> findByHostID(String id) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(id)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    Reservation reservation = deserialize(fields, id);
                    result.add(reservation);
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }



    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> allReservations = findByHostID(reservation.getHost().getHostUUID());
        int maxId = allReservations.stream()
                .mapToInt(r -> Integer.parseInt(r.getReservationIdForGuest()))
                .max()
                .orElse(0);
        reservation.setId(String.valueOf(maxId + 1));
        reservation.calculateTotal();
        allReservations.add(reservation);

        writeAll(allReservations, reservation.getHost().getHostUUID());

        return reservation;
    }



    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostID(reservation.getHost().getHostUUID());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationIdForGuest().equals(reservation.getReservationIdForGuest())) {
                reservation.calculateTotal();
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getHostUUID());
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean cancel(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostID(reservation.getHost().getHostUUID());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationIdForGuest().equals(reservation.getReservationIdForGuest())) {
                if (reservation.isInPast()) {
                    throw new DataException("Reservation has already occurred and cannot be cancelled.");
                }
                all.remove(i);
                writeAll(all, reservation.getHost().getHostUUID());
                return true;
            }
        }
        return false;
    }

    private void writeAll(List<Reservation> reservations, String id) throws DataException {
        String filePath = getFilePath(id);

        try (PrintWriter writer = new PrintWriter(getFilePath(id))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }

    private String serialize(Reservation item) {
        return String.format("%s,%s,%s,%s,%s",
                item.getReservationIdForGuest(),
                item.getStartDate(),
                item.getEndDate(),
                item.getGuest().getId(),
                item.getTotal());
    }

    private Reservation deserialize(String[] fields, String hostId) {
        Reservation result = new Reservation();
        result.setId(fields[0]);
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));

        Guest guest = new Guest();
        guest.setId(fields[3]);
        result.setGuest(guest);

        Host host = new Host();
        host.setId(hostId);
        result.setHost(host);

        result.setTotal(new BigDecimal(fields[4]));

        return result;
    }

}
