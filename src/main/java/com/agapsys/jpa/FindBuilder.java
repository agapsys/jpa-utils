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
import java.util.List;
import javax.persistence.EntityManager;

public class FindBuilder<T extends EntityObject> extends AbstractFindBuilder<T> {
	
	public FindBuilder(Class<T> entityClass) {
		super(entityClass);
	}
	
	public FindBuilder(Class<T> entityClass, String alias) {
		super(entityClass, alias);
	}
	
	public FindBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass);
	}
	
	public FindBuilder(boolean distinct, Class<T> entityClass, String alias) {
		super(distinct, entityClass, alias);
	}
	

	@Override
	public FindBuilder<T> orderBy(String ordering) {
		return (FindBuilder<T>) super.orderBy(ordering);
	}

	@Override
	public FindBuilder<T> maxResults(Integer maxResults) {
		return (FindBuilder<T>) super.maxResults(maxResults);
	}

	@Override
	public FindBuilder<T> offset(Integer offset) {
		return (FindBuilder<T>) super.offset(offset);
	}

	
	@Override
	public FindBuilder<T> by(String field, FindOperator operator, Object... values) {
		return (FindBuilder<T>) super.by(field, operator, values);
	}

	@Override
	public FindBuilder<T> by(String field, Object... values) {
		return (FindBuilder<T>) super.by(field, values);
	}	
	
	@Override
	public FindBuilder<T> by(String literal, QueryParameter...parameters) {
		return (FindBuilder<T>) super.by(literal, parameters);
	}
	
	
	@Override
	public FindBuilder<T> or(String field, FindOperator operator, Object... values) {
		return (FindBuilder<T>) super.or(field, operator, values);
	}

	@Override
	public FindBuilder<T> or(String field, Object... values) {
		return (FindBuilder<T>) super.or(field, values);
	}

	@Override
	public FindBuilder<T> or(String literal, QueryParameter...parameters) {
		return (FindBuilder<T>) super.or(literal, parameters);
	}
	
	
	@Override
	public FindBuilder<T> and(String field, FindOperator operator, Object... values) {
		return (FindBuilder<T>) super.and(field, operator, values);
	}

	@Override
	public FindBuilder<T> and(String field, Object... values) {
		return (FindBuilder<T>) super.and(field, values);
	}

	@Override
	public FindBuilder<T> and(String literal, QueryParameter...parameters) {
		return (FindBuilder<T>) super.and(literal, parameters);
	}
	
	
	@Override
	public FindBuilder<T> beginAndGroup() {
		return (FindBuilder<T>) super.beginAndGroup();
	}
	
	@Override
	public FindBuilder<T> beginOrGroup() {
		return (FindBuilder<T>) super.beginOrGroup();
	}
	
	@Override
	public FindBuilder<T> closeGroup() {
		return (FindBuilder<T>) super.closeGroup();
	}
	
	
	@Override
	public List<T> find(EntityManager entityManager) {
		return (List<T>) super.find(entityManager);
	}
	
	@Override
	public T findFirst(EntityManager entityManager) {
		return (T) super.findFirst(entityManager);
	}
}
