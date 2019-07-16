package h.uniview.smarthouse.data;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ConfigMsg")
public class ConfigMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("ocationFile")
	private String locationFile; // 此配置文件位置C:\Users\Administrator\Desktop\config.xml
	
	@XStreamAlias("EnvironmentalFile")
	private String environmentalFile; // 环境数据文件位置C:\Users\Administrator\Desktop\tt.txt
	
	@XStreamAlias("CameraOriginalCatalog")
	private String cameraOriginalCatalog;// 相机拍摄原始文件存储位置C:\Users\Administrator\Desktop\
	
	@XStreamAlias("CameraManageCatalog")
	private String cameraManageCatalog;// 相机拍摄后加工处理的文件存储位置C:\Users\Administrator\Desktop\
	
	@XStreamAlias("VideoScreenshotCatalog")
	private String videoScreenshotCatalog;// 视频快照截图存储位置C:\Users\Administrator\Desktop\
	
	@XStreamAlias("VideoVideotapeCatalog")
	private String videoVideotapeCatalog; // 视频录像存储位置C:\Users\Administrator\Desktop\
	
	@XStreamAlias("VideoVmdalarmCatalog")
	private String videoVmdalarmCatalog;// 视频移动侦测警报截图存储位置C:\Users\Administrator\Desktop\

	public String getLocationFile() {
		return locationFile;
	}

	public void setLocationFile(String locationFile) {
		this.locationFile = locationFile;
	}

	public String getEnvironmentalFile() {
		return environmentalFile;
	}

	public void setEnvironmentalFile(String environmentalFile) {
		this.environmentalFile = environmentalFile;
	}

	public String getCameraOriginalCatalog() {
		return cameraOriginalCatalog;
	}

	public void setCameraOriginalCatalog(String cameraOriginalCatalog) {
		this.cameraOriginalCatalog = cameraOriginalCatalog;
	}

	public String getCameraManageCatalog() {
		return cameraManageCatalog;
	}

	public void setCameraManageCatalog(String cameraManageCatalog) {
		this.cameraManageCatalog = cameraManageCatalog;
	}

	public String getVideoScreenshotCatalog() {
		return videoScreenshotCatalog;
	}

	public void setVideoScreenshotCatalog(String videoScreenshotCatalog) {
		this.videoScreenshotCatalog = videoScreenshotCatalog;
	}

	public String getVideoVideotapeCatalog() {
		return videoVideotapeCatalog;
	}

	public void setVideoVideotapeCatalog(String videoVideotapeCatalog) {
		this.videoVideotapeCatalog = videoVideotapeCatalog;
	}

	public String getVideoVmdalarmCatalog() {
		return videoVmdalarmCatalog;
	}

	public void setVideoVmdalarmCatalog(String videoVmdalarmCatalog) {
		this.videoVmdalarmCatalog = videoVmdalarmCatalog;
	}

}
