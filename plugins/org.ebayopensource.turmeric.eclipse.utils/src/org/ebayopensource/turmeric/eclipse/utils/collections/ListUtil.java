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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.ObjectUtils;

public class ListUtil
{
    public static < T > List< T > transformed( final Transformer< T > transformer,
                                               final List< ? > list )
    {
        return transformed( transformer, list.toArray() );
    }
    public static < T > List< T > transformed( final Transformer< T > transformer,
                                               final Object... objects )
    {
        final List< T > list = new ArrayList< T >()
        {
            private static final long serialVersionUID = 4888742388896207408L;
            private final Transformer< T > trans = transformer;
            @Override
            public void add( int index, T element )
            {
                super.add( index, trans.transform( element ) );
            }
            @Override
            public boolean add( T o )
            {
                return super.add( trans.transform( o ) );
            }
            @Override
            public boolean addAll( Collection< ? extends T > c )
            {
                final List< T > list = new ArrayList< T >();
                for( final T t : c )
                    list.add( trans.transform( t ) );
                return super.addAll( list );
            }
            @Override
            public boolean addAll( int index, Collection< ? extends T > c )
            {
                final List< T > list = new ArrayList< T >();
                for( final T t : c )
                    list.add( trans.transform( t ) );
                return super.addAll( index, list );
            }
            @Override
            public boolean contains( Object elem )
            {
                return super.contains( trans.transform( elem ) );
            }
            @Override
            public int indexOf( Object elem )
            {
                return super.indexOf( trans.transform( elem ) );
            }
            @Override
            public int lastIndexOf( Object elem )
            {
                return super.lastIndexOf( trans.transform( elem ) );
            }
            @Override
            public boolean remove( Object o )
            {
                return super.remove( trans.transform( o ) );
            }
            @Override
            public T set( int index, T element )
            {
                return super.set( index, trans.transform( element ) );
            }
            @Override
            public boolean containsAll( Collection< ? > c )
            {
                final List< T > list = new ArrayList< T >();
                for( final Object t : c )
                    list.add( trans.transform( t ) );
                return super.containsAll( list );
            }
            @Override
            public boolean removeAll( Collection< ? > c )
            {
                final List< T > list = new ArrayList< T >();
                for( final Object t : c )
                    list.add( trans.transform( t ) );
                return super.removeAll( list );
            }
            @Override
            public boolean retainAll( Collection< ? > c )
            {
                final List< T > list = new ArrayList< T >();
                for( final Object t : c )
                    list.add( trans.transform( t ) );
                return super.retainAll( list );
            }

        };
        for( final Object object : objects )
            list.add( transformer.transform( object ) );
        return list;
    }
    public static < T > List< T > arrayList( final T... objects )
    {
        return array( objects );
    }
    public static < T > List< T > arrayList( final Collection< T > collection )
    {
        return array( collection );
    }
    public static < T > List< T > array( final T... objects )
    {
        final List< T > list = new ArrayList< T >();
        add( list, objects );
        return list;
    }
    public static < T > List< T > array( final Collection< T > collection )
    {
        final List< T > list = new ArrayList< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    public static < T > List< T > linkedList( final T... objects )
    {
        return linked( objects );
    }
    public static < T > List< T > linkedList( final Collection< T > collection )
    {
        return linked( collection );
    }
    public static < T > List< T > linked( final T... objects )
    {
        final List< T > list = new LinkedList< T >();
        add( list, objects );
        return list;
    }
    public static < T > List< T > linked( final Collection< T > collection )
    {
        final List< T > list = new LinkedList< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    public static < T > List< T > newList( final T... objects )
    {
        return list( objects );
    }
    public static < T > List< T > newList( final Collection< T > collection )
    {
        return list( collection );
    }
    public static < T > List< T > toList( final T... objects )
    {
        return list( objects );
    }
    public static < T > List< T > list( final T... objects )
    {
        return array( objects );
    }
    public static < T > List< T > list( final Collection< T > collection )
    {
        final List< T > list = new ArrayList< T >();
        if( collection != null && collection.size() > 0 )
            list.addAll( collection );
        return list;
    }
    public static < T > List< T > add( final List< T > list,
                                       final T... objects )
    {
        if( objects == null )
            return list;
        for( final T object : objects )
            list.add( object );
        return list;
    }
    public static < T > List< T > addIgnoreNull( final List< T > list,
                                                 final T... objects )
    {
        if( objects == null )
            return list;
        for( final T object : objects )
            if( object != null )
                list.add( object );
        return list;
    }
    public static < T > List< T > listAdd( final List< T > list,
            						       final T... objects )
    {
    	return add( list, objects );
    }
    public static < T > List< T > remove( final List< T > list,
                                          final T... objects )
    {
        for( final T object : objects )
            list.remove( object );
        return list;
    }
    public static < T > List< T > listRemove( final List< T > list,
                                              final T... objects )
    {
        return remove( list, objects );
    }
    public static < E extends Object > List< String > caseless( final E... objects )
    {
        final Transformer< String > transformer = new Transformer< String >()
        {
            private static final long serialVersionUID = 1631027078150099602L;
            public String transform( final Object input )
            {
                return ObjectUtils.toString( input ).toLowerCase();
            }
        };
        return transformed( transformer, objects );
    }
    public static List< String > caseless( final List< ? > list )
    {
        return caseless( list.toArray() );
    }
    public static < T > List< T > vector( final T... objects )
    {
        final List< T > vector = new Vector< T >();
        add( vector, objects );
        return vector;
    }
    public static < T > List< T > vector( final Collection< T > collection )
    {
        final List< T > vector = new Vector< T >();
        if( collection != null && collection.size() > 0 )
            vector.addAll( collection );
        return vector;
    }
    
    /**
     * Change the position of the given object in the provided list for the 
     * specified number of positions.
     * 
     * Negative offset means move up, whereas Positive indicates move down.
     * 
     * @param list
     * @param object
     * @param offset
     * @return
     */
    public static < T > List< T > changePosition(final List< T > list, 
    		final T object, final int offset) {
    	
    	final int index = list.indexOf(object);
    	if (index >= 0) {
    		//found the object
    		final int newIndex = index + offset;
    		if (offset < 0 && newIndex >= 0 && list.remove(object)) {
    			//move up
    			list.add(newIndex, object);
    		} else if (offset > 0 && newIndex < list.size() 
    				&& list.remove(object)) {
    			//move down
    			list.add(newIndex, object);
    		}
    	}
    	return list;
    }
    
    public static < T > List< T > moveOnePositionUp(final List< T > list, 
    		final T object) {
    	return changePosition(list, object, -1);
    }
    
    public static < T > List< T > moveOnePositionDown(final List< T > list, 
    		final T object) {
    	return changePosition(list, object, 1);
    }
}
