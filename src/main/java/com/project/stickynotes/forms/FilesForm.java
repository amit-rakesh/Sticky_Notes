/**
 * 
 */
package com.project.stickynotes.forms;

import java.util.List;

import com.project.stickynotes.beans.FilesBean;

/**
 * @author Varun
 *
 */
public class FilesForm {
	
	/**
	 * name
	 */
	private String name;
	
	/**
	 * loggedInUser
	 */
	private String loggedInUser;
	
	/**
	 * filesToList
	 */
	private List<FilesBean> filesToList;
	
	/**
	 * foldersToList
	 */
	private List<FilesBean> foldersToList;
	
	/**
	 * fileToUpload
	 */
	private String fileToUpload;
	
	/**
	 * fileToDelete
	 */
	private String fileToDelete;
	
	/**
	 * folderName
	 */
	private String folderName;
	
	/**
	 * readFile
	 */
	private String readFile;
	
	/**
	 * newFileName
	 */
	private String newFileName;
	
	/**
	 * folderNameToCreate
	 */
	private String folderNameToCreate;

	/**
	 * @return the loggedInUser
	 */
	public String getLoggedInUser() {
		return loggedInUser;
	}

	/**
	 * @param loggedInUser the loggedInUser to set
	 */
	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	/**
	 * @return the filesToList
	 */
	public List<FilesBean> getFilesToList() {
		return filesToList;
	}

	/**
	 * @param filesToList the filesToList to set
	 */
	public void setFilesToList(List<FilesBean> filesToList) {
		this.filesToList = filesToList;
	}

	/**
	 * @return the foldersToList
	 */
	public List<FilesBean> getFoldersToList() {
		return foldersToList;
	}

	/**
	 * @param foldersToList the foldersToList to set
	 */
	public void setFoldersToList(List<FilesBean> foldersToList) {
		this.foldersToList = foldersToList;
	}

	/**
	 * @return the fileToUpload
	 */
	public String getFileToUpload() {
		return fileToUpload;
	}

	/**
	 * @param fileToUpload the fileToUpload to set
	 */
	public void setFileToUpload(String fileToUpload) {
		this.fileToUpload = fileToUpload;
	}

	/**
	 * @return the fileToDelete
	 */
	public String getFileToDelete() {
		return fileToDelete;
	}

	/**
	 * @param fileToDelete the fileToDelete to set
	 */
	public void setFileToDelete(String fileToDelete) {
		this.fileToDelete = fileToDelete;
	}

	/**
	 * @return the folderName
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * @param folderName the folderName to set
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
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
	 * @return the readFile
	 */
	public String getReadFile() {
		return readFile;
	}

	/**
	 * @param readFile the readFile to set
	 */
	public void setReadFile(String readFile) {
		this.readFile = readFile;
	}

	/**
	 * @return the newFileName
	 */
	public String getNewFileName() {
		return newFileName;
	}

	/**
	 * @param newFileName the newFileName to set
	 */
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	/**
	 * @return the folderNameToCreate
	 */
	public String getFolderNameToCreate() {
		return folderNameToCreate;
	}

	/**
	 * @param folderNameToCreate the folderNameToCreate to set
	 */
	public void setFolderNameToCreate(String folderNameToCreate) {
		this.folderNameToCreate = folderNameToCreate;
	}
}
