package ca.hferguson.spring.persistence;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Entity
@Table(name="account")
public class AccountEntity implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="uid")
	private final String uid;
	@Column(name="firstName")
	private final String firstName;
	@Column(name="lastName")
	private final String lastName;
	@Column(name="passwd")
	private final String passwd;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// We will need to adjust this to return a role from authority
		// table. For now, keep it simple
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	@Override
	public String getPassword() {
		
		return getPasswd();
	}
	@Override
	public String getUsername() {
		return getUid();
	}
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// Notably missing is the AddressEntity.
	// at db level, they need to be completely separate and independent
	
}
