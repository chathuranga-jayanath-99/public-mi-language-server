/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.eclipse.lemminx.customservice.synapse.dependency.tree.pojo;

import java.util.ArrayList;
import java.util.List;

public class DependencyTree {

    private String name;
    private String type;
    private String path;
    private final List<Dependency> dependencyList;

    public DependencyTree() {

        this.dependencyList = new ArrayList<>();
    }

    public List<Dependency> getDependencyList() {

        return dependencyList;
    }

    public void addDependency(Dependency dependency) {

        if (!dependencyList.contains(dependency)) {
            this.dependencyList.add(dependency);
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {

        return "DependencyTree{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", dependencyList=" + dependencyList +
                '}';
    }
}
