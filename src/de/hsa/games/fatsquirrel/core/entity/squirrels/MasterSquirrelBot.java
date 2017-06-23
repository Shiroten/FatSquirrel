package de.hsa.games.fatsquirrel.core.entity.squirrels;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;
import javafx.scene.paint.Color;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A MasterSquirrel controlled by an AI, which is specified by the BotController
 * Extends MasterSquirrel
 */
public class MasterSquirrelBot extends MasterSquirrel {

    /**
     * The information a squirrel can get and the actions it can do
     */
    public static class ControllerContextImpl implements ControllerContext {

        private EntityContext context;
        private MasterSquirrel masterSquirrel;
        private static final int VIEW_DISTANCE = 15;


        public ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewUpperLeft() {
            int x = (locate().getX() - VIEW_DISTANCE) < 0 ? 0 : locate().getX() - VIEW_DISTANCE;
            int y = locate().getY() - VIEW_DISTANCE < 0 ? 0 : locate().getY() - VIEW_DISTANCE;
            return new XY(x, y);
        }

        @Override
        public XY getViewLowerRight() {
            int x = locate().getX() + VIEW_DISTANCE > context.getSize().getX() ? context.getSize().getX() : locate().getX() + VIEW_DISTANCE;
            int y = locate().getY() + VIEW_DISTANCE > context.getSize().getY() ? context.getSize().getY() : locate().getY();
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return masterSquirrel.getCoordinate();
        }

        @Override
        public boolean isMine(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewUpperLeft(), getViewLowerRight()))
                throw new OutOfViewException();
            try {
                if (masterSquirrel.mySquirrel((MiniSquirrel) context.getEntity(xy)))
                    return true;
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @Override
        public void implode(int impactRadius) {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.WARNING, masterSquirrel.getFactory().getClass().getSimpleName() + " master tried to implode");
        }

        @Override
        public XY directionOfMaster() {
            return XY.ZERO_ZERO;
        }

        @Override
        public long getRemainingSteps() {
            return context.getRemainingTime();
        }

        @Override
        public EntityType getEntityAt(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewUpperLeft(), getViewLowerRight()))
                throw new OutOfViewException();
            return context.getEntityType(xy);
        }

        @Override
        public void move(XY direction) {
            if (direction.length() <= 1.5) {
                context.tryMove(masterSquirrel, direction);
            }
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws SpawnException {
            if(direction.length() > 1.5)
                throw new SpawnException();
            if (masterSquirrel.getEnergy() >= energy) {
                try {
                    if (getEntityAt(locate().plus(direction)) != EntityType.NONE)
                        throw new SpawnException();
                } catch (OutOfViewException e) {
                    throw new SpawnException();
                }
                context.spawnMiniSquirrel(locate().plus(direction), energy, masterSquirrel);
            } else {
                throw new SpawnException();
            }
        }

        @Override
        public int getEnergy() {
            return masterSquirrel.getEnergy();
        }
    }

    private int moveCounter = 0;
    private BotController masterBotController;

    public MasterSquirrelBot(int id, XY position, BotControllerFactory factory) {
        super(id, position);
        setFactory(factory);
        this.masterBotController = factory.createMasterBotController();

        this.setEntityName(getName(factory));
        setEntityColor(Color.color(0, 0.0588, 1));
    }

    @Override
    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, this);
        DebugHandler handler = new DebugHandler(view);
        ControllerContext proxyView = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        int waitingTime = 1;
        if (moveCounter % waitingTime+1 == 0) {
            if (getStunTime() > 0)
                reduceStunTime();
            else {
                masterBotController.nextStep(proxyView);
            }
        }
        moveCounter++;
    }


}

