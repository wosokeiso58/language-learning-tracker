package org.example;
public enum ActivityType {

    READ_TEXT("Read Text"),
    WATCH_CONTENT_WITH_SUBS("Content Subs"),
    WATCH_CONTENT_WITHOUT_SUBS("Content No Subs"),
    COMPREHENSIBLE_INPUT_WITHOUT_SUBS("CI No Subs"),
    COMPREHENSIBLE_INPUT_WITH_SUBS("CI Subs"),
    SHADOWING("Shadowing"),
    SPOKEN_CONVERSATION("Conversation"),
    WRITTEN_CONVERSATION("Messaging"),
    HANDWRITE_TEXT("Handwriting"),
    SCRIPT_PRACTICE("Script"),
    ANKI("Anki"),
    TEXTBOOK_WORK("Textbook"),
    PREPARED_SPEECH("Prepared Speech"),
    CROSSTALK("Crosstalk"),;

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getReadingCoefficient(){

        return switch (this) {
            case READ_TEXT -> 1;
            case WATCH_CONTENT_WITH_SUBS, HANDWRITE_TEXT, SHADOWING, SCRIPT_PRACTICE, CROSSTALK -> 0.25;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, WRITTEN_CONVERSATION -> 0.75;
            case TEXTBOOK_WORK, ANKI, PREPARED_SPEECH -> 0.5;
            default -> 0;
        };

    }

    public double getWritingCoefficient(){

        return switch (this) {
            case HANDWRITE_TEXT -> 1;
            case WRITTEN_CONVERSATION -> 0.25;
            case SCRIPT_PRACTICE -> 0.5;
            case TEXTBOOK_WORK -> 0.75;
            default -> 0;
        };
    }

    public double getSpeakingCoefficient(){

        return switch (this) {
            case PREPARED_SPEECH, SHADOWING -> 0.75;
            case WRITTEN_CONVERSATION, ANKI, COMPREHENSIBLE_INPUT_WITHOUT_SUBS -> 0.25;
            case SPOKEN_CONVERSATION -> 1;
            default -> 0;
        };

    }
    public double getListeningCoefficient(){

        return switch (this) {
            case WATCH_CONTENT_WITH_SUBS, ANKI -> 0.50;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, SPOKEN_CONVERSATION, CROSSTALK -> 0.75;
            case SHADOWING, WATCH_CONTENT_WITHOUT_SUBS -> 0.25;
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS -> 1;
            default -> 0;
        };

    }
    public double getGrindingCoefficient(){

        return switch (this) {
            case WATCH_CONTENT_WITH_SUBS, HANDWRITE_TEXT, SCRIPT_PRACTICE -> 0.25;
            case WRITTEN_CONVERSATION, COMPREHENSIBLE_INPUT_WITH_SUBS, SHADOWING, PREPARED_SPEECH, WATCH_CONTENT_WITHOUT_SUBS, SPOKEN_CONVERSATION, READ_TEXT,CROSSTALK ,COMPREHENSIBLE_INPUT_WITHOUT_SUBS -> 0.5;
            case ANKI -> 0.75;
            default -> 0;
        };


    }

    public ActivityCategory getMainCategory(){
        return switch (this) {
            case HANDWRITE_TEXT, SCRIPT_PRACTICE -> ActivityCategory.WRITING;
            case ANKI, TEXTBOOK_WORK, WRITTEN_CONVERSATION -> ActivityCategory.GRINDING;
            case SPOKEN_CONVERSATION, SHADOWING, PREPARED_SPEECH -> ActivityCategory.SPEAKING;
            case COMPREHENSIBLE_INPUT_WITH_SUBS, WATCH_CONTENT_WITH_SUBS, WATCH_CONTENT_WITHOUT_SUBS,
                 COMPREHENSIBLE_INPUT_WITHOUT_SUBS, CROSSTALK -> ActivityCategory.LISTENING;
            case READ_TEXT -> ActivityCategory.READING;
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case ANKI -> "Anki";
            case HANDWRITE_TEXT -> "Writing Text";
            case TEXTBOOK_WORK -> "Textbook Work";
            case SHADOWING -> "Shadowing";
            case SPOKEN_CONVERSATION -> "Spoken Conversation";
            case READ_TEXT -> "Reading Text";
            case PREPARED_SPEECH -> "Prepared Speech";
            case WRITTEN_CONVERSATION -> "Written Conversation";
            case WATCH_CONTENT_WITH_SUBS -> "Watching Content With Subtitles";
            case WATCH_CONTENT_WITHOUT_SUBS -> "Watching Content Without Subtitles";
            case COMPREHENSIBLE_INPUT_WITH_SUBS -> "Comprehensible Input With Subtitles";
            case COMPREHENSIBLE_INPUT_WITHOUT_SUBS  -> "Comprehensible Input Without Subtitles";
            case SCRIPT_PRACTICE -> "Script Practice";
            case CROSSTALK -> "Crosstalk";
        };
    }


}