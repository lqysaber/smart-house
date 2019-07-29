package h.uniview.smarthouse.utils;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	
	public enum PhotoType {
//		BMP、JPG、JPEG、PNG、GIF
		BMP(".bmp"),
		JPG(".jpg"),
		JPEG(".jpeg"),
		PNG(".png"),
		GIF(".gif");

		PhotoType(String value) {
			this.value = value;
		}
		private String value;
		public String getValue() {
			return value;
		}
		
		public static boolean isMember(String photoType) {
			for(PhotoType pt : values()) {
				if(pt.getValue().equals(photoType)) {
					return true;
				}
			}
			return false;
		}
	}

	public enum ConfigEnvType {
		WROKSTATION("/Configuration/WorkstationMsg"),
		CONFIGDATA("/Configuration/ConfigMsg"),
		CAMERA("/Configuration/DevMsg/CameraMsg/CameraInfo"),
		NVR("/Configuration/DevMsg/NVRMSG/NVRInfo"),
		VIDEO("/Configuration/DevMsg/VideoMsg/VideoNodeInfo"),
		SERVERNODE("/Configuration/ServerMsg/ServerNodeInfo");
		ConfigEnvType(String value) {
			this.value = value;
		}
		private String value;
		public String getValue() {
			return value;
		}
	}

	public static final int CLOUD_CTRL_ACTIVE = 1;
	public static final int CLOUD_CTRL_DISABLE = 0;
	public static Map<String, String> deviceStatus = new HashMap<String, String>();

	static {
		deviceStatus.put("1", "能见度工业相机");
		deviceStatus.put("2", "硬盘录像机");
		deviceStatus.put("3", "室内监控球机");
		deviceStatus.put("4", "室内监控枪机");
	}

	public enum DeviceType {
		CAMERA("1"),
		NVR("2"),
		VIDEO_BAL("3"),
		VIDEO_GUN("4");
		DeviceType(String value) {
			this.value = value;
		}
		private String value;
		public String getValue() {
			return value;
		}
	}

}
