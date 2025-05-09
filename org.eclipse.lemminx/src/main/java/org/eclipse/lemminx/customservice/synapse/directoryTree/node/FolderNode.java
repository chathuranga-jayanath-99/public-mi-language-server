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

package org.eclipse.lemminx.customservice.synapse.directoryTree.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderNode {

    private String name;
    private String path;
    private List<FileNode> files;
    private List<FolderNode> folders;

    public FolderNode(String name, String path) {

        this.name = name;
        this.path = path;
        this.files = new ArrayList<>();
        this.folders = new ArrayList<>();
    }

    public java.io.File[] listFiles() {

        java.io.File file = new java.io.File(path);
        java.io.File[] files = file.listFiles();
        Arrays.sort(files, (f1, f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            } else if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            } else {
                return f1.getName().compareTo(f2.getName());
            }
        });
        return files;
    }

    public void addFile(FileNode fileNode) {

        files.add(fileNode);
    }

    public void addFolder(FolderNode folderNode) {

        folders.add(folderNode);
    }

    public String getName() {

        return name;
    }

    public String getPath() {

        return path;
    }

    public List<FileNode> getFiles() {

        return files;
    }

    public List<FolderNode> getFolders() {

        return folders;
    }
}
