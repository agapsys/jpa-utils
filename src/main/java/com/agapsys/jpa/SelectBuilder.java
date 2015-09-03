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
	public AbstractSelectBuilder join(JoinType joinType, String joinField, String joinFieldAlias) {
		return super.join(joinType, joinField, joinFieldAlias); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public AbstractSelectBuilder joinFetch(String joinField) {
		return super.joinFetch(joinField); 
	}
	
	@Override
	public AbstractSelectBuilder where(String whereClause) {
		return super.where(whereClause);
	}

	@Override
	public AbstractSelectBuilder groupBy(String groupBy) {
		return super.groupBy(groupBy); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public AbstractSelectBuilder offset(int offset) {
		return super.offset(offset);
	}

	@Override
	public AbstractSelectBuilder orderBy(String ordering) {
		return super.orderBy(ordering);
	}

	@Override
	public AbstractSelectBuilder maxResults(int maxResults) {
		return super.maxResults(maxResults);
	}
	
	@Override
	public AbstractSelectBuilder value(String key, Object value) {
		return super.value(key, value);
	}
	
	@Override
	public List<T> select(EntityManager entityManager) {
		return super.select(entityManager);
	}
}
