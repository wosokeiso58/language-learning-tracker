package org.example;

public enum ActivityCategory {
    READING,
    LISTENING,
    SPEAKING,
    WRITING,
    GRINDING;

    public String getProgressBarStyle() {
        return switch (this) {
            case READING -> "reading-bar";
            case LISTENING -> "listening-bar";
            case SPEAKING -> "speaking-bar";
            case GRINDING -> "grinding-bar";
            case WRITING -> "writing-bar";
        };
    }

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
