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
import javax.persistence.EntityManager;

public class SelectBuilder<T> extends AbstractSelectBuilder<T> {

	public SelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
		super(distinct, entityClass, alias);
	}

	public SelectBuilder(Class<T> entiClass, String alias) {
		super(entiClass, alias);
	}

	@Override
	public SelectBuilder join(JoinType joinType, String joinField, String joinFieldAlias) {
		return (SelectBuilder) super.join(joinType, joinField, joinFieldAlias);
	}
	
	@Override
	public SelectBuilder joinFetch(String joinField) {
		return (SelectBuilder) super.joinFetch(joinField); 
	}
	
	@Override
	public SelectBuilder where(String whereClause) {
		return (SelectBuilder) super.where(whereClause);
	}

	@Override
	public SelectBuilder groupBy(String groupBy) {
		return (SelectBuilder) super.groupBy(groupBy);
	}
	
	@Override
	public SelectBuilder offset(int offset) {
		return (SelectBuilder) super.offset(offset);
	}

	@Override
	public SelectBuilder orderBy(String ordering) {
		return (SelectBuilder) super.orderBy(ordering);
	}

	@Override
	public SelectBuilder maxResults(int maxResults) {
		return (SelectBuilder) super.maxResults(maxResults);
	}
	
	@Override
	public SelectBuilder value(String key, Object value) {
		return (SelectBuilder) super.value(key, value);
	}
	
	@Override
	public List<T> select(EntityManager entityManager) {
		return super.select(entityManager);
	}
}
