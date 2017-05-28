package de.hsa.games.fatsquirrel.core.entity.character;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot.ControllerContextImpl;

public class DebugHandler implements InvocationHandler {
    private ControllerContextImpl view;

    public DebugHandler(ControllerContextImpl view) {
        this.view = view;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String output = "* calling method " + method + " with params ";
        if (args != null) {
            for (int i = 0; i < args.length; i++)
                output = output + " " + args[i];
            output = output + String.format("%n");
        }

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            output = output + "* exception:" + ex.getTargetException();
            throw ex.getTargetException();
        }
        output = output + "* result:" + result;
        //System.out.println(output);
        return result;
    }
}