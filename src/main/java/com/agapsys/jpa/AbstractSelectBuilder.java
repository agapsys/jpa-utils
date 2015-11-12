/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agapsys.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractSelectBuilder<T> {
	private final Class<T> entityClass;
	private final boolean  distinct;
	private final String   alias;
	private final String   entityName;

	private final Map<String, Object> values = new LinkedHashMap<>();
		
	private JoinType joinType       = null;
	private String   joinField      = null;
	private String   joinFieldAlias = null;
	private String   where          = null;
	private String   groupBy        = null;
	private String   orderBy        = null;
	private Integer  offset         = null;
	private Integer  maxResults     = null;
	
	private boolean locked = true;
	
	private AbstractSelectBuilder(boolean distinct, Class<T> entityClass, String alias, boolean ignoreAlias) {
		this.distinct = distinct;
		
		if (entityClass == null)
			throw new IllegalArgumentException("Null entityClass");
		
		if (!ignoreAlias) {
			if (alias == null || alias.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty alias");

			alias = alias.trim();

			if (alias.contains(" "))
				throw new IllegalArgumentException("alias cannot contain whitspaces");
		} else {
			alias = entityClass.getSimpleName().substring(0, 1).toLowerCase();
		}
		
		this.alias = alias;
		
		Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
		if (entityAnnotation == null)
			throw new IllegalArgumentException("invalid entity class: " + entityClass.getName());

		this.entityClass = entityClass;
		String en = entityAnnotation.name();
		if (en.isEmpty())
			this.entityName = entityClass.getSimpleName();
		else
			entityName = en;
	}
	
	public AbstractSelectBuilder(Class<T> entityClass) {
		this(false, entityClass, null, true);
	}
	
	public AbstractSelectBuilder(boolean distinct, Class<T> entityClass) {
		this(distinct, entityClass, null, true);
	}
	
	public AbstractSelectBuilder(Class<T> entityClass, String alias) {
		this(false, entityClass, alias, false);
	}
	
	public AbstractSelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
		this(distinct, entityClass, alias, false);
	}
	
	// Getters -----------------------------------------------------------------
	protected Class<T> getEntityClass() {
		return entityClass;
	}
	
	protected boolean isDistinct() {
		return distinct;
	}

	protected String getAlias() {
		return alias;
	}

	protected String getEntityName() {
		return entityName;
	}

	protected Map<String, Object> getValues() {
		return values;
	}
	
	protected String getSelectClause() {
		return String.format("%s%s", (isDistinct() ? "DISTINCT " : ""), getAlias());
	}
	
	protected JoinType getJoinType() {
		return joinType;
	}

	protected String getJoinField() {
		return joinField;
	}

	protected String getJoinFieldAlias() {
		return joinFieldAlias;
	}

	protected String getWhereClause() {
		return where;
	}
	
	protected String getGroupByClause() {
		return groupBy;
	}

	protected String getOrderByClause() {
		return orderBy;
	}

	protected Integer getOffset() {
		return offset;
	}

	protected Integer getMaxResults() {
		return maxResults;
	}
	// -------------------------------------------------------------------------	
	
	// Builder methods ---------------------------------------------------------
	protected void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	protected boolean isLocked() {
		return locked;
	}
	
	protected AbstractSelectBuilder join(JoinType joinType, String joinField, String joinFieldAlias) {
		if (this.joinField != null && isLocked())
			throw new IllegalStateException("Join field is already set");
		
		if (joinType == null && isLocked())
			throw new IllegalArgumentException("Null join type");
		
		if ((joinField == null || joinField.trim().isEmpty()) && isLocked())
			throw new IllegalArgumentException("Null/Empty join field");
		
		if ((joinFieldAlias == null || joinFieldAlias.trim().isEmpty()) && isLocked()) {
			throw new IllegalArgumentException("Null/Empty join field alias");
		}
		
		if (joinField != null)
			joinField = joinField.trim();
		
		if (joinFieldAlias != null)
			joinFieldAlias = joinFieldAlias.trim();
		
		this.joinField = joinField;
		this.joinFieldAlias = joinFieldAlias;
		this.joinType = joinType;
		
		return this;
	}
	
	protected AbstractSelectBuilder joinFetch(String joinField) {
		if (this.joinField != null && isLocked())
			throw new IllegalStateException("Join field is already set");
		
		if ((joinField == null || joinField.trim().isEmpty()) && isLocked())
			throw new IllegalArgumentException("Null/Empty join field");
		
		if (joinField != null)
			joinField = joinField.trim();
		
		this.joinField = joinField;
		return this;
	}
	
	protected AbstractSelectBuilder where(String whereClause) {
		if (this.where != null && isLocked())
			throw new IllegalStateException("Where clause is already set");

		if ((whereClause == null || whereClause.trim().isEmpty()) && isLocked())
			throw new IllegalArgumentException("Null/Empty where clause");
		
		if (whereClause != null)
			whereClause = whereClause.trim();
		
		this.where = whereClause;
		return this;
	}
	
	protected AbstractSelectBuilder groupBy(String groupBy) {
		if (this.groupBy != null && isLocked())
			throw new IllegalStateException("Group by clause is already set");
		
		if ((groupBy == null || groupBy.trim().isEmpty()) && isLocked())
			throw new IllegalArgumentException("Null/Empty group by clause");
		
		if (groupBy != null)
			groupBy = groupBy.trim();
		
		this.groupBy = groupBy;
		return this;
	}
	
	protected AbstractSelectBuilder value(String key, Object value) {
		if (values.containsKey(key))
			throw new IllegalArgumentException("Key already set: " + key);

		if (key == null || key.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty key");
		
		values.put(key, value);
		return this;
	}
		
	protected AbstractSelectBuilder orderBy(String ordering) {
		if (this.orderBy != null && isLocked())
			throw new IllegalStateException("ordering is already set");

		if ((ordering == null || ordering.trim().isEmpty()) && isLocked())
			throw new IllegalArgumentException("Null/Empty ordering");

		if (ordering != null)
			ordering = ordering.trim();
		
		this.orderBy = ordering;
		return this;
	}
		
	protected AbstractSelectBuilder offset(Integer offset) {
		if (this.offset != null && isLocked())
			throw new IllegalStateException("Offset is already set");
		
		if (offset != null && offset < 0)
			throw new IllegalArgumentException("Invalid offset: " + offset);

		this.offset = offset;
		return this;
	}
		
	protected AbstractSelectBuilder maxResults(Integer maxResults) {
		if (this.maxResults != null && isLocked())
			throw new IllegalStateException("'maxResults' is already set");
		
		if (maxResults != null && maxResults < 1)
			throw new IllegalArgumentException("Invalid 'maxResults' value: " + maxResults);

		this.maxResults = maxResults;
		return this;
	}
	// -------------------------------------------------------------------------
	protected String getQueryString() {
		StringBuilder sb = new StringBuilder();
		sb.append(
			String.format("SELECT %s FROM %s %s", getSelectClause(), getEntityName(), getAlias()));

		if (getJoinType() != null) {
			sb.append(String.format(" %s %s %s", getJoinType().getSQl(), getJoinField(), getJoinFieldAlias()));
		} else {
			if (getJoinField() != null) {
				sb.append(String.format(" JOIN FETCH %s", getJoinField()));
			}
		}
		
		if (getWhereClause() != null)
			sb.append(String.format(" WHERE %s", getWhereClause()));
		
		if (getGroupByClause() != null)
			sb.append(String.format(" GROUP BY %s", getGroupByClause()));

		if (getOrderByClause() != null)
			sb.append(String.format(" ORDER BY %s", getOrderByClause()));
		
		return sb.toString();
	}
	
	protected Query prepareQuery(EntityManager entityManager)  {
		return entityManager.createQuery(getQueryString());
	}
		
	protected List<T> select(EntityManager entityManager) {
		if (entityManager == null)
			throw new IllegalArgumentException("Null entity manager");
		
		Query query = prepareQuery(entityManager);
		
		for (Map.Entry<String, Object> entry : getValues().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		if (getOffset() != null)
			query.setFirstResult(getOffset());
		
		if (getMaxResults() != null)
			query.setMaxResults(getMaxResults());
		
		return query.getResultList();
	}
}
