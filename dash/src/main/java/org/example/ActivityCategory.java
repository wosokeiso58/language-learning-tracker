package org.example;

public enum ActivityCategory {
    READING,
    LISTENING,
    SPEAKING,
    WRITING,
    GRINDING;

    @Override
    public String toString() {
        return switch (this) {
            case READING -> "reading";
            case LISTENING -> "listening";
            case SPEAKING -> "speaking";
            case WRITING -> "writing";
            case GRINDING -> "grinding";
        };
    }
}
