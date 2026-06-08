public enum Language {
    JAPANESE,
    KOREAN,
    MANDARIN,
    SPANISH,
    GERMAN,
    FRENCH,
    ESPERANTO,
    RUSSIAN,
    VIETNAMESE;

    public int getHours(Level level) {
        return switch (level) {

            case A1 -> switch (this) {
                case ESPERANTO -> 30;
                case FRENCH, SPANISH -> 100;
                case GERMAN -> 125;
                case RUSSIAN -> 185;
                case VIETNAMESE, MANDARIN -> 215;
                case KOREAN -> 180;
                case JAPANESE -> 200;
            };

            case A2 -> switch (this) {
                case ESPERANTO -> 80;
                case FRENCH, SPANISH -> 215;
                case GERMAN -> 300;
                case RUSSIAN -> 425;
                case VIETNAMESE -> 550;
                case MANDARIN -> 600;
                case KOREAN -> 525;
                case JAPANESE -> 575;
            };

            case B1 -> switch (this) {
                case ESPERANTO -> 185;
                case FRENCH, SPANISH -> 425;
                case GERMAN -> 600;
                case RUSSIAN -> 850;
                case VIETNAMESE -> 1100;
                case MANDARIN -> 1250;
                case KOREAN -> 1150;
                case JAPANESE -> 1300;
            };

            case B2 -> switch (this) {
                case ESPERANTO -> 375;
                case FRENCH, SPANISH -> 700;
                case GERMAN -> 950;
                case RUSSIAN -> 1400;
                case VIETNAMESE -> 1700;
                case MANDARIN -> 2150;
                case KOREAN -> 2200;
                case JAPANESE -> 2500;
            };

            case C1 -> switch (this) {
                case ESPERANTO -> 750;
                case FRENCH, SPANISH -> 1200;
                case GERMAN -> 1550;
                case RUSSIAN -> 2350;
                case VIETNAMESE -> 3000;
                case MANDARIN -> 3500;
                case KOREAN -> 3600;
                case JAPANESE -> 4250;
            };

            case C2 -> switch (this) {
                case ESPERANTO -> 1500;
                case FRENCH, SPANISH -> 2000;
                case GERMAN -> 2500;
                case RUSSIAN -> 3500;
                case VIETNAMESE -> 4500;
                case MANDARIN, KOREAN -> 5000;
                case JAPANESE -> 6000;
            };

            case FLUENCY -> {
                int b2 = getHours(Level.B2);
                int c1 = getHours(Level.C1);
                yield b2 + (int) ((c1 - b2) * 0.7);
            }
        };
    }

    public int getMinutes(Level level){
        return this.getHours(level)*60;
    }

    public int getXp(Level level){
        return this.getMinutes(level)*175;
    }






}
