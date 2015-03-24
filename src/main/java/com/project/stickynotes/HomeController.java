package com.project.stickynotes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dropbox.core.DbxException;
import com.project.stickynotes.dao.CustomMongoOperations;
import com.project.stickynotes.forms.CreateFile;
import com.project.stickynotes.forms.FilesForm;
import com.project.stickynotes.forms.LoginForm;
import com.project.stickynotes.service.DropBoxService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static final String APP_KEY = "z7fbx59ihvmdpgo";
	private static final String APP_SECRET = "f6b7wd2f1m5hdbg";
	DropBoxService dbxService = new DropBoxService();
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate );
		return "home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> loginPage() Method Start");
		LoginForm loginForm = new LoginForm();
		String method = request.getParameter("method");
		if(chkIsNull(method)) {
			if(method.equalsIgnoreCase("userLoginError")) {
				model.addAttribute("loginError", "login.error");
			} 
			
			if(method.equalsIgnoreCase("userSignUpSuccess")) {
				model.addAttribute("loginInfo", "signup.success");
			} 
			
			if(method.equalsIgnoreCase("signupError")) {
				model.addAttribute("loginError", "signup.error");
			}
		}
		logger.info("HomeController class --->>> loginPage() Method End");
        return new ModelAndView("login" , "loginForm", loginForm);
    }
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logoutPage(HttpServletRequest request) {
		logger.info("HomeController class --->>> logoutPage() Method Start");
		request.getSession().invalidate();
		logger.info("HomeController class --->>> logoutPage() Method End");
        return new ModelAndView("redirect:/login");//    loginPage();
    }
	
	@RequestMapping(value = "/help", method = RequestMethod.POST)
    public String helpPage(HttpServletRequest request) {
		logger.info("HomeController class --->>> logoutPage() Method");
        return "redirect:https://www.dropbox.com/help";
    }
	
//	@RequestMapping(value = "/home", method = RequestMethod.POST)
//    public String login(HttpServletRequest request, HttpServletResponse response) {
//		logger.info("HomeController class --->>> login() Method Start");
//		String authorizeUrl = null;
//		try { 
//		    HttpSession session = request.getSession(true);
//		    authorizeUrl = dbxService.getAuthorizeUrl(APP_KEY, APP_SECRET, session);
//		} catch(Exception expt) {
//			System.out.println("Error in Login");
//			return "errorPage";
//		}
//	    logger.info("HomeController class --->>> login() Method End");
//		return "redirect:" + authorizeUrl;
//    }
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/callback", params = "code", method = RequestMethod.GET)
	@ResponseBody
    public ModelAndView accessCode(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
		logger.info("HomeController class --->>> accessCode() Method Start");
		Map map = null;
		try {
			map = request.getParameterMap();
			dbxService.linkAccount(map);
		} catch (Exception exp) {
			return new ModelAndView("redirect:/login");
		}
		logger.info("HomeController class --->>> accessCode() Method End");
		return new ModelAndView("redirect:/getFiles");
    }
	
	@RequestMapping(value = "/callback", params = "error_description", method = RequestMethod.GET)
	@ResponseBody
    public ModelAndView error(@RequestParam("state") String state, @RequestParam("error") String error, @RequestParam("error_description") String description) {
		return new ModelAndView("redirect:/login");
    }
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView upload(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> upload() Method");
		FilesForm filesForm = null;
		try {
			filesForm = getFormDetails();
			String method = request.getParameter("method");
			if(chkIsNull(method)) {
				if(method.equalsIgnoreCase("uploadFileExceeds")) {
					String isFileSizeExceeds = request.getParameter("isFileSizeExceeds");
					if(chkIsNull(isFileSizeExceeds) && isFileSizeExceeds.equalsIgnoreCase("true")) {
						model.addAttribute("resultError", "error.file.size");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("DropBox Exception");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
        return new ModelAndView("uploadFile", "filesForm", filesForm);
    }
	
	@RequestMapping(value = "/savefile", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		logger.info("HomeController class --->>> save() Method Start");
		boolean isUploaded = false;
		String folderPath = null;
		boolean isFileSizeExceeds = true;
		try {
			if(file != null && file.getSize() > 0 && file.getSize() <= 2000000) {
				isFileSizeExceeds = false;
				folderPath = request.getParameter("selectedFolder");
				if(folderPath != null && !"".equalsIgnoreCase(folderPath)) {
					folderPath = "/" + request.getParameter("selectedFolder") + "/";
				} else {
					folderPath = "/";
				}
				isUploaded = dbxService.uploadFile(file, folderPath);
			} else {
				System.out.println("File size exceeds 2MB limit");
				return new ModelAndView("redirect:/upload?method=uploadFileExceeds&isFileSizeExceeds="+isFileSizeExceeds);
			}
		} catch (Exception exp) {
			System.out.println("General Exception while upload");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> save() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=upload&isUploaded="+isUploaded);
    }
	
	@RequestMapping(value = "/getFiles", method = RequestMethod.GET)
	public ModelAndView getAllFiles(ModelMap model) {
		logger.info("HomeController class --->>> getAllFiles() Method Start");
		FilesForm filesForm = null;
		try {
			filesForm = getFormDetails();
		} catch (Exception dbxExp) {
			System.out.println("DropBox Exception");
			filesForm = new FilesForm();
			model.addAttribute("resultError", "error.dropbox");
		}
		logger.info("HomeController class --->>> getAllFiles() Method End");
		return new ModelAndView("stickynotes", "filesForm", filesForm);		
	}
	
	public FilesForm getFormDetails() throws DbxException {
		FilesForm filesForm = new FilesForm();
		filesForm.setLoggedInUser(dbxService.getLoggedInUser());
		filesForm.setFilesToList(dbxService.getFiles());
		return filesForm;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteFileFromDbx(HttpServletRequest request) {
		logger.info("HomeController class --->>> deleteFileFromDbx() Method Start");
		String pathToDelete = request.getParameter("pathToDelete");
		boolean isDeleted = false;
		try {
			dbxService.deleteFile(pathToDelete);
			isDeleted = true;
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
			isDeleted = false;
		}
		logger.info("HomeController class --->>> deleteFileFromDbx() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=delete&isDeleted="+isDeleted);
    }
	
	@RequestMapping(value = "/download", method = RequestMethod.POST)
    public ModelAndView downloadFileFromDbx(HttpServletRequest request, HttpServletResponse response) {
		logger.info("HomeController class --->>> downloadFileFromDbx() Method Start");
		String fileNameToDownload = request.getParameter("fileNameToDownload");
		String pathToDownload = request.getParameter("pathToDownload");
		boolean isDownloaded = false;
		try {
			isDownloaded = dbxService.downloadFile(fileNameToDownload, pathToDownload);
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
			isDownloaded = false;
		}
		logger.info("HomeController class --->>> downloadFileFromDbx() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=download&isDownloaded="+isDownloaded);
    }
	
	@RequestMapping(value = "/read", method = RequestMethod.POST)
    public ModelAndView readFileFromDbx(HttpServletRequest request, HttpServletResponse response) {
		logger.info("HomeController class --->>> readFileFromDbx() Method Start");
		String fileNameToRead = request.getParameter("fileNameToRead");
		String pathToRead = request.getParameter("pathToRead");
		FilesForm filesForm = null;
		try {
			if(fileNameToRead != null && fileNameToRead.endsWith(".txt")) {
				String fileRead = dbxService.readFile(fileNameToRead, pathToRead);
			    filesForm = getFormDetails();
			    filesForm.setReadFile(fileRead);
			} else {
				return new ModelAndView("redirect:/getAllFilesLater?method=readTxtOnly");
			}
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> readFileFromDbx() Method End");
		return new ModelAndView("readFile", "filesForm", filesForm);	
    }
	
	@RequestMapping(value = "/newFile", method = RequestMethod.GET)
    public ModelAndView newFile(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> newFile() Method Start");
		FilesForm filesForm = null;
		try {
		    filesForm = getFormDetails();
		    filesForm.setReadFile("");
		    
		    String method = request.getParameter("method");
			if(chkIsNull(method)) {
				if(method.equalsIgnoreCase("uploadNewFile")) {
					String isFileEmpty = request.getParameter("isFileEmpty");
					if(chkIsNull(isFileEmpty) && isFileEmpty.equalsIgnoreCase("true")) {
						model.addAttribute("resultError", "error.enter.file.name");
					}
				}
				
				if(method.equalsIgnoreCase("uploadNewFileContent")) {
					String noFileContent = request.getParameter("noFileContent");
					if(chkIsNull(noFileContent) && noFileContent.equalsIgnoreCase("true")) {
						model.addAttribute("resultError", "error.enter.file.content");
					}
				}
			}
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> newFile() Method End");
        return new ModelAndView("newFile", "filesForm", filesForm);
    }
	
	@RequestMapping(value = "/uploadNewFile", method = RequestMethod.POST)
    public ModelAndView uploadNewFileFromDbx(HttpServletRequest request, HttpServletResponse response) {
		logger.info("HomeController class --->>> uploadNewFileFromDbx() Method Start");
		
		String fileName = null;
		String path = null;
		String fileContent = null;
		boolean isUploaded = false;
		InputStream input = null;		
		try {
			fileName = request.getParameter("newFileName");
			if(chkIsNull(fileName)) {
				path = "/" + fileName + ".txt";
			} else {
				return new ModelAndView("redirect:/newFile?method=uploadNewFile&isFileEmpty=true");
			}
			fileContent = request.getParameter("readFile");
			if(chkIsNull(fileContent)) {
				input = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
				isUploaded = dbxService.uploadNewFile(input, path);
			} else {
				return new ModelAndView("redirect:/newFile?method=uploadNewFileContent&noFileContent=true");
			}
		} catch (Exception ioe) {
			System.out.println("Exception Exception"+ioe.getMessage());
			isUploaded = false;
		}
		logger.info("HomeController class --->>> uploadNewFileFromDbx() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=upload&isUploaded="+isUploaded);
    }
	
	@RequestMapping(value = "/newFolder", method = RequestMethod.GET)
    public ModelAndView newFolderOnDbx(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> newFolderOnDbx() Method Start");
		FilesForm filesForm = null;
		try {
		    filesForm = getFormDetails();
		    String method = request.getParameter("method");
			if(chkIsNull(method) && method.equalsIgnoreCase("folderCreation")) {
				String isFolderCreated = request.getParameter("isFolderCreated");
				if(chkIsNull(isFolderCreated) && isFolderCreated.equalsIgnoreCase("false")) {
					model.addAttribute("resultError", "error.enter.folder.name");
				}
			}
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> newFolderOnDbx() Method End");
        return new ModelAndView("createFolder", "filesForm", filesForm);
    }
	
	@RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public ModelAndView createFolderOnDbx(HttpServletRequest request, HttpServletResponse response) {
		logger.info("HomeController class --->>> createFolderOnDbx() Method Start");
		String folderNameToCreate = null;
		boolean isFolderCreated = false;
		try {
			folderNameToCreate = request.getParameter("folderNameToCreate");
			if(chkIsNull(folderNameToCreate)) {
				folderNameToCreate= "/" + folderNameToCreate;
				isFolderCreated = dbxService.createNewFolder(folderNameToCreate);
			} else {
				return new ModelAndView("redirect:/newFolder?method=folderCreation&isFolderCreated="+isFolderCreated);
			}
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
			isFolderCreated = false;
		}
		logger.info("HomeController class --->>> createFolderOnDbx() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=folderCreation&isFolderCreated="+isFolderCreated);
    }
	
	@RequestMapping(value = "/shareUrl", method = RequestMethod.POST)
    public ModelAndView shareUrl(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> shareUrl() Method Start");
		FilesForm filesForm = null;
		String sharePath = null;
		String getSharePath = null;
		try {
			sharePath = request.getParameter("sharePathUrl");
		    filesForm = getFormDetails();
		    getSharePath = dbxService.shareUrl(sharePath);
		    if(chkIsNull(getSharePath)) {
		    	model.addAttribute("sharePathForm", getSharePath);
		    } else {
		    	throw new Exception();
		    }
		} catch (Exception expt) {
			System.out.println("ERROR --->>> "+expt.getMessage());
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> shareUrl() Method End");
		return new ModelAndView("stickynotes", "filesForm", filesForm);
    }
	
	@RequestMapping(value = "/givesignup", method = RequestMethod.POST)
	public ModelAndView giveSignupPage(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> loginPage() Method Start");
		LoginForm loginForm = new LoginForm();
		logger.info("HomeController class --->>> loginPage() Method End");
		return new ModelAndView("signup", "loginForm", loginForm);
	}

	@RequestMapping(value = "/signupurl", method = RequestMethod.POST)
	public ModelAndView signup(@ModelAttribute("loginForm") LoginForm loginForm,HttpServletRequest request) {
		CustomMongoOperations check1 = null;
		try {
			check1 = new CustomMongoOperations();
			int flag = check1.addUser(loginForm);
			if (flag == 0) {
				return new ModelAndView("redirect:/login?method=userSignUpSuccess");
			} else {
				return new ModelAndView("redirect:/login?method=signupError");
			}
		} catch(Exception expt) {
			return new ModelAndView("redirect:/login?method=signupError");
		}
	}
	
	@RequestMapping(value = "/signinurl", method = RequestMethod.POST)
	public String login(@ModelAttribute("LoginForm") LoginForm loginForm, HttpServletRequest request) {
		logger.info("HomeController class --->>> login() Method Start");

		String appUsername = null;
		String appPassword = null;
		CustomMongoOperations check1 = null;
		LoginForm aobj = null;
		String authorizeUrl = null;
		
		try {
			appUsername = loginForm.getAppUsername();
			appPassword = loginForm.getAppPassword();
			check1 = new CustomMongoOperations();
			aobj = check1.getUser(appUsername, appPassword);
			if (aobj != null) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", aobj);
				authorizeUrl = dbxService.getAuthorizeUrl(APP_KEY, APP_SECRET, session);
				logger.info("HomeController class --->>> login() Method End");
				return "redirect:" + authorizeUrl;
			} else {
				return "redirect:/login?method=userLoginError";
			}
		} catch (Exception expt) {
			System.out.println("Error in Login");
			return "redirect:/errorPage";
		}
	}
	
	@RequestMapping(value = "/errorPage", method = RequestMethod.GET)
    public ModelAndView authorizationError() {
		logger.info("HomeController class --->>> loginPage() Method Start");
        return new ModelAndView("errorPage");
    }	
	
	@RequestMapping(value = "/getAllFilesLater", method = RequestMethod.GET)
	public ModelAndView getAllFilesLater(HttpServletRequest request, ModelMap model) {
		logger.info("HomeController class --->>> getAllFilesLater() Method Start");
		
		FilesForm filesForm = null;
		String method = null;
		String isDeleted = null;
		String isDownloaded = null;
		String isUploaded = null;
		String isFolderCreated = null;
		String isEdited = null;
		try {
			filesForm = getFormDetails();

			method = request.getParameter("method");
			if(chkIsNull(method)) {
				if(method.equalsIgnoreCase("delete")) {
					isDeleted = request.getParameter("isDeleted");
					if(chkIsNull(isDeleted) && isDeleted.equalsIgnoreCase("true")) {
						model.addAttribute("resultInfo", "information.delete");
					} else {
						model.addAttribute("resultError", "error.delete");
					}
				} else if(method.equalsIgnoreCase("download")) {
					isDownloaded = request.getParameter("isDownloaded");
					if(chkIsNull(isDownloaded) && isDownloaded.equalsIgnoreCase("true")) {
						model.addAttribute("resultInfo", "information.download");
					} else {
						model.addAttribute("resultError", "error.download");
					}
				} else if(method.equalsIgnoreCase("upload")) {
					isUploaded = request.getParameter("isUploaded");
					if(chkIsNull(isUploaded) && isUploaded.equalsIgnoreCase("true")) {
						model.addAttribute("resultInfo", "information.upload");
					} else {
						model.addAttribute("resultError", "error.upload");
					}
				} else if(method.equalsIgnoreCase("folderCreation")) {
					isFolderCreated = request.getParameter("isFolderCreated");
					if(chkIsNull(isFolderCreated) && isFolderCreated.equalsIgnoreCase("true")) {
						model.addAttribute("resultInfo", "information.folder.created");
					} else {
						model.addAttribute("resultError", "error.folder.created");
					}
				} else if(method.equalsIgnoreCase("readTxtOnly")) {
					model.addAttribute("resultError", "error.readTxtOnly");
				} else if(method.equalsIgnoreCase("gotDropboxError")) {
					model.addAttribute("resultError", "error.dropbox");
				} else if(method.equalsIgnoreCase("fileEdit")) {
					isEdited = request.getParameter("isEdited");
					if(chkIsNull(isEdited) && isEdited.equalsIgnoreCase("true")) {
						model.addAttribute("resultInfo", "information.edit");
					} else {
						model.addAttribute("resultError", "error.edit");
					}
				} else {
					model.addAttribute("resultError", "error.dropbox");
				}
			}
		} catch (Exception dbxExp) {
			System.out.println("DropBox Exception");
			filesForm = new FilesForm();
			model.addAttribute("resultError", "error.dropbox");
		}
		logger.info("HomeController class --->>> getAllFilesLater() Method End");
		return new ModelAndView("stickynotes", "filesForm", filesForm);		
	}
	
	@RequestMapping(value = "/readfile", method = RequestMethod.POST)
	public ModelAndView readfile(HttpServletRequest request) {
		logger.info("HomeController class --->>>readfile() Method Start");
		String foldername = null;
		String folderpath = null;
		String filecontent = null;
		HttpSession session = request.getSession(true);
		CreateFile filecon = null;
		try {
			foldername = request.getParameter("filename");
			folderpath = request.getParameter("pathToRead");
			
			if(chkIsNull(foldername) && foldername.endsWith(".txt")) {
				filecontent = dbxService.dwnReadFile(foldername, folderpath);
				filecon = new CreateFile();
				filecon.setFilecontents(filecontent);
				filecon.setFilePath(folderpath);
				filecon.setNewName(foldername);
				session.setAttribute("editFileContent", filecon);
			} else {
				return new ModelAndView("redirect:/getAllFilesLater?method=readTxtOnly");
			}
			
		} catch (Exception exp) {
			System.out.println("General Exception while upload");
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		return new ModelAndView("showfile", "createFile", filecon);
	}

	@RequestMapping(value = "/createsaveeditfile", method = RequestMethod.POST)
	public ModelAndView createsaveedit(
			@ModelAttribute("CreateFile") CreateFile createfile,
			HttpServletRequest request, HttpServletResponse response,
			Exception exception) {
		logger.info("HomeController class --->>> createsaveedit() Method Start");
		boolean isEdit = false;
		String folderPath = null;
		File newfile = null;
		HttpSession session = null;
		CreateFile filecon = null;
		String filecontents = null;
		String newName = null;
		InputStream inputStream = null;

		try {
			System.out.println("Inside create save edit file");
			filecontents = createfile.getFilecontents();
			newName = createfile.getNewName();
			System.out.println(" Edited File Contents in string format-" + filecontents);
			System.out.println(" Exisiting NEw  File Name=" + newName);
			
			session = request.getSession(true);
			filecon = (CreateFile) session.getAttribute("editFileContent");
			
			newfile = new File("C://Dropbox Download//" + newName);
			FileUtils.writeStringToFile(newfile, filecontents);
			
			//delete first
			String folderpath = filecon.getFilePath();
			dbxService.deleteEditFile(folderpath);
			
			//upload again
			inputStream = new ByteArrayInputStream(filecontents.getBytes());
			
			folderPath = filecon.getFilePath();
			isEdit = dbxService.uploadCreatedEditedFile(newfile, folderPath, newName);
			isEdit = true;
		} catch (Exception exp) {
			System.out.println("General Exception while upload");
//			model.addAttribute("resultError", "error.dropbox");
			return new ModelAndView("redirect:/getAllFilesLater?method=gotDropboxError");
		}
		logger.info("HomeController class --->>> createsaveedit() Method End");
		return new ModelAndView("redirect:/getAllFilesLater?method=fileEdit&isEdited="+isEdit);
	}
	
	/**
	 * Method is to check if the String is empty or NULL
	 * 
	 * @param str
	 * @return boolean
	 */
	public boolean chkIsNull(String str) {
		boolean result = false;
		if (null != str && !"".equalsIgnoreCase(str)) {
			result = true;
		}
		return result;
	}
}
