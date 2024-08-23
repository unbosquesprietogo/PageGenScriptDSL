package co.edu.unbosque.pagegenscripttokenizer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public class Token {
    public static final int PAGE = 0;
    public static final int TITLE = 1;
    public static final int STYLES = 2;
    public static final int PRIMARY_COLOR = 3;
    public static final int SECONDARY_COLOR = 4;
    public static final int SECTIONS = 5;
    public static final int TYPE = 6;
    public static final int NAVBAR = 7;
    public static final int BACKGROUND_COLOR = 8;
    public static final int ITEMS = 9;
    public static final int TEXT = 10;
    public static final int LINK = 11;
    public static final int COLOR = 12;
    public static final int HERO = 13;
    public static final int SUBTITLE = 14;
    public static final int BUTTON = 15;
    public static final int CARD_DECK = 16;
    public static final int CARDS = 17;
    public static final int CARD = 18;
    public static final int ACCORDION = 19;
    public static final int CONTENT = 20;
    public static final int ALERTS = 21;
    public static final int ALERT = 22;
    public static final int SUCCESS = 23;
    public static final int DANGER = 24;
    public static final int MESSAGE = 25;
    public static final int MODAL = 26;
    public static final int ID = 27;
    public static final int EXAMPLE_MODAL = 28;
    public static final int TITLE_MODAL = 29;
    public static final int BODY = 30;
    public static final int CAROUSEL = 31;
    public static final int IMAGE = 32;
    public static final int CAPTION = 33;
    public static final int LIST_GROUP = 34;
    public static final int TOAST = 35;
    public static final int TOAST_EXAMPLE = 36;
    public static final int DELAY = 37;
    public static final int STRING = 38;
    public static final int NUMBER = 39;
    public static final int SYMBOL = 40;
    public static final int MINUS = 41;
    public static final int COLON = 42;

    private final int token;
    private final String lexeme;
    private final int pos;

    @Override
    public String toString() {
        return "[Token:" + token + " Lexeme:" + lexeme + " Position:" + pos + "]";
    }
}
