package fr.dawan.autoquiz3000.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfessorFilter implements Filter {
	
    public ProfessorFilter() {
    }

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req= (HttpServletRequest) request;
		HttpServletResponse res= (HttpServletResponse) response;
		boolean isProfessor= false;
		
		if(req.getSession().getAttribute("isProfessor") != null) {
			isProfessor= (Boolean) req.getSession().getAttribute("isProfessor");
		}
		
		if(!isProfessor) {
			String url= req.getContextPath()+"/public/connection";
			res.sendRedirect( url );
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}
}
