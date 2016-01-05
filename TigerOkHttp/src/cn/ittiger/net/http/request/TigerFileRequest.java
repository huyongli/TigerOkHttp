package cn.ittiger.net.http.request;

import java.io.File;

import cn.ittiger.net.http.parser.TigerFileParser;
import cn.ittiger.net.http.parser.TigerParser;

/**
 * 文件下载请求
 * @auther: hyl
 * @time: 2015-9-10上午10:27:26
 */
public class TigerFileRequest extends TigerRequest<File> {
	/**
	 * 下载文件的存量地址(文件所在绝对地址，包含文件名
	 * 只需要给出地址就行，不需要保证其父目录存在，解析的时候会自动判断，父目录不存在会自动创建)
	 */
	private String fileDownloadPath;
	/**
	 * 文件下载成功的解析器
	 */
	protected TigerFileParser fileParser;
	
	public TigerFileRequest(String url, String fileDownloadPath) {
		super(url);
		this.fileDownloadPath = fileDownloadPath;
	}

	/**
	 * 获取文件下载后的存储路径
	 * @return
	 */
	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	@Override
	public TigerParser<File> getDataParser() {
		if(fileParser == null) {
			fileParser = new TigerFileParser(fileDownloadPath, this);
		}
		return fileParser;
	}
}
