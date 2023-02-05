package ru.practicum.shareit.booking.model;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State from(String state) {
        for (State value: State.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}
