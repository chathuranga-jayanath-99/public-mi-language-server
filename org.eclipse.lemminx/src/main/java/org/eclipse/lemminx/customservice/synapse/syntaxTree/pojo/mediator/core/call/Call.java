/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.core.call;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.endpoint.NamedEndpoint;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.Mediator;

public class Call extends Mediator {

    CallSource source;
    CallTarget target;
    NamedEndpoint endpoint;
    boolean blocking;
    String description;
    boolean initAxis2ClientOptions;
    String inlineEndpointXml;

    public Call() {
        setDisplayName("Call Endpoint");
    }
    public CallSource getSource() {

        return source;
    }

    public void setSource(CallSource source) {

        this.source = source;
    }

    public CallTarget getTarget() {

        return target;
    }

    public void setTarget(CallTarget target) {

        this.target = target;
    }

    public NamedEndpoint getEndpoint() {

        return endpoint;
    }

    public void setEndpoint(NamedEndpoint endpoint) {

        this.endpoint = endpoint;
    }

    public boolean isBlocking() {

        return blocking;
    }

    public void setBlocking(boolean blocking) {

        this.blocking = blocking;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public boolean getInitAxis2ClientOptions() {

        return initAxis2ClientOptions;
    }

    public void setInitAxis2ClientOptions(boolean initAxis2ClientOptions) {

        this.initAxis2ClientOptions = initAxis2ClientOptions;
    }

    public String getInlineEndpointXml() {
        return inlineEndpointXml;
    }

    public void setInlineEndpointXml(String inlineEndpointXml) {
        this.inlineEndpointXml = inlineEndpointXml;
    }
}
