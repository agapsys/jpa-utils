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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class SelectBuilderTest {
	private static class TestSelectBuilder<T> extends SelectBuilder<T> {

		public TestSelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
			super(distinct, entityClass, alias);
		}

		public TestSelectBuilder(Class<T> entiClass, String alias) {
			super(entiClass, alias);
		}

		@Override
		public String getQueryString() {
			return super.getQueryString();
		}
	}
	
	@Entity
	private static class TestEntity {
		@Id
		@GeneratedValue
		int id;
		
		private String field;
	}
	
	@Entity(name = "NamedTestEntity")
	private static class NamedEntity {
		@Id
		@GeneratedValue
		int id;
		
		private String field;
	}
	
	private TestSelectBuilder<TestEntity> testBuilder;
	
	@Before
	public void before() {
		testBuilder = new TestSelectBuilder(TestEntity.class, "t");
	}
	
	// CONSTRUCTORS ------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testNullClass() {
		new SelectBuilder<TestEntity>(null, "t");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullAlias() {
		new SelectBuilder<TestEntity>(TestEntity.class, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyAlias() {
		new SelectBuilder<TestEntity>(TestEntity.class, null);
	}
	// -------------------------------------------------------------------------
	
	// JOIN --------------------------------------------------------------------
	@Test
	public void validJoinTest() {
		testBuilder.join(JoinType.INNER_JOIN, "field", "f");
		
		before();
		testBuilder.join(JoinType.LEFT_OUTER_JOIN, "field", "f");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullJoinType() {
		testBuilder.join(null, "field", "f");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullJoinField() {
		testBuilder.join(JoinType.INNER_JOIN, null, "f");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyJoinField() {
		testBuilder.join(JoinType.INNER_JOIN, "", "f");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullJoinFieldAlias() {
		testBuilder.join(JoinType.INNER_JOIN, "field", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyJoinFieldAlias() {
		testBuilder.join(JoinType.INNER_JOIN, "field", "");
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateJoin() {
		testBuilder.join(JoinType.INNER_JOIN, "field", "f");
		testBuilder.join(JoinType.INNER_JOIN, "field2", "f2");
	}
	// -------------------------------------------------------------------------
	
	// JOIN FETCH --------------------------------------------------------------
	@Test
	public void validJoinFetchTest() {
		testBuilder.joinFetch("field");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullJoinFetchField() {
		testBuilder.joinFetch(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyJoinFetchField() {
		testBuilder.joinFetch("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateJoinFetch() {
		testBuilder.joinFetch("field");
		testBuilder.joinFetch("field2");
	}
	
	@Test(expected = IllegalStateException.class)
	public void joinAndJoinFetch() {
		testBuilder.join(JoinType.INNER_JOIN, "field", "f");
		testBuilder.joinFetch("field2");
	}
	// -------------------------------------------------------------------------
	
	// WHERE -------------------------------------------------------------------
	@Test
	public void validWhere() {
		testBuilder.where("field IS NOT NULL");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void nullWhere() {
		testBuilder.where(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void emptyWhere() {
		testBuilder.where("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateWhere() {
		testBuilder.where("field IS NOT NULL");
		testBuilder.where("field2 IS NOT NULL");
	}
	// -------------------------------------------------------------------------
	
	// GROUP BY ----------------------------------------------------------------
	@Test
	public void validGroupBy() {
		testBuilder.groupBy("field ASC");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void nullGroupBy() {
		testBuilder.groupBy(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void emptyGroupBy() {
		testBuilder.groupBy("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateGroupBy() {
		testBuilder.groupBy("field ASC");
		testBuilder.groupBy("field2 ASC");
	}
	// -------------------------------------------------------------------------
	
	// OFFSET ------------------------------------------------------------------
	@Test
	public void validOffset() {
		testBuilder.offset(1);
		before();
		testBuilder.offset(0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void invalidOffset() {
		testBuilder.offset(-1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateOffset() {
		testBuilder.offset(1);
		testBuilder.offset(3);
	}
	// -------------------------------------------------------------------------
	
	// MAX RESULTS -------------------------------------------------------------
	@Test
	public void validMaxResults() {
		testBuilder.maxResults(1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void invalidMaxResults() {
		testBuilder.maxResults(0);
	}
	
	@Test(expected = IllegalStateException.class)
	public void duplicateMaxResults() {
		testBuilder.maxResults(1);
		testBuilder.maxResults(3);
	}
	// -------------------------------------------------------------------------
	
	@Test
	public void prepareQueryString() {
		// constructor only...
		TestSelectBuilder<TestEntity> testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
		
		TestSelectBuilder<NamedEntity> testBuilder2 = new TestSelectBuilder(NamedEntity.class, "n");
		assertEquals("SELECT n FROM NamedTestEntity n", testBuilder2.getQueryString());
		
		// inner join...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.join(JoinType.INNER_JOIN, "joinField", "jf");
		assertEquals("SELECT t FROM TestEntity t JOIN joinField jf", testBuilder1.getQueryString());
		
		// left outer join...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.join(JoinType.LEFT_OUTER_JOIN, "joinField", "jf");
		assertEquals("SELECT t FROM TestEntity t LEFT JOIN joinField jf", testBuilder1.getQueryString());
		
		// where...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.where("LOWER(field) IS NOT NULL");
		assertEquals("SELECT t FROM TestEntity t WHERE LOWER(field) IS NOT NULL", testBuilder1.getQueryString());
		
		// group by...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.groupBy("fieldZ");
		assertEquals("SELECT t FROM TestEntity t GROUP BY fieldZ", testBuilder1.getQueryString());
		
		// offset...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.offset(2);
		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
		
		// maxResults...
		testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
		testBuilder1.maxResults(2);
		assertEquals("SELECT t FROM TestEntity t", testBuilder1.getQueryString());
	}
}