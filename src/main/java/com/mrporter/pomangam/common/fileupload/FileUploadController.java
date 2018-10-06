package com.mrporter.pomangam.common.fileupload;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.mrporter.pomangam.common.ajax.AjaxUtils;
import com.mrporter.pomangam.common.pattern.vo.Status;

@Controller
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	@PreAuthorize("hasAnyRole('ROLE_VOD_EDIT','ROLE_APP_EDIT')")
	@RequestMapping(value = "/upload")
	public void upload(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "returnUrl", required = false) String returnUrl,
			@RequestParam(value = "filename", required = false) String filename) throws Exception {
		
		path = request.getSession().getServletContext().getRealPath("/");
		
		System.out.println(file);
		if(file != null) {
			System.out.println("upload "+FileUploadManager.upload(type, file, filename, path));
		}
		request.setAttribute("message", "File '" + filename + "' uploaded successfully");
		if(returnUrl==null||returnUrl.length()==0) {
			// do nothing~~
		} else {
			response.sendRedirect("./"+returnUrl+".do");
		}
		
	}
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
	@ExceptionHandler
	public @ResponseBody Status handle(Exception e, HttpServletResponse response) {
		if(e.getClass().getSimpleName().equals("AccessDeniedException")) {
			try {
				response.sendRedirect("./denied"); 
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info("Exception Handler - " + e.getMessage());
		return new Status(400, "Exception handled!" + e.getMessage());
	}
}
