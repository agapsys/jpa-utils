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

public class FindBuilder<T> extends AbstractFindBuilder<T> {
	
	public FindBuilder(Class<T> entityClass) {
		super(false, entityClass);
	}
	
	public FindBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass);
	}

	@Override
	public FindBuilder orderBy(String ordering) {
		return (FindBuilder) super.orderBy(ordering);
	}

	@Override
	public FindBuilder maxResults(Integer maxResults) {
		return (FindBuilder) super.maxResults(maxResults);
	}

	@Override
	public FindBuilder offset(Integer offset) {
		return (FindBuilder) super.offset(offset);
	}

	
	@Override
	public FindBuilder or(String field, FindOperator operator, Object... values) {
		return (FindBuilder) super.or(field, operator, values);
	}

	@Override
	public FindBuilder or(String field, Object... values) {
		return (FindBuilder) super.or(field, values);
	}

	
	@Override
	public FindBuilder and(String field, FindOperator operator, Object... values) {
		return (FindBuilder) super.and(field, operator, values);
	}

	@Override
	public FindBuilder and(String field, Object... values) {
		return (FindBuilder) super.and(field, values);
	}

	
	@Override
	public FindBuilder by(String field, FindOperator operator, Object... values) {
		return (FindBuilder) super.by(field, operator, values);
	}

	@Override
	public FindBuilder by(String field, Object... values) {
		return (FindBuilder) super.by(field, values);
	}	
	
	@Override
	public List<T> find(EntityManager entityManager) {
		return super.find(entityManager);
	}
	
	@Override
	public T findFirst(EntityManager entityManager) {
		return super.findFirst(entityManager);
	}
}
