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

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

public class SelectBuilder<T extends EntityObject> extends AbstractQueryBuilder<T> {
	
	public SelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
		super(distinct, entityClass, alias);
	}

	public SelectBuilder(Class<T> entityClass, String alias) {
		super(entityClass, alias);
	}

	public SelectBuilder(String selectClause, Class<T> entityClass, String alias) {
		super(selectClause, entityClass, alias);
	}

	public SelectBuilder(String selectClause, Class<T> entityClass) {
		super(selectClause, entityClass);
	}
	
	
	@Override
	public SelectBuilder<T> join(JoinType joinType, String joinField, String joinFieldAlias) {
		return (SelectBuilder<T>) super.join(joinType, joinField, joinFieldAlias);
	}
	
	@Override
	public SelectBuilder<T> joinFetch(String joinField) {
		return (SelectBuilder<T>) super.joinFetch(joinField); 
	}
	
	@Override
	public SelectBuilder<T> where(String whereClause) {
		return (SelectBuilder<T>) super.where(whereClause);
	}

	@Override
	public SelectBuilder<T> groupBy(String groupBy) {
		return (SelectBuilder<T>) super.groupBy(groupBy);
	}
	
	@Override
	public SelectBuilder<T> offset(Integer offset) {
		return (SelectBuilder<T>) super.offset(offset);
	}

	@Override
	public SelectBuilder<T> orderBy(String ordering) {
		return (SelectBuilder<T>) super.orderBy(ordering);
	}

	@Override
	public SelectBuilder<T> maxResults(Integer maxResults) {
		return (SelectBuilder<T>) super.maxResults(maxResults);
	}
	
	@Override
	public SelectBuilder<T> value(String key, Object value) {
		return (SelectBuilder<T>) super.value(key, value);
	}

	@Override
	public SelectBuilder<T> values(Map<String, Object> values) {
		return (SelectBuilder<T>) super.values(values);
	}
	
	@Override
	public List<T> select(EntityManager entityManager) {
		return (List<T>) super.select(entityManager);
	}
	
	public T selectFirst(EntityManager entityManager) {
		Integer previousMaxResults = getMaxResults();
		
		setLocked(false); // <-- allows attribute change
		maxResults(1);		
		
		List results = (List) select(entityManager);

		maxResults(previousMaxResults);
		setLocked(true); // <-- restores locking state
		
		setLocked(true);
		
		if (results.isEmpty())
			return null;
		else
			return (T) results.get(0);
	}
	// =========================================================================
}
