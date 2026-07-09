package com.cbk.trip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbk.trip.enums.ClassType;
import com.cbk.trip.service.AttachmentService;
import com.cbk.trip.utils.CommonUtil;

@RestController
@RequestMapping(value = "/api/auth/attachment")
public class AttachmentController {
	@Autowired
	AttachmentService attachmentService;

	@PostMapping
	public ResponseEntity<?> uploadAttachments(@RequestParam("attachments") MultipartFile[] attachments,
			@RequestParam("type") ClassType type, @RequestParam("id") Long id) throws Exception {

		attachmentService.uploadAttachments(attachments, type, id);

		return new ResponseEntity<>(CommonUtil.responseString("Attachment Uploaded."), HttpStatus.CREATED);
	}

	@DeleteMapping()
	public ResponseEntity<?> deleteAttachment(@Param("ids") String ids, @Param("type") ClassType type)
			throws Exception {

		attachmentService.deleteAttachment(ids, type);

		return new ResponseEntity<>(CommonUtil.responseString("Attachment Deleted."), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> downloadAttachment(@PathVariable("id") Long id, @Param("type") ClassType type)
			throws Exception {

		Resource resource = attachmentService.downloadAttachment(id, type);

		String contentType = "application/octet-stream";
		String fileName = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, fileName)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION).body(resource);
	}
}
