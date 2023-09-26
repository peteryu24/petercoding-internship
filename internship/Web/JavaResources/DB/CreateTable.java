package gmx.upc;


import gmx.upc.comment.CommentTable;
import gmx.upc.post.PostTable;
import gmx.upc.user.UserTable;

public class CreateTable {
	public static void main(String[] args) {

		UserTable ut = new UserTable();
		PostTable pt = new PostTable();
		CommentTable ct = new CommentTable();

		ut.createTable();
		
		

		pt.createTable();
		

		ct.createTable();
		
	}
}
