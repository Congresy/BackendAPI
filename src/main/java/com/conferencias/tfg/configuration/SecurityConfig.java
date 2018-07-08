package com.conferencias.tfg.configuration;

import com.conferencias.tfg.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserAccountService userAccountService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userAccountService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().antMatchers("/actors/all").permitAll().antMatchers("/conference/**").permitAll()
				.antMatchers("/**").permitAll()
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagge‌​r-ui.html",
						"/swagger-resources/configuration/security")
				.permitAll().antMatchers("/webjars/springfox-swagger-ui/**").permitAll().anyRequest().authenticated()
				.and().formLogin()//
				.and().logout().permitAll()
				;

	}

	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring()
				.antMatchers(HttpMethod.OPTIONS,"/**");
	}

//	@Autowired
//	public void configureGlobal(@Lazy AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN").and();
//	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userAccountService).passwordEncoder(passwordEncoder());
	}


//    @Autowired
//    public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(userAccountService);
//	}

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean lfb) {
		return new ValidatingMongoEventListener(lfb);
	}
}
