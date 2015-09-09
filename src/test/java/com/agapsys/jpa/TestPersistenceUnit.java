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

import java.util.Properties;
import javax.persistence.Persistence;

public class TestPersistenceUnit extends PersistenceUnit {
	// CLASS SCOPE =============================================================
	public static final String PERSISTENCE_UNIT_NAME = "default";
	
	public static final String JDBC_DRIVER       = "org.h2.Driver";
	public static final String JDBC_URL          = "jdbc:h2:mem:";
	public static final String JDBC_USER         = "sa";
	public static final String JDBC_PASSWORD     = "sa";
	public static final String SCHEMA_GENERATION = "create";
	
	private static final Properties PROPERTIES;
	
	static {
		PROPERTIES = new Properties();
		PROPERTIES.setProperty("javax.persistence.jdbc.driver",                       JDBC_DRIVER);
		PROPERTIES.setProperty("javax.persistence.jdbc.url",                          JDBC_URL);
		PROPERTIES.setProperty("javax.persistence.jdbc.user",                         JDBC_USER);
		PROPERTIES.setProperty("javax.persistence.jdbc.password",                     JDBC_PASSWORD);
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================
	public TestPersistenceUnit() {
		super(Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,  PROPERTIES));
	}
	// =========================================================================
}
