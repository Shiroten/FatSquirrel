package de.hsa.games.fatsquirrel.core.entity.character;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot.ControllerContextImpl;

/**
 * Used as proxy to log the calling of methods
 */
public class DebugHandler implements InvocationHandler {
    private ControllerContextImpl view;

    DebugHandler(ControllerContextImpl view) {
        this.view = view;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append("* calling method ").append(method).append(" with params ");
        if (args != null) {
            for (Object arg : args) sb.append(" ").append(arg);
            sb.append(String.format("%n"));
        }

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
        sb.append("* result:").append(result);
        Logger logger = Logger.getLogger(Launcher.class.getName());
        logger.log(Level.FINER, sb.toString());

        return result;
    }
}