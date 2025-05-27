package org.eclipse.lemminx.customservice.synapse.parser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.eclipse.lemminx.customservice.synapse.parser.pom.PomParser.getPomDetails;

public class DependencyDownloadManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectorDownloadManager.class.getName());

    public static String downloadDependencies(String projectPath) {

        OverviewPageDetailsResponse pomDetailsResponse = new OverviewPageDetailsResponse();
        getPomDetails(projectPath, pomDetailsResponse);
        List<DependencyDetails> connectorDependencies = pomDetailsResponse.getDependenciesDetails().getConnectorDependencies();
        List<DependencyDetails> integrationProjectDependencies = pomDetailsResponse.getDependenciesDetails().getIntegrationProjectDependencies();
        List<String> failedConnectorDependencies = ConnectorDownloadManager.handleDependencies(projectPath, connectorDependencies);
        List<String> failedIntegrationProjectDependencies = IntegrationProjectDownloadManager.handleDependencies(projectPath, integrationProjectDependencies);
        if (!failedConnectorDependencies.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Some connectors were not downloaded: " + String.join(", ", failedConnectorDependencies));
            return "Some connectors were not downloaded: " + String.join(", ", failedConnectorDependencies);
        }
        if (!failedIntegrationProjectDependencies.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Some integration project dependencies were not downloaded: " + String.join(", ", failedIntegrationProjectDependencies));
            return "Some integration project dependencies were not downloaded: " + String.join(", ", failedIntegrationProjectDependencies);
        }
        return "Success";
    }
}
