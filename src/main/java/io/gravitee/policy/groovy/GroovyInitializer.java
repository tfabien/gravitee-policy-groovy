/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.policy.groovy;

import io.gravitee.policy.api.PolicyContext;
import io.gravitee.policy.api.PolicyContextProvider;
import io.gravitee.policy.api.PolicyContextProviderAware;
import io.gravitee.policy.groovy.sandbox.SecuredResolver;
import org.springframework.core.env.Environment;

/**
 * @author Jeoffrey HAEYAERT (jeoffrey.haeyaert at graviteesource.com)
 * @author GraviteeSource Team
 */
public class GroovyInitializer implements PolicyContext, PolicyContextProviderAware {

    private Environment environment;
    private boolean classLoaderLegacyMode = true;

    @Override
    public void onActivation() throws Exception {
        if (classLoaderLegacyMode || !SecuredResolver.isInitialized()) {
            SecuredResolver.initialize(this.environment);
        }
    }

    @Override
    public void onDeactivation() throws Exception {
        if (classLoaderLegacyMode) {
            SecuredResolver.destroy();
        }
    }

    @Override
    public void setPolicyContextProvider(PolicyContextProvider policyContextProvider) {
        this.environment = policyContextProvider.getComponent(Environment.class);
        this.classLoaderLegacyMode = environment.getProperty("classloader.legacy.enabled", Boolean.class, true);
    }
}
