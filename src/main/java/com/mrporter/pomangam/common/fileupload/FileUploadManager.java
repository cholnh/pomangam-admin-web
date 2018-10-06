package com.mrporter.pomangam.common.fileupload;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mrporter.pomangam.common.map.dao.MapCrudDAO;

/**
 * 
 * @author choln
 *
 */
public class FileUploadManager {
	
	public static boolean upload(String type, MultipartFile file) {
		return upload(type, file, null, null);
	}
	public static boolean upload(String type, MultipartFile file, String path) {
		return upload(type, file, null, path);
	}
	public static boolean upload(String type, MultipartFile file, String filename, String path) {
		boolean result = true;
		String orgfn = file.getOriginalFilename();
		String filepath;
		String fn = (filename==null||filename.length()==0)?
							(orgfn==null||orgfn.length()==0)?"error":orgfn:
								filename+(orgfn.substring(orgfn.lastIndexOf(".")));
		if(path==null) {
			filepath = new MapCrudDAO().getValue("upload_default_path");
			filepath += ("/"+fn);
		} else {
			if(type.equals("vod")) {
				filepath = path + new MapCrudDAO().getValue("vod_image_upload_path") + (path.endsWith("/")?"":"/")+fn;
			} else if (type.equals("live")) {
				filepath = path + new MapCrudDAO().getValue("live_image_upload_path") + (path.endsWith("/")?"":"/")+fn;
			} else if (type.equals("ads")) {
				filepath = path + new MapCrudDAO().getValue("ads_image_upload_path") + (path.endsWith("/")?"":"/")+fn;
			} else if (type.equals("vodFile")) {
				filepath = path + new MapCrudDAO().getValue("vod_file_upload_path") + (path.endsWith("/")?"":"/")+fn;
			} else {
				filepath = path + new MapCrudDAO().getValue("upload_default_path") + (path.endsWith("/")?"":"/")+fn;
			}
		}
		File dest = new File(filepath);
		try {
			if(dest.exists()) {
				dest.delete();
			}
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			result = false;
			e.printStackTrace();
		}
		System.out.println("filepath "+filepath);
		return result;
	}
	
	public static void main(String...args){
	}
}
