package com.example.ofs;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String date;

    @Lob
    private byte[] data;

    private boolean isPublic;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "file_allowed_users", joinColumns = @JoinColumn(name = "file_id"))
    @Column(name = "email")
    private List<String> allowedUsers;

	@Override
	public String toString() {
		return "FileEntity [id=" + id + ", name=" + fileName + ", type=" + fileType + "date=" +date+  ", data="
				+ Arrays.toString(data) + ", isPublic=" + isPublic + ", allowedUsers=" + allowedUsers + "]";
	}

	public Long getId() {
		return id;
	}

	public FileEntity() {
		super();
		this.setDate(LocalDate.now().toString());
		// TODO Auto-generated constructor stub
	}

	public FileEntity( String fileName, String fileType, byte[] data, boolean isPublic,
			List<String> allowedUsers) {
		super();
//		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
		this.isPublic = isPublic;
		this.allowedUsers = allowedUsers;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	
	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public List<String> getAllowedUsers() {
		return allowedUsers;
	}

	public void setAllowedUsers(List<String> allowedUsers) {
		this.allowedUsers = allowedUsers;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

    // getters and setters
}
