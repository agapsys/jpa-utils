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

import com.agapsys.jpa.WhereClauseBuilder.QueryParameter;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CountBuilder<T extends EntityObject> extends AbstractFindBuilder<T> {

    public CountBuilder(Class<T> entityClass) {
        super(entityClass);
    }

    public CountBuilder(Class<T> entityClass, String alias) {
        super(entityClass, alias);
    }
    
    public CountBuilder(boolean distinct, Class<T> entityClass) {
        super(distinct, entityClass);
    }
    
    public CountBuilder(boolean distinct, Class<T> entityClass, String alias) {
        super(distinct, entityClass, alias);
    }
    
    
    @Override
    public CountBuilder<T> by(String field, FindOperator operator, Object... values) {
        return (CountBuilder<T>) super.by(field, operator, values);
    }

    @Override
    public CountBuilder<T> by(String field, Object... values) {
        return (CountBuilder<T>) super.by(field, values);
    }
    
    @Override
    public CountBuilder<T> by(String literal, QueryParameter...parameters) {
        return (CountBuilder<T>) super.by(literal, parameters);
    }
    
    
    @Override
    public CountBuilder<T> or(String field, FindOperator operator, Object... values) {
        return (CountBuilder<T>) super.or(field, operator, values);
    }

    @Override
    public CountBuilder<T> or(String field, Object... values) {
        return (CountBuilder<T>) super.or(field, values);
    }

    @Override
    public CountBuilder<T> or(String literal, QueryParameter...parameters) {
        return (CountBuilder<T>) super.or(literal, parameters);
    }
    
    
    @Override
    public CountBuilder<T> and(String field, FindOperator operator, Object... values) {
        return (CountBuilder<T>) super.and(field, operator, values);
    }

    @Override
    public CountBuilder<T> and(String field, Object... values) {
        return (CountBuilder<T>) super.and(field, values);
    }

    @Override
    public CountBuilder<T> and(String literal, QueryParameter...parameters) {
        return (CountBuilder<T>) super.and(literal, parameters);
    }

    
    @Override
    public CountBuilder<T> beginGroup() {
        return (CountBuilder <T>) super.beginGroup();
    }
    
    @Override
    public CountBuilder<T> beginAndGroup() {
        return (CountBuilder<T>) super.beginAndGroup();
    }
    
    @Override
    public CountBuilder<T> beginOrGroup() {
        return (CountBuilder<T>) super.beginOrGroup();
    }
    
    @Override
    public CountBuilder<T> closeGroup() {
        return (CountBuilder<T>) super.closeGroup();
    }
    
    
    @Override
    protected String getSelectClause() {
        return "COUNT(1)";
    }
        
    public long count(EntityManager entityManager) {
        Query query = prepareQuery(entityManager);
        
        for (Map.Entry<String, Object> entry : getValues().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        
        if (getOffset() != null)
            query.setFirstResult(getOffset());
        
        return (long) query.getSingleResult();
    }
}
