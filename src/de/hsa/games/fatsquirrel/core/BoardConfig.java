package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;

public class BoardConfig {

    private final XY size;

    private final int NUMBER_OF_GB;
    private final int NUMBER_OF_BB;
    private final int NUMBER_OF_GP;
    private final int NUMBER_OF_BP;
    private final int NUMBER_OF_WA;

    private final int NUMBER_OF_BOTS;

    private final int POINTS_FOR_MINI_SQUIRREL = 150;

    private final long GAME_DURATIONE_AT_START;
    private final int TICKLENGTH;

    private final float SQUIRREL_STUN_TIME_LENGTH = 0.2f;
    private final int SQUIRREL_STUN_TIME_IN_TICKS;
    private final float BEAST_MOVE_TIME_LENGTH = 0.1f;
    private final int BEAST_MOVE_TIME_IN_TICKS = 4;
    private final float MINI_SQUIRREL_MOVE_TIME_LENGTH = 0.07f;
    private final int MINI_SQUIRREL_MOVE_TIME_IN_TICKS = 4;
    private final int VIEW_DISTANCE_OF_GOODBEAST;

    private final int VIEW_DISTANCE_OF_BADBEAST;
    private final Game.GameType gameType;

    private final String[] bots = {"BasterFactory", "GoodBeastChaserFactory"};

    public BoardConfig(XY size, int tickLength, int gb, int bb, int gp, int bp, int wa, int NUMBER_OF_BOTS
            , int viewGB, int viewBB, Game.GameType gameType, int gameDuration) {
        this.size = size;
        this.TICKLENGTH = tickLength;
        this.NUMBER_OF_GB = gb;
        this.NUMBER_OF_BB = bb;
        this.NUMBER_OF_GP = gp;
        this.NUMBER_OF_BP = bp;
        this.NUMBER_OF_WA = wa;
        this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (tickLength * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (tickLength * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (tickLength * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.NUMBER_OF_BOTS = NUMBER_OF_BOTS;
        this.VIEW_DISTANCE_OF_GOODBEAST = viewGB;
        this.VIEW_DISTANCE_OF_BADBEAST = viewBB;
        this.gameType = gameType;
        this.GAME_DURATIONE_AT_START = gameDuration;
    }


    public long getGAME_DURATIONE_AT_START() {
        return GAME_DURATIONE_AT_START;
    }

    public BoardConfig() {
        this.size = new XY (40, 30);
        this.TICKLENGTH = 60;
        this.NUMBER_OF_GB = 50;
        this.NUMBER_OF_BB = 5;
        this.NUMBER_OF_GP = 5;
        this.NUMBER_OF_BP = 5;
        this.NUMBER_OF_WA = 25;
        this.NUMBER_OF_BOTS = 4;
        this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (TICKLENGTH * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.VIEW_DISTANCE_OF_GOODBEAST = 7;
        this.VIEW_DISTANCE_OF_BADBEAST = 6;
        this.gameType = Game.GameType.WITH_BOT;
        this.GAME_DURATIONE_AT_START = 300;
    }

    public BoardConfig(XY size) {
        this.size = size;
        this.TICKLENGTH = 60;
        this.NUMBER_OF_GB = 50;
        this.NUMBER_OF_BB = 5;
        this.NUMBER_OF_GP = 5;
        this.NUMBER_OF_BP = 5;
        this.NUMBER_OF_WA = 25;
        this.NUMBER_OF_BOTS = 4;
        this.SQUIRREL_STUN_TIME_IN_TICKS = (int) (TICKLENGTH * SQUIRREL_STUN_TIME_LENGTH);
        //this.BEAST_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * BEAST_MOVE_TIME_LENGTH);
        //this.MINI_SQUIRREL_MOVE_TIME_IN_TICKS = (int) (TICKLENGTH * MINI_SQUIRREL_MOVE_TIME_LENGTH);
        this.VIEW_DISTANCE_OF_GOODBEAST = 7;
        this.VIEW_DISTANCE_OF_BADBEAST = 6;
        this.gameType = Game.GameType.WITH_BOT;
        this.GAME_DURATIONE_AT_START = 1800;

    }

    public BoardConfig(XY size, int NUMBER_OF_GB, int NUMBER_OF_BB, int NUMBER_OF_GP, int NUMBER_OF_BP, int NUMBER_OF_WA) {
        this(size, 60, NUMBER_OF_GB, NUMBER_OF_BB, NUMBER_OF_GP, NUMBER_OF_BP, NUMBER_OF_WA, 4, 6, 6, Game.GameType.SINGLE_PLAYER,1800);
    }

    Game.GameType getGameType() {
        return gameType;
    }

    public XY getSize() {
        return size;
    }

    public int getTICKLENGTH() {
        return TICKLENGTH;
    }

    int getPOINTS_FOR_MINI_SQUIRREL() {
        return POINTS_FOR_MINI_SQUIRREL;
    }

    int getSQUIRREL_STUN_TIME_IN_TICKS() {
        return SQUIRREL_STUN_TIME_IN_TICKS;
    }

    int getBEAST_MOVE_TIME_IN_TICKS() {
        return BEAST_MOVE_TIME_IN_TICKS;
    }

    int getMINI_SQUIRREL_MOVE_TIME_IN_TICKS() {
        return MINI_SQUIRREL_MOVE_TIME_IN_TICKS;
    }

    int getGOODBEAST_VIEW_DISTANCE() {
        return VIEW_DISTANCE_OF_GOODBEAST;
    }

    int getVIEW_DISTANCE_OF_BADBEAST() {
        return VIEW_DISTANCE_OF_BADBEAST;
    }

    int getNUMBER_OF_GB() {
        return NUMBER_OF_GB;
    }

    int getNUMBER_OF_BB() {
        return NUMBER_OF_BB;
    }

    int getNUMBER_OF_GP() {
        return NUMBER_OF_GP;
    }

    int getNUMBER_OF_BP() {
        return NUMBER_OF_BP;
    }

    int getNUMBER_OF_WA() {
        return NUMBER_OF_WA;
    }

    int getNUMBER_OF_BOTS() {
        return bots.length;
    }

    String[] getBots(){ return bots; }

}
