/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

import java.util.Collections;
import java.util.List;

/**
 * A do nothing implemenation.  Basically used so we don't have to return Null.
 * 
 * @author dcarver
 *
 */
public class NullTurmericWizard implements ITurmericWizard {

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#isType(java.lang.String)
	 */
	@Override
	public boolean isType(String type) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#allPages()
	 */
	@Override
	public List<ITurmericWizardPage> allPages() {
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard#hasPages()
	 */
	@Override
	public boolean hasPages() {
		return false;
	}

}
