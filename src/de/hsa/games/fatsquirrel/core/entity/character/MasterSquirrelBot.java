package de.hsa.games.fatsquirrel.core.entity.character;

import de.hsa.games.fatsquirrel.XY;
import de.hsa.games.fatsquirrel.XYsupport;
import de.hsa.games.fatsquirrel.botapi.*;
import de.hsa.games.fatsquirrel.core.entity.EntityContext;
import de.hsa.games.fatsquirrel.core.entity.EntityType;

public class MasterSquirrelBot extends MasterSquirrel {

    static class ControllerContextImpl implements ControllerContext {

        private EntityContext context;
        private XY myPosition;
        private MasterSquirrel masterSquirrel;

        ControllerContextImpl(EntityContext context, XY myPosition, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.myPosition = myPosition;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = locate().getX() - 15;
            if (x < 0)
                x = 0;
            int y = locate().getY() + 15;
            if (y > context.getSize().getY())
                y = context.getSize().getY();
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = locate().getX() + 15;
            if (x > context.getSize().getX())
                x = context.getSize().getX();
            int y = locate().getY() - 15;
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
            try {
                if (getEntityAt(locate()) == EntityType.MINISQUIRREL){
                    MasterSquirrel ms = ((MiniSquirrel)context.getEntity(locate())).getDaddy();
                    System.out.println(ms.getCoordinate());
                    return ms.getCoordinate();

                }

            } catch (OutOfViewException e) {
                e.printStackTrace();
            }
            return null;
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
                    context.tryMove(masterSquirrel, direction);
                    return;
                }
            }
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) throws SpawnException {
            try {
                if (getEntityAt(locate().plus(direction)) != EntityType.NONE)
                    throw new SpawnException();
            } catch (OutOfViewException e) {
                throw new SpawnException();
            }
            context.spawnMiniSquirrel(locate().plus(direction), energy, masterSquirrel);
        }

        @Override
        public int getEnergy() {
            return masterSquirrel.getEnergy();
        }
    }

    private BotController masterBotController;

    public MasterSquirrelBot(int id, XY position, BotControllerFactory factory) {
        super(id, position);
        this.factory = factory;
        this.masterBotController = factory.createMasterBotController();

        String factoryName = factory.getClass().getSimpleName();
        CharSequence replaceChars = "Factory";
        CharSequence with = "";
        String newName = factoryName.replace(replaceChars,with);
        this.setEntityName(newName);
    }

    @Override
    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, getCoordinate(), this);

        if (moveCounter == 0) {
            if (stunTime > 0)
                stunTime--;
            else {
                masterBotController.nextStep(view);
            }
            moveCounter++;
        } else if (moveCounter == 2)
            moveCounter = 0;
        else
            moveCounter++;

    }

}
