/**
 * anaptecs GmbH, Ricarda-Huch-Str. 71, 72760 Reutlingen, Germany
 * 
 * Copyright 2004 - 2022. All rights reserved.
 */
package com.anaptecs.jeaf.rest.executor.api.jeaf;

import com.anaptecs.jeaf.core.api.ServiceProvider;
import com.anaptecs.jeaf.rest.executor.api.RESTRequestExecutor;

/**
 * Interface enables {@link RESTRequestExecutor} to be used as JEAF Service Provider.
 * 
 * @author JEAF Development Team
 */
public interface RESTRequestExecutorServiceProvider extends RESTRequestExecutor, ServiceProvider {
}
