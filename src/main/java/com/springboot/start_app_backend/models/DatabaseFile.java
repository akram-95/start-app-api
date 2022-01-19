package com.springboot.start_app_backend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;



@Entity
@Table(name = "files")
public class DatabaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fileName;
    private String fileType;
    private String downloadFileUrl;
    @Lob
    private byte[] data;

    public DatabaseFile() {

    }

    public DatabaseFile(String fileName, String fileType, byte[] data,String downloadFileUrl) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.downloadFileUrl = downloadFileUrl;
    }

    public String getDownloadFileUrl() {
		return downloadFileUrl;
	}

	public void setDownloadFileUrl(String downloadFileUrl) {
		this.downloadFileUrl = downloadFileUrl;
	}

	public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
