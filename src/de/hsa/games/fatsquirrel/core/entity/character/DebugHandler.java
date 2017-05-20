import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import de.hsa.games.fatsquirrel.core.entity.character.MasterSquirrelBot.ControllerContextImpl;

/*public class DebugHandler implements InvocationHandler  {
    private ControllerContextImpl view;

    public DebugHandler(ControllerContextImpl view)  { this.view = view; }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print("* calling method " + method + " with params ");
        for (int i = 0; i < args.length; i++)
            System.out.print(" " + args[i]);
        System.out.println();

        Object result = null;
        try  {
            result = method.invoke(view, args);
        } catch(IllegalAccessException ex)  {
            ex.printStackTrace();
        } catch(InvocationTargetException ex)  {
            System.out.println("* exception:" + ex.getTargetException());
            throw ex.getTargetException();
        }
        System.out.println("* result:" + result);
        return result;
    }
}*/