package studio.mevera.synapse.impl;

import studio.mevera.synapse.SynapseBase;
import studio.mevera.synapse.platform.Platform;
import studio.mevera.synapse.platform.PlatformInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSynapse extends SynapseBase<TestOrigin, TestUser, TestNeuron> {

    private final Platform platform = new PlatformInfo("test", "1.0");
    private final Map<TestOrigin, TestUser> userCache = new ConcurrentHashMap<>();

    @Override
    public Platform platform() {
        return platform;
    }

    @Override
    public TestUser asUser(final TestOrigin origin) {
        return this.userCache.computeIfAbsent(origin, TestUser::new);
    }

}
