package h.uniview.smarthouse.data;

import java.io.Serializable;

public class ConfigMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String LocationFile; // 此配置文件位置C:\Users\Administrator\Desktop\config.xml
	
	private String EnvironmentalFile; // 环境数据文件位置C:\Users\Administrator\Desktop\tt.txt
	
	private String CameraOriginalCatalog;// 相机拍摄原始文件存储位置C:\Users\Administrator\Desktop\
	
	private String CameraManageCatalog;// 相机拍摄后加工处理的文件存储位置C:\Users\Administrator\Desktop\
	
	private String VideoScreenshotCatalog;// 视频快照截图存储位置C:\Users\Administrator\Desktop\
	
	private String VideoVideotapeCatalog; // 视频录像存储位置C:\Users\Administrator\Desktop\
	
	private String VideoVmdalarmCatalog;// 视频移动侦测警报截图存储位置C:\Users\Administrator\Desktop\

	public String getLocationFile() {
		return LocationFile;
	}

	public void setLocationFile(String locationFile) {
		LocationFile = locationFile;
	}

	public String getEnvironmentalFile() {
		return EnvironmentalFile;
	}

	public void setEnvironmentalFile(String environmentalFile) {
		EnvironmentalFile = environmentalFile;
	}

	public String getCameraOriginalCatalog() {
		return CameraOriginalCatalog;
	}

	public void setCameraOriginalCatalog(String cameraOriginalCatalog) {
		CameraOriginalCatalog = cameraOriginalCatalog;
	}

	public String getCameraManageCatalog() {
		return CameraManageCatalog;
	}

	public void setCameraManageCatalog(String cameraManageCatalog) {
		CameraManageCatalog = cameraManageCatalog;
	}

	public String getVideoScreenshotCatalog() {
		return VideoScreenshotCatalog;
	}

	public void setVideoScreenshotCatalog(String videoScreenshotCatalog) {
		VideoScreenshotCatalog = videoScreenshotCatalog;
	}

	public String getVideoVideotapeCatalog() {
		return VideoVideotapeCatalog;
	}

	public void setVideoVideotapeCatalog(String videoVideotapeCatalog) {
		VideoVideotapeCatalog = videoVideotapeCatalog;
	}

	public String getVideoVmdalarmCatalog() {
		return VideoVmdalarmCatalog;
	}

	public void setVideoVmdalarmCatalog(String videoVmdalarmCatalog) {
		VideoVmdalarmCatalog = videoVmdalarmCatalog;
	}

	@Override
	public String toString() {
		return "ConfigMsg{" +
				"LocationFile='" + LocationFile + '\'' +
				", EnvironmentalFile='" + EnvironmentalFile + '\'' +
				", CameraOriginalCatalog='" + CameraOriginalCatalog + '\'' +
				", CameraManageCatalog='" + CameraManageCatalog + '\'' +
				", VideoScreenshotCatalog='" + VideoScreenshotCatalog + '\'' +
				", VideoVideotapeCatalog='" + VideoVideotapeCatalog + '\'' +
				", VideoVmdalarmCatalog='" + VideoVmdalarmCatalog + '\'' +
				'}';
	}
}
