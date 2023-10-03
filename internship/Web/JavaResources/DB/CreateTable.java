package gmx.upc;


import gmx.upc.comment.CommentTable;
import gmx.upc.file.FileTable;
import gmx.upc.post.PostTable;
import gmx.upc.user.UserTable;

public class CreateTable {
	public static void main(String[] args) {

		UserTable ut = new UserTable();
		PostTable pt = new PostTable();
		CommentTable ct = new CommentTable();
		FileTable ft = new FileTable();

		ut.createTable();
		
		

		pt.createTable();
		

		ct.createTable();
		
		ft.createTable();
		
	}
}
