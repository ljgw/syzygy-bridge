package com.winkelhagen.chess.syzygy;

/**
 * Created by laurens on 3-11-18 for frankwalter.
 */
public class SyzygyConstants {

    public static final int  TB_LOSS                     = 0;       /* LOSS */
    public static final int  TB_BLESSED_LOSS             = 1;       /* LOSS but 50-move draw */
    public static final int  TB_DRAW                     = 2;       /* DRAW */
    public static final int  TB_CURSED_WIN               = 3;       /* WIN but 50-move draw  */
    public static final int  TB_WIN                      = 4;       /* WIN  */
    public static final int  TB_PROMOTES_NONE            = 0;
    public static final int  TB_PROMOTES_QUEEN           = 1;
    public static final int  TB_PROMOTES_ROOK            = 2;
    public static final int  TB_PROMOTES_BISHOP          = 3;
    public static final int  TB_PROMOTES_KNIGHT          = 4;
    public static final int  TB_RESULT_WDL_MASK          = 0x0000000F;
    public static final int  TB_RESULT_TO_MASK           = 0x000003F0;
    public static final int  TB_RESULT_FROM_MASK         = 0x0000FC00;
    public static final int  TB_RESULT_PROMOTES_MASK     = 0x00070000;
    public static final int  TB_RESULT_DTZ_MASK          = 0xFFF00000;
    public static final int  TB_RESULT_WDL_SHIFT         = 0;
    public static final int  TB_RESULT_TO_SHIFT          = 4;
    public static final int  TB_RESULT_FROM_SHIFT        = 10;
    public static final int  TB_RESULT_PROMOTES_SHIFT    = 16;
    public static final int  TB_RESULT_DTZ_SHIFT         = 20;

}
