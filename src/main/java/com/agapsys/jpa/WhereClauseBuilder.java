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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class WhereClauseBuilder {
	// CLASS SCOPE =============================================================
	private static class WhereClause {
		public final String clause;
		public final Map<String, Object> values;
		
		public WhereClause(String clause, Map<String, Object> values) {
			this.clause = clause;
			this.values = values;
		}
	}

	private static class FindToken {
		// CLASS SCOPE =========================================================
		public static WhereClause generateWhereClause(List<FindToken> tokens, String paramPrefix) {
			if (paramPrefix == null || paramPrefix.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty param prefix");
			
			StringBuilder sb = new StringBuilder();
			Map<String, Object> values = new LinkedHashMap<>();
			
			int paramCounter = 0;
			
			for (FindToken token : tokens) {
				if (token.isAnd != null)
					sb.append(token.isAnd ? " AND " : " OR ");
				
				sb.append("(");
				
				switch(token.operator) {
					case IS_NOT_NULL:
					case IS_NULL:
					case NOT:
						sb.append(token.operator.getSqlExpression(token.field, ""));
						break;
						
					default:
						int valueCounter = 0;

						for (Object value : token.values) {
							if (valueCounter > 0)
								sb.append(" AND ");

							String[] valueKeys;
							String paramName;

							if (value instanceof Range) {
								Range range = (Range) value;
								
								valueKeys = new String[2];
								
								paramName = paramPrefix + paramCounter;
								valueKeys[0] = ":" + paramName;
								values.put(paramName, range.getMin());
								paramCounter++;
								
								paramName = paramPrefix + paramCounter;
								valueKeys[1] = ":" + paramName;
								values.put(paramName, range.getMax());
								paramCounter++;
							} else {
								valueKeys = new String[1];
								
								paramName = paramPrefix + paramCounter;
								valueKeys[0] = ":" + paramName;
								values.put(paramName, value);
								paramCounter++;
							}
							
							sb.append(token.operator.getSqlExpression(token.field, valueKeys));
							valueCounter++;
						}
						break;
				}
				
				sb.append(")");
			}
			
			String whereClause = sb.toString();
			
			if (whereClause.isEmpty())
				whereClause = null;
			
			return new WhereClause(whereClause, values);
		}
		// =====================================================================
		
		public final Boolean      isAnd;
		public final String       field;
		public final FindOperator operator;
		public final Object[]     values;
				
		public FindToken(Boolean isAnd, String field, FindOperator operator, Object[] values) {			
			if (field == null || field.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty field");
		
			if (operator == null)
				throw new IllegalArgumentException("Null operator");
			
			switch(operator) {
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT:
					if (values.length != 0)
						throw new IllegalArgumentException(String.format("Unary operator (%s) does not require a value", operator.name()));
					
					break;
					
				case BETWEEN:
				case NOT_BETWEEN:
					if (values.length == 0)
						throw new IllegalArgumentException(String.format("Operator (%s) requires a range", operator.name()));
					
					for (int i = 0; i < values.length; i++) {
						Object value = values[i];
						
						if (value == null)
							throw new IllegalArgumentException("Null value at index " + i);
						
						if (!(value instanceof Range))
							throw new IllegalArgumentException(String.format("Given value is not a range: %s", value.getClass().getName()));
					}
					break;
				
				default:
					if (values.length == 0)
						throw new IllegalArgumentException(String.format("Operator (%s) requires a value", operator.name()));
					
					for (int i = 0; i < values.length; i++) {
						Object value = values[i];
						
						if (value == null)
							throw new IllegalArgumentException("Null value at index " + i);
					}
					break;
			}
			
			this.isAnd = isAnd;
			this.field = field;
			this.operator = operator;
			this.values = values;
		}		
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private final List<FindToken> tokens = new LinkedList<>();
	private WhereClause whereClause = null;
	private boolean whereClauseGenerated = false;
	private final String paramPrefix;
	
	private void throwIfGenerated() throws IllegalStateException {
		if (whereClauseGenerated)
			throw new IllegalStateException("WHERE clause was already generated");
	}
	
	public WhereClauseBuilder(String paramPrefix, String field, Object...values) {
		if (paramPrefix == null || paramPrefix.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty param prefix");
		
		this.paramPrefix = paramPrefix;
		
		and(field, FindOperator.EQUALS, values);
	}
	
	public WhereClauseBuilder(String paramPrefix, String field, FindOperator operator, Object...values) {
		if (paramPrefix == null || paramPrefix.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty param prefix");
		
		this.paramPrefix = paramPrefix;
		
		and(field, operator, values);
	}
	
	
	public WhereClauseBuilder and(String field, Object...values) {
		return and(field, FindOperator.EQUALS, values);
	}	
	
	public WhereClauseBuilder and(String field, FindOperator operator, Object...values) {
		throwIfGenerated();
		
		Boolean isAnd;
		
		if (tokens.isEmpty())
			isAnd = null;
		else
			isAnd = true;
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		FindToken token = new FindToken(isAnd, field, operator, values);
		tokens.add(token);
		return this;
	}
	
	
	public WhereClauseBuilder or(String field, Object...values) {
		return or(field, FindOperator.EQUALS, values);
	}
	
	public WhereClauseBuilder or(String field, FindOperator operator, Object...values) {
		throwIfGenerated();
		
		Boolean isAnd = false;
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		FindToken token = new FindToken(isAnd, field, operator, values);
		tokens.add(token);
		return this;
	}
	
	
	private void _build() {
		if (!whereClauseGenerated) {
			whereClause = FindToken.generateWhereClause(tokens, paramPrefix);
			whereClauseGenerated = true;
		}
	}
	
	public String build() {
		_build();
		return whereClause.clause;
	}
	
	public Map<String, Object> getValues() {
		_build();
		return whereClause.values;
	}
	// =========================================================================
}
