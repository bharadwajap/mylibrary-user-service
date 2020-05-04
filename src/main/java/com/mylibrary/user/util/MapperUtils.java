package com.mylibrary.user.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MatchingStrategy;

/**
 * This class provides the utilities for mapping logic from DTOs to Entities and
 * vice versa.
 */

public final class MapperUtils {

	private static final ModelMapper mapper = new ModelMapper();

	/**
	 * default private constructor for final classes.
	 */
	private MapperUtils() {

	}

	/**
	 * 
	 * Maps source to an instance of target class.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static <D, T> D mapTo(final T source, final Class<D> target) {
		return mapper.map(source, target);
	}

	/**
	 * Maps source list to an instance of target class.
	 * 
	 * @param sourceList
	 * @param target
	 * @return
	 */
	public static <D, T> List<D> mapListTo(final List<T> sourceList, final Class<D> target) {
		final List<D> list = new ArrayList<D>();
		sourceList.forEach(source -> list.add(mapTo(source, target)));
		return list;
	}

	/**
	 * Maps source list to an instance of target class with provided property
	 * map.
	 * 
	 * @param sourceList
	 * @param target
	 * @return
	 */
	public static <S, D> List<D> mapListTo(final List<S> sourceList, final Class<D> target, PropertyMap<S, D> map) {
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(map);
		final List<D> list = new ArrayList<D>();
		sourceList.forEach(source -> list.add(mapTo(source, target, mapper)));
		return list;
	}
	
	/**
	 * @param sourceList
	 * @param target
	 * @param map
	 * @param strategy
	 * @return
	 */
	public static <S, D> List<D> mapListTo(final List<S> sourceList, final Class<D> target, PropertyMap<S, D> map,MatchingStrategy strategy) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(strategy);
		mapper.addMappings(map);
		final List<D> list = new ArrayList<D>();
		sourceList.forEach(source -> list.add(mapTo(source, target, mapper)));
		return list;
	}
	
	/**
	 * Maps source list to an instance of target class with provided property
	 * map.
	 * 
	 * @param sourceList
	 * @param target
	 * @return
	 */
	public static <S, D> List<D> mapListTo(final List<S> sourceList, final Class<D> target, ModelMapper mapper) {
		final List<D> list = new ArrayList<D>();
		sourceList.forEach(source -> list.add(mapTo(source, target, mapper)));
		return list;
	}
	
	/**
	 * Maps source to an instance of target class with provided mapper.
	 * 
	 * @param source
	 * @param target
	 * @param mapper
	 * @return
	 */
	public static <S, D> D mapTo(final S source, final Class<D> target, ModelMapper mapper) {
		return mapper.map(source, target);
	}
}

