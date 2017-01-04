/* 
 * Copyright (C) 2015 Agapsys Tecnologia - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.agapsys.jpa;

import com.agapsys.jpa.entity.TestEntity;
import org.junit.Assert;
import org.junit.Test;

public class DeleteBuilderTest {
    // CLASS SCOPE =============================================================
    private static class CustomDeleteBuilder<T extends EntityObject> extends DeleteBuilder<T> {

        public CustomDeleteBuilder(Class<T> entityClass) {
            super(entityClass);
        }
        
        @Override
        public String getQueryString() {
            return super.getQueryString();
        }
    }
    // =========================================================================
    
    // INSTANCE SCOPE ==========================================================
    @Test
    public void testJpql() {
        DeleteBuilder deleteBuilder = new CustomDeleteBuilder(TestEntity.class)
            .where("id", 12, 13)
            .and("id", FindOperator.GREATER_THAN, 45)
            .or("id", FindOperator.LESS_THAN_EQUALS, 100)
            .and("field", FindOperator.LIKE, "abc", "def")
            .or("field", FindOperator.ILIKE, "ghi", "jkl");
        
        Assert.assertEquals(
            "DELETE FROM TestEntity t WHERE (t.id = :f0 AND t.id = :f1) AND (t.id > :f2) OR (t.id <= :f3) AND (t.field LIKE :f4 AND t.field LIKE :f5) OR (LOWER(t.field) LIKE LOWER(:f6) AND LOWER(t.field) LIKE LOWER(:f7))",
            ((CustomDeleteBuilder) deleteBuilder).getQueryString()
        );
    }
    // =========================================================================
}
