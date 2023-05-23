package admin.domain;

import admin.data.DataException;
import admin.data.GuestRepository;
import admin.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    public Guest findGuestByEmail(String email) {
        return guestRepository.findGuestByEmail(email);
    }

    public Guest findGuestById(String id) {
        return guestRepository.findGuestById(id);
    }

    public List<Guest> findGuestsByLastNameInitial(char initial) {
        return findAll().stream()
                .filter(guest -> guest.getLastName().charAt(0) == Character.toUpperCase(initial))
                .collect(Collectors.toList());
    }


    private Result<Guest> validate(Guest guest) {
        Result<Guest> result = new Result<>();

        if (guest == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }


        return result;
    }

}
