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
    ANKI,
    TEXTBOOK,
    LOOKING_UP,
    PREPARED_SPEECH;



    public float getReadingCoefficient(){

        return switch (this) {
            case READ_TEXT -> 1f;
            case WATCH_CONTENT_WITH_SUBS, WRITE_TEXT -> 0.25f;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, ANKI -> 0.75f;
            case SHADOWING, TEXTBOOK -> 0.5f;
            case LOOKING_UP -> 0.125f;
            default -> 0;
        };

    }

    public float getWritingCoefficient(){

        return switch (this) {
            case WRITE_TEXT, WRITTEN_CONVERSATION -> 1f;
            default -> 0;
        };
    }

    public float getSpeakingCoefficient(){

        return switch (this) {
            case SHADOWING, PREPARED_SPEECH -> 0.875f;
            case ANKI -> 0.125f;
            case SPOKEN_CONVERSATION -> 1f;
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
            case READ_TEXT, WATCH_CONTENT_WITH_SUBS, WRITE_TEXT, TEXTBOOK -> 0.25f;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, SHADOWING, PREPARED_SPEECH, COMPREHENSIBLE_INPUT_WITHOUT_SUBS,
                 WATCH_CONTENT_WITHOUT_SUBS -> 0.5f;
            case ANKI -> 1f;
            case LOOKING_UP -> 0.125f;
            case SPOKEN_CONVERSATION, WRITTEN_CONVERSATION -> 0.75f;
        };

    }
    public float getNewCoefficient(){

        return switch (this) {
            case READ_TEXT, COMPREHENSIBLE_INPUT_WITH_SUBS -> 1f;
            case WATCH_CONTENT_WITH_SUBS, LOOKING_UP, SPOKEN_CONVERSATION, WRITTEN_CONVERSATION -> 0.75f;
            case SHADOWING, TEXTBOOK, WATCH_CONTENT_WITHOUT_SUBS -> 0.5f;
            case ANKI, PREPARED_SPEECH, WRITE_TEXT -> 0.125f;
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS -> 0.875f;
        };

    }

    @Override
    public String toString() {
        return switch (this){
            case ANKI -> "Anki";
            case WRITE_TEXT -> "Writing Text";
            case TEXTBOOK ->  "Textbook Work";
            case SHADOWING ->  "Shadowing";
            case SPOKEN_CONVERSATION ->  "Spoken Conversation";
            case READ_TEXT ->  "Reading Text";
            case LOOKING_UP ->   "Looking Things Up";
            case PREPARED_SPEECH ->    "Prepared Speech";
            case WRITTEN_CONVERSATION ->   "Written Conversation";
            case WATCH_CONTENT_WITH_SUBS ->   "Watching Content With Subtitles";
            case WATCH_CONTENT_WITHOUT_SUBS ->    "Watching Content Without Subtitles";
            case COMPREHENSIBLE_INPUT_WITH_SUBS ->    "Comprehensible Input With Subtitles";
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS  ->    "Comprehensible Input Without Subtitles";
        };
    }
}