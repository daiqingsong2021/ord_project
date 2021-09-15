package com.wisdom.acm.szxm.controller.doc;

import com.wisdom.acm.szxm.vo.doc.FastDFSFile;
import com.wisdom.acm.szxm.vo.doc.FdfsFileVo;
import com.wisdom.base.common.util.SysContext;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Properties;

public class FastDFSClient {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

	private static boolean loadProperties = false;
	static {
		try {
			if(!loadProperties){
				InputStream is = SysContext.findResoureFile("application-fdfs-config.properties");
				Properties p = new Properties();
				p.load(is);
				ClientGlobal.initByProperties(p);
			}
			/*
			String filePath = new ClassPathResource("config/fdfs_client.conf").getFile().getAbsolutePath();
			ClientGlobal.init(filePath);
			*/
		} catch (Exception e) {
			logger.error("FastDFS Client Init Fail!",e);
		}
	}

	public static String[] upload(FastDFSFile file) {
		logger.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);

		NameValuePair[] meta_list = new NameValuePair[1];
		meta_list[0] = new NameValuePair("author", file.getAuthor());

		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		StorageClient storageClient=null;
		try {
			storageClient = getTrackerClient();
			uploadResults = storageClient.uploadFile(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			logger.error("IO Exception when uploadind the file:" + file.getName(), e);
		} catch (Exception e) {
			logger.error("Non IO Exception when uploadind the file:" + file.getName(), e);
		}
		logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

		if (uploadResults == null && storageClient!=null) {
			logger.error("upload file fail, error code:" + storageClient.getErrorCode());
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];

		logger.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
		return uploadResults;
	}

	public static FileInfo getFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();
			return storageClient.getFileInfo(groupName, remoteFileName);
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static InputStream downFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();

			byte[] fileByte = storageClient.downloadFile(groupName, remoteFileName);
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public static byte[] getFileContent(String groupName, String remoteFileName){
		byte[] content = null ;
		try {
			InputStream in = FastDFSClient.downFile(groupName,remoteFileName);
			content = new byte[in.available()];
			in.read(content);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	public static void deleteFile(String groupName, String remoteFileName)
			throws Exception {
		StorageClient storageClient = getTrackerClient();
		int i = storageClient.deleteFile(groupName, remoteFileName);
		logger.info("delete file successfully!!!" + i);
	}

	public static StorageServer[] getStoreStorages(String groupName)
			throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getStoreStorages(trackerServer, groupName);
	}

	public static ServerInfo[] getFetchStorages(String groupName,
												String remoteFileName) throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
	}

	public static String getTrackerUrl() throws IOException {
		return "http://"+getTrackerServer().getInetSocketAddress().getHostString()+":"+ClientGlobal.getTrackerHttpPort()+"/";
	}

	private static StorageClient getTrackerClient() throws IOException {
		TrackerServer trackerServer = getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);

		return  storageClient;
	}

	private static TrackerServer getTrackerServer() throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return  trackerServer;
	}

	/**
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	public static FdfsFileVo uploadFile(MultipartFile multipartFile) throws IOException {

		String fileName=multipartFile.getOriginalFilename();
		InputStream inputStream=multipartFile.getInputStream();
		return uploadFile(fileName,inputStream);
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static FdfsFileVo uploadFile(File file) throws IOException {

		String fileName=file.getName();
		InputStream inputStream = new FileInputStream(file);

		return uploadFile(fileName,inputStream);
	}

	/**
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static FdfsFileVo uploadFile(String fileName,InputStream inputStream) throws IOException {

		String[] fileAbsolutePath={};
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		byte[] file_buff = null;
		if(inputStream!=null){
			int len1 = inputStream.available();
			file_buff = new byte[len1];
			inputStream.read(file_buff);
		}
		inputStream.close();
		FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
		try {
			fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs
		} catch (Exception e) {
			logger.error("upload file Exception!",e);
		}
		if (fileAbsolutePath==null) {
			logger.error("upload file failed,please upload again!");
		}
		String path= FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
		System.out.println("fastdfs url :" + path);

		FdfsFileVo fdfsFileVo = new FdfsFileVo(fileAbsolutePath[0],fileAbsolutePath[1]);

		return fdfsFileVo;
	}

	public static String getFdfsUrl(FdfsFileVo fdfsFileVo) throws IOException {
        return FastDFSClient.getTrackerUrl()+fdfsFileVo.getGroupName()+ "/"+fdfsFileVo.getRemoteFileName();
	}

    public static String getFdfsRelativelyUrl(FdfsFileVo fdfsFileVo) {
        return fdfsFileVo.getGroupName()+ "/"+fdfsFileVo.getRemoteFileName();
    }
}