package com.example.springsecurity.security;

import com.example.springsecurity.filters.JWTDataSource;
import com.example.springsecurity.filters.JWTRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final JWTRequestFilter jwtRequestFilter;

	public SecurityConfig(final UserDetailsService userDetailsService, final JWTRequestFilter jwtRequestFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.cors().disable().csrf().disable();
		http.authorizeRequests()
			.antMatchers("/auth/**", "/h2/**").permitAll()
			.anyRequest()
			.fullyAuthenticated();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(final WebSecurity web) {
		web.ignoring().antMatchers("/h2/**", "/auth/**").and().ignoring().antMatchers("/auth");
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public JWTDataSource getJwtDataSource() {return new JWTDataSource();}

}
