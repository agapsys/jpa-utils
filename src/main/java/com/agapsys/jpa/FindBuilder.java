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
	public List<T> find(EntityManager entityManager) {
		return super.find(entityManager);
	}
}
