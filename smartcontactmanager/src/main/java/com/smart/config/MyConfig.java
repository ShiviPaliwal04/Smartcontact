//package com.smart.config;
//
//import java.beans.Customizer;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class MyConfig{
//@Bean
//public UserDetailsService getUserDetailService() {
//	return new UserDetailsServiceImpl();
//			}
//@Bean
//public PasswordEncoder passwordEncoder() {
//	return new BCryptPasswordEncoder();
//			}
//@Bean
//public DaoAuthenticationProvider authenticationProvider() {
//DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
//daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//return daoAuthenticationProvider;
//}
//
//
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	http
//     .authorizeHttpRequests((authz) -> authz
//         .requestMatchers("/admin/**").hasRole("ADMIN")
//         .requestMatchers("/user/**").hasRole("USER")
//         .anyRequest().permitAll()
//     )
//     
////     .formLogin(form -> form
////             // Use the default login page
////             .defaultSuccessUrl("/", true) // Redirect to homepage after successful login
////             .permitAll() // Allow access to the default login page
////         )
//     .csrf(e -> e.disable())
//     .formLogin(form -> form
//             .loginPage("/signin") // Custom login page URL
//             .defaultSuccessUrl("/")// Redirect after successful login
//             .permitAll() 
//            
//             // Allow everyone to access the login page
//         )
//    
//     .logout(logout -> logout
//             .logoutUrl("/logout")
//             .logoutSuccessUrl("/signin?logout")
//             .invalidateHttpSession(true)
//             .deleteCookies("JSESSIONID")
//         )
//     .sessionManagement((session) -> session
//             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//             
//         )
//     
//     ;
//     
//
////         .logout(logout -> logout
////             .logoutUrl("/logout") // URL for logout
////             .logoutSuccessUrl("/signin?logout") // Redirect after logout
////             .invalidateHttpSession(true) // Invalidate session on logout
////             .deleteCookies("JSESSIONID") // Delete cookies on logout
////         );
////     .formLogin().loginPage("/signin")
////     .logout(logout -> logout
////             .logoutUrl("/logout")
////             .logoutSuccessUrl("/signin?logout")  // Redirect to login page after logout
////             .invalidateHttpSession(true)         // Invalidate session
////             .deleteCookies("JSESSIONID")          // Delete session cookie
////         );
//	  // Ensure this maps to your custom login page
//     // Matches form field name for password
//    // Allow access to the login page
//     
//	 return http.build();
//	 }
//
////public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////	 return http.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/**"))
////             .permitAll())
////         .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/customers/**"))
////             .authenticated().and()
////         .formLoginCustomizer()
////         .build()
////        
////        ); 
//// }
//	// http
//// .authorizeHttpRequests((requests) -> requests
////			.requestMatchers("/", "/**").permitAll()
////			.requestMatchers("/user/**").authenticated()
////		)
////		.formLogin((form) -> form
////			.loginPage("/login"));
//			//		)    .authorizeHttpRequests((authz) -> authz
////	                .requestMatchers("/admin/**").hasRole("ADMIN")
////	                .requestMatchers("/user/**").hasRole("USER")
////	                .anyRequest().authenticated())
////	            
////	            .formLogin(withDefaults());
//	       // return http.build();
//	    }
//
////	.authorizeHttpRequests(requests -> requests
////		.requestMatchers("/user/**").authenticated()
////        .anyRequest().permitAll()
////	)
////			.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/**"))
////	                .permitAll())	
////	.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/user/**"))
////            .authenticated())
////	.formLogin(form -> form
////            .loginPage("/login") // Use the default Spring Security login page
////            .permitAll()
////        )     
//	   // Allow everyone to access the login page
//       
//		
////	.csrf(e->e.disable())
//	//.build();
//        
//
//
//

package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Create a request cache to store the URL the user originally requested
        RequestCache requestCache = new HttpSessionRequestCache();

        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable()) // CSRF protection disabled; enable if needed
            .formLogin(form -> form
                .loginPage("/signin")
                .permitAll()
                .defaultSuccessUrl("/user/index", true) // Redirect to home page after successful login
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication)
//                            throws IOException, ServletException {
//                        // Retrieve the saved request URL if any
//                        String redirectUrl = requestCache.getRequest(request, response).getRedirectUrl();
//                        // Redirect to the saved request URL or default to the home page
//                        response.sendRedirect(redirectUrl != null ? redirectUrl : "/");
//                    }
//
//				
//                })
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/signin?logout")// Redirect after logout
                .invalidateHttpSession(true) // Invalidate session
                .deleteCookies("JSESSIONID") // Delete session cookies
                .clearAuthentication(true) // Clear authentication information
            )
            .requestCache(requestCacheConfigurer -> requestCacheConfigurer
                .requestCache(requestCache) // Set the custom request cache
            );

        return http.build();
    }
}










