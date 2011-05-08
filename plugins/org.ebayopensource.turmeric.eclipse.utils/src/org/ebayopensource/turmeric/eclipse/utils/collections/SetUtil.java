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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class SetUtil.
 */
public class SetUtil
{
	
	/**
	 * Tree set.
	 *
	 * @param <T> the generic type
	 * @param objects the objects
	 * @return the sets the
	 */
	public static < T > Set< T > treeSet( final T... objects )
    {
        final Set< T > list = new TreeSet< T >();
        add( list, objects );
        return list;
    }
    
    /**
     * Tree set.
     *
     * @param <T> the generic type
     * @param collection the collection
     * @return the sets the
     */
    public static < T > Set< T > treeSet( final Collection< T > collection )
    {
        final Set< T > list = new TreeSet< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
	
	/**
	 * Hash set.
	 *
	 * @param <T> the generic type
	 * @param objects the objects
	 * @return the sets the
	 */
	public static < T > Set< T > hashSet( final T... objects )
    {
        final Set< T > list = new HashSet< T >();
        add( list, objects );
        return list;
    }
    
    /**
     * Hash set.
     *
     * @param <T> the generic type
     * @param collection the collection
     * @return the sets the
     */
    public static < T > Set< T > hashSet( final Collection< T > collection )
    {
        final Set< T > list = new HashSet< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    
    /**
     * Linked set.
     *
     * @param <T> the generic type
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > linkedSet( final T... objects )
    {
        final Set< T > list = new LinkedHashSet< T >();
        add( list, objects );
        return list;
    }
    
    /**
     * Linked set.
     *
     * @param <T> the generic type
     * @param collection the collection
     * @return the sets the
     */
    public static < T > Set< T > linkedSet( final Collection< T > collection )
    {
        final Set< T > list = new LinkedHashSet< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    
    /**
     * Sets the.
     *
     * @param <T> the generic type
     * @param collection the collection
     * @return the sets the
     */
    public static < T > Set< T > set( final Collection< T > collection )
    {
        final Set< T > list = new HashSet< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    
    /**
     * New set.
     *
     * @param <T> the generic type
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > newSet( final T... objects )
    {
        return set( objects );
    }
    
    /**
     * To set.
     *
     * @param <T> the generic type
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > toSet( final T... objects )
    {
        return set( objects );
    }
    
    /**
     * Sets the.
     *
     * @param <T> the generic type
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > set( final T... objects )
    {
        return hashSet( objects );
    }
    
    /**
     * Adds the.
     *
     * @param <T> the generic type
     * @param set the set
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > add( final Set< T > set,
            					      final T... objects )
    {
        if( objects == null )
            return set;
    	for( final T object : objects )
    		set.add( object );
        return set;
    }
    
    /**
     * Sets the add.
     *
     * @param <T> the generic type
     * @param set the set
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > setAdd( final Set< T > set,
			  				             final T... objects )
    {
    	return add( set, objects );
    }
    
    /**
     * Removes the.
     *
     * @param <T> the generic type
     * @param set the set
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > remove( final Set< T > set,
                                         final T... objects )
    {
        for( final T object : objects )
            set.remove( object );
        return set;
    }
    
    /**
     * Sets the remove.
     *
     * @param <T> the generic type
     * @param set the set
     * @param objects the objects
     * @return the sets the
     */
    public static < T > Set< T > setRemove( final Set< T > set,
                                            final T... objects )
    {
        return remove( set, objects );
    }

}
