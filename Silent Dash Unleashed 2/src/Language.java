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

    public int getHours(){
        return switch(this){
            case ESPERANTO -> 300;
            case FRENCH, SPANISH -> 875;
            case GERMAN -> 1050;
            case RUSSIAN, VIETNAMESE -> 1500;
            case KOREAN -> 3500;
            case JAPANESE, MANDARIN -> 4000;
        };
    }

}
