package ru.aspu.medstat.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.services.CustomUserDetailsService;
import ru.aspu.medstat.utils.PasswordCrypto;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/api/**", "/auth/**", "/static/**").permitAll()
                .antMatchers("/admin/**").access("hasRole('" + User.Roles.ADMIN.getName() + "')")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login").permitAll()
                .and()
                .logout().logoutUrl("/auth/logout").logoutSuccessUrl("/").permitAll()
                .and();

        http.csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(PasswordCrypto.getInstance().getPasswordEncoder());
    }
}
