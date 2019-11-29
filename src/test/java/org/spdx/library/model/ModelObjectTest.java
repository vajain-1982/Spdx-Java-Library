/**
 * Copyright (c) 2019 Source Auditor Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.spdx.library.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.spdx.library.InvalidSPDXAnalysisException;
import org.spdx.library.SpdxConstants;
import org.spdx.storage.IModelStore.IdType;
import org.spdx.storage.IModelStore.ModelUpdate;
import org.spdx.storage.simple.InMemSpdxStore;

import junit.framework.TestCase;

/**
 * @author gary
 *
 */
public class ModelObjectTest extends TestCase {

	private static final String TEST_DOCUMENT_URI = "https://test.document.uri";
	private static final String TEST_ID = "testId";
	private static final Object TEST_VALUE1 = "value1";
	private static final String TEST_PROPERTY1 = "property1";
	static final String TEST_TYPE1 = SpdxConstants.CLASS_SPDX_LICENSE_EXCEPTION;
	static final String TEST_TYPE2 = SpdxConstants.CLASS_SPDX_EXTRACTED_LICENSING_INFO;

	static final String[] TEST_STRING_VALUE_PROPERTIES = new String[] {"valueProp1", "valueProp2", "valueProp3"};
	static final Object[] TEST_STRING_VALUE_PROPERTY_VALUES = new Object[] {"value1", "value2", "value3"};
	static final String[] TEST_BOOLEAN_VALUE_PROPERTIES = new String[] {"boolProp1", "boolProp2"};
	static final Object[] TEST_BOOLEAN_VALUE_PROPERTY_VALUES = new Object[] {true, false};
	static final String[] TEST_LIST_PROPERTIES = new String[] {"listProp1", "listProp2", "listProp3"};
	static final String[] TEST_TYPED_PROPERTIES = new String[] {"typeProp1", "typeProp2"};
	TypedValue[] TEST_TYPED_PROP_VALUES;
	List<?>[] TEST_LIST_PROPERTY_VALUES;
	Map<String, Object> ALL_PROPERTY_VALUES;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		TEST_LIST_PROPERTY_VALUES = new List<?>[] {Arrays.asList("ListItem1", "listItem2", "listItem3"), 
			Arrays.asList(true, false, true),
			Arrays.asList(new TypedValue[] {new TypedValue(TEST_DOCUMENT_URI, "typeId1", TEST_TYPE1), new TypedValue(TEST_DOCUMENT_URI, "typeId2", TEST_TYPE2)})};
			TEST_TYPED_PROP_VALUES = new TypedValue[] {new TypedValue(TEST_DOCUMENT_URI, "typeId1", TEST_TYPE1), new TypedValue(TEST_DOCUMENT_URI, "typeId2", TEST_TYPE2)};
			ALL_PROPERTY_VALUES = new HashMap<>();
			for (int i = 0; i < TEST_STRING_VALUE_PROPERTIES.length; i++) {
				ALL_PROPERTY_VALUES.put(TEST_STRING_VALUE_PROPERTIES[i], TEST_STRING_VALUE_PROPERTY_VALUES[i]);
			}
			for (int i = 0; i < TEST_BOOLEAN_VALUE_PROPERTIES.length; i++) {
				ALL_PROPERTY_VALUES.put(TEST_BOOLEAN_VALUE_PROPERTIES[i], TEST_BOOLEAN_VALUE_PROPERTY_VALUES[i]);
			}
			for (int i = 0; i < TEST_LIST_PROPERTIES.length; i++) {
				ALL_PROPERTY_VALUES.put(TEST_LIST_PROPERTIES[i], TEST_LIST_PROPERTY_VALUES[i]);
			}
			for (int i = 0; i < TEST_TYPED_PROPERTIES.length; i++) {
				ALL_PROPERTY_VALUES.put(TEST_TYPED_PROPERTIES[i], TEST_TYPED_PROP_VALUES[i]);
			}
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void addTestValues(ModelObject mo) throws InvalidSPDXAnalysisException {
		for (Entry<String, Object> entry:ALL_PROPERTY_VALUES.entrySet()) {
			mo.setPropertyValue(entry.getKey(), entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean compareLists(Object olist1, Object olist2) throws InvalidSPDXAnalysisException {
		List<Object> list1;
		if (olist1 instanceof List) {
			if (((List<Object>)(olist1)).size() > 0 && ((List<Object>)(olist1)).get(0) instanceof ModelObject) {
				// convert type TypedValue
				list1 = new ArrayList<Object>();
				for (Object o:((List<Object>)(olist1))) {
					list1.add(((ModelObject)o).toTypedValue());
				}
			} else {
				list1 = (List<Object>)(olist1);
			}
		} else if (olist1 instanceof Object[]) {
			if (((Object[])olist1).length > 0 && ((Object[])olist1)[0] instanceof ModelObject) {
				list1 = new ArrayList<Object>();
				for (Object o:((Object[])olist1)) {
					list1.add(((ModelObject)o).toTypedValue());
				}
			} else {
				list1 = Arrays.asList(olist1);
			}
		} else {
			return false;
		}
		List<Object> list2;
		if (olist2 instanceof List) {
			if (((List<Object>)(olist2)).size() > 0 && ((List<Object>)(olist2)).get(0) instanceof ModelObject) {
				// convert type TypedValue
				list2 = new ArrayList<Object>();
				for (Object o:((List<Object>)(olist2))) {
					list2.add(((ModelObject)o).toTypedValue());
				}
			} else {
				list2 = (List<Object>)(olist2);
			}
		} else if (olist2 instanceof Object[]) {
			if (((Object[])olist2).length > 0 && ((Object[])olist2)[0] instanceof ModelObject) {
				list2 = new ArrayList<Object>();
				for (Object o:((Object[])olist2)) {
					list2.add(((ModelObject)o).toTypedValue());
				}
			} else {
				list2 = Arrays.asList(olist2);
			}
		} else {
			return false;
		}
		assertEquals(list1.size(), list2.size());
		if (list1.size() > 0 && list1.get(0) instanceof ModelObject) {
			// convert to type
		}
		for (Object list1item:list1) {
			if (!list2.contains(list1item)) {
				return false;
			}
		}
		return true;
	}
	
	public void testModelObjectCreate() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		try {
			new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, false);
			fail("This should not have worked since created is set to false and the ID does not exist");
		} catch (InvalidSPDXAnalysisException ex) {
			// expected
		}
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		gmo.setPropertyValue(TEST_PROPERTY1, TEST_VALUE1);
		GenericModelObject gmo2 = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, false);
		assertTrue(gmo2.getStringPropertyValue(TEST_PROPERTY1).isPresent());
		assertEquals(gmo2.getStringPropertyValue(TEST_PROPERTY1).get(), TEST_VALUE1);
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getDocumentUri()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	public void testGetDocumentUri() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		assertEquals(TEST_DOCUMENT_URI, gmo.getDocumentUri());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getId()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	public void testGetId() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		assertEquals(TEST_ID, gmo.getId());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getModelStore()}.
	 */
	public void testGetModelStore() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		assertEquals(store, gmo.getModelStore());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getPropertyValueNames()}.
	 */
	public void testGetPropertyValueNames() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		List<String> result = gmo.getPropertyValueNames();
		assertEquals(0, result.size());
		addTestValues(gmo);
		result = gmo.getPropertyValueNames();
		assertEquals(ALL_PROPERTY_VALUES.size(), result.size());
		for (String property:ALL_PROPERTY_VALUES.keySet()) {
			assertTrue(result.contains(property));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getObjectPropertyValue(java.lang.String)}.
	 */
	public void testGetObjectPropertyValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		assertEquals(0, gmo.getPropertyValueNames().size());
		addTestValues(gmo);
		for (Entry<String, Object> entry:ALL_PROPERTY_VALUES.entrySet()) {
			Optional<Object> result = gmo.getObjectPropertyValue(entry.getKey());
			assertTrue(result.isPresent());
			if (result.get() instanceof List) {
				assertTrue(compareLists(entry.getValue(), result.get()));
			} else if (result.get() instanceof ModelObject) {
				assertEquals(entry.getValue(), ((ModelObject)(result.get())).toTypedValue());
			} else {
				assertEquals(entry.getValue(), result.get());
			}
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#setPropertyValue(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)}.
	 */
	public void testSetPropertyValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		String prop = "property";
		String val = "value";
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
		gmo.setPropertyValue(prop, val);
		assertTrue(gmo.getObjectPropertyValue(prop).isPresent());
		assertEquals(val, gmo.getObjectPropertyValue(prop).get());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updatePropertyValue(java.lang.String, java.lang.Object)}.
	 */
	public void testUpdatePropertyValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		String prop = "property";
		String val = "value";
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
		ModelUpdate mu = gmo.updatePropertyValue(prop, val);
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
		mu.apply();
		assertTrue(gmo.getObjectPropertyValue(prop).isPresent());
		assertEquals(val, gmo.getObjectPropertyValue(prop).get());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getStringPropertyValue(java.lang.String)}.
	 */
	public void testGetStringPropertyValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		for (int i = 0; i < TEST_STRING_VALUE_PROPERTIES.length; i++) {
			assertEquals(TEST_STRING_VALUE_PROPERTY_VALUES[i], gmo.getStringPropertyValue(TEST_STRING_VALUE_PROPERTIES[i]).get());
		}
		try {
			gmo.getStringPropertyValue(TEST_BOOLEAN_VALUE_PROPERTIES[0]);
			fail("No exception on getting the wrong type");
		} catch(SpdxInvalidTypeException ex) {
			// expected
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getBooleanPropertyValue(java.lang.String)}.
	 */
	public void testGetBooleanPropertyValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		for (int i = 0; i < TEST_BOOLEAN_VALUE_PROPERTIES.length; i++) {
			assertEquals(TEST_BOOLEAN_VALUE_PROPERTY_VALUES[i], gmo.getBooleanPropertyValue(TEST_BOOLEAN_VALUE_PROPERTIES[i]).get());
		}
		try {
			gmo.getBooleanPropertyValue(TEST_STRING_VALUE_PROPERTIES[0]);
			fail("No exception on getting the wrong type");
		} catch(SpdxInvalidTypeException ex) {
			// expected
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#removeProperty(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testRemovePropertyIModelStoreStringStringString() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		String prop = "property";
		String val = "value";
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
		gmo.setPropertyValue(prop, val);
		assertTrue(gmo.getObjectPropertyValue(prop).isPresent());
		assertEquals(val, gmo.getObjectPropertyValue(prop).get());
		gmo.removeProperty(prop);
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updateRemoveProperty(java.lang.String)}.
	 */
	public void testUpdateRemoveProperty() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		String prop = "property";
		String val = "value";
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
		gmo.setPropertyValue(prop, val);
		assertTrue(gmo.getObjectPropertyValue(prop).isPresent());
		assertEquals(val, gmo.getObjectPropertyValue(prop).get());
		ModelUpdate mu = gmo.updateRemoveProperty(prop);
		assertTrue(gmo.getObjectPropertyValue(prop).isPresent());
		assertEquals(val, gmo.getObjectPropertyValue(prop).get());
		mu.apply();
		assertFalse(gmo.getObjectPropertyValue(prop).isPresent());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#clearPropertyValueList(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testClearPropertyValueList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		gmo.clearPropertyValueList(TEST_LIST_PROPERTIES[0]);
		assertEquals(0, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0]).size());
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updateClearPropertyValueList(java.lang.String)}.
	 */
	public void testUpdateClearPropertyValueList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		ModelUpdate mu = gmo.updateClearPropertyValueList(TEST_LIST_PROPERTIES[0]);
		for (int i = 0; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
		mu.apply();
		assertEquals(0, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0]).size());
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#addPropertyValueToList(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)}.
	 */
	public void testAddPropertyValueToList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		@SuppressWarnings("unchecked")
		List<String> expected = new ArrayList<String>((List<String>)TEST_LIST_PROPERTY_VALUES[0]);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		String newValue = "newValue";
		expected.add(newValue);
		gmo.addPropertyValueToList(TEST_LIST_PROPERTIES[0], newValue);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updateAddPropertyValueToList(java.lang.String, java.lang.Object)}.
	 */
	public void testUpdateAddPropertyValueToList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		@SuppressWarnings("unchecked")
		List<String> expected = new ArrayList<String>((List<String>)TEST_LIST_PROPERTY_VALUES[0]);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		String newValue = "newValue";
		ModelUpdate mu = gmo.updateAddPropertyValueToList(TEST_LIST_PROPERTIES[0], newValue);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		expected.add(newValue);
		mu.apply();
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#replacePropertyValueList(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String, java.util.List)}.
	 */
	public void testReplacePropertyValueList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		@SuppressWarnings("unchecked")
		List<String> expected = new ArrayList<String>((List<String>)TEST_LIST_PROPERTY_VALUES[0]);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		expected = Arrays.asList("newList1", "newList2");
		gmo.setPropertyValue(TEST_LIST_PROPERTIES[0], expected);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updateReplacePropertyValueList(java.lang.String, java.util.List)}.
	 */
	public void testUpdateReplacePropertyValueList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[0], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		List<String> expected = Arrays.asList("newList1", "newList2");
		ModelUpdate mu = gmo.updatePropertyValue(TEST_LIST_PROPERTIES[0], expected);
		assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[0], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		mu.apply();
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#removePropertyValueFromList(org.spdx.storage.IModelStore, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)}.
	 */
	public void testRemovePropertyValueFromList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		@SuppressWarnings("unchecked")
		List<Object> expected = new ArrayList<Object>((List<Object>)TEST_LIST_PROPERTY_VALUES[0]);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		Object removed = expected.get(0);
		expected.remove(removed);
		gmo.removePropertyValueFromList(TEST_LIST_PROPERTIES[0], removed);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#updateRemovePropertyValueFromList(java.lang.String, java.lang.Object)}.
	 */
	public void testUpdateRemovePropertyValueFromList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		@SuppressWarnings("unchecked")
		List<Object> expected = new ArrayList<Object>((List<Object>)TEST_LIST_PROPERTY_VALUES[0]);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		Object removed = expected.get(0);
		ModelUpdate mu = gmo.updateRemovePropertyValueFromList(TEST_LIST_PROPERTIES[0], removed);
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		expected.remove(removed);
		mu.apply();
		assertTrue(compareLists(expected, gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[0])));
		for (int i = 1; i < TEST_LIST_PROPERTIES.length; i++) {
			assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[i], gmo.getObjectPropertyValueList(TEST_LIST_PROPERTIES[i])));
		}
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#getStringPropertyValueList(java.lang.String)}.
	 */
	public void testGetStringPropertyValueList() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		List<String> result = gmo.getStringPropertyValueList(TEST_LIST_PROPERTIES[0]);
		assertTrue(compareLists(TEST_LIST_PROPERTY_VALUES[0], result));
		try {
			gmo.getStringPropertyValueList(TEST_LIST_PROPERTIES[1]);
			fail("No exception on getting the wrong type");
		} catch(SpdxInvalidTypeException ex) {
			// expected
		}
		result = gmo.getStringPropertyValueList("not used");
		assertEquals(0, result.size());
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#equivalent(org.spdx.library.model.ModelObject)}.
	 */
	public void testEquivalent() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		assertTrue(gmo.equivalent(gmo));
		// same store
		GenericModelObject gmo2 = new GenericModelObject(store, TEST_DOCUMENT_URI, "TestId2", true);
		addTestValues(gmo2);
		assertTrue(gmo.equivalent(gmo2));
		assertTrue(gmo2.equivalent(gmo));
		// different store
		InMemSpdxStore store2 = new InMemSpdxStore();
		GenericModelObject gmo3 = new GenericModelObject(store2, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo3);
		assertTrue(gmo.equivalent(gmo3));
		assertTrue(gmo3.equivalent(gmo2));
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		assertTrue(gmo.equals(gmo));
		// different ID's
		GenericModelObject gmo2 = new GenericModelObject(store, TEST_DOCUMENT_URI, "TestId2", true);
		addTestValues(gmo2);
		assertFalse(gmo.equals(gmo2));
		assertFalse(gmo2.equals(gmo));
		// same ID's, different store
		InMemSpdxStore store2 = new InMemSpdxStore();
		GenericModelObject gmo3 = new GenericModelObject(store2, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo3);
		assertTrue(gmo.equals(gmo3));
		assertTrue(gmo3.equals(gmo));
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#clone()}.
	 */
	public void testClone() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		InMemSpdxStore store2 = new InMemSpdxStore();
		ModelObject result = gmo.clone(store2);
		assertTrue(result instanceof GenericModelObject);
		assertTrue(result.equals(gmo));
		assertTrue(result.equivalent(gmo));
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#copyFrom(org.spdx.library.model.ModelObject)}.
	 */
	public void testCopyFrom() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		GenericModelObject gmo2 = new GenericModelObject(store, TEST_DOCUMENT_URI, "id2", true);
		gmo2.copyFrom(gmo);
		assertTrue(gmo.equivalent(gmo2));
		// different store
		InMemSpdxStore store2 = new InMemSpdxStore();
		GenericModelObject gmo3 = new GenericModelObject(store2, TEST_DOCUMENT_URI, TEST_ID, true);
		gmo3.copyFrom(gmo3);
		assertTrue(gmo.equivalent(gmo3));
		assertTrue(gmo3.equivalent(gmo2));
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#idToIdType(java.lang.String)}.
	 */
	public void testIdToIdType() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		assertEquals(IdType.Anonomous, gmo.idToIdType("anything"));
		assertEquals(IdType.DocumentRef, gmo.idToIdType(SpdxConstants.EXTERNAL_DOC_REF_PRENUM + "12"));
		assertEquals(IdType.LicenseRef, gmo.idToIdType(SpdxConstants.NON_STD_LICENSE_ID_PRENUM + "12"));
		assertEquals(IdType.ListedLicense, gmo.idToIdType("Apache-2.0"));
		assertEquals(IdType.Literal, gmo.idToIdType(SpdxConstants.NONE_VALUE));
		assertEquals(IdType.Literal, gmo.idToIdType(SpdxConstants.NOASSERTION_VALUE));
		assertEquals(IdType.SpdxId, gmo.idToIdType(SpdxConstants.SPDX_ELEMENT_REF_PRENUM + "12"));
	}

	/**
	 * Test method for {@link org.spdx.library.model.ModelObject#toTypedValue()}.
	 */
	public void testToTypeValue() throws InvalidSPDXAnalysisException {
		InMemSpdxStore store = new InMemSpdxStore();
		GenericModelObject gmo = new GenericModelObject(store, TEST_DOCUMENT_URI, TEST_ID, true);
		addTestValues(gmo);
		TypedValue result = gmo.toTypedValue();
		assertEquals(TEST_DOCUMENT_URI, result.getDocumentUri());
		assertEquals(TEST_ID, result.getId());
		assertEquals(gmo.getType(), result.getType());
	}

}