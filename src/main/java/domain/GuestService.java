package domain;

import data.DataException;
import data.GuestRepository;
import domain.Result;
import models.Guest;

import java.util.List;

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

    public Result<Guest> addGuest(Guest guest) {
        Result<Guest> result = validate(guest);
        if (!result.isSuccess()) {
            return result;
        }

        try {
            guest = guestRepository.add(guest);
        } catch (DataException ex) {
            result.addErrorMessage("Could not add the guest: " + ex.getMessage());
            return result;
        }

        result.setPayload(guest);
        return result;
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
