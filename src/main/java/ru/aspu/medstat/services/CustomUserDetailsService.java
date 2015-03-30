package ru.aspu.medstat.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new CustomUserDetails(user);
    }

    private static class CustomUserDetails implements UserDetails {
        private static final long serialVersionUID = -9192793385060344547L;
        
		private User user;

        public CustomUserDetails(final User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
        	Collection<? extends GrantedAuthority> authorities = null;
        	
        	if (user.role == User.Roles.ADMIN.getValue()) {
        		authorities = AuthorityUtils.createAuthorityList(User.Roles.ADMIN.getName());
        	} else if (user.role == User.Roles.DOCTOR.getValue()) {
        		authorities = AuthorityUtils.createAuthorityList(User.Roles.DOCTOR.getName());
        	} else if (user.role == User.Roles.PATIENT.getValue()){
        		authorities = AuthorityUtils.createAuthorityList(User.Roles.PATIENT.getName());
        	}
        	
        	return authorities;
        }

        @Override
        public String getPassword() {
            if (user == null) {
                return null;
            }
            return user.password;
        }

        @Override
        public String getUsername() {
            if (this.user == null) {
                return null;
            }
            return user.email;
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
    }
}

