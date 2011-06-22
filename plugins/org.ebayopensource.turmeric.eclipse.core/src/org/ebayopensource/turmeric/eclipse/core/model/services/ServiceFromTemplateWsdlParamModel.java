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
package org.ebayopensource.turmeric.eclipse.core.model.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.TemplateBinding;


/**
 * The Class ServiceFromTemplateWsdlParamModel.
 *
 * @author yayu
 * 
 */
public class ServiceFromTemplateWsdlParamModel extends
ServiceFromWsdlParamModel {

	private URL templateFile;
	private Set<Binding> bindings = new HashSet<Binding>();
	private List<Operation> operations = new ArrayList<Operation>();

	/**
	 * Instantiates a new service from template wsdl param model.
	 */
	public ServiceFromTemplateWsdlParamModel() {
		super();
	}

	/**
	 * Gets the template file.
	 *
	 * @return A URL representation of the template file
	 */
	public URL getTemplateFile() {
		return templateFile;
	}

	/**
	 * Sets the template file.
	 *
	 * @param templateFile a URL representation of a template file
	 */
	public void setTemplateFile(URL templateFile) {
		this.templateFile = templateFile;
	}

	/**
	 * Gets the sel bindings.
	 *
	 * @return a Set of TemplateBinding objects
	 */
	public Set<TemplateBinding> getSelBindings() {
		Set<TemplateBinding> selBindings = new HashSet<TemplateBinding>();
		for (Binding binding : bindings) {
			selBindings.add(binding.getBinding());
		}
		return selBindings;
	}

	/**
	 * Gets the bindings.
	 *
	 * @return A set of Binding objects
	 */
	public Set<Binding> getBindings() {
		return bindings;
	}

	/**
	 * Adds the binding.
	 *
	 * @param binding a Binding object to be added.
	 */
	public void addBinding(Binding binding) {
		this.bindings.add(binding);
	}

	/**
	 * Gets the operations.
	 *
	 * @return a List of Operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * Adds the operation.
	 *
	 * @param operation an Operation to be added
	 */
	public void addOperation(Operation operation) {
		this.operations.add(operation);
	}

	/**
	 * Sets the bindings.
	 *
	 * @param bindings A Set of Bindings
	 */
	public void setBindings(Set<Binding> bindings) {
		this.bindings = bindings;
	}

	/**
	 * Sets the operations.
	 *
	 * @param operations A List of Operation objects
	 */
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	/**
	 * Creates a new operation.
	 * @param opName operation name
	 * @return an instance of an operation.
	 */
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

	/**
	 * Gets the default bindings.
	 *
	 * @return A Collection of Binding, typically TemplateBindings for HTTP and SOAP
	 */
	public static Collection<Binding> getDefaultBindings() {
		final Collection<Binding> result = new ArrayList<Binding>(2);
		result.add(new Binding(TemplateBinding.HTTP));
		result.add(new Binding(TemplateBinding.SOAP));
		return result;
	}

	// ///////////////////////////////////////////
	// Inner Classes
	// ///////////////////////////////////////////
	/**
	 * 
	 * A class representing the Template WSDL.
	 *
	 */
	public static abstract class TemplateWSDLModel extends
			BaseServiceParamModel {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean validate() {
			return true;
		}
	}

	/**
	 * 
	 * Represents and Operation.
	 *
	 */
	public static class Operation extends TemplateWSDLModel {
		private String name;
		private Parameter outputParameter;
		private Parameter inputParameter;

		/**
		 * Instantiates a new operation.
		 */
		public Operation() {
			super();
		}

		/**
		 * Instantiates a new operation.
		 *
		 * @param name operation name
		 * @param outputParameter output Parameter
		 */
		public Operation(String name, Parameter outputParameter) {
			this(name, null, outputParameter);
		}

		/**
		 * Instantiates a new operation.
		 *
		 * @param name operation name
		 * @param inputParameter input Parameter
		 * @param outputParameter output Parameter
		 */
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

		/**
		 * Gets the name.
		 *
		 * @return the Operation name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the operation name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the output parameter.
		 *
		 * @return the output Parameter
		 */
		public Parameter getOutputParameter() {
			return outputParameter;
		}

		/**
		 * Sets the output parameter.
		 *
		 * @param outputParameter the output Parameter
		 */
		public void setOutputParameter(Parameter outputParameter) {
			this.outputParameter = outputParameter;
		}

		/**
		 * Gets the input parameter.
		 *
		 * @return the input Parameter
		 */
		public Parameter getInputParameter() {
			return inputParameter;
		}

		/**
		 * Sets the input parameter.
		 *
		 * @param inputParameter the input Parameter
		 */
		public void setInputParameter(Parameter inputParameter) {
			this.inputParameter = inputParameter;
		}
	}

	/**
	 * 
	 * Represents a Parameter Element.
	 *
	 */
	public static class ParameterElement extends TemplateWSDLModel implements
			IParameterElement {
		private String name;
		private Object datatype;
		private int minOccurs = 1;
		// less than zero means unbounded
		private int maxOccurs = 1;

		/**
		 * Instantiates a new parameter element.
		 *
		 * @param name the element name
		 * @param datatype the data type
		 */
		public ParameterElement(String name, String datatype) {
			super();
			this.name = name;
			this.datatype = datatype;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getDatatype() {
			return datatype;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDatatype(Object datatype) {
			this.datatype = datatype;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMinOccurs() {
			return minOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMaxOccurs() {
			return maxOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException {
			if (maxOccurs >= 0 && maxOccurs < minOccurs)
				throw new IllegalArgumentException(
						"The specified max occurs is less than the current min occurs->"
								+ minOccurs);
			this.maxOccurs = maxOccurs;
		}
	}

	/**
	 * The Class Parameter.
	 */
	public static class Parameter extends TemplateWSDLModel {
		private String name;
		private List<IParameterElement> elements = new ArrayList<IParameterElement>();

		/**
		 * Instantiates a new parameter.
		 */
		public Parameter() {
			super();
		}

		/**
		 * Instantiates a new parameter.
		 *
		 * @param name the parameter name
		 */
		public Parameter(String name) {
			super();
			this.name = name;
		}

		/**
		 * Gets the name.
		 *
		 * @return the parameter name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the parameter name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the elements.
		 *
		 * @return a List of IParameterElement objects
		 */
		public List<IParameterElement> getElements() {
			return elements;
		}

		/**
		 * Sets the elements.
		 *
		 * @param elements A List of elements
		 */
		public void setElements(List<? extends IParameterElement> elements) {
			if (elements != null) {
				this.elements.clear();
				this.elements.addAll(elements);
			}
		}
	}

	/**
	 * The Class Binding.
	 */
	public static class Binding extends TemplateWSDLModel {
		private TemplateBinding binding;


		/**
		 * Instantiates a new binding.
		 */
		public Binding() {
			super();
		}

		/**
		 * Instantiates a new binding.
		 *
		 * @param binding a TemplateBinding
		 */
		public Binding(TemplateBinding binding) {
			super();
			this.binding = binding;
		}

		/**
		 * Sets the binding.
		 *
		 * @param binding a TemplateBinding
		 */
		public void setBinding(TemplateBinding binding) {
			this.binding = binding;
		}

		/**
		 * Gets the binding.
		 *
		 * @return the TemplateBinding
		 */
		public TemplateBinding getBinding() {
			return binding;
		}
	}
}
