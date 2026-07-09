package com.cbk.devconstruction.dto;

import com.cbk.devconstruction.entity.AttachmentBaseEntity;
import com.cbk.devconstruction.utils.NginxUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author sansintkyaw
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class AttachmentDTO extends CommonDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;
	private String fileUrl;

	public <T extends AttachmentBaseEntity> AttachmentDTO(T entity) {
		super(entity);
		this.fileName = entity.getFileName();
		this.fileUrl = NginxUtil.getFileUrl(entity.getFileUrl(), false);
	}

}
