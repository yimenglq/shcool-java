package ysp;

public class zy {
	public static void main(String[] args) {
		mk l = new mk();
		l.po(100);
		System.out.print(l.op());
		
	}
	
}

//另一个类
class mk{
	private int s=0;
	
	//给private的s赋值方法
	public void po(int p) {
		s=p;
	}
	
	//获取s值的方法
	public int op() {
		return s;
		
	}
}

