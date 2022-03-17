package org.acme.model;

import java.util.HashMap;
import java.util.Map;

public enum Position {
    MAG_BOT_YEL1(1),
    MAG_BOT_YEL2(2),
    MAG_BOT_RED1(3),
    MAG_BOT_RED2(4),
    MAG_TOP_YEL1(19),
    MAG_TOP_YEL2(20),
    MAG_TOP_RED1(21),
    MAG_TOP_RED2(22),
    MAIN_BOT1(12),
    MAIN_BOT2(13),
    MAIN_BOT3(14),
    MAIN_BOT4(15),
    MAIN_BOT5(16),
    MAIN_BOT6(17),
    MAIN_BOT7(18),
    MAIN_TOP1(5),
    MAIN_TOP2(6),
    MAIN_TOP3(7),
    MAIN_TOP4(8),
    MAIN_TOP5(9),
    MAIN_TOP6(10),
    MAIN_TOP7(11),
    RETURN_FIRST_ROW(23),
    RETURN_REMAINING_ROWS(24),
    RETURN_END(25);

    //sets an integer value for each enum
    private final int i;
    Position(final int i) {
        this.i = i;
    }

    //create a map
    private static final Map<Integer, Position> I_MAPPING = new HashMap<>();

    //mapping
    static {
        //iterates trough all enums and assigns values to them
        for (Position elem : values()) {
            I_MAPPING.put(elem.value(), elem);
        }
    }

    //get value of enum element
    public int value() {
        return i;
    }

    //pass value and get corresponding enum
    public static Position valueOfI(int i) throws ConnectFourException {
        Position result = I_MAPPING.get(i);
        //an exception is thrown if i is not a valid position
        if (result == null) {
            throw new ConnectFourException(i + " ist keine Position!");
        }
        return result;
    }
}
