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

public class RangeTest {
	@Test(expected = IllegalArgumentException.class)
	public void nullMin() {
		new Range<Integer>(null, 2);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullMax() {
		new Range<Integer>(2, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidRange() {
		new Range<Integer>(3, 2);
	}
	
	@Test
	public void checkGetters() {
		Integer min = 2;
		Integer max = 3;
		
		Range<Integer> range = new Range<>(min, max);
		Assert.assertEquals(min, range.getMin());
		Assert.assertEquals(max, range.getMax());
	}
}