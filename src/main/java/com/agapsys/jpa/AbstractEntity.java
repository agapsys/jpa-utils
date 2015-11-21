/* 
 * Copyright (C) 2015 Agapsys Tecnologia - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.agapsys.jpa;

import javax.persistence.EntityManager;

public abstract class AbstractEntity implements EntityObject {
	// CLASS SCOPE =============================================================
	private static Class<? extends AbstractEntity> getRunningClass() {
		try {
			StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
			Class<? extends AbstractEntity> runningClass = (Class<? extends AbstractEntity>) Class.forName(ste.getClassName());
			if (runningClass == AbstractEntity.class)
				throw new RuntimeException(String.format("Method cannot be called from %s", AbstractEntity.class.getName()));
			
			return runningClass;
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static FindBuilder find() {		
		return new FindBuilder(getRunningClass());
	}
	
	public static CountBuilder count() {
		return new CountBuilder(getRunningClass());
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	@Override
	public EntityObject save(EntityManager em) {
		em.persist(this);
		return this;
	}
	// =========================================================================
}
