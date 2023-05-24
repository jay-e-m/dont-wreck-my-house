package admin.ui.menus;

public enum FindGuestOption {
    BY_LAST_NAME_INITIAL(1, "Find by last name"),
    BY_EMAIL(2, "Find by email"),
    BY_ID(3, "Find by ID"),
    BACK(4, "Go back");

    private final int value;
    private final String description;

    FindGuestOption(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static FindGuestOption fromValue(int value) {
        for (FindGuestOption option : FindGuestOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return null;
    }
}


