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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.template;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.NamedSequence;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.endpoint.NamedEndpoint;

public class Template extends STNode {

    STNode description;
    TemplateParameter[] parameter;
    NamedEndpoint endpoint;
    NamedSequence sequence;
    String name;
    String onError;
    TemplateArtifactType type;

    public STNode getDescription() {

        return description;
    }

    public void setDescription(STNode description) {

        this.description = description;
    }

    public TemplateParameter[] getParameter() {

        return parameter;
    }

    public boolean hasParameter(String key) {

        if (parameter != null) {
            for (TemplateParameter param : parameter) {
                if (param.getName().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TemplateParameter getParameter(String key) {

        if (parameter != null) {
            for (TemplateParameter param : parameter) {
                if (param.getName().equals(key)) {
                    return param;
                }
            }
        }
        return null;
    }

    public void setParameter(TemplateParameter[] parameter) {

        this.parameter = parameter;
    }

    public NamedEndpoint getEndpoint() {

        return endpoint;
    }

    public void setEndpoint(NamedEndpoint endpoint) {

        this.endpoint = endpoint;
    }

    public NamedSequence getSequence() {

        return sequence;
    }

    public void setSequence(NamedSequence sequence) {

        this.sequence = sequence;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getOnError() {

        return onError;
    }

    public void setOnError(String onError) {

        this.onError = onError;
    }

    public TemplateArtifactType getType() {

        return type;
    }

    public void setType(TemplateArtifactType type) {

        this.type = type;
    }
}
