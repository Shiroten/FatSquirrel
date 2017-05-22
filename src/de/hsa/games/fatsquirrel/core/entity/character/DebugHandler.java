package de.hsa.games.fatsquirrel.core.entity.character;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.*;

import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot.ControllerContextImpl;

public class DebugHandler implements InvocationHandler {
    private ControllerContextImpl view;
    private Logger logger;

    public DebugHandler(ControllerContextImpl view) {
        this.view = view;

        this.logger  = Logger.getLogger(MasterSquirrelBot.class.getName());
        this.logger.setLevel(Level.OFF);
        /*try {
            Handler handler = new FileHandler("logMasterSquirrelBot.txt");
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            handler.setLevel(Level.ALL);
            this.logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        logger.log(Level.INFO, "* calling method " + method + " with params ");
        if (args != null) {
            for (int i = 0; i < args.length; i++)
                logger.log(Level.INFO, " " + args[i]);
        }

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            logger.log(Level.INFO, "* exception:" + ex.getTargetException());
            throw ex.getTargetException();
        }
        if (result != null)
        logger.log(Level.INFO, "* result:" + result);
        return result;
    }
}