package com.quizmania.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * You provide the meat, this class does the boilerplate work.
 * 
 * @param <U>
 *            The type of the entity representing a user. Must be a JPA entity.
 * @param <G>
 *            The type of the entity representing a group. Must be a JPA entity.
 */
public abstract class JpaUserDetailsManager<U, G> extends
		JpaUserDetailsService<U> implements UserDetailsManager, GroupManager {

	/**
	 * Converts a group of type G to a name.
	 */
	protected abstract String convertGroupToName(G group);

	/**
	 * Converts a user of type U to a name.
	 */
	protected abstract String convertUserToName(U user);

	/**
	 * Creates a new JPA entity of type G with the given name and list of
	 * authorities.
	 */
	protected abstract G createGroupEntity(String groupName,
			List<GrantedAuthority> authorities);

	/**
	 * Sets the given group's name to that given.
	 */
	protected abstract void setGroupName(G g, String newName);

	/**
	 * Adds the given user to the given group.
	 */
	protected abstract void addUserToGroup(U u, G g);

	/**
	 * Adds the given user to the given group.
	 */
	protected abstract void removeUserFromGroup(U u, G g);

	/**
	 * Returns a list of authorities granted to the given group.
	 */
	protected abstract List<GrantedAuthority> getGroupGrantedAuthorities(G g);

	/**
	 * Adds the given authority to the given group.
	 */
	protected abstract void addGrantedAuthorityToGroup(
			GrantedAuthority authority, G g);

	/**
	 * Removes the given authority from the given group.
	 */
	protected abstract void removeGrantedAuthorityFromGroup(
			GrantedAuthority authority, G g);

	/**
	 * Creates a new user entity of type U from the given UserDetails.
	 */
	protected abstract U createUserEntity(UserDetails user);

	/**
	 * Updates the given user with the given UserDetails.
	 */
	protected abstract void updateUserEntity(U u, UserDetails user);

	/**
	 * Changes the given user's password from the given old one to the given
	 * new.
	 */
	protected abstract void changeUserPassword(String oldPassword,
			String newPassword, U u);

	protected boolean flushEagerly = true;

	protected String findAllGroupsQuery;

	protected String findUsersInGroupQueryArgumentName = "groupName";

	protected String findUsersInGroupQuery;

	protected String findGroupByNameQueryArgumentName = "groupName";

	protected String findGroupByNameQuery;

	protected JpaUserDetailsManager(EntityManagerFactory emf) {
		super(emf);
	}

	protected JpaUserDetailsManager(EntityManager em) {
		super(em);
	}
	
	@Override
	public List<String> findAllGroups() {
		JpaTemplate t = getJpaTemplate();

		@SuppressWarnings("unchecked")
		List<G> results = t.find(getFindAllGroupsQuery());

		List<String> names = new ArrayList<String>(results.size());
		for (G group : results) {
			names.add(convertGroupToName(group));
		}

		return names;
	}

	@Override
	public List<String> findUsersInGroup(String groupName) {
		JpaTemplate t = getJpaTemplate();

		Map<String, String> params = new HashMap<String, String>();
		params.put(getFindUsersInGroupQueryArgumentName(), groupName);

		@SuppressWarnings("unchecked")
		List<U> results = t.findByNamedParams(getFindUsersInGroupQuery(),
				params);

		List<String> names = new ArrayList<String>(results.size());
		for (U user : results) {
			names.add(convertUserToName(user));
		}

		return names;
	}

	@Override
	public void createGroup(String groupName, List<GrantedAuthority> authorities) {
		G g = createGroupEntity(groupName, authorities);

		JpaTemplate t = getJpaTemplate();

		t.persist(g);
		flushIf(t);
	}

	protected void flushIf(JpaTemplate t) {
		if (getFlushEagerly()) {
			t.flush();
		}
	}

	@Override
	public void deleteGroup(String groupName) {
		JpaTemplate t = getJpaTemplate();

		t.remove(getGroupByName(groupName));
		flushIf(t);
	}

	protected G getGroupByName(String groupName) {
		JpaTemplate t = getJpaTemplate();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(getFindGroupByNameQueryArgumentName(), groupName);

		@SuppressWarnings("unchecked")
		List<G> results = t
				.findByNamedParams(getFindGroupByNameQuery(), params);
		if (results.size() != 1) {
			throw new EntityNotFoundException(groupName);
		}
		G g = (G) results.get(0);
		return g;
	}

	@Override
	public void renameGroup(String oldName, String newName) {
		G g = getGroupByName(oldName);
		setGroupName(g, newName);

		flushIf(getJpaTemplate());
	}

	@Override
	public void addUserToGroup(String username, String group) {
		U u = getUserByUsername(username);
		G g = getGroupByName(group);

		addUserToGroup(u, g);

		flushIf(getJpaTemplate());
	}

	@Override
	public void removeUserFromGroup(String username, String groupName) {
		U u = getUserByUsername(username);
		G g = getGroupByName(groupName);

		removeUserFromGroup(u, g);

		flushIf(getJpaTemplate());
	}

	@Override
	public List<GrantedAuthority> findGroupAuthorities(String groupName) {
		G g = getGroupByName(groupName);
		return getGroupGrantedAuthorities(g);
	}

	@Override
	public void addGroupAuthority(String groupName, GrantedAuthority authority) {
		G g = getGroupByName(groupName);

		addGrantedAuthorityToGroup(authority, g);

		flushIf(getJpaTemplate());
	}

	@Override
	public void removeGroupAuthority(String groupName,
			GrantedAuthority authority) {
		G g = getGroupByName(groupName);

		removeGrantedAuthorityFromGroup(authority, g);

		flushIf(getJpaTemplate());
	}

	@Override
	public void createUser(UserDetails user) {
		U u = createUserEntity(user);

		JpaTemplate t = getJpaTemplate();

		t.persist(u);
		flushIf(t);
	}

	@Override
	public void updateUser(UserDetails user) {
		U u = getUserByUsername(user.getUsername());

		updateUserEntity(u, user);

		flushIf(getJpaTemplate());
	}

	@Override
	public void deleteUser(String username) {
		U u = getUserByUsername(username);

		JpaTemplate t = getJpaTemplate();

		t.remove(u);
		flushIf(t);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		U u = getUserByUsername(username);

		changeUserPassword(oldPassword, newPassword, u);

		flushIf(getJpaTemplate());

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(username, newPassword));
	}

	@Override
	public boolean userExists(String username) {
		try {
			getUserByUsername(username);
			return true;
		} catch (UsernameNotFoundException x) {
			return false;
		}
	}

	public String getFindUsersInGroupQueryArgumentName() {
		return findUsersInGroupQueryArgumentName;
	}

	public void setFindUsersInGroupQueryArgumentName(
			String findUsersInGroupQueryArgumentName) {
		this.findUsersInGroupQueryArgumentName = findUsersInGroupQueryArgumentName;
	}

	public String getFindUsersInGroupQuery() {
		return findUsersInGroupQuery;
	}

	/**
	 * JPQL query string to find all users in a group with a given name
	 * (":groupName" by default). The result of this query must be a
	 * <code>java.util.List&lt;U&gt;</code>.
	 * 
	 * @see #findUsersInGroupQueryArgumentName
	 */
	public void setFindUsersInGroupQuery(String findUsersInGroupQuery) {
		this.findUsersInGroupQuery = findUsersInGroupQuery;
	}

	/**
	 * JPQL query string to find all groups. The results of this query must be a
	 * <code>java.util.List&lt;G&gt;</code>.
	 */
	public String getFindAllGroupsQuery() {
		return findAllGroupsQuery;
	}

	/**
	 * JPQL query string to find all groups. The result of this query must be a
	 * <code>java.util.List&lt;G&gt;</code>.
	 */
	public void setFindAllGroupsQuery(String findAllGroupsQuery) {
		this.findAllGroupsQuery = findAllGroupsQuery;
	}

	public String getFindGroupByNameQueryArgumentName() {
		return findGroupByNameQueryArgumentName;
	}

	public void setFindGroupByNameQueryArgumentName(
			String findGroupByNameQueryArgumentName) {
		this.findGroupByNameQueryArgumentName = findGroupByNameQueryArgumentName;
	}

	public String getFindGroupByNameQuery() {
		return findGroupByNameQuery;
	}

	public void setFindGroupByNameQuery(String findGroupByNameQuery) {
		this.findGroupByNameQuery = findGroupByNameQuery;
	}

	public boolean getFlushEagerly() {
		return flushEagerly;
	}

	public void setFlushEagerly(boolean flushEagerly) {
		this.flushEagerly = flushEagerly;
	}
}
