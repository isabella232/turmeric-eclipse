/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * This wizard page ONLY shows (lineated) text in a scrolling, copiable form. It
 * is normally used to display errors, so only cancel button is available.
 * Errors related to the wizard page entries should be set in the status area.
 * This is only for situations where errors extrinsic to the wizard prevent
 * continuing.
 * 
 * @author wisberg
 * 
 */
public class SOATextPage extends WizardPage {
	protected final List<String> messages;

	public SOATextPage(String name, String title, String description,
			IStatus model) {
		super(name);
		setTitle(title);
		setDescription(description);
		List<String> message = new ArrayList<String>();
		if (model.isMultiStatus()) {
			for (IStatus status : model.getChildren()) {
				message.add(status.getMessage());
			}
		} else {
			message.add(model.getMessage());
		}
		if ((null == message) || message.isEmpty()) {
			this.messages = Collections.emptyList();
		} else {
			this.messages = Collections.unmodifiableList(message);
		}
	}

	public SOATextPage(String name, String title, String description,
			List<String> messages) {
		super(name);
		setTitle(title);
		setDescription(description);
		if ((null == messages) || messages.isEmpty()) {
			this.messages = Collections.emptyList();
		} else {
			this.messages = Collections.unmodifiableList(messages);
		}
	}

	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		String allErrors = combineMessages(65);
		createMessageControl(container, allErrors);
		setControl(container);
		UIUtil.getHelpSystem().setHelp(container,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_PROBLEM));
	}

	/**
	 * This implementation returns false
	 */
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	/**
	 * This implementation returns false
	 */
	@Override
	public boolean isPageComplete() {
		return false;
	}

	/**
	 * Creates a control showing the input and adds to container.
	 * 
	 * @param container
	 *            the {@link Composite} to add to
	 * @param input
	 *            the String input to display
	 */
	protected void createMessageControl(Composite container, String input) {
		Text text = new Text(container, SWT.LEFT | SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL);
		text.setText(input);
		text.setVisible(true);
		text.setEditable(false);
		text.setFont(container.getFont());
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	/**
	 * Join messages in {@link #messages} into one String for display.
	 * 
	 * @param lineWidth
	 *            the int proposed "line width"
	 * @return non-null String containing all messages
	 */
	protected String combineMessages(int lineWidth) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		boolean useNumberPrefix = 1 < messages.size();
		for (String error : messages) {
			if (useNumberPrefix) {
				sb.append("(");
				sb.append(++count);
				sb.append(") ");
			}
			StringUtil.lineate(error, lineWidth, sb);
			sb.append(StringUtil.EOL);
		}
		return sb.toString();
	}
}
