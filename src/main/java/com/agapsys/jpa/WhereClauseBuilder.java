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
	private static final String DEFAULT_PARAM_PREFIX = "p";

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
				if (token.isLiteral()) {
					// Literal token -------------------------------------------
					if (!token.isInitialCondition())
						sb.append(token.isAnd() ? " AND " : " OR ");
					
					sb.append("(");
					sb.append(token.literal);
					sb.append(")");
					
					for (QueryParameter qp : token.queryParameters) {
						if (values.put(qp.paramName, qp.paramValue) != null)
							throw new IllegalStateException("Duplicate parameter: " + qp.paramName);
					}
					// ---------------------------------------------------------
					
				} else if (token.isGroupToken()) {
					// Group token ---------------------------------------------
					if (token.isGroupOpen()) {
						sb.append(token.isAnd() ? " AND " : " OR ");						
						sb.append("(");
					} else {
						sb.append(")");
					}
					// ---------------------------------------------------------
					
				} else {
					// Simple token --------------------------------------------
					if (!token.isInitialCondition())
						sb.append(token.isAnd() ? " AND " : " OR ");

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
					// ---------------------------------------------------------
				}
			}
			
			String whereClause = sb.toString();
			
			if (whereClause.isEmpty())
				whereClause = null;
			
			return new WhereClause(whereClause, values);
		}
		// =====================================================================
		
		public final Boolean          isGroupOpen;
		public final Boolean          isAnd;
		public final String           field;
		public final FindOperator     operator;
		public final Object[]         values;
		public final String           literal;
		public final QueryParameter[] queryParameters;
		
		
		public FindToken(Boolean isAnd, String field, FindOperator operator, Object[] values) {
			// Unused fields ---------------------------------------------------
			this.isGroupOpen = null;
			this.literal = null;
			this.queryParameters = null;
			// -----------------------------------------------------------------
			
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
		
		public FindToken(boolean groupBegin, Boolean isAnd) {
			// Unused field ----------------------------------------------------
			this.field           = null;
			this.operator        = null;
			this.values          = null;
			this.literal         = null;
			this.queryParameters = null;
			// -----------------------------------------------------------------
			
			this.isGroupOpen     = groupBegin;
			this.isAnd           = isAnd;
		}
		
		public FindToken(Boolean isAnd, String literal, QueryParameter...queryParameters) {
			// Unused field ----------------------------------------------------
			this.isGroupOpen     = null;
			this.field           = null;
			this.operator        = null;
			this.values          = null;
			// -----------------------------------------------------------------
			
			if (literal == null || literal.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty literal");
			
			this.literal         = literal;
			this.queryParameters = queryParameters;
			this.isAnd           = isAnd;
		}
		
		
		public boolean isGroupToken() {
			return isGroupOpen != null;
		}
		
		public boolean isGroupOpen() {
			return isGroupToken() && isGroupOpen;
		}
		
		public boolean isGroupClose() {
			return isGroupToken() && !isGroupOpen;
		}
		
		public boolean isInitialCondition() {
			return isAnd == null;
		}
		
		public boolean isSimple() {
			return !isInitialCondition();
		}
		
		
		public boolean isAnd() {
			if (isAnd == null)
				throw new IllegalStateException("isAnd is null");
			
			return isAnd;
		}
		
		public boolean isOr() {
			if (isAnd == null)
				throw new IllegalStateException("isAnd is null");
			
			return !isAnd;
		}
		
		
		public boolean isLiteral() {
			return literal != null;
		}
	}
	
	public static class QueryParameter {
		private final String paramName;
		private final Object paramValue;
		
		public QueryParameter(String paramName, Object paramValue) {
			if (paramName == null || paramName.trim().isEmpty())
				throw new IllegalArgumentException("Null/Empty parameter name");
			
			this.paramName = paramName;
			this.paramValue = paramValue;
		}

		public String getParamName() {
			return paramName;
		}

		public Object getParamValue() {
			return paramValue;
		}
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	private final List<FindToken> tokens = new LinkedList<>();
	private final String paramPrefix;

	private WhereClause whereClause = null;
	private boolean whereClauseGenerated = false;
	private boolean isGroupOpen = false;

	
	public WhereClauseBuilder(String paramPrefix) {
		if (paramPrefix == null || paramPrefix.trim().isEmpty())
			throw new IllegalArgumentException("Null/Empty param prefix");
		
		this.paramPrefix = paramPrefix;
	}
	
	public WhereClauseBuilder() {
		this(DEFAULT_PARAM_PREFIX);
	}
	
	
	private void checkAndOrAllowed() {
		FindToken lastToken = tokens.isEmpty() ? null : tokens.get(tokens.size() - 1);
		boolean allowed = lastToken != null && (!lastToken.isGroupToken() || lastToken.isGroupClose());
		
		if (!allowed)
			throw new IllegalStateException("AND/OR cannot be set at current state");
	}

	private void checkInitialConditionAllowed() {
		FindToken lastToken = tokens.isEmpty() ? null : tokens.get(tokens.size() - 1);
		boolean allowed = (lastToken == null || lastToken.isGroupOpen());
		if (!allowed)
			throw new IllegalStateException("Initial condition cannot be set at current state");
	}
	
	private void checkQueryParameters(QueryParameter...parameters) {
		if (parameters.length > 0) {
			for (QueryParameter qp : parameters) {
				if (qp.getParamName().startsWith(paramPrefix))
					throw new IllegalArgumentException("Invalid parameter name: " + qp.getParamName());
			}
		}
	}
	
	
	public WhereClauseBuilder initialCondition(String field, Object...values) {
		return initialCondition(field, FindOperator.EQUALS, values);
	}
	
	public WhereClauseBuilder initialCondition(String field, FindOperator operator, Object...values) {
		whereClauseGenerated = false;
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		checkInitialConditionAllowed();
		
		tokens.add(new FindToken(null, field, operator, values));
		return this;
	}
	
	public WhereClauseBuilder initialCondition(String literal, QueryParameter...parameters) {
		whereClauseGenerated = false;
		
		checkQueryParameters(parameters);
		checkInitialConditionAllowed();
		
		tokens.add(new FindToken(null, literal, parameters));
		return this;
	}
	
	
	public WhereClauseBuilder and(String field, Object...values) {
		return and(field, FindOperator.EQUALS, values);
	}	
	
	public WhereClauseBuilder and(String field, FindOperator operator, Object...values) {
		whereClauseGenerated = false;
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		checkAndOrAllowed();
		
		tokens.add(new FindToken(true, field, operator, values));
		return this;
	}
	
	public WhereClauseBuilder and(String literal, QueryParameter...parameters) {
		whereClauseGenerated = false;
		
		checkQueryParameters(parameters);
		checkAndOrAllowed();
		
		tokens.add(new FindToken(true, literal, parameters));
		return this;
	}
	
	
	public WhereClauseBuilder or(String field, Object...values) {
		return or(field, FindOperator.EQUALS, values);
	}
	
	public WhereClauseBuilder or(String field, FindOperator operator, Object...values) {
		whereClauseGenerated = false;
				
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		checkAndOrAllowed();
		
		tokens.add(new FindToken(false, field, operator, values));
		return this;
	}
	
	public WhereClauseBuilder or(String literal, QueryParameter...parameters) {
		whereClauseGenerated = false;
		
		checkQueryParameters(parameters);
		checkAndOrAllowed();
		
		tokens.add(new FindToken(false, literal, parameters));
		return this;
	}
	
	
	public WhereClauseBuilder beginAndGroup() {
		whereClauseGenerated = false;
		
		if (tokens.isEmpty())
			throw new IllegalStateException("Group cannot be created yet");
		
		tokens.add(new FindToken(true, true));
		isGroupOpen = true;
		return this;
	}
	
	public WhereClauseBuilder beginOrGroup() {
		whereClauseGenerated = false;
		
		if (tokens.isEmpty())
			throw new IllegalStateException("Group cannot be created yet");
		
		tokens.add(new FindToken(true, false));
		isGroupOpen = true;
		return this;
	}
	
	public WhereClauseBuilder closeGroup() {
		whereClauseGenerated = false;
		
		if (tokens.isEmpty())
			throw new IllegalStateException("Group cannot be closed at current state");
		
		if (!isGroupOpen)
			throw new IllegalStateException("There is no open group");
		
		tokens.add(new FindToken(false, null));
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
