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
		private static final int    KEY_LENGTH = 6;
		private static final char[] CHARS      = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		private static String getRandomKey() {
			return Utils.getRandomString(KEY_LENGTH, CHARS);
		}
		
		public static WhereClause generateWhereClause(List<FindToken> tokens) {
			StringBuilder sb = new StringBuilder();
			Map<String, Object> values = new LinkedHashMap<>();
			
			int maxAllowedKeys = (int) Math.pow(CHARS.length, KEY_LENGTH);
			int c = 0;
			
			for (FindToken token : tokens) {
				if (token.isAnd != null)
					sb.append(token.isAnd ? " AND " : " OR ");
				
				sb.append("(");
				
				if (token.operator.isUnary()) {
					sb.append(token.operator.getSqlExpression().replace(":field", token.field));
					c++;
				} else {
					int i = 0;
					for (Object value : token.values) {
						if (i > 0)
							sb.append(" AND ");

						String valueKey;
						
						do {
							valueKey = getRandomKey();
						} while (values.containsKey(valueKey));

						sb.append(token.operator.getSqlExpression().replace(":field", token.field).replace(":value", valueKey));
						values.put(valueKey, value);

						i++;
						c++;
					}
					sb.append(")");
				}
				
				if (c > maxAllowedKeys)
					throw new IndexOutOfBoundsException("parameter count overflow");
			}
			
			return new WhereClause(sb.toString(), values);
		}
		// =====================================================================
		
		public final Boolean      isAnd;
		public final String       field;
		public final FindOperator operator;
		public final Object[]     values;
		
		public FindToken(Boolean isAnd, String field, FindOperator operator, Object[] values) {
			if (values.length != 0 && operator.isUnary())
				throw new IllegalArgumentException(String.format("Unary operator (%s) does not require a value", operator.name()));
			else if (values.length == 0 && !operator.isUnary())
				throw new IllegalArgumentException(String.format("Binary operator (%s) requires a value", operator.name()));
			
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
			throw new IllegalStateException("OR clause cannot be the first");
		
		if (operator == null)
			throw new IllegalArgumentException("Null operator");
		
		FindToken token = new FindToken(isAnd, field, operator, values);
		tokens.add(token);
		return this;
	}
	
	@Override
	public AbstractFindBuilder offset(int offset) {
		return (AbstractFindBuilder) super.offset(offset);
	}

	@Override
	public AbstractFindBuilder maxResults(int maxResults) {
		return (AbstractFindBuilder) super.maxResults(maxResults);
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
