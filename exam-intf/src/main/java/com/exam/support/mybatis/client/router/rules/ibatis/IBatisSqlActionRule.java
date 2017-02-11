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
package com.exam.support.mybatis.client.router.rules.ibatis;

import com.exam.support.mybatis.client.router.support.IBatisRoutingFact;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * iBatis SQL-Map specific vertical partitioning rule.<br>
 * 
 * @author fujohnwang
 * @since 1.0
 */
public class IBatisSqlActionRule extends AbstractIBatisOrientedRule {

	public IBatisSqlActionRule(String pattern, String action) {
		super(pattern, action);
	}

	public boolean isDefinedAt(IBatisRoutingFact routeFact) {
		Validate.notNull(routeFact);
		return StringUtils.equals(getTypePattern(), routeFact.getAction());
	}

	@Override
	public String toString() {
		return "IBatisSqlActionRule [getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
	}

}
