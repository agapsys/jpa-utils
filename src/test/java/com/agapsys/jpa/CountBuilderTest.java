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
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/*
 * NOTE: Since CountBuilder extends FindBuilder, it's not required to re-test
 * builder methods
 */

public class CountBuilderTest {
    // CLASS SCOPE =============================================================
    private static class TestCountBuilder<T extends EntityObject> extends CountBuilder<T> {
        public TestCountBuilder(Class<T> entiClass) {
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
    @Test
    public void testQueryBuilder() {
        TestCountBuilder countBuilder;
        
        countBuilder = new TestCountBuilder(TestEntity.class);
        Assert.assertEquals("SELECT COUNT(1) FROM TestEntity t", countBuilder.getQueryString());
    }
    // =========================================================================
}
