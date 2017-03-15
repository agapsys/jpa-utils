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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Transient;

public abstract class AbstractEntity<T extends AbstractEntity> implements EntityObject {
    
    @Transient
    private boolean modified = true;
    public boolean isModified() {
        return modified;
    }
    
    protected void setModified(boolean modified) {
        this.modified = modified;
    }
    
    protected void setModified() {
        setModified(true);
    }
    
    public T save(EntityManager em) {
        if (isModified()) {
            EntityTransaction transaction = em.getTransaction();
            if (!transaction.isActive())
                transaction.begin();

            em.persist(this);
            setModified(false);
        }
        return (T) this;
    }
    
    public void delete(EntityManager em) {
        em.remove(this);
    }

    @Override
    public String toString() {
        Object id = getId();
        return String.format("%s:%s", getClass().getName(), id != null ? id.toString() : "null");
    }
}
