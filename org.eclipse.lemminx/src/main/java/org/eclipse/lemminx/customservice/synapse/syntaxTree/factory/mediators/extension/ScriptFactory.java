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

package org.eclipse.lemminx.customservice.synapse.syntaxTree.factory.mediators.extension;

import org.eclipse.lemminx.customservice.synapse.syntaxTree.factory.mediators.AbstractMediatorFactory;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.STNode;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.Mediator;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.extension.Script;
import org.eclipse.lemminx.customservice.synapse.syntaxTree.pojo.mediator.extension.ScriptLanguage;
import org.eclipse.lemminx.customservice.synapse.utils.Constant;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;

import java.util.ArrayList;
import java.util.List;

public class ScriptFactory extends AbstractMediatorFactory {

    private static final String SCRIPT = "script";

    @Override
    protected Mediator createSpecificMediator(DOMElement element) {

        Script script = new Script();
        script.elementNode(element);
        populateAttributes(script, element);
        // TODO: check handling <xs:any> elements
        List<DOMNode> children = element.getChildren();
        List<Object> elements = new ArrayList<>();
        List<String> scriptKeys = new ArrayList<>();
        if (children != null && !children.isEmpty()) {
            for (DOMNode node : children) {
                if (Constant.INCLUDE.equalsIgnoreCase(node.getNodeName())) {
                    String keyValue = node.getAttribute("key");
                    scriptKeys.add(keyValue);
                } else {
                    String xml = Utils.getInlineString(node);
                    elements.add(xml);
                }
            }
            script.setContent(elements.toArray());
            script.setInclude(scriptKeys.toArray(new String[scriptKeys.size()]));
        }
        return script;
    }

    @Override
    public void populateAttributes(STNode node, DOMElement element) {

        String language = element.getAttribute(Constant.LANGUAGE);
        ScriptLanguage languageEnum = Utils.getEnumFromValue(language, ScriptLanguage.class);
        if (languageEnum != null) {
            ((Script) node).setLanguage(languageEnum);
        }
        String key = element.getAttribute(Constant.KEY);
        if (key != null) {
            ((Script) node).setKey(key);
        }
        String function = element.getAttribute(Constant.FUNCTION);
        if (function != null) {
            ((Script) node).setFunction(function);
        }
        String description = element.getAttribute(Constant.DESCRIPTION);
        if (description != null) {
            ((Script) node).setDescription(description);
        }
    }

    @Override
    public String getTagName() {

        return SCRIPT;
    }
}
