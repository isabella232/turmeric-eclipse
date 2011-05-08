/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class CollectionUtil.
 *
 * @author smathew
 */
public class CollectionUtil {

	/**
	 * Intersection.
	 *
	 * @param <T> the generic type
	 * @param set1 the set1
	 * @param set2 the set2
	 * @return Uses the intersection paradigm. Non destructive
	 */
	public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
		Set<T> tempSet = new HashSet<T>(set1);
		tempSet.retainAll(set2);
		return tempSet;
	}

	/**
	 * Two way diff.
	 *
	 * @param <T> the generic type
	 * @param set1 the set1
	 * @param set2 the set2
	 * @return Uses the diff paradigm. Non destructive set1-set2+set2-set1
	 */
	public static <T> Set<T> twoWayDiff(Set<T> set1, Set<T> set2) {
		Set<T> tempSet1 = new HashSet<T>(set1);
		tempSet1.removeAll(set2);
		Set<T> tempSet2 = new HashSet<T>(set2);
		tempSet2.removeAll(set1);
		tempSet1.addAll(tempSet2);
		return tempSet1;
	}

	/**
	 * One way diff.
	 *
	 * @param <T> the generic type
	 * @param set1 the set1
	 * @param set2 the set2
	 * @return Uses the diff paradigm. Non destructive set1 - set2
	 */
	public static <T> Set<T> oneWayDiff(Set<T> set1, Set<T> set2) {
		Set<T> tempSet1 = new HashSet<T>(set1);
		tempSet1.removeAll(set2);
		return tempSet1;
	}

	/**
	 * Checks if is empty.
	 *
	 * @param <T> the generic type
	 * @param collection the collection
	 * @return true, if is empty
	 * Null safe
	 * False if the collection passed in is null
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}
}
