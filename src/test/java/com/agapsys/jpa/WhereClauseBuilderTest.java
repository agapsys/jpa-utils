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

import org.junit.Assert;
import org.junit.Test;

public class WhereClauseBuilderTest {
	// CLASS SCOPE =============================================================

	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private WhereClauseBuilder wcb;
	
	@Test
	public void testClauseGeneration() {
		wcb = new WhereClauseBuilder("x", "field1", 1, 2, 3).and("field2", FindOperator.BETWEEN, new Range(2, 10));
		Assert.assertEquals("(field1 = :x0 AND field1 = :x1 AND field1 = :x2) AND (field2 BETWEEN :x3 AND :x4)", wcb.build());
		Assert.assertEquals(1, (int)wcb.getValues().get("x0"));
		Assert.assertEquals(2, (int)wcb.getValues().get("x1"));
		Assert.assertEquals(3, (int)wcb.getValues().get("x2"));
		Assert.assertEquals(2, (int)wcb.getValues().get("x3"));
		Assert.assertEquals(10, (int)wcb.getValues().get("x4"));
	}
	// =========================================================================
}
