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
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class DeleteBuilder<T extends EntityObject> extends AbstractFindBuilder<T> {

    public DeleteBuilder(Class<T> entityClass) {
        super(entityClass);
    }
    
    public DeleteBuilder(Class<T> entityclass, String alias) {
        super(entityclass, alias);
    }
    
    @Override
    public Class<T> getEntityClass() {
        return super.getEntityClass();
    }
    
    
    public DeleteBuilder<T> where(String field, FindOperator operator, Object... values) {
        return (DeleteBuilder<T>) super.by(field, operator, values);
    }

    public DeleteBuilder<T> where(String field, Object... values) {
        return (DeleteBuilder<T>) super.by(field, values);
    }
    
    public DeleteBuilder<T> where(String literal, QueryParameter...parameters) {
        return (DeleteBuilder<T>) super.by(literal, parameters);
    }
    
    
    @Override
    public DeleteBuilder<T> or(String field, FindOperator operator, Object... values) {
        return (DeleteBuilder<T>) super.or(field, operator, values);
    }

    @Override
    public DeleteBuilder<T> or(String field, Object... values) {
        return (DeleteBuilder<T>) super.or(field, values);
    }

    @Override
    public DeleteBuilder<T> or(String literal, QueryParameter...parameters) {
        return (DeleteBuilder<T>) super.or(literal, parameters);
    }
    
    
    @Override
    public DeleteBuilder<T> and(String field, FindOperator operator, Object... values) {
        return (DeleteBuilder<T>) super.and(field, operator, values);
    }

    @Override
    public DeleteBuilder<T> and(String field, Object... values) {
        return (DeleteBuilder<T>) super.and(field, values);
    }

    @Override
    public DeleteBuilder<T> and(String literal, QueryParameter...parameters) {
        return (DeleteBuilder<T>) super.and(literal, parameters);
    }
    
    
    @Override
    public DeleteBuilder<T> beginAndGroup() {
        return (DeleteBuilder<T>) super.beginAndGroup();
    }
    
    @Override
    public DeleteBuilder<T> beginOrGroup() {
        return (DeleteBuilder<T>) super.beginOrGroup();
    }
    
    @Override
    public DeleteBuilder<T> closeGroup() {
        return (DeleteBuilder<T>) super.closeGroup();
    }
    
    
    @Override
    protected Object executeQuery(Query q) {
        return q.executeUpdate();
    }

    @Override
    protected String getQueryString() {
        String findText = Pattern.quote(String.format("SELECT %s", getAlias()));
        return super.getQueryString().replaceFirst(findText, "DELETE");
    }
    
    public int delete(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        
        if (!transaction.isActive())
            transaction.begin();
        
        return (int) super.select(entityManager);
    }
}
