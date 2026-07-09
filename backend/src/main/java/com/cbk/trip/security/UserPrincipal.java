package com.cbk.trip.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cbk.trip.entity.User;
import com.cbk.trip.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * UserPrincipal class to perform authentication and authorization.
 * 
 * @author sansintkyaw
 *
 */
public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	@JsonIgnore
	private String password;

	private User user;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String username, String password, User user,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.user = user;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user) {
		GrantedAuthority auth = new SimpleGrantedAuthority(user.getRole().toString());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(auth);

		return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user, authorities);
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
		if (Status.INACTIVE.equals(user.getStatus())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

}
