package org.example;

public enum Level {
    BEGINNER,
    A1,
    A2,
    B1,
    B2,
    C1,
    C2,
    FLUENCY;

    public String getSymbol() {
        return switch (this) {
            case BEGINNER -> "A0";
            case A1 -> "A1";
            case A2 -> "A2";
            case B1 -> "B1";
            case B2 -> "B2";
            case FLUENCY -> "FLUENT";
            case C1 -> "C1";
            case C2 -> "C2";
        };
    }
}