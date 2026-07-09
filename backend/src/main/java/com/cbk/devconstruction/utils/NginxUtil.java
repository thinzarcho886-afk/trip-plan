package com.cbk.devconstruction.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.regex.Pattern;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.common.MessageConst;
import com.cbk.devconstruction.config.NginxProperties;

/**
 * 
 * @author sansintkyaw
 *
 */
@Component
public class NginxUtil {

	private static NginxProperties nginxProperties;

	@Autowired
	public NginxUtil(NginxProperties nginxProperties) {
		NginxUtil.nginxProperties = nginxProperties;
	}

	/**
	 * Get full file url
	 * 
	 * @param url     file url
	 * @param isImage {@code true} if it is for image
	 * @return {@code null} if not provided
	 */
	public static String getFileUrl(String url, boolean isImage) {
		if (!StringUtils.isEmpty(url)) {
			return (isImage ? nginxProperties.getUrl() : nginxProperties.getFileUrl()) + url;
		} else
			return null;
	}

	/**
	 * Upload base64 string Image to Server
	 * 
	 * @param image    base64 String
	 * @param fileName uploaded image file name without extension
	 * @return image sub directory path
	 * @throws IOException
	 * 
	 */
	public static String uploadImage(String image, String fileName) throws IOException {

		// Get the file type by splitting the base64 string
		String[] parts = image.split(",");
		String fileType = parts[0].split(":")[1].split(";")[0];

		// Get the file extension by splitting the file type
		String fileExtension = fileType.substring(fileType.lastIndexOf('/') + 1);

		// full file path
		String filePath = Instant.now().toEpochMilli() + File.separator + fileName + "." + fileExtension;
		String fullFilePath = nginxProperties.getDirectory() + filePath;

		// decode string to byte
		byte[] imageByte = Base64.decodeBase64(parts[1]);

		try {

			// create folder
			File folder = new File(fullFilePath);
			folder.getParentFile().mkdirs();

			// populate the fileBytes with the byte array of the file
			Path path = Paths.get(fullFilePath);
			Files.write(path, imageByte);

		} catch (IOException e) {
			throw new IOException(MessageConst.FILE_EXCEPTION);
		}

		return filePath;

	}

	/**
	 * Upload multipart file to Server
	 * 
	 * @param multipartFile
	 * @param imageDirectory sub directory including filename to upload file.
	 * @return image sub directory path
	 * @throws IOException
	 */
	public static String uploadMultipartFile(MultipartFile multipartFile, String imageDirectory) throws IOException {

		// full file path
		String filePath = nginxProperties.getFileDirectory() + imageDirectory;

		try {

			// get image byte
			byte[] imageByte = multipartFile.getBytes();

			// create folder if it doesn't exist
			File folder = new File(filePath);
			folder.getParentFile().mkdirs();

			// populate the fileBytes with the byte array of the file
			Path path = Paths.get(filePath);
			Files.write(path, imageByte);

		} catch (IOException e) {
			throw new IOException(MessageConst.FILE_EXCEPTION);
		}

		return imageDirectory;

	}

	/**
	 * Delete Image
	 * 
	 * @param oldFile
	 * @param isImage
	 * @throws IOException
	 */
	public static void deleteImage(String oldFile, boolean isImage) throws IOException {
		try {
			String nginxDirectory = "";
			nginxDirectory = isImage ? nginxProperties.getDirectory() : nginxProperties.getFileDirectory();

			// Remove old folder
			if (!StringUtils.isEmpty(oldFile)) {
				String delimiter = Pattern.quote(System.getProperty("file.separator"));
				String[] parts = oldFile.split(delimiter);
				String oldFolderId = parts[0];
				String oldFilePath = nginxDirectory + File.separator + oldFolderId;
				File f = new File(oldFilePath);
				FileUtils.deleteDirectory(f);
			}

		} catch (IOException ie) {
			throw new IOException(MessageConst.FILE_EXCEPTION);
		}
	}

	/**
	 * Get Original file name without extension
	 * 
	 * @param multipartFile
	 * @return
	 */
	public static String getFileNameWithoutExtension(MultipartFile multipartFile) {
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = "";
		int dotIndex = originalFileName.lastIndexOf(".");
		fileName = originalFileName.substring(0, dotIndex);
		return fileName;
	}

	/**
	 * 
	 * @param subDirectory
	 * @return
	 */
	public static String getFilePath(String subDirectory) {
		String filePath = nginxProperties.getFileDirectory() + subDirectory;
		return filePath;
	}

	/**
	 * Save base64 image to server
	 * 
	 * @param base64Image
	 * @param imageName
	 * @return
	 * @throws IOException
	 */
	public static String saveImage(String base64Image, String imageName) throws IOException {
		String imageUrl = null;
		// write image
		if (!StringUtils.isEmpty(base64Image)) {
			imageUrl = uploadImage(base64Image, imageName + "-" + Instant.now().toEpochMilli());
		}
		return imageUrl;
	}

	/**
	 * Update image in server. Delete image from server if {@code base64Image} is
	 * Empty and {@code isDelete} is true
	 * 
	 * @param base64Image
	 * @param oldFileUrl
	 * @param imageName
	 * @param isDelete
	 * @return
	 * @throws IOException
	 */
	public static String updateImage(String base64Image, String oldFileUrl, String imageName, boolean isDelete)
			throws IOException {
		String imageUrl = oldFileUrl;
		if (!StringUtils.isEmpty(base64Image)) {

			// delete old image
			NginxUtil.deleteImage(oldFileUrl, true);

			// upload image
			imageUrl = NginxUtil.uploadImage(base64Image, imageName + "-" + Instant.now().toEpochMilli());

		} else {

			// delete old image
			if (isDelete) {
				NginxUtil.deleteImage(oldFileUrl, true);
				imageUrl = null;
			}
		}
		return imageUrl;
	}

	/**
	 * Get File Directory for Import/Export
	 * 
	 * @param isImport
	 * @return
	 */
	public static String getExcelFilePath(String fileName, boolean isImport) {
		String filePath = "";
		if (isImport) {
			filePath = nginxProperties.getImportFolder() + fileName;
		} else {
			filePath = nginxProperties.getExportFolder() + fileName;
		}
		return filePath;
	}

	/**
	 * Delete file from absolute path
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

}
