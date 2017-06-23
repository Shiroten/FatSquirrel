package de.hsa.games.fatsquirrel.core.entity.squirrels;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hsa.games.fatsquirrel.Launcher;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;

/**
 * Used as proxy to log the calling of methods
 */
public class DebugHandler implements InvocationHandler {
    private ControllerContext view;

    DebugHandler(ControllerContext view) {
        this.view = view;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        Logger logger = Logger.getLogger(Launcher.class.getName());

        sb.append("* calling method ").append(method).append(" with params ");
        if (args != null) {
            for (Object arg : args) sb.append(" ").append(arg);
            sb.append("\n");
        }

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException ex) {
            logger.log(Level.FINER, ex.getMessage());
        } catch (InvocationTargetException ex){
            throw  ex.getTargetException();
        }
        sb.append("* result:").append(result);
        logger.log(Level.FINER, sb.toString());

        return result;
    }
}