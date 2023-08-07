/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jan-20 2:00:52 pm 
 * 
 */
package FYP;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 21011805
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(memberDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//				.antMatchers("/categories/add", "/categories/edit/*", "/categories/save", "/categories/delete/*",
//						 "/items/edit/*", "/items/delete/*",  
//						"/members/edit/*", "/members/delete/*")
//				.hasAnyRole("ADMIN").antMatchers("/").permitAll().antMatchers("/members/add").permitAll()
//				.antMatchers("/members/save").permitAll().antMatchers("/bootstrap/*/*").permitAll()
//				.antMatchers("/images/*/*").permitAll().anyRequest().authenticated().and().formLogin() 
//				.loginPage("/login").permitAll().and().logout().logoutUrl("/logout").permitAll().and()
//				.exceptionHandling().accessDeniedPage("/403");
//	}
	
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/categories/edit/*", "/categories/delete/*",
                         "/items/edit/*", "/items/delete/*",
                         "/members/edit/*", "/members/delete/*", "/adminDashboard")
            .hasAnyRole("ADMIN")
            .antMatchers("/", "/members/add", "/members/save", "/bootstrap/**", "/images/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .logout().logoutUrl("/logout").permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403");
    }
}
