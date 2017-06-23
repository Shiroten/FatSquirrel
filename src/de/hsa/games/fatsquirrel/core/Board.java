package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.character.*;
import de.hsa.games.fatsquirrel.core.entity.character.Character;
import de.hsa.games.fatsquirrel.gui.ImplosionContext;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Manages everything on the board, and initializes it.
 */
public class Board {

    private BoardConfig config;
    private int idCounter = 0;
    private long remainingGameTime;

    private List<MasterSquirrel> masterSquirrel = new ArrayList<>();
    private ArrayList<ImplosionContext> implosions;
    private List<Entity> entityList = new ArrayList<>();

    public Board() {

        this.config = new BoardConfig("default.props");
        this.implosions = new ArrayList<>();
        this.remainingGameTime = config.getGAME_DURATION_AT_START();
    }

    public Board(String configName) {
        this.config = new BoardConfig(configName);
        this.implosions = new ArrayList<>();
        this.remainingGameTime = config.getGAME_DURATION_AT_START();
        initBoard();
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    ArrayList<ImplosionContext> getImplosions() {
        return implosions;
    }

    public BoardConfig getConfig() {
        return config;
    }

    public FlattenedBoard flatten() {
        Entity[][] list = new Entity[config.getSize().getY()][config.getSize().getX()];
        for (Entity e : entityList) {
            list[e.getCoordinate().getY()][e.getCoordinate().getX()] = e;
        }

        return new FlattenedBoard(config.getSize(), this, list);
    }

    private int setID() {
        return idCounter++;
    }

    /**
     * Initialize the entire Board
     */
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

    /**
     * Place the outer Border of the Field
     */
    private void initOuterWalls() {
        int x = config.getSize().getX(), y = config.getSize().getY();
        for (int i = 0; i < x; i++) {
            entityList.add(new Wall(setID(), new XY(i, 0)));
            entityList.add(new Wall(setID(), new XY(i, y - 1)));
        }
        for (int i = 1; i < y - 1; i++) {
            entityList.add(new Wall(setID(), new XY(0, i)));
            entityList.add(new Wall(setID(), new XY(x - 1, i)));
        }
    }

    public void nextStep(EntityContext context) {

        try {
            for(Entity e : new ArrayList<>(entityList)){
                if(entityList.contains(e)) {
                    if (e instanceof de.hsa.games.fatsquirrel.core.entity.character.Character)
                        ((Character) e).nextStep(context);
                }
            }
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    /**
     * @param type   EntityType which to Add
     * @param amount Wished amount top Add
     */
    private void addEntity(EntityType type, int amount) {

        int numberOfAIs = 0;

        Entity entityToAdd = null;
        for (int i = 0; i < amount; i++) {

            XY position;
            try {
                position = randomPosition(config.getSize());
            } catch (FullFieldException e) {
                return;
            }

            //TODO: Überlegen, ob man das per Intro-/Reflection lösen kann
            switch (type) {
                case WALL:
                    entityToAdd = new Wall(setID(), position);
                    break;
                case BADBEAST:
                    entityToAdd = new BadBeast(setID(), position);
                    break;
                case BADPLANT:
                    entityToAdd = new BadPlant(setID(), position);
                    break;
                case GOODBEAST:
                    entityToAdd = new GoodBeast(setID(), position);
                    break;
                case GOODPLANT:
                    entityToAdd = new GoodPlant(setID(), position);
                    break;
                case MASTERSQUIRREL:
                    if (config.getGameType() != Game.GameType.BOT_ONLY && getHandOperatedMasterSquirrel() == null) {
                        entityToAdd = new HandOperatedMasterSquirrel(-100, position);
                    } else {
                        try {
                            BotControllerFactory factory = (BotControllerFactory) Class.forName("de.hsa.games.fatsquirrel.botimpls." + config.getBots()[numberOfAIs]).newInstance();
                            entityToAdd = new MasterSquirrelBot(setID(), position, factory);
                            numberOfAIs++;
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                            //TODO: In Log schreiben
                            e.printStackTrace();
                        }
                    }
                    masterSquirrel.add((MasterSquirrel) entityToAdd);
                    break;
            }
            entityList.add(entityToAdd);
        }
    }

    /**
     * @param type     EntitType which will be placed
     * @param position at given Position
     * @return Entity to be added to List
     */
    Entity addEntity(EntityType type, XY position) {

        Entity addEntity = null;

        //TODO: Per Reflection/Introspection lösen
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
        entityList.add(addEntity);

        return addEntity;
    }

    /**
     * @param position Position in XY of Mini
     * @param energy   StartEnergy of MiniSquirrel
     * @param daddy    MasterSquirrel Reference of the MiniSquirrel
     */
    void addMiniSquirrel(XY position, int energy, MasterSquirrel daddy) {
        MiniSquirrel msb = new MiniSquirrelBot(setID(), position, energy, daddy);
        entityList.add(msb);
    }

    /**
     * @param size to calculate the maximum of Entity which the Board can contain
     * @return free Positon
     * @throws FullFieldException if full
     */
    private XY randomPosition(XY size) throws FullFieldException {

        boolean collision;
        int newX, newY;

        if (entityList.size() == size.getY() * size.getX())
            throw new FullFieldException();
        do {
            collision = false;
            newX = (int) ((Math.random() * size.getX()));
            newY = (int) ((Math.random() * size.getY()));

            //Durchsuchen des Entityset nach möglichen Konflikten
            for (Entity e : entityList) {
                if (newX == e.getCoordinate().getX() && newY == e.getCoordinate().getY()) {
                    collision = true;
                    break;
                }
            }

        } while (collision);
        return new XY(newX, newY);
    }

    /**
     * @param e Kill given Entity
     */
    //Package Private
    void killEntity(Entity e) {
        entityList.remove(e);
    }

    /**
     * @param toAdd Adds given Entity List
     */
    public void add(Entity toAdd) {
        entityList.add(toAdd);
    }

    /**
     * @param entities Add Multiple Entity's
     */
    public void add(Entity... entities) {
        for (Entity e : entities) {
            this.entityList.add(e);
        }
    }

    /**
     * @return gets the Reference of the actual Player
     */
    public HandOperatedMasterSquirrel getHandOperatedMasterSquirrel() {
        for(Entity e : entityList){
            if(e instanceof HandOperatedMasterSquirrel)
                return (HandOperatedMasterSquirrel) e;
        }
        return null;
    }

    /**
     * @return List of all MasterSquirrel for the HighScore
     */
    List<MasterSquirrel> getMasterSquirrel() {
        return masterSquirrel;
    }

    public long getRemainingGameTime() {
        return remainingGameTime;
    }

    public void reduceRemainingGameTime() {
        remainingGameTime--;
    }
}
