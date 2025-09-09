package studio.mevera.synapse.impl;

import studio.mevera.synapse.SynapseBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSynapse extends SynapseBase<TestOrigin, TestUser, TestNeuron> {

    private final Map<TestOrigin, TestUser> userCache = new ConcurrentHashMap<>();

    @Override
    public TestUser asUser(final TestOrigin origin) {
        return this.userCache.computeIfAbsent(origin, TestUser::new);
    }

}
