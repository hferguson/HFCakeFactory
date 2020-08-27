package ca.hferguson.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CFUserDetailsService implements UserDetailsService {

	//@Autowired
	private IAccountService accountService;
	
	public CFUserDetailsService(IAccountService service) {
		this.accountService = service;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//log("UserDetailsService.loadUserByUsername called. Username " + username);
		UserDetails uDetails = accountService.findUsername(username);
		if (uDetails == null)
			throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
		//log("retrieved username " + uDetails.getUsername());
		//log("retrieved passwd " + uDetails.getPassword());
		return uDetails;
	}

}
