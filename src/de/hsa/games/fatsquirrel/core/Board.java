package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botapi.bots.Baster.BasterFactory;
import de.hsa.games.fatsquirrel.botapi.bots.GoodBeastChaser.GoodBeastChaserFactory;
import de.hsa.games.fatsquirrel.botapi.bots.Shiroten.ShirotenFactory;
import de.hsa.games.fatsquirrel.gui.ImplosionContext;
import de.hsa.games.fatsquirrel.core.entity.Wall;
import de.hsa.games.fatsquirrel.core.entity.GoodPlant;
import de.hsa.games.fatsquirrel.core.entity.BadPlant;
import de.hsa.games.fatsquirrel.core.entity.EntitySet;
import de.hsa.games.fatsquirrel.core.entity.Entity;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import de.hsa.games.fatsquirrel.core.entity.character.HandOperatedMasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrel;
import de.hsa.games.fatsquirrel.core.entity.character.GoodBeast;
import de.hsa.games.fatsquirrel.core.entity.character.BadBeast;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;
import de.hsa.games.fatsquirrel.core.entity.character.MiniSquirrelBot;
import java.util.ArrayList;


public class Board {

    private EntitySet set;
    private BoardConfig config;
    private int idCounter = 0;

    private MasterSquirrel masterSquirrel[] = new MasterSquirrel[10];
    private BotControllerFactory factoryList[] = {new BasterFactory(), new ShirotenFactory(), new GoodBeastChaserFactory()};

    private ArrayList<ImplosionContext> implosions;

    public Board() {

        this.set = new EntitySet(new XY(20, 20));
        this.config = new BoardConfig(new XY(20, 20), 60, 0, 0, 0, 0, 0, 0, 0, 0, Game.GameType.WITH_BOT);

        //initBoard();
    }

    public Board(EntitySet set, BoardConfig config){
        this.set = set;
        this.config = config;
    }

    public Board(BoardConfig config) {
        this.set = new EntitySet(config.getSize());
        this.config = config;
        this.implosions = new ArrayList<>();
        initBoard();
    }

    EntitySet getSet() {
        return set;
    }

    ArrayList<ImplosionContext> getImplosions() {
        return implosions;
    }

    public BoardConfig getConfig() {
        return config;
    }

    public FlattenedBoard flatten() {
        Entity[][] list = new Entity[config.getSize().getY()][config.getSize().getX()];
        for (int i = 0; i < set.getNumberOfMaxEntities(); i++) {
            if (set.getEntity(i) != null) {
                Entity dummy = set.getEntity(i);
                try {
                    list[dummy.getCoordinate().getY()][dummy.getCoordinate().getX()] = dummy;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(dummy.toString());
                }
            }
        }

        return new FlattenedBoard(config.getSize(), this, list);
    }

    private int setID() {

        return idCounter++;
    }

    private void initBoard() {
        //Äußere Mauern
        initOuterWalls();

        //Random Entitys auf der Map verteilt
        addEntity(EntityType.WALL, config.getNUMBER_OF_WA());
        addEntity(EntityType.BADBEAST, config.getNUMBER_OF_BB());
        addEntity(EntityType.BADPLANT, config.getNUMBER_OF_BP());
        addEntity(EntityType.GOODBEAST, config.getNUMBER_OF_GB());
        addEntity(EntityType.GOODPLANT, config.getNUMBER_OF_GP());

        if (config.getGameType() == Game.GameType.WITH_BOT) {
            addEntity(EntityType.MASTERSQUIRREL, config.getNUMBER_OF_BOTS() + 1);
        } else {
            addEntity(EntityType.MASTERSQUIRREL, 1);
        }

    }

    private void initOuterWalls() {
        int WallIDs = setID();
        int x = config.getSize().getX(), y = config.getSize().getY();
        for (int i = 0; i < x; i++) {
            set.add(new Wall(WallIDs, new XY(i, 0)));
            set.add(new Wall(WallIDs, new XY(i, y - 1)));
        }
        for (int i = 1; i < y - 1; i++) {
            set.add(new Wall(WallIDs, new XY(0, i)));
            set.add(new Wall(WallIDs, new XY(x - 1, i)));
        }
    }

    private void addEntity(EntityType type, int ammount) {

        int numberOfAIs = 0;

        Entity entityToAdd = null;
        for (int i = 0; i < ammount; i++) {

            XY position = randomPosition(config.getSize());
            int randomX = position.getX(), randomY = position.getY();

            switch (type) {
                case WALL:
                    entityToAdd = new Wall(setID(), new XY(randomX, randomY));
                    break;
                case BADBEAST:
                    entityToAdd = new BadBeast(setID(), new XY(randomX, randomY));
                    break;
                case BADPLANT:
                    entityToAdd = new BadPlant(setID(), new XY(randomX, randomY));
                    break;
                case GOODBEAST:
                    entityToAdd = new GoodBeast(setID(), new XY(randomX, randomY));
                    break;
                case GOODPLANT:
                    entityToAdd = new GoodPlant(setID(), new XY(randomX, randomY));
                    break;
                case MASTERSQUIRREL:
                    if (config.getGameType() != Game.GameType.BOT_ONLY && set.getHandOperatedMasterSquirrel() == null) {
                        entityToAdd = new HandOperatedMasterSquirrel(-100, new XY(randomX, randomY));
                    } else {
                        entityToAdd = new MasterSquirrelBot(setID(), new XY(randomX, randomY), getFactory(numberOfAIs));
                        numberOfAIs++;
                    }
                    break;
            }
            set.add(entityToAdd);
        }
    }

    private BotControllerFactory getFactory(int aiNumber) {
        return factoryList[aiNumber % factoryList.length];
    }

    //Package Private
    //TODO: wieder auf Package-Private setzen nach Test
    public Entity addEntity(EntityType type, XY position) {

        Entity addEntity = null;

        switch (type) {
            case WALL:
                addEntity = new Wall(setID(), position);
                break;
            case BADBEAST:
                addEntity = new BadBeast(setID(), position);
                break;
            case BADPLANT:
                addEntity = new BadPlant(setID(), position);
                break;
            case GOODBEAST:
                addEntity = new GoodBeast(setID(), position);
                break;
            case GOODPLANT:
                addEntity = new GoodPlant(setID(), position);
                break;
        }
        set.add(addEntity);

        return addEntity;
    }

    void addMiniSquirrel(XY position, int energy, MasterSquirrel daddy) {
        MiniSquirrel msb = new MiniSquirrelBot(setID(), position, energy, daddy);
        set.add(msb);
    }

    private XY randomPosition(XY size) {

        boolean collision;
        int newX, newY;

        do {
            collision = false;
            newX = (int) ((Math.random() * size.getX()));
            newY = (int) ((Math.random() * size.getY()));

            //Durchsuchen des Entityset nach möglichen Konflikten
            for (int i = 0; i < set.getNumberOfMaxEntities(); i++) {

                if (set.getEntity(i) == null) {
                    return new XY(newX, newY);
                } else if (newX == set.getEntity(i).getCoordinate().getX() && newY == set.getEntity(i).getCoordinate().getY()) {
                    collision = true;
                    break;
                }
            }

        } while (collision);
        return new XY(newX, newY);
    }

    //Package Private
    void killEntity(Entity e) {
        this.set.delete(e);
    }

    private void initPac() {
        if (this.config.getSize().getX() == 20 && this.getConfig().getSize().getY() == 20) {
            initOuterWalls();
            String field =
                    "000X0000000000X000|" +
                            "0X0X0XXXX0XXX0X0X0|" +
                            "0X0x0xxxx0xxx0x0x0|" +
                            "00000x000000x000x0|" +
                            "xxxx0x0xxxx0x0x0x0|" +
                            "00000x0x00000000x0|" +
                            "0xxx000x0xxxxxx000|" +
                            "0x000xxx0xxxxxx0xx|" +
                            "0x0x0x00000000x000|" +
                            "000X000xx0xxx0X000|" +
                            "0X0X0XXXX0XXX0X0X0|" +
                            "0X0x0xxxx0xxx0x0x0|" +
                            "00000x000000x000x0|" +
                            "xxxx0x0xxxx0x0x0x0|" +
                            "00000x0x00000000x0|" +
                            "0xx0000x0xx0x0x000|" +
                            "0xx0xxxx0xx0x0x0xx|" +
                            "000000000000x00000|";
            parseWalls(field);
        }
    }

    private void parseWalls(String field) {
        int x = 0, y = 1;
        field = field.toUpperCase();
        for (char c : field.toCharArray()) {
            x++;
            if (c == 'X') {
                addEntity(EntityType.WALL, new XY(x, y));
            } else if (c == '|') {
                x = 0;
                y++;
            }
        }
    }

    public void add(Entity toAdd) {
        this.set.add(toAdd);
    }

    public HandOperatedMasterSquirrel getHandOperatedMasterSquirrel() {
        return (HandOperatedMasterSquirrel) set.getHandOperatedMasterSquirrel();
    }

    public MasterSquirrel[] getMasterSquirrel() {
        return masterSquirrel;
    }
}
