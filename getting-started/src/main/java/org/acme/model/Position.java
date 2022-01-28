package org.acme.model;

public enum Position {
    HOME(0),
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

    Position(int i) {
        this.i = i;
    }
}
