package com.cbk.devconstruction.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.devconstruction.enums.ClassType;
import com.cbk.devconstruction.exception.InternalServerException;

/**
 * 
 * @author sansintkyaw
 *
 */
@Service
public class AttachmentService {

	@Autowired
	private ApplicationContext context;

	@Transactional(rollbackFor = Exception.class)
	public void uploadAttachments(MultipartFile[] attachments, ClassType type, Long id) throws Exception {

		String className = type.getAttachmentValue();

		Class<?> serviceClass = Class.forName(className);

		Method method = serviceClass.getSuperclass().getDeclaredMethod("uploadAttachments", MultipartFile[].class,
				Long.class);

		try {
			method.invoke(context.getBean(serviceClass), attachments, id);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteAttachment(String ids, ClassType type) throws Exception {

		String className = type.getAttachmentValue();

		Class<?> serviceClass = Class.forName(className);

		Method method = serviceClass.getSuperclass().getDeclaredMethod("deleteAttachment", String.class);

		try {
			method.invoke(context.getBean(serviceClass), ids);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

	}

	public Resource downloadAttachment(Long id, ClassType type) throws Exception {

		String className = type.getAttachmentValue();

		Class<?> serviceClass = Class.forName(className);

		Method method = serviceClass.getSuperclass().getDeclaredMethod("downloadAttachment", Long.class);

		Resource resource = null;
		try {
			resource = (Resource) method.invoke(context.getBean(serviceClass), id);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				throw new InternalServerException(cause.getLocalizedMessage());
			} else
				throw new InternalServerException(e.getLocalizedMessage());
		}

		return resource;

	}

}
