package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ConfigDataForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String cameraOriginalCatalog;// 相机拍摄原始文件存储位置C:\Users\Administrator\Desktop\
	
	@NotNull
	private String cameraManageCatalog;// 相机拍摄后加工处理的文件存储位置C:\Users\Administrator\Desktop\
	
	@NotNull
	private String videoScreenshotCatalog;// 视频快照截图存储位置C:\Users\Administrator\Desktop\
	
	@NotNull
	private String videoVideotapeCatalog; // 视频录像存储位置C:\Users\Administrator\Desktop\
	
	@NotNull
	private String videoVmdalarmCatalog;// 视频移动侦测警报截图存储位置C:\Users\Administrator\Desktop\

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
