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
package com.exam.support.mybatis.client.support.execution;

import com.exam.support.mybatis.client.support.SqlSessionCallback;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;

/**
 * {@link #action} will be executed on {@link #dataSource} with
 * {@link #executor} asynchronously.<br>
 * 
 * @author fujohnwang
 * @since 1.0
 */
public class ConcurrentRequest {
	private SqlSessionCallback action;
	private DataSource dataSource;
	private ExecutorService executor;
	private int dataSourceIndex;

	public SqlSessionCallback getAction() {
		return action;
	}

	public void setAction(SqlSessionCallback action) {
		this.action = action;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public int getDataSourceIndex() {
		return dataSourceIndex;
	}

	public void setDataSourceIndex(int dataSourceIndex) {
		this.dataSourceIndex = dataSourceIndex;
	}
}
