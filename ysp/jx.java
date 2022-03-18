package ysp;

public class jx {
	public static void main(String[] args) {
		ck s= new ck(100,900);
		s.s();
	}

}

class ck{  //矩形 长宽
	private int c=0;//长
	private int k=0;//宽
	private int s=0;//面积
	public  ck(int c,int k) {
		if(c>=0&&k>=0) {
			this.c= c;
			this.k= k;
		}
	} 
	public void s(){
		s= c*k;
		System.out.print(s);
	}
}
