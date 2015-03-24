/**
 * 
 */
package com.project.stickynotes.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.Folder;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWriteMode;
import com.project.stickynotes.beans.FilesBean;

/**
 * @author Varun
 *
 */
public class DropBoxService {
	
	private static final Logger logger = LoggerFactory.getLogger(DropBoxService.class);
	
	private DbxAppInfo appInfo;
	private DbxRequestConfig config;
	private DbxWebAuth webAuth;
	private DbxAuthFinish authFinish;
	private DbxClient client;
	
	private static final String REDIRECT_URI = "http://localhost:8080/stickynotes/callback";
//	private static final String REDIRECT_URI = "http://stickynotes.cfapps.io/stickynotes/callback";
	private static final String SESSION_KEY = "dropbox-auth-csrf-token";
    
    /**
     * Method to get the Dropbox Account's Authorization URL
     * @param appKey
     * @param appSecret
     * @param session
     * @return String authorizeUrl
     */
    public String getAuthorizeUrl(String appKey, String appSecret, HttpSession session) {
    	logger.info("DropBoxService class --->>> getAuthorizeUrl() Method Start");
    	
    	appInfo = new DbxAppInfo(appKey, appSecret);
		config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
	    DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);
	    webAuth = new DbxWebAuth(config, appInfo, REDIRECT_URI, csrfTokenStore);
	    
	    //Have the user sign in and authorize your app.
	    String authorizeUrl = webAuth.start();
	    
	    logger.info("DropBoxService class --->>> getAuthorizeUrl() Method End");
	    
	    return authorizeUrl;
    }
    
    /**
     * Method to link the User's Dropbox account to Stickynote Application
     * @param map
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void linkAccount(Map map) throws Exception {
    	logger.info("DropBoxService class --->>> linkAccount() Method Start");
    	
    	authFinish = webAuth.finish(map);
		String accessToken = authFinish.accessToken;
		
		client = new DbxClient(config, accessToken);
		System.out.println("Linked account: " + client.getAccountInfo().displayName);
		
		logger.info("DropBoxService class --->>> linkAccount() Method End");
    }
    
    /**
     * Method to get the current Logged-in user
     * @return String loggedInUser
     * @throws DbxException
     */
    public String getLoggedInUser() throws DbxException {
    	logger.info("DropBoxService class --->>> getLoggedInUser() Method Start");
    	
    	String loggedInUser = client.getAccountInfo().displayName;
    	
    	logger.info("DropBoxService class --->>> getLoggedInUser() Method End");
    	
    	return loggedInUser;
    }
    
    /**
     * Method to get the files/folders on the User's DropBox Account
     * @return
     * @throws DbxException
     */
    public List<FilesBean> getFiles() throws DbxException {
    	logger.info("DropBoxService class --->>> getFiles() Method Start");
    	
		DbxEntry.WithChildren listing = null;
		DbxEntry.WithChildren nestedListing = null;
		List<FilesBean> filesList = new ArrayList<FilesBean>();
		List<FilesBean> nestedFilesList = null;
		FilesBean filesBean = null;
		FilesBean nestedFilesBean = null;
		
		listing = client.getMetadataWithChildren("/");
		for (DbxEntry child : listing.children) {
			filesBean = new FilesBean();
			filesBean.setName(child.name);
			
			if(child.isFile()) {
				filesBean.setType("File"); 
				filesBean.setFile(true);
				filesBean.setLastModified(child.asFile().lastModified.toString());
				filesBean.setFileSize(child.asFile().humanSize);
				filesBean.setPath(child.asFile().path);
			} else {
				filesBean.setFile(false);
				filesBean.setType(child.iconName);
				filesBean.setPath(child.asFolder().path);
				nestedListing = client.getMetadataWithChildren("/" + child.name);
				nestedFilesList = new ArrayList<FilesBean>();
				for (DbxEntry nestedChild : nestedListing.children) {
					nestedFilesBean = new FilesBean();
					nestedFilesBean.setName(nestedChild.name);
					
					if(nestedChild.isFile()) {
						nestedFilesBean.setType("File");
						nestedFilesBean.setLastModified(nestedChild.asFile().lastModified.toString());
						nestedFilesBean.setFileSize(nestedChild.asFile().humanSize);
						nestedFilesBean.setPath(nestedChild.asFile().path);
						nestedFilesList.add(nestedFilesBean);
					} 
				}
				filesBean.setNestedFolders(nestedFilesList);
			}
			filesList.add(filesBean);
		}
		
		logger.info("DropBoxService class --->>> getFiles() Method End");
		
		return filesList; 		
	} 
    
    /**
     * Method to upload the file to the DropBox Account
     * @param file
     * @return boolean isUploaded
     * @throws IOException
     * @throws DbxException
     */
    public boolean uploadFile(MultipartFile file, String path) throws IOException, DbxException {
    	logger.info("DropBoxService class --->>> uploadFile() Method Start");
    	String name = null;
		InputStream inputStream = null;
		DbxEntry.File uploadedFile = null;
		boolean isUploaded = false;
		try {
			name = file.getOriginalFilename();
			inputStream = file.getInputStream();
			uploadedFile = client.uploadFile(path + name, DbxWriteMode.add(), file.getSize(), inputStream);
	        System.out.println("Uploaded: " + uploadedFile.toString());
	        isUploaded = true;
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
        }
		logger.info("DropBoxService class --->>> uploadFile() Method End");
		return isUploaded;
    }
    
    /**
     * Method to delete the file to the DropBox Account
     * @param file
     * @throws IOException
     * @throws DbxException
     */
    public void deleteFile(String pathToDelete) throws DbxException {
    	logger.info("DropBoxService class --->>> deleteFile() Method Start");
		client.delete(pathToDelete);
		logger.info("DropBoxService class --->>> deleteFile() Method End");
    }
    
    /**
     * Method to download the file to the DropBox Account
     * @param file
     * @return boolean isUploaded
     * @throws IOException
     * @throws DbxException
     */
    public boolean downloadFile(String fileName, String path) throws DbxException, IOException {
    	logger.info("DropBoxService class --->>> downloadFile() Method Start");
    	
    	java.io.File target = new java.io.File("C://Dropbox Download//"+fileName);
    	OutputStream outputStream = null;
		DbxEntry.File downloadFile = null;
		boolean isDownloaded = false;
		try {
			outputStream = new FileOutputStream(target);
			downloadFile = client.getFile(path, null, outputStream);
	        System.out.println("Downloaded: " + downloadFile.toString());
	        isDownloaded = true;
		} finally {
			outputStream.close();
        }
		
		logger.info("DropBoxService class --->>> downloadFile() Method End");
		return isDownloaded;
    }
    
    /**
     * Method to read the file to the DropBox Account
     * @param file
     * @return boolean isUploaded
     * @throws IOException
     * @throws DbxException
     */
    public String readFile(String fileName, String path) throws DbxException, IOException {
    	logger.info("DropBoxService class --->>> readFile() Method Start");
    	InputStream input = null;
    	StringBuilder out = new StringBuilder();
    	DbxClient.Downloader downloader = null;
		try {
			downloader = client.startGetFile(path, null);
			input = downloader.body;
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF8"));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        reader.close();
		} finally {
			if(downloader != null) {
				downloader.close();
			}
		}
		logger.info("DropBoxService class --->>> readFile() Method End");
		return out.toString();
    }
    
    /**
     * Method to upload the file to the DropBox Account
     * @param file
     * @return boolean isUploaded
     * @throws IOException
     * @throws DbxException
     */
    public boolean uploadNewFile(InputStream inputStream, String path) throws IOException, DbxException {
    	logger.info("DropBoxService class --->>> uploadNewFile() Method Start");
		DbxEntry.File uploadedFile = null;
		boolean isUploaded = false;
		try {
			uploadedFile = client.uploadFile(path, DbxWriteMode.update(null), -1, inputStream);
	        System.out.println("Uploaded: " + uploadedFile.toString());
	        isUploaded = true;
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
        }
		logger.info("DropBoxService class --->>> uploadNewFile() Method End");
		return isUploaded;
    }
    
    /**
     * Method to Create New Folder the file to the DropBox Account
     * @param folderName
     * @return
     * @throws IOException
     * @throws DbxException
     */
    public boolean createNewFolder(String folderName) throws DbxException {
    	logger.info("DropBoxService class --->>> createNewFolder() Method Start");
		boolean isFolderCreated = false;
		Folder createdFolder = client.createFolder(folderName);
		if(createdFolder != null && createdFolder.isFolder()) {
			isFolderCreated = true;
		}
		logger.info("DropBoxService class --->>> createNewFolder() Method End");
		return isFolderCreated;
    } 
    
    /**
     * Method to Create New Folder the file to the DropBox Account
     * @param folderName
     * @return
     * @throws IOException
     * @throws DbxException
     */
    public String shareUrl(String path) throws DbxException {
    	logger.info("DropBoxService class --->>> shareUrl() Method");
		return client.createShareableUrl(path);
    } 
    
    public String dwnReadFile(String filename, String path) throws IOException, DbxException {
		logger.info("DropBoxService class --->>> uploadFile() Method Start");
		File file = new File("C://Dropbox Download//" + filename);
		FileOutputStream outputStream = new FileOutputStream(file);
		DbxClient.Downloader downloader = client.startGetFile(path, null);
		StringBuilder out = new StringBuilder();
		InputStream in = null;
		String theString = null;
		if (downloader != null) {
			try {
				in = downloader.body;
				StringWriter writer = new StringWriter();
				IOUtils.copy(in, writer, "UTF-8");
				theString = writer.toString();
			} finally {
				downloader.close();
			}
		} else {
			System.out.println("downloader null");
		}
		logger.info("DropBoxService class --->>> uploadFile() Method End");
		return theString;
	}
    
    /**
	 * Method to delete the file to the DropBox Account
	 * 
	 * @param file
	 * @throws IOException
	 * @throws DbxException
	 */
	public void deleteEditFile(String path)
			throws DbxException {
		logger.info("DropBoxService class --->>> deleteEditFile() Method Start");
		client.delete(path);
		logger.info("DropBoxService class --->>> deleteEditFile() Method End");
	}
	
	/**
	 * Method to read the file to the DropBox Account
	 * 
	 * @param file
	 * @return boolean isUploaded
	 * @throws IOException
	 * @throws DbxException
	 */
	public boolean uploadCreatedEditedFile(File is, String path, String name)
			throws IOException, DbxException {
		logger.info("DropBoxService class --->>> uploadCreatedEditedFile() Method Start");
		InputStream inputStream = null;
		DbxEntry.File uploadedFile = null;
		boolean isUploaded = false;
		try {
			uploadedFile = client.uploadFile(path, DbxWriteMode.add(), is.length(), new FileInputStream(is));
			System.out.println("Uploaded: " + uploadedFile.toString());
			isUploaded = true;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		logger.info("DropBoxService class --->>> uploadCreatedEditedFile() Method End");
		return isUploaded;
	}
}
