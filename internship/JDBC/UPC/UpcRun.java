package gmx.upc;

import gmx.upc.comment.CommentTable;
import gmx.upc.post.PostTable;
import gmx.upc.user.UserTable;

public class UpcRun {
	public static void main(String[] args) {
		UserTable.createTable();
		UserTable.insertValue();
		UserTable.input();
		UserTable.print();
		UserTable.update();
		UserTable.print();
		UserTable.delete();
		UserTable.print();
		
		PostTable.createTable();
		PostTable.insertValue();
		PostTable.input();
		PostTable.print();
		PostTable.update();
		PostTable.print();
		PostTable.delete();
		PostTable.print();
		
		CommentTable.createTable();
		CommentTable.insertValue();
		CommentTable.input();
		CommentTable.print();
		CommentTable.update();
		CommentTable.print();
		CommentTable.delete();
		CommentTable.print();
	}
}
