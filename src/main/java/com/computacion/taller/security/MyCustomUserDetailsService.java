package com.computacion.taller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.computacion.taller.model.TsscAdmin;
import com.computacion.taller.repository.AdminDao;


@Service
public class MyCustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	AdminDao adminDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TsscAdmin admin = adminDao.findByUser(username);
		if (admin != null) {
			User.UserBuilder builder = User.withUsername(username).password(admin.getPassword()).roles(admin.getSuperAdmin());
			return builder.build();
		} else {
			throw new UsernameNotFoundException("User not found.");
		}
	}
}