package gmx.upc.file; // 패키지 경로는 실제 프로젝트에 따라 수정해야 합니다.



public class FileVo {
	private int fileId; // 파일의 고유 ID
	private int postId; // 연관된 게시글의 ID
	private String fileName; // 파일의 원래 이름
	private String filePath; // 파일의 서버 상 저장 경로
	private byte[] fileData; // 파일의 바이너리 데이터

	// Getter와 Setter 메서드
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }



}
