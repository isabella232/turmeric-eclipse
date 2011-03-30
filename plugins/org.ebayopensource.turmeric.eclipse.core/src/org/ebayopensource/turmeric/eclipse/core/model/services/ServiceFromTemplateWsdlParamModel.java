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
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.TemplateBinding;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;


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

	/**
	 * 
	 * @return A URL representation of the template file
	 */
	public URL getTemplateFile() {
		return templateFile;
	}

	/**
	 * 
	 * @param templateFile a URL representation of a template file
	 */
	public void setTemplateFile(URL templateFile) {
		this.templateFile = templateFile;
	}

	/**
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
	 * 
	 * @return A set of Binding objects
	 */
	public Set<Binding> getBindings() {
		return bindings;
	}

	/**
	 * 
	 * @param binding a Binding object to be added.
	 */
	public void addBinding(Binding binding) {
		this.bindings.add(binding);
	}

	/**
	 * 
	 * @return a List of Operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * 
	 * @param operation an Operation to be added
	 */
	public void addOperation(Operation operation) {
		this.operations.add(operation);
	}

	/**
	 * 
	 * @param bindings A Set of Bindings
	 */
	public void setBindings(Set<Binding> bindings) {
		this.bindings = bindings;
	}

	/**
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
		 * 
		 */
		public Operation() {
			super();
		}

		/**
		 * 
		 * @param name operation name
		 * @param outputParameter output Parameter
		 */
		public Operation(String name, Parameter outputParameter) {
			this(name, null, outputParameter);
		}

		/**
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
		 * 
		 * @return the Operation name
		 */
		public String getName() {
			return name;
		}

		/**
		 * 
		 * @param name the operation name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 
		 * @return the output Parameter
		 */
		public Parameter getOutputParameter() {
			return outputParameter;
		}

		/**
		 * 
		 * @param outputParameter the output Parameter
		 */
		public void setOutputParameter(Parameter outputParameter) {
			this.outputParameter = outputParameter;
		}

		/**
		 * 
		 * @return the input Parameter
		 */
		public Parameter getInputParameter() {
			return inputParameter;
		}

		/**
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
		public String getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getDatatype() {
			return datatype;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setDatatype(Object datatype) {
			this.datatype = datatype;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getMinOccurs() {
			return minOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
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
		public int getMaxOccurs() {
			return maxOccurs;
		}

		/**
		 * {@inheritDoc}
		 */
		public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException {
			if (maxOccurs >= 0 && maxOccurs < minOccurs)
				throw new IllegalArgumentException(
						"The specified max occurs is less than the current min occurs->"
								+ minOccurs);
			this.maxOccurs = maxOccurs;
		}
	}

	/**
	 * 
	 * 
	 *
	 */
	public static class Parameter extends TemplateWSDLModel {
		private String name;
		private List<IParameterElement> elements = new ArrayList<IParameterElement>();

		/**
		 * 
		 */
		public Parameter() {
			super();
		}

		/**
		 * 
		 * @param name the parameter name
		 */
		public Parameter(String name) {
			super();
			this.name = name;
		}

		/**
		 * 
		 * @return the parameter name
		 */
		public String getName() {
			return name;
		}

		/**
		 * 
		 * @param name the parameter name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 
		 * @return a List of IParameterElement objects
		 */
		public List<IParameterElement> getElements() {
			return elements;
		}

		/**
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
	 * 
	 * 
	 *
	 */
	public static class Binding extends TemplateWSDLModel {
		private TemplateBinding binding;


		/**
		 * 
		 */
		public Binding() {
			super();
		}

		/**
		 * 
		 * @param binding a TemplateBinding
		 */
		public Binding(TemplateBinding binding) {
			super();
			this.binding = binding;
		}

		/**
		 * 
		 * @param binding a TemplateBinding
		 */
		public void setBinding(TemplateBinding binding) {
			this.binding = binding;
		}

		/**
		 * 
		 * @return the TemplateBinding
		 */
		public TemplateBinding getBinding() {
			return binding;
		}
	}
}
