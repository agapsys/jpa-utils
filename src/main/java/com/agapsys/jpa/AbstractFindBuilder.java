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
import javax.persistence.EntityManager;

public abstract class AbstractFindBuilder<T extends EntityObject> extends AbstractQueryBuilder<T> {
	// CLASS SCOPE =============================================================
	private static final String DEFAULT_PARAM_PREFIX = "f";
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private WhereClauseBuilder whereBuilder = null;
	
	public AbstractFindBuilder(Class<T> entityClass) {
		super(entityClass);
	}
	
	public AbstractFindBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass);
	}
	
	
	private String _(String field) {
		if (field == null || field.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty field");
		
		return String.format("%s.%s", getAlias(), field);
	}
	
	private AbstractFindBuilder andOrBy(boolean byClause, String field, FindOperator operator, Object...values) {		
		if (whereBuilder == null && !byClause)
			throw new IllegalStateException("AND cannot be called yet");
				
		field = _(field);
		
		if (whereBuilder == null) {
			whereBuilder = new WhereClauseBuilder(DEFAULT_PARAM_PREFIX, field, operator, values);
		} else {
			whereBuilder.and(field, operator, values);
		}
		
		return this;
	}
	
	
	protected AbstractFindBuilder by(String field, Object...values) {
		return andOrBy(true, field, FindOperator.EQUALS, values);
	}
	
	protected AbstractFindBuilder by(String field, FindOperator operator, Object...values) {
		return andOrBy(true, field, operator, values);
	}
	
	
	protected AbstractFindBuilder and(String field, Object...values) {
		return andOrBy(false, field, FindOperator.EQUALS, values);
	}	
	
	protected AbstractFindBuilder and(String field, FindOperator operator, Object...values) {
		return andOrBy(false, field, operator, values);
	}
	
	
	protected AbstractFindBuilder or(String field, Object...values) {
		return or(field, FindOperator.EQUALS, values);
	}
	
	protected AbstractFindBuilder or(String field, FindOperator operator, Object...values) {
		
		if (whereBuilder == null)
			throw new IllegalStateException("OR cannot be called yet");
		
		field = _(field);
		
		whereBuilder.or(field, operator, values);
		return this;
	}
	
	
	@Override
	protected AbstractFindBuilder offset(Integer offset) {
		return (AbstractFindBuilder) super.offset(offset);
	}

	@Override
	protected AbstractFindBuilder maxResults(Integer maxResults) {
		return (AbstractFindBuilder) super.maxResults(maxResults);
	}

	@Override
	protected AbstractFindBuilder orderBy(String ordering) {
		return (AbstractFindBuilder) super.orderBy(ordering);
	}
	
	@Override
	protected String getWhereClause() {
		if (whereBuilder == null)
			return null;
		
		return whereBuilder.build();
	}

	@Override
	protected Map<String, Object> getValues() {
		if (whereBuilder == null)
			return new LinkedHashMap<>();
		
		return whereBuilder.getValues();
	}

	protected Object find(EntityManager entityManager) {
		return super.select(entityManager);
	}
	
	protected Object findFirst(EntityManager entityManager) {
		Integer previousMaxResults = getMaxResults();
		
		setLocked(false); // <-- allows attribute change
		maxResults(1);		
		
		List<T> results = (List<T>) find(entityManager);

		maxResults(previousMaxResults);
		setLocked(true); // <-- restores locking state
		
		setLocked(true);
		
		if (results.isEmpty())
			return null;
		else
			return results.get(0);
	}
	// =========================================================================
}
