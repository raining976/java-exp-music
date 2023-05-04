package musicclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

import httpclient.FileDownloader;
import httpclient.MusicSheetAndFilesUploader;
import httpclient.MusicSheetTaker;
import model.MusicSheet;

/**
 * MusicOperationClient 与music.server进行互操作的类
 * 
 * @author xiaodong
 *
 */
public class MusicOperationClient {

	private final String URL_Server = "http://119.167.221.16:38080/music.server";

	private final String URL_DownloadMusicFile = URL_Server + "/downloadMusic";
	private final String URL_DownloadMusicSheetPicture = URL_Server + "/downloadPicture";
	private final String URL_CreateMusicSheetAndUploadFiles = URL_Server + "/upload";
	private final String URL_QueryAllMusicSheets = URL_Server + "/queryMusicSheets?type=all";

	public MusicOperationClient() {
	}

	/**
	 * createMusicSheetAndUploadFiles 在服务器上创建音乐单并上传音乐文件
	 * 
	 * @param musicSheet     MusicSheet对象
	 * @param musicFilePaths 上传的音乐列表对象，列表中的项为音乐在本地磁盘的路径字符串
	 */
	public void createMusicSheetAndUploadFiles(MusicSheet musicSheet, List<String> musicFilePaths) {
		MusicSheetAndFilesUploader.createMusicSheetAndUploadFiles(URL_CreateMusicSheetAndUploadFiles, musicSheet,
				musicFilePaths);
	}

	/**
	 * queryAllMusicSheets 从服务器查询所有音乐单，返回MusicSheet类型对象列表
	 * 
	 * @return List<MusicSheet>
	 */
	public List<MusicSheet> queryAllMusicSheets() {
		try {
			return MusicSheetTaker.queryMusicSheets(URL_QueryAllMusicSheets);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * downloadMusicFile 下载音乐文件，需要传入预下载音乐文件的md5及本地保存路径；本地保存路径为本地目录，不需要包含文件名
	 * 
	 * @param fileMd5    音乐文件md5字符串
	 * @param targetPath 音乐文件本地存储路径
	 */
	public void downloadMusicFile(String fileMd5, String targetPath) {
		FileDownloader.downloadMusicFile(URL_DownloadMusicFile, fileMd5, targetPath);
	}

	/**
	 * downloadMusicSheetPicture
	 * 下载音乐单封面图片，需要传入预下载图片的音乐单uuid及本地保存路径；本地保存路径为本地目录，不需要包含文件名
	 * 
	 * @param musicSheetUuid 音乐单uuid
	 * @param targetPath     音乐单封面图片的本地保存路径
	 */
	public void downloadMusicSheetPicture(String musicSheetUuid, String targetPath) {
		FileDownloader.downloadMusicSheetPicture(URL_DownloadMusicSheetPicture, musicSheetUuid, targetPath);
	}

	public static void main(String[] args) {
		MusicOperationClient client = new MusicOperationClient();

		/*
		 * 列出所有歌单
		 */
		List<MusicSheet> sheets = client.queryAllMusicSheets();
		System.out.println(sheets.get(0).getName());

		/*
		 * 创建歌单并上传音乐
		 */
		/*
		 * MusicSheet sheet = new MusicSheet(); sheet.setCreator("王晓东");
		 * sheet.setCreatorId("2011022"); sheet.setDateCreated("2023-04-03");
		 * sheet.setName("枪炮与玫瑰");
		 * sheet.setPicture("/Users/xiaodong/Music/Music2017/GunsNRoses.jpeg");
		 * 
		 * List<String> filePaths = new ArrayList<String>();
		 * 
		 * filePaths.add("/Users/xiaodong/Music/Music2017/Knockin' On Heaven's Door.mp3"
		 * );
		 * 
		 * client.createMusicSheetAndUploadFiles(sheet, filePaths);
		 */
		MusicSheet sheet = new MusicSheet();
		sheet.setCreator("张润宁");
		sheet.setCreatorId("21020007127");
		sheet.setDateCreated("2023-04-10");
		sheet.setName("最好的薛之谦");
		sheet.setPicture("D:\\photo\\xzq.jfif");

		List<String> filePaths = new ArrayList<String>();

		filePaths.add("D:\\CloudMusic\\薛之谦 - 丑八怪.mp3");
//		filePaths.add("/Users/xiaodong/Music/Music2017/哆啦A梦_梦想成真.mp3");
//		filePaths.add("/Users/xiaodong/Music/Music2017/哆啦A梦_幸福之门.mp3");
//		filePaths.add("/Users/xiaodong/Music/Music2017/哆啦A梦_我想见你.mp3");

		client.createMusicSheetAndUploadFiles(sheet, filePaths);

	}
}
