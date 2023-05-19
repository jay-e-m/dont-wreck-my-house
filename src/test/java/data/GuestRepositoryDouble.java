package data;

import models.Guest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuestRepositoryDouble implements GuestRepository {

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        Guest guest = new Guest();
        guest.setId("10");
        guest.setFirstName("Nicolas");
        guest.setLastName("Cage");
        guest.setEmail("ncage@museumonh.org");
        guest.setPhoneNumber("(001) 7417760");
        guest.setState("VA");

        guests.add(guest);
    }

    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findGuestByEmail(String email) {
        return guests.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findGuestById(String id) {
        return guests.stream()
                .filter(g -> g.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        guest.setId(java.util.UUID.randomUUID().toString());
        guests.add(guest);
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        return false;
    }

    @Override
    public boolean delete(Guest guest) throws DataException {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestRepositoryDouble that)) return false;
        return Objects.equals(guests, that.guests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guests);
    }
}

