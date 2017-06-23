package de.hsa.games.fatsquirrel.core.entity.squirrels;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A MiniSquirrel controlled by an AI, which is specified by the BotController
 * Extends MiniSquirrel
 */
public class MiniSquirrelBot extends MiniSquirrel {
    public static class ControllerContextImpl implements ControllerContext {

        private EntityContext context;
        private XY myPosition;
        private MiniSquirrel miniSquirrel;
        private static final int VIEW_DISTANCE = 10;

        public ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.myPosition = miniSquirrel.getCoordinate();
            this.miniSquirrel = miniSquirrel;
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
            return myPosition;
        }

        @Override
        public boolean isMine(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewUpperLeft(), getViewLowerRight()))
                throw new OutOfViewException();
            try {
                return context.getEntity(xy).equals(miniSquirrel.getDaddy());
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void implode(int impactRadius) {
            context.implode(miniSquirrel, impactRadius);
        }

        @Override
        public XY directionOfMaster() {
            return XYsupport.normalizedVector(miniSquirrel.getDaddy().getCoordinate().minus(miniSquirrel.getCoordinate()));
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
                context.tryMove(miniSquirrel, direction);
            }
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws SpawnException {
            Logger logger = Logger.getLogger(Launcher.class.getName());
            logger.log(Level.WARNING, miniSquirrel.getEntityName() + " mini tried to spawn another bot");
        }

        @Override
        public int getEnergy() {
            return miniSquirrel.getEnergy();
        }
    }

    private BotController miniBotController;

    public MiniSquirrelBot(int id, XY position, int energy, MasterSquirrel daddy) {
        super(id, position, energy, daddy);
        this.miniBotController = daddy.getFactory().createMiniBotController();
    }

    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, this);
        DebugHandler handler = new DebugHandler(view);
        ControllerContext proxyView = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        int waitingTime = 0;
        if (moveCounter % waitingTime + 1 == 0) {
            if (getStunTime() > 0)
                reduceStunTime();
            else {
                if (implode)
                    view.implode(implosionRadius);
                else
                    miniBotController.nextStep(proxyView);
            }
        }
        moveCounter++;
    }


}



