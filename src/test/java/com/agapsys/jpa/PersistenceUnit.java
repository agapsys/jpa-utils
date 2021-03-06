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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUnit {
    // CLASS SCOPE =============================================================
    private static EntityManagerFactory emf = null;
    
    public static EntityManager getEntityManager() {
        if (emf == null) {
            Logger.getLogger("org.hibernate").setLevel(Level.OFF);
            emf = Persistence.createEntityManagerFactory("test-pu");
        }
        
        return emf.createEntityManager();
    }
    
    public static void close() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
    // =========================================================================

    // INSTANCE SCOPE ==========================================================
    private PersistenceUnit() {}
    // =========================================================================
}
