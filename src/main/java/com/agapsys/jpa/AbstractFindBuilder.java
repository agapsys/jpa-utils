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
import java.util.Random;
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
		/** 
		 * Generates a random string (chars: [a-z][A-Z][0-9]).
		 * @param length length of returned string
		 * @return a random string with given length.
		 * @throws IllegalArgumentException if (length &lt; 1)
		 */
		private static String getRandomString(int length) throws IllegalArgumentException {
			char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			return getRandomString(length, chars);
		}

		/**
		 * Generates a random String 
		 * @param length length of returned string
		 * @param chars set of chars which will be using during random string generation
		 * @return a random string with given length.
		 * @throws IllegalArgumentException if (length &lt; 1 || chars == null || chars.length == 0)
		 */
		private static String getRandomString(int length, char[] chars) throws IllegalArgumentException {
			if (length < 1)
				throw new IllegalArgumentException("Invalid length: " + length);

			if (chars == null || chars.length == 0)
				throw new IllegalArgumentException("Null/Empty chars");

			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < length; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			return sb.toString();
		}
		
		public static WhereClause generateWhereClause(List<FindToken> tokens) {
			StringBuilder sb = new StringBuilder();
			Map<String, Object> values = new LinkedHashMap<>();
			
			for (FindToken token : tokens) {
				if (token.isAnd != null)
					sb.append(token.isAnd ? " AND " : " OR ");
				
				sb.append("(");
				int i = 0;
				for (Object value : token.values) {
					if (i > 0)
						sb.append(" AND ");
					
					String valueKey;
					do {
						valueKey = getRandomString(6);
					} while (values.containsKey(valueKey));
					
					sb.append(token.operator.getSqlExpression().replace(":field", token.field).replace(":value", valueKey));
					values.put(valueKey, value);
					
					i++;
				}
				sb.append(")");
			}
			
			return new WhereClause(sb.toString(), values);
		}
		// =====================================================================
		
		public final Boolean      isAnd;
		public final String       field;
		public final FindOperator operator;
		public final Object[]     values;
		
		public FindToken(Boolean isAnd, String field, FindOperator operator, Object[] values) {
			switch(operator) {
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT:
					if (values.length != 0)
						throw new IllegalArgumentException("Unary operator does not require a value");
					break;
					
				default:
					if (values.length == 0)
						throw new IllegalArgumentException("Binary operator requires a value: " + operator.name());
					
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
	
	public AbstractFindBuilder(Class<T> entityClass) {
		this(false, entityClass);
	}
	
	public AbstractFindBuilder(boolean distinct, Class<T> entityClass) {
		super(distinct, entityClass, entityClass.getSimpleName().substring(1, 1).toLowerCase());
	}
	
	public AbstractFindBuilder by(String field, Object...values) {
		return by(field, FindOperator.EQUALS, values);
	}
	
	public AbstractFindBuilder by(String field, FindOperator operator, Object...values) {
		return and(field, operator, values);
	}
	
	public AbstractFindBuilder and(String field, Object...values) {
		return and(field, FindOperator.EQUALS, values);
	}
	
	public AbstractFindBuilder and(String field, FindOperator operator, Object...values) {
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
	
	public AbstractFindBuilder or(String field, Object...values) {
		return or(field, FindOperator.EQUALS, values);
	}
	
	public AbstractFindBuilder or(String field, FindOperator operator, Object...values) {
		Boolean isAnd = false;
		if (tokens.isEmpty())
			throw new IllegalStateException("or cannot be the called yet");
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		FindToken token = new FindToken(isAnd, field, operator, values);
		tokens.add(token);
		return this;
	}
	
	@Override
	public AbstractFindBuilder offset(int offset) {
		super.offset(offset);
		return this;
	}

	@Override
	public AbstractFindBuilder maxResults(int maxResults) {
		super.maxResults(maxResults);
		return this;
	}

	private void generateWhereClause() {
		if (whereClause == null) {
			whereClause = FindToken.generateWhereClause(tokens);
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
	// =========================================================================
}
