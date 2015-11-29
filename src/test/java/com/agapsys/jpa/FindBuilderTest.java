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
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FindBuilderTest {
	// CLASS SCOPE =============================================================
	private static class TestFindBuilder<T extends EntityObject> extends FindBuilder<T> {

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

		@Override
		public Map<String, Object> getValues() {
			return super.getValues();
		}
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	// CONSTRUCTORS ------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testNullClass() {
		new TestFindBuilder<TestEntity>(null);
	}
	
	@Test
	public void testSimplest() {
		TestFindBuilder findBuilder;
		
		findBuilder = new TestFindBuilder(true, TestEntity.class);
		assertEquals("SELECT DISTINCT t FROM TestEntity t", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(false, TestEntity.class);
		assertEquals("SELECT t FROM TestEntity t", findBuilder.getQueryString());
	}
	// -------------------------------------------------------------------------
	
	// SIMPLE BY ---------------------------------------------------------------
	@Test
	public void SIMPLE_BY_valid() {
		TestFindBuilder findBuilder;
		
		findBuilder = new TestFindBuilder(true, TestEntity.class);
		findBuilder.by("field", "123");
		assertEquals("SELECT DISTINCT t FROM TestEntity t WHERE (t.field = :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", "123").and("field2", "456", "789");
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field = :f0) AND (t.field2 = :f1 AND t.field2 = :f2)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(true, TestEntity.class);
		findBuilder.by("field", "123", "456", "789").or("field2", "456");
		assertEquals("SELECT DISTINCT t FROM TestEntity t WHERE (t.field = :f0 AND t.field = :f1 AND t.field = :f2) OR (t.field2 = :f3)", findBuilder.getQueryString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void SIMPLE_BY_nullField() {
		TestFindBuilder findBuilder;

		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by(null, "123");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void SIMPLE_BY_emptyField() {
		TestFindBuilder findBuilder;

		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("", "123");
	}
	// -------------------------------------------------------------------------
	
	// BY WITH AN OPERATOR------------------------------------------------------
	@Test
	public void BY_InvalidValuesAccordingToOperator() {
		TestFindBuilder findBuilder;

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
	public void BY_validValuesAccordingToOperator() {
		TestFindBuilder findBuilder;

		// LESS THAN -----------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field < :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field < :f0 AND t.field < :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// LESS THAN EQUALS ----------------------------------------------------		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN_EQUALS, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field <= :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LESS_THAN_EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field <= :f0 AND t.field <= :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// GREATER THAN --------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field > :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field > :f0 AND t.field > :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// GREATER THAN EQUALS -------------------------------------------------
		findBuilder = new TestFindBuilder(true, TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN_EQUALS, 2);
		assertEquals("SELECT DISTINCT t FROM TestEntity t WHERE (t.field >= :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(false, TestEntity.class);
		findBuilder.by("field",FindOperator.GREATER_THAN_EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field >= :f0 AND t.field >= :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// EQUALS --------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.EQUALS, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field = :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.EQUALS, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field = :f0 AND t.field = :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// ILIKE ---------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.ILIKE, "%ABc%");
		assertEquals("SELECT t FROM TestEntity t WHERE (LOWER(t.field) LIKE LOWER(:f0))", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.ILIKE, "%ABc%", "%DeF%");
		assertEquals("SELECT t FROM TestEntity t WHERE (LOWER(t.field) LIKE LOWER(:f0) AND LOWER(t.field) LIKE LOWER(:f1))", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// LIKE ---------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LIKE, "%ABc%");
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field LIKE :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.LIKE, "%ABc%", "%DeF%");
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field LIKE :f0 AND t.field LIKE :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// NOT_EQUAL -----------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.NOT_EQUAL, 2);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field <> :f0)", findBuilder.getQueryString());
		
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.NOT_EQUAL, 2, 3);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field <> :f0 AND t.field <> :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// IS_NOT_NULL ---------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.IS_NOT_NULL);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field IS NOT NULL)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// IS_NULL -------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field",FindOperator.IS_NULL);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field IS NULL)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// NOT -----------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("fieldA",FindOperator.NOT);
		assertEquals("SELECT t FROM TestEntity t WHERE (NOT t.fieldA)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
		
		// BETWEEN -------------------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("fieldA",FindOperator.BETWEEN, new Range<>(2, 3));
		assertEquals("SELECT t FROM TestEntity t WHERE (t.fieldA BETWEEN :f0 AND :f1)", findBuilder.getQueryString());
		// ---------------------------------------------------------------------
	}
	// -------------------------------------------------------------------------
	
	// AND ---------------------------------------------------------------------
	@Test(expected = IllegalStateException.class)
	public void AND_first() {
		TestFindBuilder findBuilder;

		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.and("field",FindOperator.LESS_THAN, 2);
	}
	
	@Test
	public void BY_followedBy_AND() {
		TestFindBuilder findBuilder;
		Map<String, Object> expectedParamMap;
		// BY WITH OPERATOR ----------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", FindOperator.LESS_THAN, 2).and("field2", 3, 4, 5);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field < :f0) AND (t.field2 = :f1 AND t.field2 = :f2 AND t.field2 = :f3)", findBuilder.getQueryString());
		
		expectedParamMap = new LinkedHashMap();
		expectedParamMap.put("f0", 2);
		expectedParamMap.put("f1", 3);
		expectedParamMap.put("f2", 4);
		expectedParamMap.put("f3", 5);
		
		assertEquals(expectedParamMap, findBuilder.getValues());
		// ---------------------------------------------------------------------
		
		// AND WITH OPERATOR ---------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", 2).and("field2", FindOperator.GREATER_THAN_EQUALS, 3, 4, 5);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field = :f0) AND (t.field2 >= :f1 AND t.field2 >= :f2 AND t.field2 >= :f3)", findBuilder.getQueryString());
		
		expectedParamMap = new LinkedHashMap();
		expectedParamMap.put("f0", 2);
		expectedParamMap.put("f1", 3);
		expectedParamMap.put("f2", 4);
		expectedParamMap.put("f3", 5);
		
		assertEquals(expectedParamMap, findBuilder.getValues());
		// ---------------------------------------------------------------------
	}
	// -------------------------------------------------------------------------
	
	// OR ----------------------------------------------------------------------
	@Test(expected = IllegalStateException.class)
	public void OR_first() {
		TestFindBuilder findBuilder;

		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.or("field",FindOperator.LESS_THAN, 2);
	}
	
	@Test
	public void BY_followedBy_OR() {
		TestFindBuilder findBuilder;
		Map<String, Object> expectedParamMap;
		
		// BY WITH OPERATOR ----------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", FindOperator.LESS_THAN, 2).or("field2", 3, 4, 5);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field < :f0) OR (t.field2 = :f1 AND t.field2 = :f2 AND t.field2 = :f3)", findBuilder.getQueryString());
		
		expectedParamMap = new LinkedHashMap();
		expectedParamMap.put("f0", 2);
		expectedParamMap.put("f1", 3);
		expectedParamMap.put("f2", 4);
		expectedParamMap.put("f3", 5);
		
		assertEquals(expectedParamMap, findBuilder.getValues());
		// ---------------------------------------------------------------------
		
		// OR WITH OPERATOR ----------------------------------------------------
		findBuilder = new TestFindBuilder(TestEntity.class);
		findBuilder.by("field", 2).or("field2", FindOperator.GREATER_THAN_EQUALS, 3, 4, 5);
		assertEquals("SELECT t FROM TestEntity t WHERE (t.field = :f0) OR (t.field2 >= :f1 AND t.field2 >= :f2 AND t.field2 >= :f3)", findBuilder.getQueryString());
		
		expectedParamMap = new LinkedHashMap();
		expectedParamMap.put("f0", 2);
		expectedParamMap.put("f1", 3);
		expectedParamMap.put("f2", 4);
		expectedParamMap.put("f3", 5);
		
		assertEquals(expectedParamMap, findBuilder.getValues());
		// ---------------------------------------------------------------------
	}
	// =========================================================================
}
