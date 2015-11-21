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

import com.agapsys.jpa.entity.TestEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeleteBuilderIntegrationTest {
	// CLASS SCOPE =============================================================
	private static final int ROWS = 100;
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private EntityManager em;
	
	@Before
	public void before() {
		em = PersistenceUnit.getEntityManager();

		EntityTransaction transaction =  em.getTransaction();
		transaction.begin();
		
		for (int i = 0; i < ROWS; i++) {
			TestEntity testEntity = new TestEntity();
			testEntity.setField(String.format("test_%d", i+1));
			em.persist(testEntity);
		}
		
		transaction.commit();
	}
	
	@After
	public void after() {
		em.close();
		PersistenceUnit.close();
	}
	
	@Test
	public void simpleDelete() {
		int deleteCount = new DeleteBuilder(TestEntity.class).delete(em);
		em.getTransaction().commit();
		Assert.assertEquals(ROWS, deleteCount);
	}
	
	@Test
	public void completeTest() {
		long deleteCount = new DeleteBuilder(TestEntity.class).where("id", FindOperator.BETWEEN, new Range(60, 80)).or("id", FindOperator.BETWEEN, new Range(20, 40)).delete(em);
		em.getTransaction().commit();
		Assert.assertEquals(42, deleteCount);
	}
	// =========================================================================
}
