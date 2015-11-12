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

import com.agapsys.Utils;
import com.agapsys.jpa.entity.NamedEntity;
import com.agapsys.jpa.entity.TestEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CountBuilderIntegrationTest {
	// CLASS SCOPE =============================================================
	private static final int ROWS = 100;
	
	@BeforeClass
	public static void beforeClass() {
		Utils.printCurrenTest();
		
		EntityManager em = PersistenceUnit.getEntityManager();
		
		EntityTransaction transaction =  em.getTransaction();
		transaction.begin();
		
		for (int i = 0; i < ROWS; i++) {
			TestEntity testEntity = new TestEntity();
			testEntity.setField(String.format("test_%d", i+1));
			em.persist(testEntity);
			
			NamedEntity namedEntity = new NamedEntity();
			namedEntity.setTestEntity(testEntity);
			em.persist(namedEntity);
				
			System.out.println(namedEntity);
		}
		
		transaction.commit();
		em.close();
	}
	
	@AfterClass
	public static void afterClass() {
		PersistenceUnit.close();
	}
	// =========================================================================
	private EntityManager em;
	
	@Before
	public void before() {
		em = PersistenceUnit.getEntityManager();
	}
	
	@After
	public void after() {
		em.close();
	}
	
	// INSTANCE SCOPE ==========================================================
	@Test
	public void simpleCount() {
		Utils.printCurrenTest();
		
		long count = new CountBuilder(TestEntity.class).count(em);
		
		Assert.assertEquals(ROWS, count);
	}
	
	@Test
	public void completeTest() {
		Utils.printCurrenTest();
		
		long count = new CountBuilder(TestEntity.class).by("id", FindOperator.BETWEEN, new Range(60, 80)).or("id", FindOperator.BETWEEN, new Range(20, 40)).count(em);
		
		Assert.assertEquals(42, count);
	}
	// =========================================================================
}
