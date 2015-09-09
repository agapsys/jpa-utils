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
		
	public AbstractSelectBuilder(Class<T> entityClass, String alias) {
		this(false, entityClass, alias);
	}
		
	public AbstractSelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
		this.distinct = distinct;

		if (alias == null || alias.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty alias");

		alias = alias.trim();

		if (alias.contains(" "))
			throw new IllegalArgumentException("alias cannot contain whitspaces");

		this.alias = alias;

		if (entityClass == null)
			throw new IllegalArgumentException("Null entityClass");
		
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
	
	// Getters -----------------------------------------------------------------
	protected Class<T> getEntityClass() {
		return entityClass;
	}
	
	protected boolean isDistinct() {
		return distinct;
	}

	protected String getAlias() {
		if (isDistinct())
			return "DISTINCT " + alias;
		else
			return alias;
	}

	protected String getEntityName() {
		return entityName;
	}

	protected Map<String, Object> getValues() {
		return values;
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
	protected AbstractSelectBuilder join(JoinType joinType, String joinField, String joinFieldAlias) {
		if (joinType == null)
			throw new IllegalArgumentException("Null join type");
		
		if (joinField == null || joinField.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty join field");
		
		if (joinFieldAlias == null || joinFieldAlias.trim().isEmpty()) {
			throw new IllegalArgumentException("Null/Empty join field alias");
		}
		
		joinField = joinField.trim();
		joinFieldAlias = joinFieldAlias.trim();
		
		if (this.joinField != null)
			throw new IllegalStateException("Join field is already set");
		
		this.joinField = joinField;
		this.joinFieldAlias = joinFieldAlias;
		this.joinType = joinType;
		
		return this;
	}
	
	protected AbstractSelectBuilder joinFetch(String joinField){
		if (joinField == null || joinField.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty join field");
		
		joinField = joinField.trim();
		
		if (this.joinField != null)
			throw new IllegalStateException("Join field is already set");
		
		this.joinField = joinField;
		return this;
	}
	
	protected AbstractSelectBuilder where(String whereClause) {
		if (this.where != null)
			throw new IllegalStateException("Where clause is already set");

		if (whereClause == null || whereClause.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty where clause");
		
		this.where = whereClause.trim();
		return this;
	}
	
	protected AbstractSelectBuilder groupBy(String groupBy) {
		if (groupBy == null || groupBy.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty group by clause");
		
		groupBy = groupBy.trim();
		
		if (this.groupBy != null)
			throw new IllegalStateException("Group by clause is already set");
		
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
		if (this.orderBy != null)
			throw new IllegalStateException("ordering is already set");

		if (ordering == null || ordering.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty ordering");

		this.orderBy = ordering.trim();
		return this;
	}
		
	protected AbstractSelectBuilder offset(int offset) {
		if (offset < 0)
			throw new IllegalArgumentException("Invalid offset: " + offset);

		if (this.offset != null)
			throw new IllegalStateException("Offset is already set");

		this.offset = offset;

		return this;
	}
			
	protected AbstractSelectBuilder maxResults(int maxResults) {
		if (maxResults < 1)
			throw new IllegalArgumentException("Invalid 'maxResults' value: " + maxResults);

		if (this.maxResults != null)
			throw new IllegalStateException("'maxResults' is already set");

		this.maxResults = maxResults;

		return this;
	}
	// -------------------------------------------------------------------------
	
	protected Query prepareQuery(EntityManager entityManager)  {
		StringBuilder sb = new StringBuilder();
		sb.append(
			String.format("SELECT %s FROM %s %s", getAlias(), getEntityName(), getAlias()));

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
		
		return entityManager.createQuery(sb.toString());
	}
		
	protected List<T> select(EntityManager entityManager) {
		if (entityManager == null)
			throw new IllegalArgumentException("Null entity manager");
		
		Query query = prepareQuery(entityManager);
		
		for (Map.Entry<String, Object> entry : getValues().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		query.setFirstResult(getOffset());
		query.setMaxResults(getMaxResults());
		
		return query.getResultList();
	}
}
