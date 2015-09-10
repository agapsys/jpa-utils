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

public enum FindOperator {
	LESS_THAN           (":field < :value"),
	LESS_THAN_EQUALS    (":field <= :value"),
	GREATER_THAN        (":field > :value"),
	GREATER_THAN_EQUALS (":field >= :value"),
	ILIKE               ("LOWER(:field) LIKE LOWER(:value)"),
	LIKE                (":field LIKE :value"),
	EQUALS              (":field = :value"),
	NOT_EQUAL           (":field <> :value"),
	IS_NOT_NULL         (":field IS NOT NULL"),
	IS_NULL             (":field IS NULL"),
	NOT                 ("NOT :field"),
	BETWEEN             (":field BETWEEN :min AND :max"),
	NOT_BETWEEN         (":field NOT BETWEEN :min AND :max");

	private final String sqlExpression;

	private FindOperator(String sqlExpression) {
		this.sqlExpression = sqlExpression;
	}

	String getSqlExpression(String field, String...valueKeys) {
		switch (this) {
			case BETWEEN:
			case NOT_BETWEEN:
				return sqlExpression.replace(":field", field).replace(":min", valueKeys[0]).replace(":max", valueKeys[1]);
				
			default:
				return sqlExpression.replace(":field", field).replace(":value", valueKeys[0]);
		}
	}
}
	
