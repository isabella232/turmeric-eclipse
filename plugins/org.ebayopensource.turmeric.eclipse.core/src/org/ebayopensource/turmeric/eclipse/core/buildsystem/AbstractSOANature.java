/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.buildsystem;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;


/**
 * The common configuration code goes here. Implementors are supposed to just
 * override the builder name in most cases., at least for soa use cases.
 * 
 * @author smathew
 * 
 */
public abstract class AbstractSOANature implements IProjectNature {
	private IProject project;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure() throws CoreException {
		final IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		if (Arrays.asList(commands).contains(getBuilderName())) {
			return;
		}
		
		final ICommand command = desc.newCommand();
		command.setBuilderName(getBuilderName());
		desc.setBuildSpec(addCommand(commands, command));
		commands = desc.getBuildSpec();
		project.setDescription(desc, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deconfigure() throws CoreException {
		final IProjectDescription description = getProject().getDescription();
		final ICommand[] commands = description.getBuildSpec();
		description.setBuildSpec(removeCommand(commands, getBuilderName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject getProject() {
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProject(final IProject soaProject) {
		this.project = soaProject;
	}

	/**
	 * Child Classes should define this method. This is how the nature class
	 * would know the id/name of the builder
	 *
	 * @return the builder name
	 */
	public abstract String getBuilderName();

	/**
	 * Adds the new command to the start of the array. As such JDT has a habit
	 * of inserting command in the top and we would like to have our builders at
	 * the top because JDT builder should pitch in after our builder has
	 * finished the job
	 *
	 * @param commands the commands
	 * @param commandToBeAdded the command to be added
	 * @return the i command[]
	 */
	protected ICommand[] addCommand(ICommand[] commands,
			ICommand commandToBeAdded) {
		ICommand retCommands[] = new ICommand[commands.length + 1];
		retCommands[0] = commandToBeAdded;
		System.arraycopy(commands, 0, retCommands, 1, commands.length);
		return retCommands;
	}

	/**
	 * Removes the build command that has the specified builder ID and then
	 * return the remaining commands. It will return the original command array
	 * if the specified command could not be found.
	 *
	 * @param commands the commands
	 * @param builderId the builder id
	 * @return the i command[]
	 */
	protected ICommand[] removeCommand(ICommand[] commands, String builderId) {
		final List<ICommand> retCommands = ListUtil.arrayList(commands);
		for (Iterator<ICommand> it = retCommands.iterator(); it.hasNext();) {
			final ICommand cmd = it.next();
			if (cmd.getBuilderName().equals(builderId)) {
				it.remove();
			}
		}
		return retCommands.toArray(new ICommand[0]);
	}

}
