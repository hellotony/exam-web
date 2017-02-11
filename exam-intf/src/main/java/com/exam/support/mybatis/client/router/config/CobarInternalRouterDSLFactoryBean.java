package com.exam.support.mybatis.client.router.config;

import com.exam.support.mybatis.client.router.CobarClientInternalRouter;
import com.exam.support.mybatis.client.router.rules.IRoutingRule;
import com.exam.support.mybatis.client.router.support.IBatisRoutingFact;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CobarInternalRouterDSLFactoryBean extends
        AbstractCobarInternalRouterConfigurationFactoryBean {

    @Override
    protected void assembleRulesForRouter(
                                          CobarClientInternalRouter router,
                                          Resource configLocation,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionShardingRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> sqlActionRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceShardingRules,
                                          Set<IRoutingRule<IBatisRoutingFact, List<String>>> namespaceRules)
            throws IOException {
        // TODO Auto-generated method stub

    }

}
