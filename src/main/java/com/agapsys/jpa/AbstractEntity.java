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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class AbstractEntity implements EntityObject {
	public EntityObject save(EntityManager em) {
		EntityTransaction transaction = em.getTransaction();
		if (!transaction.isActive())
			transaction.begin();
		
		em.persist(this);
		return this;
	}
	
	public void delete(EntityManager em) {
		em.remove(this);
	}

	@Override
	public String toString() {
		Object id = getId();
		return String.format("%s:%s", getClass().getName(), id != null ? id.toString() : "null");
	}
}
