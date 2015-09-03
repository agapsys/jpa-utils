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

/**
 *
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public enum FindOperator {
	LESS_THAN           (":field < :value"),
	LESS_THAN_EQUALS    (":field <= :value"),
	GREATER_THAN        (":field > :value"),
	GREATER_THAN_EQUALS (":field >= :value"),
	LIKE                (":field LIKE :value"),
	EQUALS              (":field = :value"),
	NOT_EQUAL           (":field != :value"),
	ILIKE               ("LOWER (:field) LIKE :value"),
	IS_NOT_NULL         (":field IS NOT NULL"),
	IS_NULL             (":field IS NULL"),
	NOT                 ("NOT :field");

	private final String sqlExpression;

	private FindOperator(String sqlExpression) {
		this.sqlExpression = sqlExpression;
	}

	public String getSqlExpression() {
		return sqlExpression;
	}
}
	
