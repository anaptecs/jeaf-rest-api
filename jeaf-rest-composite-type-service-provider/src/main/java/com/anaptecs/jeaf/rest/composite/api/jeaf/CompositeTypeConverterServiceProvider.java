/**
 * Copyright 2004 - 2022 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.rest.composite.api.jeaf;

import com.anaptecs.jeaf.core.api.ServiceProvider;
import com.anaptecs.jeaf.rest.composite.api.CompositeTypeConverter;

/**
 * Interface enables {@link CompositeTypeConverter} to be used as JEAF Service Provider.
 * 
 * @author JEAF Development Team
 */
public interface CompositeTypeConverterServiceProvider extends CompositeTypeConverter, ServiceProvider {
}
