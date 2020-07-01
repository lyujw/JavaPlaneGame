package cn.sxt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.xml.crypto.Data;

/**
 * 飞机游戏的主窗口
 * @author StanL
 *
 */
public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	Plane plane = new Plane(planeImg,250,250);
//	Shell shell = new Shell();
	
	Shell[] shells = new Shell[50];
	
	Explode bao;
	Date startTime = new Date();
	Date endTime;
	int period;   //  游戏持续的时间 
	
//	Plane plane2 = new Plane(planeImg,350,250);
//	Plane plane3 = new Plane(planeImg,450,250);
	
	@Override
	public void paint(Graphics g) {   //自动被调用
		super.paint(g);
		Color c = g.getColor();
		g.drawImage(bg, 0, 0, null);
		plane.drawSelf(g);//画飞机
		for (int i = 0; i < shells.length; i++) {
			shells[i].draw(g);
			
			boolean peng=shells[i].getRect().intersects(plane.getRect());
			if (peng) {
//				System.out.println("相撞了");
				plane.live=false;
				if (bao==null) {
					bao = new Explode(plane.x, plane.y);
					
					endTime=new Date();
					period = (int)((endTime.getTime()-startTime.getTime())/1000);
				}
				
				bao.draw(g);
			}
			//计时功能，	给出提示
			if (!plane.live) {
				Font f = new Font("宋体",Font.BOLD,50);
				g.setFont(f);
				g.setColor(Color.RED);
				g.drawString("时间："+period+"秒", (int)plane.x, (int)plane.y);
			}	
				
		}
		
		g.setColor(c);
//		plane2.drawSelf(g);
//		plane3.drawSelf(g);
		
//		g.drawImage(plane, planeX, planeY, null);
//		planeX++;	
	}
	
	//帮助我们反复的重画窗口！
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
//				System.out.println("窗口画一次");
				repaint();  //重画
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//定义键盘监听的内部类
	class keyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			
			super.keyPressed(e);
//			System.out.println("按下"+e.getKeyCode());
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			super.keyReleased(e);
//			System.out.println("抬起"+e.getKeyCode());
			plane.minusDirection(e);
		} 
		
		
		
	}
	
	/**
	 * 初始化窗口
	 */
	public void lauchFrame() {
		setTitle("吕家伟作品");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		this.setLocation(300,300);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();   //启动重画窗口的线程
		addKeyListener(new keyMonitor()); //给窗口增加键盘的监听
		
		//初始化50个炮弹
		for (int i = 0; i < shells.length; i++) {
			shells[i]=new Shell();
		}
		
	}
	
	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.lauchFrame();
	}
	
	private Image offScreenImage = null;
	
	public void update(Graphics g) {
		if (offScreenImage==null) {
			offScreenImage=this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		}
		
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

}
