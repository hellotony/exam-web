/**
 * Copyright 1999-2011 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exam.support.mybatis.client.support.vo;

import com.exam.support.mybatis.client.support.utils.CollectionUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * TODO Comment of CobarHive
 * 
 * @author fujohnwang
 * 
 */
public class CobarMRBase<M, T> {

	private Map<String, List<T>> resources = new HashMap<String, List<T>>();
	private Map<String, Map<M, List<T>>> groupResource = new HashMap<String, Map<M, List<T>>>();
	private Map<String, Lock> locks = new HashMap<String, Lock>();
	private Set<String> keys = new HashSet<String>();

	public CobarMRBase(String[] keys) {
		this(Arrays.asList(keys));
	}

	public CobarMRBase(List<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			throw new IllegalArgumentException("empty collection is invalid for hive to spawn data holders.");
		}
		this.keys.addAll(keys);
		initResourceHolders();
		initResourceLocks();
	}

	public CobarMRBase(Set<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			throw new IllegalArgumentException("empty collection is invalid for hive to spawn data holders.");
		}
		this.keys.addAll(keys);
		initResourceHolders();
		initResourceLocks();
	}

	private void initResourceLocks() {
		for (String key : keys) {
			locks.put(key, new ReentrantLock());
		}
	}

	private void initResourceHolders() {
		for (String key : keys) {
			resources.put(key, new ArrayList<T>());
		}
	}

	public Map<String, List<T>> getResources() {
		return this.resources;
	}

	public Map<String, Map<M, List<T>>> getGroupResource() {
		return groupResource;
	}

	public void emit(String key, M group, T entity) {
		Lock lock = locks.get(key);
		lock.lock();
		try {
			Map<M, List<T>> groupMap = groupResource.get(key);
			if (groupMap == null) {
				groupMap = new HashMap<M, List<T>>();
				groupResource.put(key, groupMap);
			}
			if (!groupMap.containsKey(group)) {
				groupMap.put(group, new ArrayList<T>());
			}
			groupMap.get(group).add(entity);
		} finally {
			lock.unlock();
		}
	}

	public void emit(String key, T entity) {
		Lock lock = locks.get(key);
		lock.lock();
		try {
			resources.get(key).add(entity);
		} finally {
			lock.unlock();
		}
	}
}
