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
		wcb = new WhereClauseBuilder("x").initialCondition("field1", 1, 2, 3).and("field2", FindOperator.BETWEEN, new Range(2, 10));
		Assert.assertEquals("(field1 = :x0 AND field1 = :x1 AND field1 = :x2) AND (field2 BETWEEN :x3 AND :x4)", wcb.build());
		Assert.assertEquals(1, (int)wcb.getValues().get("x0"));
		Assert.assertEquals(2, (int)wcb.getValues().get("x1"));
		Assert.assertEquals(3, (int)wcb.getValues().get("x2"));
		Assert.assertEquals(2, (int)wcb.getValues().get("x3"));
		Assert.assertEquals(10, (int)wcb.getValues().get("x4"));
	}
	
	@Test
	public void testGroup() {
		wcb = new WhereClauseBuilder("x")
			.initialCondition("field1", 1)
			.beginAndGroup()
				.initialCondition("field2", FindOperator.BETWEEN, new Range(2, 10))
				.or("field2", FindOperator.NOT_BETWEEN, new Range(5, 6))
			.closeGroup()
			.and("field3", 8);
		
		Assert.assertEquals("(field1 = :x0) AND ((field2 BETWEEN :x1 AND :x2) OR (field2 NOT BETWEEN :x3 AND :x4)) AND (field3 = :x5)", wcb.build());
		Assert.assertEquals(1, (int)wcb.getValues().get("x0"));
		Assert.assertEquals(2, (int)wcb.getValues().get("x1"));
		Assert.assertEquals(10, (int)wcb.getValues().get("x2"));
		Assert.assertEquals(5, (int)wcb.getValues().get("x3"));
		Assert.assertEquals(6, (int)wcb.getValues().get("x4"));
		Assert.assertEquals(8, (int)wcb.getValues().get("x5"));
	}
	
	@Test
	public void testInitialCondition() {
		Exception error;
		
		// ---------------------------------------------------------------------
		error = null;
		wcb = new WhereClauseBuilder();
		try {
			wcb.and("field1", 1);
		} catch (Exception ex) {
			error = ex;
		}
		
		Assert.assertNotNull(error);
		Assert.assertEquals("AND/OR cannot be set at current state", error.getMessage());
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		error = null;
		wcb = new WhereClauseBuilder();
		try {
			wcb.or("field1", 1);
		} catch (Exception ex) {
			error = ex;
		}
		
		Assert.assertNotNull(error);
		Assert.assertEquals("AND/OR cannot be set at current state", error.getMessage());
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		error = null;
		wcb = new WhereClauseBuilder();
		wcb.initialCondition("field1", 1);
		
		try {
			wcb.initialCondition("field1", 2);
		} catch (Exception ex) {
			error = ex;
		}
		
		Assert.assertNotNull(error);
		Assert.assertEquals("Initial condition cannot be set at current state", error.getMessage());
		// ---------------------------------------------------------------------
		
		// ---------------------------------------------------------------------
		error = null;
		wcb = new WhereClauseBuilder();
		wcb.initialCondition("field1", 1);
		
		try {
			wcb.or("field1", 2);
		} catch (Exception ex) {
			error = ex;
		}
		
		Assert.assertNull(error);
		// ---------------------------------------------------------------------
	}
	
	//Testar initial condition 
	// =========================================================================
}
