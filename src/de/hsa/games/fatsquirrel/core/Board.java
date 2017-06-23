package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.core.entity.*;
import de.hsa.games.fatsquirrel.core.entity.squirrels.*;
import de.hsa.games.fatsquirrel.core.entity.Character;
import de.hsa.games.fatsquirrel.gui.ImplosionContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.hsa.games.fatsquirrel.core.entity.EntityType.BADBEAST;

/**
 * Manages everything on the board, and initializes it.
 */
public class Board {

    private BoardConfig config;
    private int idCounter = 2;
    private long remainingGameTime;
    private List<MasterSquirrel> masterSquirrel = new ArrayList<>();
    private ArrayList<ImplosionContext> implosions = new ArrayList<>();
    private List<Entity> entityList = new ArrayList<>();
    private Logger logger = Logger.getLogger(Launcher.class.getName());

    public Board() {

        this.config = new BoardConfig("default.props");
        this.implosions = new ArrayList<>();
        this.remainingGameTime = config.getGameDurationAtStart();
    }

    public Board(String configName) {
        this.config = new BoardConfig(configName);
        this.implosions = new ArrayList<>();
        this.remainingGameTime = config.getGameDurationAtStart();
        initBoard();
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public ArrayList<ImplosionContext> getImplosions() {
        return implosions;
    }

    void addImplosions(ImplosionContext implosions) {
        this.implosions.add(implosions);
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
        createMasterSquirrels();

        addEntity(EntityType.WALL, config.getNumberOfWalls());
        addEntity(BADBEAST, config.getNumberOfBadBeasts());
        addEntity(EntityType.BADPLANT, config.getNumberOfBadPlants());
        addEntity(EntityType.GOODBEAST, config.getNumberOfGoodBeasts());
        addEntity(EntityType.GOODPLANT, config.getNumberOfGoodPlants());

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
            for (Entity e : new ArrayList<>(entityList)) {
                if (entityList.contains(e)) {
                    if (e instanceof Character)
                        ((Character) e).nextStep(context);
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type   EntityType which to Add
     * @param amount Wished amount top Add
     */
    private void addEntity(EntityType type, int amount) {
        if(getBasicEntities().contains(type)) {
            for (int i = 0; i < amount; i++) {
                try {
                    entityList.add(createBasicEntityFromType(type, randomPosition()));
                } catch (FullFieldException e) {
                    logger.log(Level.WARNING, "Board full while spawning Entities");
                }
            }
        }
    }

    /**
     * @param type     EntitType which will be placed
     * @param position at given Position
     * @return Entity to be added to List
     */
    Entity addEntity(EntityType type, XY position) {


        Entity addEntity = null;
        if(getBasicEntities().contains(type)) {
            switch (type) {
                case MINISQUIRREL:
                case MASTERSQUIRREL:
                case NONE:
                    break;
                default:
                    addEntity = createBasicEntityFromType(type, position);
            }
            entityList.add(addEntity);
        }
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
     * @return free Positon
     * @throws FullFieldException if full
     */
    private XY randomPosition() throws FullFieldException {
        boolean collision;
        int newX, newY;
        XY size = config.getSize();

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
        Collections.addAll(this.entityList, entities);
    }

    /**
     * @return gets the Reference of the actual Player
     */
    public HandOperatedMasterSquirrel getHandOperatedMasterSquirrel() {
        for (Entity e : masterSquirrel) {
            if (e instanceof HandOperatedMasterSquirrel)
                return (HandOperatedMasterSquirrel) e;
        }
        return null;
    }

    private Entity createBasicEntityFromType(EntityType entityType, XY position) {
        try {
            return (Entity) Class.forName("de.hsa.games.fatsquirrel.core.entity."+entityType.getClassName()).getDeclaredConstructors()[0].newInstance(setID(), position);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.log(Level.SEVERE, "Klasse der Entity konnte nicht gefunden werden");
        }
        return null;
    }

    private void createMasterSquirrels() {
        try {
            MasterSquirrel toAdd;
            switch (config.getGameType()){
                case BOT_ONLY:
                    createBots();
                    break;
                case WITH_BOT:
                    toAdd = new HandOperatedMasterSquirrel(1, randomPosition());
                    entityList.add(toAdd);
                    masterSquirrel.add(toAdd);
                    createBots();
                    break;
                case SINGLE_PLAYER:
                    toAdd = new HandOperatedMasterSquirrel(1, randomPosition());
                    entityList.add(toAdd);
                    masterSquirrel.add(toAdd);
                    break;
            }
        } catch (FullFieldException e) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.WARNING, "Board is full");
        }
    }

    private void createBots() {
        for (int i = 0; i < config.getNUMBER_OF_BOTS(); i++) {
            try {
                BotControllerFactory factory = (BotControllerFactory) Class.forName("de.hsa.games.fatsquirrel.botimpls." + config.getBots()[i]).newInstance();
                MasterSquirrel toAdd = new MasterSquirrelBot(setID(), randomPosition(), factory);
                entityList.add(toAdd);
                masterSquirrel.add(toAdd);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.log(Level.WARNING, "Factory konnte nicht gefunden werden");
            } catch (FullFieldException e) {
                logger.log(Level.WARNING, "Field full while spawning bots");
            }
        }
    }

    private List<EntityType> getBasicEntities(){
        List<EntityType> basicList = new ArrayList<>();
        basicList.add(EntityType.BADBEAST);
        basicList.add(EntityType.GOODBEAST);
        basicList.add(EntityType.BADPLANT);
        basicList.add(EntityType.GOODPLANT);
        basicList.add(EntityType.WALL);
        return basicList;
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
