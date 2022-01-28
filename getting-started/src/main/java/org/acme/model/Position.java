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
    MAIN_TOP7(11);

    private final int i;

    Position(final int i) {
        this.i = i;
    }

    private static final Map<Integer, Position> I_MAPPING = new HashMap<>();    //creates a map
    static {
        for (Position elem : values()) {    //iterates trough all enums and assigns values to them
            I_MAPPING.put(elem.value(), elem);
        }
    }

    public int value() {    //get value of enum element
        return i;
    }

    public static Position valueOfI(int i) throws ConnectFourExeption { //get the enum from by giving an integer
        Position result = I_MAPPING.get(i);
        if (result == null) {
            throw new ConnectFourExeption(i + " ist keine Position!");
        }
        return result;
    }
}
