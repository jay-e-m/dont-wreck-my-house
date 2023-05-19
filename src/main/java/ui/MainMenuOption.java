package ui;

import org.springframework.stereotype.Component;

@Component
public enum MainMenuOption {
    VIEW_RESERVATIONS_BY_ID(1, "View Reservations for Host by ID"),
    //need to implement a view all reservations feature?
    MAKE_RESERVATION(2, "Make a Reservation"),
    EDIT_RESERVATION(3, "Edit a Reservation"),
    CANCEL_RESERVATION(4, "Cancel a Reservation"),
    EXIT(5, "Exit");

    private final int optionNumber;
    private final String description;

    MainMenuOption(int optionNumber, String description) {
        this.optionNumber = optionNumber;
        this.description = description;
    }

    public int getOptionNumber() {
        return this.optionNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public static MainMenuOption fromOptionNumber(int optionNumber) {
        for (MainMenuOption option : values()) {
            if (option.getOptionNumber() == optionNumber) {
                return option;
            }
        }
        return null;
    }
}