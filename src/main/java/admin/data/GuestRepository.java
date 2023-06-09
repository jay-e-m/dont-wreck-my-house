package admin.data;

import admin.models.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll();

    Guest findGuestByEmail(String email);

    Guest findGuestById(String id);

    List<Guest> findGuestByLastNameInitial(char initial);

}
