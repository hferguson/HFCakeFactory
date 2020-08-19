package ca.hferguson.spring.filter;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ca.hferguson.spring.bean.IBasket;

@Component
@Order(1)
public class UserBasketFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBasketFilter.class);
	private IBasket basket;
	
	public UserBasketFilter(IBasket basket) {
		this.basket = basket;
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

	  HttpServletRequest request = (HttpServletRequest) servletRequest;
	  HttpServletResponse response = (HttpServletResponse) servletResponse;
	  LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

	  request.setAttribute("basketTotal", basket.getItemsInBasket());
	  request.setAttribute("basketPrice", basket.getCartTotal());
	  
	  LOGGER.info("basketTotal set to {}", basket.getItemsInBasket());
	  
	  //call next filter in the filter chain
	  filterChain.doFilter(request, response);
	  
	}
	
}
