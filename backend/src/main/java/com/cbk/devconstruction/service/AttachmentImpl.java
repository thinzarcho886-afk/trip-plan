package com.cbk.devconstruction.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.entity.AttachmentBaseEntity;
import com.cbk.devconstruction.exception.ResourceNotFoundException;
import com.cbk.devconstruction.utils.CommonUtil;
import com.cbk.devconstruction.utils.NginxUtil;

/**
 * Default implementation of the attachment files upload
 * 
 * @author sansintkyaw
 * 
 * @param <T>  type of parent entity
 * @param <R>  type of parent repository
 * @param <AT> type of attachment detail entity
 * @param <AR> type of attachment detail repository
 */
public abstract class AttachmentImpl<T, R extends JpaRepository<T, Long>, AT extends AttachmentBaseEntity, AR extends JpaRepository<AT, Long>> {

	@Autowired
	private R repository;

	@Autowired
	private AR attachmentRepository;

	/**
	 * create attachment detail entity
	 */
	protected abstract AT createAttachmentEntity(T entity);

	/**
	 * upload attachment files
	 */
	void uploadAttachments(MultipartFile[] attachments, Long id) throws IOException {

		// get entity
		T entity = CommonUtil.checkValidById(id, repository);

		List<AT> entityList = new ArrayList<>();
		int i = 0;
		for (MultipartFile attachment : attachments) {

			// upload file
			String fileUrl = NginxUtil.uploadMultipartFile(attachment, entity.getClass().getSimpleName()
					+ new Date().getTime() + i + File.separator + attachment.getOriginalFilename());

			String fileName = NginxUtil.getFileNameWithoutExtension(attachment);

			AT attachmentEntity = createAttachmentEntity(entity);
			attachmentEntity.setFileName(fileName);
			attachmentEntity.setFileUrl(fileUrl);

			entityList.add(attachmentEntity);

			i++;
		}

		attachmentRepository.saveAll(entityList);
	}

	/**
	 * Delete Attachment files
	 * 
	 * @throws IOException
	 */
	void deleteAttachment(String ids) throws IOException {

		List<Long> idList = CommonUtil.stringToList(ids, ",", Long::valueOf);

		for (Long id : idList) {
			AT detailEntity = CommonUtil.checkValidById(id, attachmentRepository);

			String fileUrl = detailEntity.getFileUrl();

			NginxUtil.deleteImage(fileUrl, false);

			attachmentRepository.delete(detailEntity);
		}
	}

	Resource downloadAttachment(Long id) throws MalformedURLException {

		AT detailEntity = CommonUtil.checkValidById(id, attachmentRepository);

		String fileUrl = detailEntity.getFileUrl();

		String filePath = NginxUtil.getFilePath(fileUrl);

		Path path = Paths.get(filePath);

		if (path == null) {
			throw new ResourceNotFoundException("File not found.");
		}

		return new UrlResource(path.toUri());

	}

}
