package admin.ui.menus;

public enum FindHostOption {
    BY_NAME(1, "Find by name"),
    BY_STATE(2, "Find by state"),
    BY_ID(3, "Find by ID"),
    BACK(4, "Go back");

    private final int value;
    private final String description;

    FindHostOption(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static FindHostOption fromValue(int value) {
        for (FindHostOption option : FindHostOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return null;
    }
}

