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
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FindBuilderTest {
	private static class TestFindBuilder<T> extends FindBuilder<T> {

		public TestFindBuilder(boolean distinct, Class<T> entityClass) {
			super(distinct, entityClass);
		}

		public TestFindBuilder(Class<T> entiClass) {
			super(entiClass);
		}

		@Override
		public String getQueryString() {
			return super.getQueryString();
		}
	}
		
	private TestFindBuilder<TestEntity> findBuilder;
	
	@Before
	public void before() {
		findBuilder = new TestFindBuilder(TestEntity.class);
	}
	
	// CONSTRUCTORS ------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testNullClass() {
		new TestFindBuilder<TestEntity>(null);
	}
	// -------------------------------------------------------------------------
	
	// SIMPLE BY ---------------------------------------------------------------
	@Test
	public void validSimpleByTest() {
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", "123");
		assertEquals("SELECT t FROM TestEntity t WHERE (field = :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", "123").by("field2", "456");
		assertEquals("SELECT t FROM TestEntity t WHERE (field = :f0) AND (field2 = :f1)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", "123", "456", "789").by("field2", "456");
		assertEquals("SELECT t FROM TestEntity t WHERE (field = :f0 AND field = :f1 AND field = :f2) AND (field2 = :f3)", findBuilder.getQueryString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullFieldSimpleByTest() {
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by(null, "123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyFieldSimpleByTest() {
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("", "123");
	}
	// -------------------------------------------------------------------------
	
	// BY WITH AN OPERATOR------------------------------------------------------
	@Test
	public void InvalidValuesAccordingToByOperator() {
		boolean errorCaught = false;
		
		// LESS THAN -----------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.LESS_THAN);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// LESS THAN EQUALS ----------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.LESS_THAN_EQUALS);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// GREATER THAN --------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.GREATER_THAN);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// GREATER THAN EQUALS -------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.GREATER_THAN_EQUALS);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// EQUALS --------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.EQUALS);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// ILIKE ---------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.ILIKE);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// LIKE ----------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.LIKE);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// NOT_EQUAL -----------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.NOT_EQUAL);
		} catch (IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------		
		
		// IS_NOT_NULL ---------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.IS_NOT_NULL, 2);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("does not require a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// IS_NULL -------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.IS_NULL, 2);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("does not require a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// NOT -----------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.NOT, 2);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("does not require a value"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// BETWEEN -------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.BETWEEN, 2);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("is not a range"));
		}
		assertTrue(errorCaught);
		
		
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.BETWEEN);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a range"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
		
		// NOT_BETWEEN -------------------------------------------------------------
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.NOT_BETWEEN, 2);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("is not a range"));
		}
		assertTrue(errorCaught);
		
		errorCaught = false;
		findBuilder = new TestFindBuilder(TestEntity.class);
		try {
			findBuilder.by("field",FindOperator.NOT_BETWEEN);
		} catch(IllegalArgumentException e) {
			errorCaught = true;
			assertTrue(e.getMessage().contains("requires a range"));
		}
		assertTrue(errorCaught);
		// ---------------------------------------------------------------------
	}
	
	@Test
	public void validByWithOperator() {
		// LESS THAN -----------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field < :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field < :f0 AND field < :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// LESS THAN EQUALS ----------------------------------------------------		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN_EQUALS, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field <= :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN_EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field <= :f0 AND field <= :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// GREATER THAN --------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field > :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field > :f0 AND field > :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// GREATER THAN EQUALS -------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN_EQUALS, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field >= :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN_EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field >= :f0 AND field >= :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// EQUALS --------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.EQUALS, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field = :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field = :f0 AND field = :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// ILIKE ---------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.ILIKE, "%ABc%");
		assertEquals("SELECT t FROM TestEntity t WHERE (LOWER(field) LIKE LOWER(:f0))", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.ILIKE, "%ABc%", "%DeF%");
		assertEquals("SELECT t FROM TestEntity t WHERE (LOWER(field) LIKE LOWER(:f0) AND LOWER(field) LIKE LOWER(:f1))", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// LIKE ---------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LIKE, "%ABc%");
		assertEquals("SELECT t FROM TestEntity t WHERE (field LIKE :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LIKE, "%ABc%", "%DeF%");
		assertEquals("SELECT t FROM TestEntity t WHERE (field LIKE :f0 AND field LIKE :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// NOT_EQUAL -----------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.NOT_EQUAL, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (field <> :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.NOT_EQUAL, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (field <> :f0 AND field <> :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// IS_NOT_NULL ---------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.IS_NOT_NULL);
		assertEquals("SELECT t FROM TestEntity t WHERE (field IS NOT NULL)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// IS_NULL -------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.IS_NULL);
		assertEquals("SELECT t FROM TestEntity t WHERE (field IS NULL)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// NOT -----------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("fieldA",FindOperator.NOT);
		assertEquals("SELECT t FROM TestEntity t WHERE (NOT fieldA)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// BETWEEN -------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("fieldA",FindOperator.BETWEEN, new Range<Integer>(2, 3));
		assertEquals("SELECT t FROM TestEntity t WHERE (fieldA BETWEEN :f0 AND :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
	}
	// -------------------------------------------------------------------------
	
//	@Test(expected = IllegalArgumentException.class)
//	public void nullJoinType() {
//		findBuilder.join(null, "field", "f");
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void nullJoinField() {
//		findBuilder.join(JoinType.INNER_JOIN, null, "f");
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void emptyJoinField() {
//		findBuilder.join(JoinType.INNER_JOIN, "", "f");
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void nullJoinFieldAlias() {
//		findBuilder.join(JoinType.INNER_JOIN, "field", null);
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void emptyJoinFieldAlias() {
//		findBuilder.join(JoinType.INNER_JOIN, "field", "");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateJoin() {
//		findBuilder.join(JoinType.INNER_JOIN, "field", "f");
//		findBuilder.join(JoinType.INNER_JOIN, "field2", "f2");
//	}
//	// -------------------------------------------------------------------------
//	
//	// JOIN FETCH --------------------------------------------------------------
//	@Test
//	public void validJoinFetchTest() {
//		findBuilder.joinFetch("field");
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void nullJoinFetchField() {
//		findBuilder.joinFetch(null);
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void emptyJoinFetchField() {
//		findBuilder.joinFetch("");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateJoinFetch() {
//		findBuilder.joinFetch("field");
//		findBuilder.joinFetch("field2");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void joinAndJoinFetch() {
//		findBuilder.join(JoinType.INNER_JOIN, "field", "f");
//		findBuilder.joinFetch("field2");
//	}
//	// -------------------------------------------------------------------------
//	
//	// WHERE -------------------------------------------------------------------
//	@Test
//	public void validWhere() {
//		findBuilder.where("field IS NOT NULL");
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void nullWhere() {
//		findBuilder.where(null);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void emptyWhere() {
//		findBuilder.where("");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateWhere() {
//		findBuilder.where("field IS NOT NULL");
//		findBuilder.where("field2 IS NOT NULL");
//	}
//	// -------------------------------------------------------------------------
//	
//	// GROUP BY ----------------------------------------------------------------
//	@Test
//	public void validGroupBy() {
//		findBuilder.groupBy("field ASC");
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void nullGroupBy() {
//		findBuilder.groupBy(null);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void emptyGroupBy() {
//		findBuilder.groupBy("");
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateGroupBy() {
//		findBuilder.groupBy("field ASC");
//		findBuilder.groupBy("field2 ASC");
//	}
//	// -------------------------------------------------------------------------
//	
//	// OFFSET ------------------------------------------------------------------
//	@Test
//	public void validOffset() {
//		findBuilder.offset(1);
//		before();
//		findBuilder.offset(0);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void invalidOffset() {
//		findBuilder.offset(-1);
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateOffset() {
//		findBuilder.offset(1);
//		findBuilder.offset(3);
//	}
//	// -------------------------------------------------------------------------
//	
//	// MAX RESULTS -------------------------------------------------------------
//	@Test
//	public void validMaxResults() {
//		findBuilder.maxResults(1);
//	}
//	
//	@Test (expected = IllegalArgumentException.class)
//	public void invalidMaxResults() {
//		findBuilder.maxResults(0);
//	}
//	
//	@Test(expected = IllegalStateException.class)
//	public void duplicateMaxResults() {
//		findBuilder.maxResults(1);
//		findBuilder.maxResults(3);
//	}
//	// -------------------------------------------------------------------------
//	
//	@Test
//	public void prepareQueryString() {
//		// constructor only...
//		TestFindBuilder<TestEntity> testBuilder1 = new TestFindBuilder(TestEntity.class);
//		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
//		
//		TestFindBuilder<NamedEntity> testBuilder2 = new TestFindBuilder(NamedEntity.class);
//		assertEquals("SELECT n FROM NamedTestEntity n", testBuilder2.getQueryString());
//		
//		// inner join...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.join(JoinType.INNER_JOIN, "joinField", "jf");
//		assertEquals("SELECT t FROM TestEntity t JOIN joinField jf", testBuilder1.getQueryString());
//		
//		// left outer join...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.join(JoinType.LEFT_OUTER_JOIN, "joinField", "jf");
//		assertEquals("SELECT t FROM TestEntity t LEFT JOIN joinField jf", testBuilder1.getQueryString());
//		
//		// where...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.where("LOWER(field) IS NOT NULL");
//		assertEquals("SELECT t FROM TestEntity t WHERE LOWER(field) IS NOT NULL", testBuilder1.getQueryString());
//		
//		// group by...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.groupBy("fieldZ");
//		assertEquals("SELECT t FROM TestEntity t GROUP BY fieldZ", testBuilder1.getQueryString());
//		
//		// offset...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.offset(2);
//		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
//		
//		// maxResults...
//		testBuilder1 = new TestFindBuilder(TestEntity.class);
//		testBuilder1.maxResults(2);
//		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
//	}
}
