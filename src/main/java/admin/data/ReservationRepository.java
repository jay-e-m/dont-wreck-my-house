package admin.data;

import admin.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostID(String id);

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean cancel(Reservation reservation) throws DataException;
}
