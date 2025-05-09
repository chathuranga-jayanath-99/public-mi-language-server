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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.misc.wsdl20;

public class MessageRefType extends ExtensibleDocumentedType {

    Object otherAttributes;
    Object[] any;
    String messageLabel;
    String element;

    @Override
    public Object getOtherAttributes() {

        return otherAttributes;
    }

    @Override
    public void setOtherAttributes(Object otherAttributes) {

        this.otherAttributes = otherAttributes;
    }

    public Object[] getAny() {

        return any;
    }

    public void setAny(Object[] any) {

        this.any = any;
    }

    public String getMessageLabel() {

        return messageLabel;
    }

    public void setMessageLabel(String messageLabel) {

        this.messageLabel = messageLabel;
    }

    public String getElement() {

        return element;
    }

    public void setElement(String element) {

        this.element = element;
    }
}