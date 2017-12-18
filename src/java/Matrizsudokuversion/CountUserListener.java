package Matrizsudokuversion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author janto
 */
public class CountUserListener implements HttpSessionListener {

    ServletContext ctx = null;
    static int totalUserCount = 0, currentUserCount = 0;

    //Aumenta en 1 el numero de usuario totales y concurrentes cada vez que se abre una sesion
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("sessionCreated method has been called in "
                + this.getClass().getName());

        totalUserCount++;
        currentUserCount++;

        ctx = httpSessionEvent.getSession().getServletContext();

        ctx.setAttribute("totalusers", totalUserCount);
        ctx.setAttribute("currentusers", currentUserCount);

    }

    //Disminuye en 1 el numero de usuario concurrentes cada vez que se cierra una sesion
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        System.out.println("sessionDestroyed method has been called in "
                + this.getClass().getName());
        currentUserCount--;
        ctx.setAttribute("currentusers", currentUserCount);
    }

}
