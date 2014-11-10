package com.quizmania.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * You provide the meat, this class does the boilerplate work.
 * @param <U> The type of the entity representing a user.
 */
public abstract class JpaUserDetailsService<U> extends JpaDaoSupport implements UserDetailsService {

	public static String DEFAULT_FIND_USER_BY_USERNAME_QUERY_ARGUMENT = "username";
	
	protected abstract UserDetails convertToUserDetails(U user);

	protected final Log logger = LogFactory.getLog(getClass());

	protected String findUserByUsernameQuery;

	protected String findUserByUsernameQueryArgumentName = DEFAULT_FIND_USER_BY_USERNAME_QUERY_ARGUMENT;

	protected JpaUserDetailsService(EntityManager em) {
		setEntityManager(em);
	}
	
	protected JpaUserDetailsService(EntityManagerFactory emf) {
		setEntityManagerFactory(emf);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		U u = getUserByUsername(username);
		return convertToUserDetails(u);
	}

	public U getUserByUsername(String username) {
		JpaTemplate t = getJpaTemplate();
		
		Map<String, String> params = new HashMap<String,String>();
		params.put(getFindUserByUsernameQueryArgumentName(), username);
		
		@SuppressWarnings("unchecked")
		List<U> results = t.findByNamedParams(getFindUserByUsernameQuery(), params);
		
		if (results.size() != 1) {
			throw new UsernameNotFoundException(username);
		}
		
		return results.get(0);
	}
	
	public String getFindUserByUsernameQuery() {
		return findUserByUsernameQuery;
	}

	public void setFindUserByUsernameQuery(String userByUsernameQuery) {
		if (!StringUtils.hasText(userByUsernameQuery)) {
			throw new IllegalArgumentException("no userByUsernameQuery given");
		}
		if (logger.isWarnEnabled() && !userByUsernameQuery.contains(":" + getFindUserByUsernameQueryArgumentName())) {
			logger.warn(String.format("given query [%s] does not contain username argument with name [%s]", userByUsernameQuery, getFindUserByUsernameQueryArgumentName()));
		}
		this.findUserByUsernameQuery = userByUsernameQuery;
	}

	public String getFindUserByUsernameQueryArgumentName() {
		return findUserByUsernameQueryArgumentName;
	}

	public void setFindUserByUsernameQueryArgumentName(
			String userByUsernameQueryArgumentName) {
		if (!StringUtils.hasText(userByUsernameQueryArgumentName)) {
			throw new IllegalArgumentException("no userByUsernameQueryArgumentName given");
		}
		if (logger.isWarnEnabled() && !getFindUserByUsernameQuery().contains(":" + userByUsernameQueryArgumentName)) {
			logger.warn(String.format("given query argument [%s] is not contained by query [%s]", userByUsernameQueryArgumentName, getFindUserByUsernameQuery()));
		}
		this.findUserByUsernameQueryArgumentName = userByUsernameQueryArgumentName;
	}
}
