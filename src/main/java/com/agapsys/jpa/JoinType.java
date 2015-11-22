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

public enum JoinType {
	INNER_JOIN      ("JOIN"),
	LEFT_OUTER_JOIN ("LEFT JOIN");
	
	private final String sql;
	private JoinType(String sql) {
		this.sql = sql;
	}
	
	String getSQl() {
		return sql;
	}
}
