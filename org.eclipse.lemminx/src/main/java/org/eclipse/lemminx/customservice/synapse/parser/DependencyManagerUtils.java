package org.eclipse.lemminx.customservice.synapse.parser;

import org.eclipse.lemminx.customservice.synapse.utils.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.logging.Level;

public class DependencyManagerUtils {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DependencyManagerUtils.class.getName());

    protected static File getDependencyFromLocalRepo(String groupId, String artifactId, String version, String type) {

        String localMavenRepo = Path.of(System.getProperty(Constant.USER_HOME),  Constant.M2,
                Constant.REPOSITORY).toString();
        String artifactPath = Path.of(localMavenRepo, groupId.replace(".", File.separator), artifactId,
                version, artifactId + "-" + version + "." + type).toString();
        File artifactFile = new File(artifactPath);
        if(artifactFile.exists()) {
            LOGGER.log(Level.INFO, "Dependency found in the local repository: " + artifactId);
            return artifactFile;
        } else {
            LOGGER.log(Level.INFO, "Dependency not found in the local repository: " + artifactId);
            return null;
        }
    }

    protected static void copyFile(File source, File destinationFolder) throws IOException {

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        File destinationFile = Path.of(destinationFolder.getAbsolutePath(), source.getName()).toFile();
        try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred while copying dependency from local repository: " + e.getMessage());
            throw e;
        }
    }
}
