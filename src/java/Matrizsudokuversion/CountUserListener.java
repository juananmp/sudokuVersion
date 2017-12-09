/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    static int     totalUserCount = 0, currentUserCount = 0;

    public void sessionCreated( HttpSessionEvent httpSessionEvent )
    {
        System.out.println("sessionCreated method has been called in "
                + this.getClass().getName());

        
        totalUserCount++;
        currentUserCount++;

        ctx = httpSessionEvent.getSession().getServletContext();
        
        ctx.setAttribute("totalusers", totalUserCount);
        ctx.setAttribute("currentusers", currentUserCount);

    }

    public void sessionDestroyed( HttpSessionEvent httpSessionEvent )
    {
        
        System.out.println("sessionDestroyed method has been called in "
                + this.getClass().getName());       
        currentUserCount--;
        ctx.setAttribute("currentusers", currentUserCount);
    }

}
