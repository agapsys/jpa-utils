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
import javax.persistence.EntityManager;

public abstract class AbstractFindBuilder<T> extends AbstractSelectBuilder<T> {
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
		private static final String PARAM_NAME = "f";
		
		public static WhereClause generateWhereClause(List<FindToken> tokens) {
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
								
								paramName = PARAM_NAME + paramCounter;
								valueKeys[0] = ":" + paramName;
								values.put(paramName, range.getMin());
								paramCounter++;
								
								paramName = PARAM_NAME + paramCounter;
								valueKeys[1] = ":" + paramName;
								values.put(paramName, range.getMax());
								paramCounter++;
							} else {
								valueKeys = new String[1];
								
								paramName = PARAM_NAME + paramCounter;
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
	
	public AbstractFindBuilder(Class<T> entityClass) {
		super(entityClass);
	}
	
	public AbstractFindBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass);
	}
	
	
	private AbstractFindBuilder andOrBy(boolean byClause, String field, FindOperator operator, Object...values) {
		Boolean isAnd;
		
		if (tokens.isEmpty() && !byClause)
			throw new IllegalStateException("AND cannot be called yet");
		
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
	
	
	protected AbstractFindBuilder by(String field, Object...values) {
		return andOrBy(true, field, FindOperator.EQUALS, values);
	}
	
	protected AbstractFindBuilder by(String field, FindOperator operator, Object...values) {
		return andOrBy(true, field, operator, values);
	}
	
	
	protected AbstractFindBuilder and(String field, Object...values) {
		return andOrBy(false, field, FindOperator.EQUALS, values);
	}	
	
	protected AbstractFindBuilder and(String field, FindOperator operator, Object...values) {
		return andOrBy(false, field, operator, values);
	}
	
	
	protected AbstractFindBuilder or(String field, Object...values) {
		return or(field, FindOperator.EQUALS, values);
	}
	
	protected AbstractFindBuilder or(String field, FindOperator operator, Object...values) {
		Boolean isAnd = false;
		if (tokens.isEmpty())
			throw new IllegalStateException("OR cannot be called yet");
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		FindToken token = new FindToken(isAnd, field, operator, values);
		tokens.add(token);
		return this;
	}
	
	
	@Override
	protected AbstractFindBuilder offset(Integer offset) {
		return (AbstractFindBuilder) super.offset(offset);
	}

	@Override
	protected AbstractFindBuilder maxResults(Integer maxResults) {
		return (AbstractFindBuilder) super.maxResults(maxResults);
	}

	@Override
	protected AbstractFindBuilder orderBy(String ordering) {
		return (AbstractFindBuilder) super.orderBy(ordering);
	}

	
	private void generateWhereClause() {
		if (!whereClauseGenerated) {
			whereClause = FindToken.generateWhereClause(tokens);
			whereClauseGenerated = true;
		}
	}
	
	@Override
	protected String getWhereClause() {
		generateWhereClause();
		return whereClause.clause;
	}

	@Override
	protected Map<String, Object> getValues() {
		generateWhereClause();
		return whereClause.values;
	}

	protected List<T> find(EntityManager entityManager) {
		return super.select(entityManager);
	}
	
	protected T findFirst(EntityManager entityManager) {
		Integer previousMaxResults = getMaxResults();
		
		setLocked(false); // <-- allows attribute change
		maxResults(1);		
		
		List<T> results = find(entityManager);

		maxResults(previousMaxResults);
		setLocked(true); // <-- restores locking state
		
		setLocked(true);
		
		if (results.isEmpty())
			return null;
		else
			return results.get(0);
	}
	// =========================================================================
}
