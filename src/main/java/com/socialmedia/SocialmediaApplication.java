package com.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.filter.JwtFilter;
import com.service.CustomUserDetailService;

@SpringBootApplication(scanBasePackages="com.controller,com.service,com.globalException")
@EntityScan("com.model")
@EnableJpaRepositories("com.dao")
public class SocialmediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialmediaApplication.class, args);
	}
		@Bean
		@DependsOn("userDetailsService")
		public DaoAuthenticationProvider daoAuthenticationProvider() {
		    CustomUserDetailService service=userDetailsService();
		    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		    provider.setUserDetailsService(service);
		    provider.setPasswordEncoder(passwordEncoder());
		    return provider;
		}
	 
		@Bean
		public CustomUserDetailService userDetailsService() {
			
		
		    return new CustomUserDetailService(); // Implement your own user details service
		}
	 
		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder(); // Use a password encoder of your choice
		}
		
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		    System.out.println("invoked");
			http
		        .csrf().disable() // Disable CSRF protection
		        
		        .authorizeRequests()
		            .requestMatchers(HttpMethod.POST,"/api/login").permitAll()
		            
		                .requestMatchers("/api/user/register").permitAll()
		                .requestMatchers("/api/admin/register").permitAll()

		                .requestMatchers("/api/admin/login").hasAnyRole("ADMIN")
		                
		                .requestMatchers(HttpMethod.GET, "/api/users/all").hasAnyRole("ADMIN")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}").hasAnyRole("ADMIN")
		                .requestMatchers(HttpMethod.GET, "/api/users/search/{username}").hasAnyRole("ADMIN")
		                .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/users/update/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/users/delete/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/posts").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/posts/comments").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/friends").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/friend-requests/pending").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/users/{userId}/friend-requests/send/{friendId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/messages/{otherUserId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/users/{userId}/messages/send/{otherUserId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/posts/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/notifications").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/groups").hasAnyRole("ADMIN", "USER")
		                
		                // Posts endpoints
		                .requestMatchers(HttpMethod.POST, "/api/post").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/post/{postId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/post/update/{postId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/post/delete/{postId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/posts/{postId}/likes/add/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/posts/{postId}/likes/remove/{likeId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/posts/likes").hasAnyRole("ADMIN", "USER")
		                
		                // Friends endpoints
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/friends").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/users/{userId}/friends/{friendId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/friends/{friendId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/friends/{friendshipId}/messages").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/friends/{friendshipId}/messages/send").hasAnyRole("ADMIN", "USER")
		                
		                // Comments endpoints
		                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}/comments").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/comments").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/comments/{commentId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/comments/{commentId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/comments/{commentId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/posts/{postId}/comments").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/posts/{postId}/comments/{commentId}").hasAnyRole("ADMIN", "USER")
		                
		                // Notifications endpoints
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/notifications").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/users/{userId}/notifications/mark-read/{notificationId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/notifications").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/notifications/delete/{notificationId}").hasAnyRole("ADMIN", "USER")
		                
		                // Messages endpoints
		                .requestMatchers(HttpMethod.GET, "/api/messages").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/messages/{messageId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/messages").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/messages/{messageId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/messages/{messageId}").hasAnyRole("ADMIN", "USER")
		                
		                //likes endpoints
		                .requestMatchers(HttpMethod.POST, "/api/posts/{postId}/likes/add/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/posts/{postId}/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/posts/{postId}/likes/remove/{likeId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/posts/users/{userId}/posts/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/likes").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/likes/{likeID}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/likes/posts/{postID}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/likes/users/{userID}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/likes/addlike").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/likes/{likeID}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/likes/post/{postId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/likes/user/{userId}").hasAnyRole("ADMIN", "USER")
		                
		                // Groups endpoints
		                .requestMatchers(HttpMethod.GET, "/api/groups").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/groups/{groupId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/groups").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.PUT, "/api/groups/{groupId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/groups/{groupId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/groups/{groupId}/leave/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/groups/{groupId}/join/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/groups").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/groups/{groupId}/messages").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/groups/{groupId}/messages/send/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/users/{userId}/friends/groups").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/groups/{groupId}/friends").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.GET, "/api/groups/{groupId}/members").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.POST, "/api/groups/{groupId}/members/add/{userId}").hasAnyRole("ADMIN", "USER")
		                .requestMatchers(HttpMethod.DELETE, "/api/groups/{groupId}/members/remove/{userId}").hasAnyRole("ADMIN", "USER")
		                	              
		            .anyRequest().authenticated()
		            .and()
		             .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
		           
		        .sessionManagement()
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		            .and()
		            .sessionManagement().disable()
		            	
		            
		                
		        .authenticationManager(new ProviderManager(daoAuthenticationProvider()));
		        
		
		        
		    return http.build();
		}

	
}
