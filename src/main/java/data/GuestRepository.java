package data;

import models.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll();

    Guest findGuestByEmail(String email);

    Guest findGuestById(String id);

    Guest add(Guest guest) throws DataException;

    boolean update(Guest guest) throws DataException;

    boolean delete(Guest guest) throws DataException;
}
