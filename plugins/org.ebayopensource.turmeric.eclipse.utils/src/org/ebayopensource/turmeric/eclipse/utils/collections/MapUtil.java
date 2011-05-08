/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class MapUtil.
 */
public class MapUtil
{
    
    /**
     * To array.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the map
     * @return the object[]
     */
    public static < K, V > Object[] toArray( final Map< K, V > map )
    {
        return toArray( map, new Object[ 0 ], false );
    }
    
    /**
     * To array.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the map
     * @param ignoreNull the ignore null
     * @return the object[]
     */
    public static < K, V > Object[] toArray( final Map< K, V > map,
                                             final boolean ignoreNull )
    {
        return toArray( map, new Object[ 0 ], ignoreNull );
    }
    
    /**
     * To array.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param <T> the generic type
     * @param map the map
     * @param array this ensures the type of the returned array instance. the passed instances will not be added to the result
     * @return the t[]
     */
    public static < K, V, T > T[] toArray( final Map< K, V > map, 
                                                final T[] array )
    {
        return toArray( map, array, false );
    }
    
    /**
     * To array.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param <T> the generic type
     * @param map the map
     * @param array the array
     * @param ignoreNull the ignore null
     * @return the t[]
     */
    public static < K, V, T > T[] toArray( final Map< K, V > map, 
                                                final T[] array,
                                                final boolean ignoreNull )
    {
        final List< T > list = new ArrayList<T>();
        if( map == null )
            return null;
        for( final Map.Entry< K, V > entry : map.entrySet() )
        {
        	if (ignoreNull == true) {
        		if( entry.getKey() != null )
                    list.add( ( T )entry.getKey() );
                if( entry.getValue() != null )
                    list.add( ( T )entry.getValue() );
        	} else {
        		list.add( ( T )entry.getKey() );
        		list.add( ( T )entry.getValue() );
        	}
        }
        
        return list.toArray( array );
    }
}
