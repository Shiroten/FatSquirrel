package de.hsa.games.fatsquirrel.core.entity.character;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot.ControllerContextImpl;

/**
 * Used as proxy to log the calling of methods
 */
public class DebugHandler implements InvocationHandler {
    private ControllerContextImpl view;

    public DebugHandler(ControllerContextImpl view) {
        this.view = view;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append("* calling method " + method + " with params ");
        if (args != null) {
            for (int i = 0; i < args.length; i++)
                sb.append(" " + args[i]);
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
        sb.append("* result:" + result);
        //Todo: output in logger
        //System.out.println(sb.toString());
        return result;
    }
}