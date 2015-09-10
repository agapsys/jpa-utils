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

import com.agapsys.jpa.entity.NamedEntity;
import com.agapsys.jpa.entity.TestEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FindBuilderIntegrationTest {
	// CLASS SCOPE =============================================================
	private static PersistenceUnit persistenceUnit;
	private static final int ROWS = 100;
	
	@BeforeClass
	public static void beforeClass() {
		Utils.printCurrenTest();
		
		persistenceUnit = PersistenceUnitFactory.getInstance();
		EntityManager em = persistenceUnit.getEntityManager();
		
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
		persistenceUnit.close();
	}
	// =========================================================================
	private EntityManager em;
	
	@Before
	public void before() {
		em = persistenceUnit.getEntityManager();
	}
	
	@After
	public void after() {
		em.close();
	}
	
	
	// INSTANCE SCOPE ==========================================================
	@Test
	public void simpleFind() {
		Utils.printCurrenTest();
		
		FindBuilder findBuilder = new FindBuilder(TestEntity.class);
		List<TestEntity> list = findBuilder.find(em);
		
		Assert.assertEquals(ROWS, list.size());
		for (TestEntity t : list)
			System.out.println(t);
	}
	
	@Test
	public void completeTest() {
		Utils.printCurrenTest();

		final int testEntityId = 40;
		Assert.assertTrue(testEntityId + 1 < ROWS);
		
		TestEntity testEntity = em.find(TestEntity.class, testEntityId);
		Assert.assertTrue(testEntity != null);
				
		List<NamedEntity> list = new FindBuilder(NamedEntity.class).by("testEntity", testEntity).orderBy("id ASC").find(em);
		Assert.assertEquals(1, list.size());
		
		testEntity = em.find(TestEntity.class, testEntityId + 1);
		Assert.assertTrue(testEntity != null);
		list = new FindBuilder(NamedEntity.class).by("testEntity", testEntity).orderBy("id ASC").find(em);
		Assert.assertEquals(1, list.size());
		
		list = new FindBuilder(NamedEntity.class).by("id", FindOperator.BETWEEN, new Range(60, 80)).or("id", FindOperator.BETWEEN, new Range(20, 40)).find(em);
		for (NamedEntity n : list)
			System.out.println(n);
		
		Assert.assertEquals(42, list.size());
		
	}
	// =========================================================================
}
