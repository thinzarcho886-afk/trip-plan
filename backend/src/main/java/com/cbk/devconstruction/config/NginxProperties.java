package com.cbk.devconstruction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author sansintkyaw
 *
 */
@Component
@ConfigurationProperties("nginx")
@Data
public class NginxProperties {

	private String directory;
	private String url;
	private String fileDirectory;
	private String fileUrl;
	private String importFolder;
	private String exportFolder;

}
