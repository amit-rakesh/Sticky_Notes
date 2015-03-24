/**
 * 
 */
package com.project.stickynotes.beans;

import java.util.List;

/**
 * @author Varun
 *
 */
public class FilesBean {
	private boolean file;
	private String type;
	private String name;
	private String fileSize;
	private String lastModified;
	private String path;
	List<FilesBean> nestedFolders;
	
	/**
	 * @return the file
	 */
	public boolean isFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(boolean file) {
		this.file = file;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the nestedFolders
	 */
	public List<FilesBean> getNestedFolders() {
		return nestedFolders;
	}
	/**
	 * @param nestedFolders the nestedFolders to set
	 */
	public void setNestedFolders(List<FilesBean> nestedFolders) {
		this.nestedFolders = nestedFolders;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
}
