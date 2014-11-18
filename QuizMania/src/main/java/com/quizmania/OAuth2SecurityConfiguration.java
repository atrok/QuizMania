package com.quizmania;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.quizmania.auth.ClientAndUserDetailsService;
import com.quizmania.auth.RepositoryUserDetailsService;
import com.quizmania.auth.Role;
import com.quizmania.auth.UserDetailsImpl;
import com.quizmania.client.GameSvcApi;
import com.quizmania.repository.User;
import com.quizmania.repository.UserBuilder;
import com.quizmania.repository.UserRepository;

/**
 *	Configure this web application to use OAuth 2.0.
 *
 * 	The resource server is located at "/video", and can be accessed only by retrieving a token from "/oauth/token"
 *  using the Password Grant Flow as specified by OAuth 2.0.
 *  
 *  Most of this code can be reused in other applications. The key methods that would definitely need to
 *  be changed are:
 *  
 *  ResourceServer.configure(...) - update this method to apply the appropriate 
 *  set of scope requirements on client requests
 *  
 *  OAuth2Config constructor - update this constructor to create a "real" (not hard-coded) UserDetailsService
 *  and ClientDetailsService for authentication. The current implementation should never be used in any
 *  type of production environment as these hard-coded credentials are highly insecure.
 *  
 *  OAuth2SecurityConfiguration.containerCustomizer(...) - update this method to use a real keystore
 *  and certificate signed by a CA. This current version is highly insecure.
 *  
 */

 
 
@Configuration
public class OAuth2SecurityConfiguration {
	



	// This first section of the configuration just makes sure that Spring Security picks
	// up the UserDetailsService that we create below. 
	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
		
		@Autowired
		public PasswordEncoder passwordEncoder;
		
		@Autowired
		public UserDetailsService userDetailsService;
		
		// This anonymous inner class' onAuthenticationSuccess() method is invoked
		// whenever a client successfully logs in. The class just sends back an
		// HTTP 200 OK status code to the client so that they know they logged
		// in correctly. The class does not redirect the client anywhere like the
		// default handler does with a HTTP 302 response. The redirect has been
		// removed to be friendlier to mobile clients and Retrofit.
		private static final AuthenticationSuccessHandler NO_REDIRECT_SUCCESS_HANDLER = new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request,
					HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
				response.setStatus(HttpStatus.SC_OK);
			}
		};
		
		// Normally, the logout success handler redirects the client to the login page. We
		// just want to let the client know that it succcessfully logged out and make the
		// response a bit of JSON so that Retrofit can handle it. The handler sends back
		// a 200 OK response and an empty JSON object.
		private static final LogoutSuccessHandler JSON_LOGOUT_SUCCESS_HANDLER = new LogoutSuccessHandler() {
			@Override
			public void onLogoutSuccess(HttpServletRequest request,
					HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
				response.setStatus(HttpStatus.SC_OK);
				response.setContentType("application/json");
				response.getWriter().write("{}");
			}
		};

		
		@Autowired
		protected void registerAuthentication(
				final AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
		
		}
		
		/**
		 * This method is used to inject access control policies into Spring
		 * security to control what resources / paths / http methods clients have
		 * access to.
		 */
		@Override
		protected void configure(final HttpSecurity http) throws Exception {
			// By default, Spring inserts a token into web pages to prevent
			// cross-site request forgery attacks. 
			// See: http://en.wikipedia.org/wiki/Cross-site_request_forgery
			//
			// Unfortunately, there is no easy way with the default setup to communicate 
			// these CSRF tokens to a mobile client so we disable them.
			// Don't worry, the next iteration of the example will fix this
			// problem.
			http.csrf().disable();
			// We don't want to cache requests during login
			http.requestCache().requestCache(new NullRequestCache());
			
			/*
			// Allow all clients to access the login page and use
			// it to login
			http.formLogin()
				// The default login url on Spring is "j_security_check" ...
			    // which isn't very friendly. We change the login url to
			    // something more reasonable ("/login").
				.loginProcessingUrl(GameSvcApi.TOKEN_PATH)
				// The default login system is designed to redirect you to
				// another URL after you successfully authenticate. For mobile
				// clients, we don't want to be redirected, we just want to tell
				// them that they successfully authenticated and return a session
				// cookie to them. this extra configuration option ensures that the 
				// client isn't redirected anywhere with an HTTP 302 response code.
				.successHandler(NO_REDIRECT_SUCCESS_HANDLER)
				// Allow everyone to access the login URL
				.permitAll();
			
			*/
			// POST requests to /user is user creation requests, so it could be anonymous
			http.authorizeRequests().antMatchers(HttpMethod.POST, "/user")
			.anonymous();
						
			// Make sure that clients can logout too!!
			http.logout()
			    // Change the default logout path to /logout
				//.logoutUrl(VideoSvcApi.LOGOUT_PATH)
				// Make sure that a redirect is not sent to the client
				// on logout
				.logoutSuccessHandler(JSON_LOGOUT_SUCCESS_HANDLER)
				// Allow everyone to access the logout URL
				.permitAll();
			
			// Require clients to login and have an account with the "user" role
			// in order to access /video
			 http.authorizeRequests().antMatchers("/oath/authorize").hasRole("user");
			
			// Require clients to login and have an account with the "user" role
			// in order to send a POST request to /video
			// http.authorizeRequests().antMatchers(HttpMethod.POST, "/video").hasRole("user");
			
			// We force clients to authenticate before accessing ANY URLs 
			// other than the login and lougout that we have configured above.
			//http.authorizeRequests().anyRequest().authenticated();
		}
		
		@Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
		
		@Bean(name="simpleMappingExceptionResolver")
	    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
	        SimpleMappingExceptionResolver r =
	              new SimpleMappingExceptionResolver();

	        Properties mappings = new Properties();
	        mappings.setProperty("DatabaseException", "databaseError");
	        mappings.setProperty("InvalidCreditCardException", "creditCardError");
	        mappings.setProperty("UserException", "userError");

	        r.setExceptionMappings(mappings);  // None by default
	        r.setDefaultErrorView("error");    // No default
	        r.setExceptionAttribute("ex");     // Default is "exception"
	        r.setWarnLogCategory("example.MvcLogger");     // No default
	        return r;
	    }
	}


	/**
	 *	This method is used to configure who is allowed to access which parts of our
	 *	resource server 
	 */
	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends
			ResourceServerConfigurerAdapter {

		// This method configures the OAuth scopes required by clients to access
		// all of the paths in the video service.
		@Override
		public void configure(HttpSecurity http) throws Exception {
			
			http.csrf().disable();
			
			http
			.authorizeRequests()
				.antMatchers("/oauth/token").anonymous();
			
			

			//other request like update/delete should have write access only
			http
			.authorizeRequests()
				.antMatchers(HttpMethod.POST,"/user/*")
				.access("#oauth2.hasScope('write')");
			

			// If you were going to reuse this class in another
			// application, this is one of the key sections that you
			// would want to change
			
			// Require all GET requests to have client "read" scope
			http
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/**")
				.access("#oauth2.hasScope('read')");
			
			// Require all other requests to have "write" scope
			http
			.authorizeRequests()
				.antMatchers("/games")
				.access("#oauth2.hasScope('write')");
		}

	}



	/**
	 * This class is used to configure how our authorization server (the "/oauth/token" endpoint) 
	 * validates client credentials.
	 */
	
	@Configuration
	@EnableAuthorizationServer
	@Order(Ordered.LOWEST_PRECEDENCE - 100)
	protected static class OAuth2Config extends
			AuthorizationServerConfigurerAdapter {

		private static Logger log=Logger.getLogger(OAuth2Config.class);
		
		// Delegate the processing of Authentication requests to the framework
		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;
		

		// A data structure used to store both a ClientDetailsService and a UserDetailsService
		private ClientAndUserDetailsService combinedService_;
		
		//private ClientDetailsService client;
		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private UserDetailsService userDetailsService;
		

		
		/**
		 * 
		 * This constructor is used to setup the clients and users that will be able to login to the
		 * system. This is a VERY insecure setup that is using hard-coded lists of clients / users /
		 * passwords and should never be used for anything other than local testing
		 * on a machine that is not accessible via the Internet. Even if you use
		 * this code for testing, at the bare minimum, you should consider changing the
		 * passwords listed below and updating the VideoSvcClientApiTest.
		 * 
		 * @param auth
		 * @throws Exception
		 */
		
		
		public OAuth2Config() throws Exception {
			
			// If you were going to reuse this class in another
			// application, this is one of the key sections that you
			// would want to change
			
			log.info("Initializing main constructor");
			// Create a service that has the credentials for all our clients
			ClientDetailsService csvc = new InMemoryClientDetailsServiceBuilder()
					// Create a client that has "read" and "write" access to the
			        // video service
					.withClient("mobile").authorizedGrantTypes("password")
					.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
					.scopes("read","write").resourceIds("games","user","scoreboard")
					.and()
					// Create a second client that only has "read" access to the
					// video service
					.withClient("mobileReader").authorizedGrantTypes("password")
					.authorities("ROLE_CLIENT")
					.scopes("read").resourceIds("games","user","scoreboard")
					.accessTokenValiditySeconds(3600).and().build();

			// Create a series of hard-coded users. 
			UserDetailsImpl.Builder user = UserDetailsImpl.getBuilder();
			
			
			UserDetailsService svc = new InMemoryUserDetailsManager(
					Arrays.asList(
							user
								.username("admin")
								.password("none")
								.role(Role.ROLE_ADMIN,Role.ROLE_USER)
								.build(),
							user
								.username("user0")
								.password("pass")
								.role(Role.ROLE_USER)
								.build()));
			
			// Since clients have to use BASIC authentication with the client's id/secret,
			// when sending a request for a password grant, we make each client a user
			// as well. When the BASIC authentication information is pulled from the
			// request, this combined UserDetailsService will authenticate that the
			// client is a valid "user". 
			
			combinedService_ = new ClientAndUserDetailsService(csvc, svc);
		}
	
	

		/**
		 * Return the list of trusted client information to anyone who asks for it.
	 */

		@Bean
		public ClientDetailsService clientDetailsService() throws Exception {
			log.info("Initializing ClientDetailsService");
			return combinedService_;
		}


		/**
		 * Return all of our user information to anyone in the framework who requests it.
		 */
		@Bean
		public UserDetailsService userDetailsService() {
			log.info("Initializing UserDetailsService");
			return new RepositoryUserDetailsService();
		}


		/**
		 * This method tells our AuthorizationServerConfigurerAdapter to use the delegated AuthenticationManager
		 * to process authentication requests.
		 */
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			log.info("Initializing configure(AuthorizationServerEndpointsConfigurer endpoints)");
			
			endpoints.authenticationManager(authenticationManager);
		}

		/**
		 * This method tells the AuthorizationServerConfigurerAdapter to use our self-defined client details service to
		 * authenticate clients with.
		 */

		@Override
		public void configure(ClientDetailsServiceConfigurer clients)
				throws Exception {
			
			log.info("Initializing configure(ClientDetailsServiceConfigurer clients)");
			

			clients.withClientDetails(clientDetailsService());
		}
		

	}
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
	
	@Bean
    public UserDetailsService userDetailsService() {
		return new RepositoryUserDetailsService();
    }
	
	
	

}
		

