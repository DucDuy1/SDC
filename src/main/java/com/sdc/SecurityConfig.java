package com.sdc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true) //bảo mật từng hàm

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(10000000);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/billitems/**")
				// .hasAnyRole("ADMIN","SUBADMIN") //role
				.hasAnyAuthority("ROLE_ADMIN")
				.antMatchers("/**").authenticated()
			    .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.anyRequest().permitAll().and()
         		.csrf().disable()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.failureUrl("/login?err=true")
				.defaultSuccessUrl("/bill/search", true)
				.and().exceptionHandling().accessDeniedPage("/login?err=true")
				.and().logout()
				.logoutUrl("/logout").logoutSuccessUrl("/login")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.permitAll()
				.and().exceptionHandling().accessDeniedPage("/login");
	}
}