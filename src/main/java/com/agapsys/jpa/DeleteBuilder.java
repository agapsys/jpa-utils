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

import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class DeleteBuilder<T> extends AbstractFindBuilder<T> {

	public DeleteBuilder(Class<T> entityClass) {
		super(entityClass);
	}
	
	@Override
	public Class<T> getEntityClass() {
		return super.getEntityClass();
	}
	
	@Override
	public DeleteBuilder or(String field, FindOperator operator, Object... values) {
		return (DeleteBuilder) super.or(field, operator, values);
	}

	@Override
	public DeleteBuilder or(String field, Object... values) {
		return (DeleteBuilder) super.or(field, values);
	}

	
	@Override
	public DeleteBuilder and(String field, FindOperator operator, Object... values) {
		return (DeleteBuilder) super.and(field, operator, values);
	}

	@Override
	public DeleteBuilder and(String field, Object... values) {
		return (DeleteBuilder) super.and(field, values);
	}

	
	public DeleteBuilder where(String field, FindOperator operator, Object... values) {
		return (DeleteBuilder) super.by(field, operator, values);
	}

	public DeleteBuilder where(String field, Object... values) {
		return (DeleteBuilder) super.by(field, values);
	}

	@Override
	protected Object executeQuery(Query q) {
		return q.executeUpdate();
	}

	@Override
	protected String getQueryString() {
		String findText = Pattern.quote(String.format("SELECT %s", getAlias()));
		return super.getQueryString().replaceFirst(findText, "DELETE");
	}
	
	public int delete(EntityManager entityManager) {
		EntityTransaction transaction = entityManager.getTransaction();
		
		if (!transaction.isActive())
			transaction.begin();
		
		return (int) super.select(entityManager);
	}
}
