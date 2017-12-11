/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Matrizsudokuversion;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author janto
 */
public class FiltroUsuarioConectado implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        String user = null;

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession cliente = req.getSession(true);
        user = (String) cliente.getAttribute("user");

        if (user == null) {

            cliente.invalidate();
            res.sendRedirect("index.html");

        } else {
            // pass the request along the filter chain
            chain.doFilter(request, response);
        }

    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * Init method for this filter
     */
}
