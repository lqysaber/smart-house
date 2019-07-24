package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ConfigDataForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String locationFile;
	
	@NotEmpty
	private String environmentalFile;
	
	@NotEmpty
	private String cameraOriginalCatalog;// 相机拍摄原始文件存储位置C:\Users\Administrator\Desktop\
	
	@NotEmpty
	private String cameraManageCatalog;// 相机拍摄后加工处理的文件存储位置C:\Users\Administrator\Desktop\
	
	@NotEmpty
	private String videoScreenshotCatalog;// 视频快照截图存储位置C:\Users\Administrator\Desktop\
	
	@NotEmpty
	private String videoVideotapeCatalog; // 视频录像存储位置C:\Users\Administrator\Desktop\
	
	@NotEmpty
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
	
	@NotNull
	private Integer cursor;

	public Integer getCursor() {
		return null == cursor ? 0 : cursor.intValue();
	}

	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}
	
	
}
