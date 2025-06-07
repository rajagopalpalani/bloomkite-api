package com.sowisetech.advisor.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
//@PropertySource("classpath:amazon.properties")
public class AmazonClient {

	private AmazonS3 s3client;
	private static final Logger logger = LoggerFactory.getLogger(AmazonClient.class);

	@Value("${endpointUrl}")
	private String endpointUrl;
	@Value("${bucketName}")
	private String bucketName;
	@Value("${region}")
	private String region;

	// @Value("${accessKey}")
	// private String accessKey;
	// @Value("${secretKey}")
	// private String secretKey;
	// AWSCredentialsProvider awsCredentialsProvider;

	@PostConstruct
	private void initializeAmazon() {
		// AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
		// this.secretKey);
		// awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
		// this.s3client =
		// AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(region)
		// .build();
		this.s3client = AmazonS3ClientBuilder.standard().withRegion(region).build();
		// this.s3client = AmazonS3ClientBuilder.standard().withRegion(region)
		// .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
	}

	protected void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));

	}

	public String uploadFile(MultipartFile multipartFile) {
		String fileUrl = "";
		boolean fileDeleteResult;
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			fileDeleteResult = file.delete();
			return fileUrl;
		} catch (Exception e) {
			logger.error("Error occurred while converting file");
			return null;
		}
	}

	public void deleteFile(String fileName) {

		logger.info("Deleting file with name= " + fileName);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		logger.info("File deleted successfully.");

	}

	protected File convertMultiPartToFile(MultipartFile file) throws IOException {
		FileOutputStream fos = null;
		try {
			File convFile = new File(file.getOriginalFilename());
			fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			return convFile;
		} catch (Exception e) {
			logger.error("Error occurred while converting file");
			return null;
		} finally {
			fos.close();
		}
	}

	protected String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	public String uploadPdfFile(MultipartFile multipartFile, String createdPdfName) {
		String fileUrl = "";
		boolean fileDeleteResult;
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = createdPdfName;
			fileUrl = endpointUrl + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			fileDeleteResult = file.delete();
			return fileUrl;
		} catch (Exception e) {
			logger.error("Error occurred while converting file");
			return null;
		}
	}

}
