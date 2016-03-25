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

import com.agapsys.jpa.WhereClauseBuilder.QueryParameter;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CountBuilder<T extends EntityObject> extends AbstractFindBuilder<T> {
	// STATIC SCOPE ============================================================
	public static <T extends EntityObject> CountBuilder<T> forClass(Class<T> entityClass) {
		return new CountBuilder<T>(entityClass);
	}
	
	public static <T extends EntityObject> CountBuilder<T> forClass(Class<T> entityClass, String alias) {
		return new CountBuilder<T>(entityClass, alias);
	}
	
	public static <T extends EntityObject> CountBuilder<T> forClass(boolean distinct, Class<T> entityClass) {
		return new CountBuilder<T>(distinct, entityClass);
	}
	
	public static <T extends EntityObject> CountBuilder<T> forClass(boolean distinct, Class<T> entityClass, String alias) {
		return new CountBuilder<T>(distinct, entityClass, alias);
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	protected CountBuilder(Class<T> entityClass) {
		super(entityClass);
	}

	protected CountBuilder(Class<T> entityClass, String alias) {
		super(entityClass, alias);
	}
	
	protected CountBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass);
	}
	
	protected CountBuilder(boolean distinct, Class<T> entityClass, String alias) {
		super(distinct, entityClass, alias);
	}
	
	
	@Override
	public CountBuilder by(String field, FindOperator operator, Object... values) {
		return (CountBuilder) super.by(field, operator, values);
	}

	@Override
	public CountBuilder by(String field, Object... values) {
		return (CountBuilder) super.by(field, values);
	}
	
	@Override
	public CountBuilder by(String literal, QueryParameter...parameters) {
		return (CountBuilder) super.by(literal, parameters);
	}
	
	
	@Override
	public CountBuilder or(String field, FindOperator operator, Object... values) {
		return (CountBuilder) super.or(field, operator, values);
	}

	@Override
	public CountBuilder or(String field, Object... values) {
		return (CountBuilder) super.or(field, values);
	}

	@Override
	public CountBuilder or(String literal, QueryParameter...parameters) {
		return (CountBuilder) super.or(literal, parameters);
	}
	
	
	@Override
	public CountBuilder and(String field, FindOperator operator, Object... values) {
		return (CountBuilder) super.and(field, operator, values);
	}

	@Override
	public CountBuilder and(String field, Object... values) {
		return (CountBuilder) super.and(field, values);
	}

	@Override
	public CountBuilder and(String literal, QueryParameter...parameters) {
		return (CountBuilder) super.and(literal, parameters);
	}
	
	
	@Override
	public CountBuilder beginAndGroup() {
		return (CountBuilder) super.beginAndGroup();
	}
	
	@Override
	public CountBuilder beginOrGroup() {
		return (CountBuilder) super.beginOrGroup();
	}
	
	@Override
	public CountBuilder closeGroup() {
		return (CountBuilder) super.closeGroup();
	}
	
	
	@Override
	protected String getSelectClause() {
		return "COUNT(1)";
	}
		
	public long count(EntityManager entityManager) {
		Query query = prepareQuery(entityManager);
		
		for (Map.Entry<String, Object> entry : getValues().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		if (getOffset() != null)
			query.setFirstResult(getOffset());
		
		return (long) query.getSingleResult();
	}
	// =========================================================================
}
