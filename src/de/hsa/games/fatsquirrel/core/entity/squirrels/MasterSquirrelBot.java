package de.hsa.games.fatsquirrel.core.entity.squirrels;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.lang.reflect.Proxy;

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
            int x = locate().getX() - VIEW_DISTANCE;
            if (x < 0)
                x = 0;
            int y = locate().getY() - VIEW_DISTANCE;
            if (y < 0)
                y = 0;
            return new XY(x, y);
        }

        @Override
        public XY getViewLowerRight() {
            int x = locate().getX() + VIEW_DISTANCE;
            if (x > context.getSize().getX())
                x = context.getSize().getX();
            int y = locate().getY() + VIEW_DISTANCE;
            if (y > context.getSize().getY())
                y = context.getSize().getY();
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
        }

        @Override
        public XY directionOfMaster() {
            return null;
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
            for (XY xy : XYsupport.directions()) {
                if (xy.equals(direction)) {
                    context.tryMove(masterSquirrel, direction);
                    return;
                }
            }
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws SpawnException {
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
        this.factory = factory;
        this.masterBotController = factory.createMasterBotController();

        this.setEntityName(getName(factory));
    }

    @Override
    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, this);
        DebugHandler handler = new DebugHandler(view);
        ControllerContext proxyView = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        if (moveCounter == 0) {
            if (getStunTime() > 0)
                reduceStunTime();
            else {
                masterBotController.nextStep(proxyView);
            }
            moveCounter++;
        } else if (moveCounter == 2)
            moveCounter = 0;
        else
            moveCounter++;
    }


}

