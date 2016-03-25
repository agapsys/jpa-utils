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
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class DeleteBuilder<T extends EntityObject> extends AbstractFindBuilder<T> {
	// CLASS SCOPE =============================================================
	public static <T extends EntityObject> DeleteBuilder<T> forClass(Class<T> entityClass) {
		return new DeleteBuilder<T>(entityClass);
	}
	
	public static <T extends EntityObject> DeleteBuilder<T> forClass(Class<T> entityClass, String alias) {
		return new DeleteBuilder<T>(entityClass, alias);
	}
	// =========================================================================

	// INSTANCE SCOPE =========================================================
	protected DeleteBuilder(Class<T> entityClass) {
		super(entityClass);
	}
	
	protected DeleteBuilder(Class<T> entityclass, String alias) {
		super(entityclass, alias);
	}
	
	@Override
	public Class<T> getEntityClass() {
		return super.getEntityClass();
	}
	
	
	public DeleteBuilder where(String field, FindOperator operator, Object... values) {
		return (DeleteBuilder) super.by(field, operator, values);
	}

	public DeleteBuilder where(String field, Object... values) {
		return (DeleteBuilder) super.by(field, values);
	}
	
	public DeleteBuilder where(String literal, QueryParameter...parameters) {
		return (DeleteBuilder) super.by(literal, parameters);
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
	public DeleteBuilder or(String literal, QueryParameter...parameters) {
		return (DeleteBuilder) super.or(literal, parameters);
	}
	
	
	@Override
	public DeleteBuilder and(String field, FindOperator operator, Object... values) {
		return (DeleteBuilder) super.and(field, operator, values);
	}

	@Override
	public DeleteBuilder and(String field, Object... values) {
		return (DeleteBuilder) super.and(field, values);
	}

	@Override
	public DeleteBuilder and(String literal, QueryParameter...parameters) {
		return (DeleteBuilder) super.and(literal, parameters);
	}
	
	
	@Override
	public DeleteBuilder beginAndGroup() {
		return (DeleteBuilder) super.beginAndGroup();
	}
	
	@Override
	public DeleteBuilder beginOrGroup() {
		return (DeleteBuilder) super.beginOrGroup();
	}
	
	@Override
	public DeleteBuilder closeGroup() {
		return (DeleteBuilder) super.closeGroup();
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
	// =========================================================================
}
