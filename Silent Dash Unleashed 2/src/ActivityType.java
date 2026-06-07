public enum ActivityType {

    READ_TEXT,
    WATCH_CONTENT_WITH_SUBS,
    WATCH_CONTENT_WITHOUT_SUBS,
    COMPREHENSIBLE_INPUT_WITHOUT_SUBS,
    COMPREHENSIBLE_INPUT_WITH_SUBS,
    SHADOWING,
    SPOKEN_CONVERSATION,
    WRITTEN_CONVERSATION,
    WRITE_TEXT,
    SCRIP_PRACTICE,
    ANKI,
    TEXTBOOK,
    PREPARED_SPEECH;

    //TODO get main category

    public double getReadingCoefficient(){

        return switch (this) {
            case READ_TEXT -> 1;
            case WATCH_CONTENT_WITH_SUBS, WRITE_TEXT -> 0.25;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, ANKI, WRITTEN_CONVERSATION -> 0.75;
            case SHADOWING, TEXTBOOK, SCRIP_PRACTICE -> 0.5;
            default -> 0;
        };

    }

    public double getWritingCoefficient(){

        return switch (this) {
            case WRITE_TEXT, SCRIP_PRACTICE -> 1;
            case WRITTEN_CONVERSATION -> 0.25;
            default -> 0;
        };
    }

    public double getSpeakingCoefficient(){

        return switch (this) {
            case SHADOWING, PREPARED_SPEECH -> 0.875;
            case ANKI, WRITTEN_CONVERSATION -> 0.25;
            case SPOKEN_CONVERSATION -> 1;
            default -> 0;
        };

    }
    public float getListeningCoefficient(){

        return switch (this) {
            case WATCH_CONTENT_WITH_SUBS, ANKI -> 0.75f;
            case COMPREHENSIBLE_INPUT_WITH_SUBS -> 0.875f;
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS, SPOKEN_CONVERSATION -> 1f;
            case SHADOWING -> 0.5f;
            default -> 0;
        };

    }
    public float getGrindingCoefficient(){

        return switch (this) {
            case READ_TEXT, WATCH_CONTENT_WITH_SUBS, WRITE_TEXT, TEXTBOOK, SCRIP_PRACTICE -> 0.25f;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, SHADOWING, PREPARED_SPEECH, COMPREHENSIBLE_INPUT_WITHOUT_SUBS,
                 WATCH_CONTENT_WITHOUT_SUBS -> 0.5f;
            case ANKI -> 1f;
            case SPOKEN_CONVERSATION, WRITTEN_CONVERSATION -> 0.75f;
        };


    }

    public ActivityCategory getMainCategory(){
        return switch (this) {
            case WRITE_TEXT, SCRIP_PRACTICE -> ActivityCategory.WRITING;
            case ANKI, TEXTBOOK, WRITTEN_CONVERSATION -> ActivityCategory.GRINDING;
            case SPOKEN_CONVERSATION, SHADOWING, PREPARED_SPEECH -> ActivityCategory.SPEAKING;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, WATCH_CONTENT_WITH_SUBS, WATCH_CONTENT_WITHOUT_SUBS,
                 COMPREHENSIBLE_INPUT_WITHOUT_SUBS -> ActivityCategory.LISTENING;
            case READ_TEXT -> ActivityCategory.READING;
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case ANKI -> "Anki";
            case WRITE_TEXT -> "Writing Text";
            case TEXTBOOK -> "Textbook Work";
            case SHADOWING -> "Shadowing";
            case SPOKEN_CONVERSATION -> "Spoken Conversation";
            case READ_TEXT -> "Reading Text";
            case PREPARED_SPEECH -> "Prepared Speech";
            case WRITTEN_CONVERSATION -> "Written Conversation";
            case WATCH_CONTENT_WITH_SUBS -> "Watching Content With Subtitles";
            case WATCH_CONTENT_WITHOUT_SUBS -> "Watching Content Without Subtitles";
            case COMPREHENSIBLE_INPUT_WITH_SUBS -> "Comprehensible Input With Subtitles";
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS  -> "Comprehensible Input Without Subtitles";
            case SCRIP_PRACTICE -> "Script Practice";
        };
    }
}