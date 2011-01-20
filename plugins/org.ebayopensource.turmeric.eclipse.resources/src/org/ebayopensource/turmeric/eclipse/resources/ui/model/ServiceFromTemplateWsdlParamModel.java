/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.resources.ui.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.TemplateBinding;


/**
 * @author yayu
 * 
 */
public class ServiceFromTemplateWsdlParamModel extends
ServiceFromWsdlParamModel {

	private URL templateFile;
	private Set<Binding> bindings = new HashSet<Binding>();
	private List<Operation> operations = new ArrayList<Operation>();

	/**
	 * 
	 */
	public ServiceFromTemplateWsdlParamModel() {
		super();
	}

	public URL getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(URL templateFile) {
		this.templateFile = templateFile;
	}

	public Set<TemplateBinding> getSelBindings() {
		Set<TemplateBinding> selBindings = new HashSet<TemplateBinding>();
		for (Binding binding : bindings) {
			selBindings.add(binding.getBinding());
		}
		return selBindings;
	}

	public Set<Binding> getBindings() {
		return bindings;
	}

	public void addBinding(Binding binding) {
		this.bindings.add(binding);
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void addOperation(Operation operation) {
		this.operations.add(operation);
	}

	public void setBindings(Set<Binding> bindings) {
		this.bindings = bindings;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public static Operation createOperation(String opName) {
		final Parameter stringParam = new Parameter(opName
				+ SOAProjectConstants.PARAMETER_OUTPUT_SUFFIX);
		final ParameterElement elem = new ParameterElement("output", "string");
		stringParam.getElements().add(elem);
		
		final Parameter inputParam = new Parameter(opName
				+ SOAProjectConstants.PARAMETER_INPUT_SUFFIX);
		final Operation operation = new Operation(opName, inputParam, stringParam);
		return operation;
	}

	public static Collection<Binding> getDefaultBindings() {
		final Collection<Binding> result = new ArrayList<Binding>(2);
		result.add(new Binding(TemplateBinding.HTTP));
		result.add(new Binding(TemplateBinding.SOAP));
		return result;
	}

	// ///////////////////////////////////////////
	// Inner Classes
	// ///////////////////////////////////////////
	public static abstract class TemplateWSDLModel extends
			BaseServiceParamModel {
		public boolean validate() {
			return true;
		}
	}

	public static class Operation extends TemplateWSDLModel {
		private String name;
		private Parameter outputParameter;
		private Parameter inputParameter;

		public Operation() {
			super();
		}

		public Operation(String name, Parameter outputParameter) {
			this(name, null, outputParameter);
		}

		public Operation(String name, Parameter inputParameter,
				Parameter outputParameter) {
			super();
			if (StringUtils.isBlank(name))
				throw new IllegalArgumentException(
						"Operation name must not be null or empty->" + name);
			if (outputParameter == null)
				throw new IllegalArgumentException(
						"Output parameter must not be null");
			this.name = name;
			this.inputParameter = inputParameter;
			this.outputParameter = outputParameter;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Parameter getOutputParameter() {
			return outputParameter;
		}

		public void setOutputParameter(Parameter outputParameter) {
			this.outputParameter = outputParameter;
		}

		public Parameter getInputParameter() {
			return inputParameter;
		}

		public void setInputParameter(Parameter inputParameter) {
			this.inputParameter = inputParameter;
		}
	}

	public static class ParameterElement extends TemplateWSDLModel implements
			IParameterElement {
		private String name;
		private Object datatype;
		private int minOccurs = 1;
		// less than zero means unbounded
		private int maxOccurs = 1;

		public ParameterElement(String name, String datatype) {
			super();
			this.name = name;
			this.datatype = datatype;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getDatatype() {
			return datatype;
		}

		public void setDatatype(Object datatype) {
			this.datatype = datatype;
		}

		public int getMinOccurs() {
			return minOccurs;
		}

		public void setMinOccurs(int minOccurs) throws IllegalArgumentException {
			if (minOccurs < 0)
				throw new IllegalArgumentException(
						"The min occurs must be greater than zero->"
								+ minOccurs);

			if (maxOccurs >= 0 && minOccurs > maxOccurs)
				throw new IllegalArgumentException(
						"The min occurs can not be greater than max occurs->"
								+ maxOccurs);
			this.minOccurs = minOccurs;
		}

		public int getMaxOccurs() {
			return maxOccurs;
		}

		public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException {
			if (maxOccurs >= 0 && maxOccurs < minOccurs)
				throw new IllegalArgumentException(
						"The specified max occurs is less than the current min occurs->"
								+ minOccurs);
			this.maxOccurs = maxOccurs;
		}
	}

	public static class Parameter extends TemplateWSDLModel {
		private String name;
		private List<IParameterElement> elements = new ArrayList<IParameterElement>();

		public Parameter() {
			super();
		}

		public Parameter(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<IParameterElement> getElements() {
			return elements;
		}

		public void setElements(List<? extends IParameterElement> elements) {
			if (elements != null) {
				this.elements.clear();
				this.elements.addAll(elements);
			}
		}
	}

	public static class Binding extends TemplateWSDLModel {
		private TemplateBinding binding;

		public Binding() {
			super();
		}

		public Binding(TemplateBinding binding) {
			super();
			this.binding = binding;
		}

		public void setBinding(TemplateBinding binding) {
			this.binding = binding;
		}

		public TemplateBinding getBinding() {
			return binding;
		}
	}
}
