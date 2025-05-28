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

package org.eclipse.lemminx.customservice.synapse.parser;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.lemminx.customservice.synapse.utils.Constant;
import org.eclipse.lemminx.customservice.synapse.utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.eclipse.lemminx.customservice.synapse.parser.DependencyManagerUtils.copyFile;
import static org.eclipse.lemminx.customservice.synapse.parser.DependencyManagerUtils.getDependencyFromLocalRepo;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.ARTIFACT_ID;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.CAR_EXTENSION;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.DEPENDENCY;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.CAPP_DESCRIPTOR_FILE;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.DOWNLOADED;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.EXTRACTED;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.GROUP_ID_KEY;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.HYPHEN;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.INTEGRATION_PROJECT_DEPENDENCIES;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.TYPE;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.UNDERSCORE;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.USER_HOME;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.VERSION;
import static org.eclipse.lemminx.customservice.synapse.utils.Constant.WSO2_MI;

public class IntegrationProjectDownloadManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectorDownloadManager.class.getName());

    public static List<String> handleDependencies(String projectPath, List<DependencyDetails> dependencies) {

        String projectId = new File(projectPath).getName() + UNDERSCORE + Utils.getHash(projectPath);
        File directory = Path.of(System.getProperty(USER_HOME), WSO2_MI, INTEGRATION_PROJECT_DEPENDENCIES,
                projectId).toFile();
        File downloadDirectory = Path.of(directory.getAbsolutePath(), DOWNLOADED).toFile();
        File extractDirectory = Path.of(directory.getAbsolutePath(), EXTRACTED).toFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!extractDirectory.exists()) {
            extractDirectory.mkdirs();
        }
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }

        List<String> failedDependencies = new ArrayList<>();
        Set<String> fetchedDependencies = new HashSet<>();

        for (DependencyDetails dependency : dependencies) {
            try {
                fetchDependencyRecursively(dependency, downloadDirectory, fetchedDependencies);
            } catch (Exception e) {
                String failedDependency = dependency.getGroupId() + HYPHEN + dependency.getArtifact() + HYPHEN + dependency.getVersion();
                LOGGER.log(Level.WARNING, "Error occurred while downloading dependency " + failedDependency + ": " + e.getMessage());
                failedDependencies.add(failedDependency);
            }
        }
        return failedDependencies;
    }

    static void fetchDependencyRecursively(DependencyDetails dependency, File downloadDirectory,
                                           Set<String> fetchedDependencies) throws Exception {
        String dependencyKey = dependency.getGroupId() + HYPHEN + dependency.getArtifact() + HYPHEN + dependency.getVersion();
        if (fetchedDependencies.contains(dependencyKey)) {
            return; // Skip already fetched dependencies
        }

        fetchedDependencies.add(dependencyKey);

        // Fetch the .car file for the dependency
        File carFile = fetchDependencyFile(dependency, downloadDirectory);
        if (!carFile.exists()) {
            throw new Exception("Failed to fetch .car file for dependency: " + dependencyKey);
        }

        // Parse the descriptor.xml to find additional dependencies
        List<DependencyDetails> additionalDependencies = parseDescriptorFile(carFile);

        // Recursively fetch additional dependencies
        for (DependencyDetails additionalDependency : additionalDependencies) {
            fetchDependencyRecursively(additionalDependency, downloadDirectory, fetchedDependencies);
        }
    }

    private static File fetchDependencyFile(DependencyDetails dependency, File downloadDirectory) {
        File dependencyFile = new File(downloadDirectory, dependency.getArtifact() + HYPHEN + dependency.getVersion() + CAR_EXTENSION);
        if (dependencyFile.exists() && dependencyFile.isFile()) {
            LOGGER.log(Level.INFO, "Dependency already downloaded: " + dependencyFile.getName());
        } else {
            File existingArtifact = getDependencyFromLocalRepo(dependency.getGroupId(),
                    dependency.getArtifact(), dependency.getVersion(), dependency.getType());
            if (existingArtifact != null) {
                LOGGER.log(Level.INFO, "Copying dependency from local repository: " + dependencyFile.getName());
                try {
                    copyFile(existingArtifact, downloadDirectory);
                } catch (IOException e) {
                    String failedDependency = dependency.getGroupId() + HYPHEN + dependency.getArtifact() + HYPHEN + dependency.getVersion();
                    LOGGER.log(Level.WARNING, "Error occurred while downloading dependency " + failedDependency + ": " + e.getMessage());
                }
            } else {
                // if the dependency is not found in the local repository, download it from the remote repository
            }
        }
        return dependencyFile;
    }

    private static List<DependencyDetails> parseDescriptorFile(File carFile) throws Exception {
        List<DependencyDetails> dependencies = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(carFile)) {
            ZipEntry descriptorEntry = zipFile.getEntry(CAPP_DESCRIPTOR_FILE);
            if (descriptorEntry == null) {
                throw new Exception("descriptor.xml not found in .car file: " + carFile.getName());
            }

            InputStream inputStream = zipFile.getInputStream(descriptorEntry);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList dependencyNodes = document.getElementsByTagName(DEPENDENCY);
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Element dependencyElement = (Element) dependencyNodes.item(i);
                String groupId = dependencyElement.getAttribute(GROUP_ID_KEY);
                String artifactId = dependencyElement.getAttribute(ARTIFACT_ID);
                String version = dependencyElement.getAttribute(VERSION);
                String type = dependencyElement.getAttribute(TYPE);

                if (StringUtils.isNotEmpty(groupId) && StringUtils.isNotEmpty(artifactId)
                        && StringUtils.isNotEmpty(version) && StringUtils.isNotEmpty(type)) {
                    DependencyDetails dependency = new DependencyDetails();
                    dependency.setGroupId(groupId);
                    dependency.setArtifact(artifactId);
                    dependency.setVersion(version);
                    dependency.setType(type);
                    dependencies.add(dependency);
                }
            }
        }
        return dependencies;
    }

    private static void deleteRemovedIntegrationProjectDependencies(File downloadDirectory, List<DependencyDetails> dependencies,
                                                                    String projectPath) {

        List<String> existingDependencies =
                dependencies.stream().map(dependency -> dependency.getArtifact() + HYPHEN + dependency.getVersion())
                        .collect(Collectors.toList());
        File[] files = downloadDirectory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (isIntegrationProjectRemoved(file, existingDependencies)) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error occurred while deleting removed dependency: " + file.getName());
                }
            }
        }
    }

    private static boolean isIntegrationProjectRemoved(File file, List<String> existingConnectors) {

        return file.isFile() && !existingConnectors.contains(file.getName().replace(Constant.CAR_EXTENSION, ""));
    }
}
