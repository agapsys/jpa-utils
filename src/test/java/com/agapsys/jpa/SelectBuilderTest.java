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
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SelectBuilderTest {
    private static class TestSelectBuilder<T extends EntityObject> extends SelectBuilder<T> {

        public TestSelectBuilder(boolean distinct, Class<T> entityClass, String alias) {
            super(distinct, entityClass, alias);
        }

        public TestSelectBuilder(Class<T> entiClass, String alias) {
            super(entiClass, alias);
        }

        public TestSelectBuilder(String selectClause, Class<T> entityClass, String alias) {
            super(selectClause, entityClass, alias);
        }
        
        

        @Override
        public String getQueryString() {
            return super.getQueryString();
        }

        @Override
        public Map<String, Object> getValues() {
            return super.getValues();
        }

        @Override
        public boolean isLocked() {
            return super.isLocked();
        }

        @Override
        protected void setLocked(boolean locked) {
            super.setLocked(locked);
        }
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
    
    
    // ORDERING ----------------------------------------------------------------
    @Test
    public void validOrdering() {
        testBuilder.orderBy("id ASC");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void emptyOrdering() {
        testBuilder.orderBy("");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void nullOrdering() {
        testBuilder.orderBy(null);
    }
    
    @Test (expected = IllegalStateException.class)
    public void duplicateOrdering() {
        testBuilder.orderBy("id ASC");
        testBuilder.orderBy("field DESC");
    }
    
    @Test
    public void prepareQueryString() {
        // constructor only...
        TestSelectBuilder<TestEntity> testBuilder1 = new TestSelectBuilder(true, TestEntity.class, "t");
        assertEquals("SELECT DISTINCT t FROM TestEntity t", testBuilder1.getQueryString());
        
        testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
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
        
        // order by...
        testBuilder1 = new TestSelectBuilder(TestEntity.class, "t");
        testBuilder1.orderBy("id ASC, field DESC");
        assertEquals("SELECT t FROM TestEntity t ORDER BY id ASC, field DESC", testBuilder1.getQueryString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testLocking() {
        TestSelectBuilder tb = new TestSelectBuilder(TestEntity.class, "t");
        tb.maxResults(1);
        tb.maxResults(2); // <-- throws IllegalStateException
    }
    
    @Test
    public void testUnlocking() {
        TestSelectBuilder tb = new TestSelectBuilder(TestEntity.class, "t");
        tb.maxResults(1);
        tb.setLocked(false);
        tb.maxResults(2);
    }
    
    // SELECT clause + WhereClauseBuilder
    @Test
    public void testIntegrationWithWhereClauseBuilder() {
        testBuilder = new TestSelectBuilder("SUM(e.value)", TestEntity.class, "e");
        WhereClauseBuilder wcb = new WhereClauseBuilder("z").initialCondition("e.filter", FindOperator.BETWEEN, new Range(3, 10)).or("e.filter", 7);
        testBuilder.where(wcb.build());
        testBuilder.values(wcb.getValues());
        
        assertEquals("SELECT SUM(e.value) FROM TestEntity e WHERE (e.filter BETWEEN :z0 AND :z1) OR (e.filter = :z2)", testBuilder.getQueryString());
    }
}
