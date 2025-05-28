/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     WSO2 LLC - support for WSO2 Micro Integrator Configuration
 */

package org.eclipse.lemminx.customservice.synapse.resourceFinder;

import org.eclipse.lemminx.customservice.synapse.resourceFinder.pojo.RequestedResource;
import org.eclipse.lemminx.customservice.synapse.resourceFinder.pojo.Resource;
import org.eclipse.lemminx.customservice.synapse.resourceFinder.pojo.ResourceResponse;
import org.eclipse.lemminx.customservice.synapse.utils.Constant;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.eclipse.lemminx.customservice.synapse.utils.Constant.EXTRACTED;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.INTEGRATION_PROJECT_DEPENDENCIES;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.USER_HOME;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.WSO2_MI;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.list;

public class NewProjectResourceFinder extends AbstractResourceFinder {

    @Override
    protected ResourceResponse findResources(String projectPath, List<RequestedResource> types) {

        ResourceResponse response = new ResourceResponse();

        findArtifactResources(projectPath, types, response);
        findRegistryResources(projectPath, types, response);

        Map<String, ResourceResponse> dependentResourcesMap = getDependentResourcesMap();
        for (RequestedResource type : types) {
            String resourceType = type.getType();
            if (dependentResourcesMap.containsKey(resourceType)) {
                ResourceResponse dependentResponse = dependentResourcesMap.get(resourceType);
                mergeResourceResponses(response, dependentResponse);
            }
        }
        return response;
    }

    /**
    * This method searches for artifact resources and registry resources within the given project path without
    * considering its dependencies.
    *
    * @param projectPath The path to the root project directory.
    * @param types       A list of requested resource types to search for.
    * @return A `ResourceResponse` containing the found resources.
    */
    private ResourceResponse findRootProjectResources(String projectPath, List<RequestedResource> types) {

        ResourceResponse response = new ResourceResponse();
        findArtifactResources(projectPath, types, response);
        findRegistryResources(projectPath, types, response);
        return response;
    }

    private void findArtifactResources(String projectPath, List<RequestedResource> types, ResourceResponse response) {

        Path artifactsPath = Path.of(projectPath, "src", "main", "wso2mi", "artifacts");
        List<Resource> resourcesInArtifacts = findResourceInArtifacts(artifactsPath, types);
        Path localEntryPath = Path.of(artifactsPath.toString(), "local-entries");
        List<Resource> resourcesInLocalEntry = findResourceInLocalEntry(localEntryPath, types);
        resourcesInArtifacts.addAll(resourcesInLocalEntry);
        response.setResources(resourcesInArtifacts);
    }

    private void findRegistryResources(String projectPath, List<RequestedResource> types, ResourceResponse response) {

        Path registryPath = Path.of(projectPath, Constant.SRC, Constant.MAIN, Constant.WSO2MI, Constant.RESOURCES);
        List<Resource> resourcesInRegistry = findResourceInRegistry(registryPath, types);
        response.setRegistryResources(resourcesInRegistry);
    }

    @Override
    public void loadDependentResources(String projectPath) {

        initDependentResourcesMap();
        String projectName = Path.of(projectPath).getFileName().toString();
        Path dependenciesTempDir = Path.of(System.getProperty(USER_HOME), WSO2_MI, INTEGRATION_PROJECT_DEPENDENCIES);
        try {
            Path projectDependencyDir = findProjectDependencyDir(dependenciesTempDir, projectName);
            if (projectDependencyDir != null) {
                Path extractedDir = projectDependencyDir.resolve(EXTRACTED);
                if (exists(extractedDir) && isDirectory(extractedDir)) {
                    processDependentProjects(extractedDir);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading dependent resources", e);
        }
    }

    /**
     * Finds the dependency directory for the given project name.
     *
     * @param dependenciesTempDir The root directory for dependencies.
     * @param projectName         The name of the project.
     * @return The path to the dependency directory, or null if not found.
     * @throws IOException If an error occurs while listing directories.
     */
    private Path findProjectDependencyDir(Path dependenciesTempDir, String projectName) throws IOException {
        return list(dependenciesTempDir)
                .filter(path -> path.getFileName().toString().startsWith(projectName) && isDirectory(path))
                .findFirst()
                .orElse(null);
    }

    /**
     * Processes all dependent projects within the extracted directory.
     *
     * @param extractedDir The directory containing extracted dependent projects.
     * @throws IOException If an error occurs while listing directories.
     */
    private void processDependentProjects(Path extractedDir) throws IOException {
        Map<String, ResourceResponse> dependentResourcesMap = getDependentResourcesMap();

        for (Path dependentProject : list(extractedDir).toArray(Path[]::new)) {
            if (isDirectory(dependentProject)) {
                // Process each resource type for the dependent project
                for (Map.Entry<String, ResourceResponse> entry : dependentResourcesMap.entrySet()) {
                    String type = entry.getKey();
                    ResourceResponse dependentResources = entry.getValue();

                    // Create a requested resource for the current type
                    RequestedResource requestedResource = new RequestedResource(type, true);

                    // Find resources in the dependent project and merge them into the map
                    ResourceResponse resources = findRootProjectResources(dependentProject.toString(), List.of(requestedResource));
                    mergeResourceResponses(dependentResources, resources);
                }
            }
        }
    }

    @Override
    protected String getArtifactFolder(String type) {

        if (Constant.API.equalsIgnoreCase(type)) {
            return "apis";
        } else if (Constant.ENDPOINT.equalsIgnoreCase(type)) {
            return "endpoints";
        } else if (Constant.SEQUENCE.equalsIgnoreCase(type)) {
            return "sequences";
        } else if (Constant.MESSAGE_STORE.equalsIgnoreCase(type)) {
            return "message-stores";
        } else if (Constant.MESSAGE_PROCESSOR.equalsIgnoreCase(type)) {
            return "message-processors";
        } else if ("endpointTemplate".equalsIgnoreCase(type)) {
            return "templates";
        } else if ("sequenceTemplate".equalsIgnoreCase(type)) {
            return "templates";
        } else if (Constant.TASK.equalsIgnoreCase(type)) {
            return "tasks";
        } else if (Constant.LOCAL_ENTRY.equalsIgnoreCase(type)) {
            return "local-entries";
        } else if (Constant.INBOUND_DASH_ENDPOINT.equalsIgnoreCase(type)) {
            return "inbound-endpoints";
        } else if (Constant.DATA_SERVICE.equalsIgnoreCase(type)) {
            return "data-services";
        } else if (Constant.DATA_SOURCE.equalsIgnoreCase(type)) {
            return "data-sources";
        } else if (Constant.PROXY_SERVICE.equalsIgnoreCase(type)) {
            return "proxy-services";
        }
        return null;
    }
}
