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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.endpoint;

public class EndpointAddress extends DefaultEndpoint {

    String uri;

    public EndpointAddress(DefaultEndpoint defaultEndpoint) {

        setEnableRM(defaultEndpoint.getEnableRM());
        setEnableSec(defaultEndpoint.getEnableSec());
        setEnableAddressing(defaultEndpoint.getEnableAddressing());
        setTimeout(defaultEndpoint.getTimeout());
        setSuspendOnFailure(defaultEndpoint.getSuspendOnFailure());
        setMarkForSuspension(defaultEndpoint.getMarkForSuspension());

        setFormat(defaultEndpoint.getFormat());
        setOptimize(defaultEndpoint.getOptimize());
        setEncoding(defaultEndpoint.getEncoding());
        setStatistics(defaultEndpoint.getStatistics());
        setTrace(defaultEndpoint.getTrace());
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        this.uri = uri;
    }
}