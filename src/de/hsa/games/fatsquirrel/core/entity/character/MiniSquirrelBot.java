package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public class MiniSquirrelBot extends MiniSquirrel {
    class ControllerContextImpl implements ControllerContext {

        private EntityContext context;
        private XY myPosition;
        private MiniSquirrel miniSquirrel;

        ControllerContextImpl(EntityContext context, XY myPosition, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.myPosition = myPosition;
            this.miniSquirrel = miniSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = locate().getX() - 10;
            if (x < 0)
                x = 0;
            int y = locate().getY() + 10;
            if (y > context.getSize().getY())
                y = context.getSize().getY();
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = locate().getX() + 10;
            if (x > context.getSize().getX())
                x = context.getSize().getX();
            int y = locate().getY() - 10;
            if (y < 0)
                y = 0;
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return myPosition;
        }

        @Override
        public boolean isMine(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight()))
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
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight()))
                throw new OutOfViewException();

            return context.getEntityType(xy);
        }

        @Override
        public void move(XY direction) {
            for (XY xy : XYsupport.directions()) {
                if (xy.equals(direction)) {
                    context.tryMove(miniSquirrel, direction);
                    return;
                }
            }
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws SpawnException {
        }

        @Override
        public int getEnergy() {
            return miniSquirrel.getEnergy();
        }
    }

    private BotController miniBotController;
    private BotControllerFactory factory;

    public MiniSquirrelBot(int id, XY position, int energy, MasterSquirrel daddy) {
        super(id, position, energy, daddy);
        factory = daddy.getFactory();
        this.miniBotController = factory.createMiniBotController();
    }

    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, getCoordinate(), this);

        if (moveCounter == 0) {
            if (stunTime > 0)
                stunTime--;
            else {
                if (implode) {
                    implode(context, implosionRadius);
                } else {
                    miniBotController.nextStep(view);
                }
            }
            moveCounter++;
        } else if (moveCounter == context.getMINI_SQUIRREL_MOVE_TIME_IN_TICKS())
            moveCounter = 0;
        else
            moveCounter++;


    }


}



