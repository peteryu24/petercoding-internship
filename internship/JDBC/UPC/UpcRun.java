package gmx.upc;

import gmx.upc.comment.CommentTable;
import gmx.upc.post.PostTable;
import gmx.upc.user.UserTable;

public class UpcRun {
	public static void main(String[] args) {

		UserTable ut = new UserTable();
		PostTable pt = new PostTable();
		CommentTable ct = new CommentTable();

		ut.createTable();
		ut.insertValue();
		ut.input();
		ut.print();
		ut.update();
		ut.print();
		ut.delete();
		ut.print();

		pt.createTable();
		pt.insertValue();
		pt.input();
		pt.print();
		pt.update();
		pt.print();
		pt.delete();
		pt.print();

		ct.createTable();
		ct.insertValue();
		ct.input();
		ct.print();
		ct.update();
		ct.print();
		ct.delete();
		ct.print();
	}
}
