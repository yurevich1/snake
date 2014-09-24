package ru.ps.habrsnake;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
public class A extends Activity {
	static final int RESULT_FINISH = 0, RESULT_OK = 1, RESULT_LOCK = 2,RESULT_SELF = 3, RESULT_INC = 4, RESULT_WIN = 5, RATE = 250, cSnake = 0xAACC33AA, cMeat = 0xEEAACC33, cBG = 0xFF000000, cText = 0xFFFFFFFF, width_cells = 10;
	public V gv;
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		gv = new V(this);
		setContentView(gv);}
	protected void onDestroy() {
		super.onDestroy();
		gv.ph.timer.cancel();}
	class V extends View implements OnTouchListener{
		public float diam_cell = 10.f,x,y;
		public P ph;
		public Paint paintSnake = new Paint(), paintMeat = new Paint();
		public V(Activity activity) {
			super(activity);
			setBackgroundColor(cBG);
			paintSnake.setColor(cSnake);
			paintMeat.setColor(cMeat);
			setOnTouchListener(this);
	        diam_cell = ((float) A.this.getWindowManager().getDefaultDisplay().getWidth()) / ((float) width_cells);
	    	ph = new P(width_cells, (int) (A.this.getWindowManager().getDefaultDisplay().getHeight() / diam_cell));}
		public void invalidateWrapper(){
				A.this.runOnUiThread(new Runnable() {public void run() {invalidate();}});}
	    public void onDraw(Canvas canvas) {
			for (int i = 0; i < ph.arSnake.size(); canvas.drawCircle((ph.arSnake.get(i) % width_cells +.5f) * diam_cell, (ph.arSnake.get(i) / width_cells +.5f) * diam_cell, diam_cell*.5f, paintSnake),i++){}
			canvas.drawCircle((ph.posMeat % width_cells +.5f) * diam_cell, (ph.posMeat / width_cells +.5f) * diam_cell, diam_cell*.5f, paintMeat);
			canvas.drawText(ph.scores + "", canvas.getWidth() * .5f ,paintMeat.getTextSize(), paintMeat);}
		public boolean onTouch(View v, MotionEvent event) {
				x = (event.getActionMasked() == MotionEvent.ACTION_DOWN)?event.getX():x;
				y = (event.getActionMasked() == MotionEvent.ACTION_DOWN)?event.getY():y;
	            ph.setDir((event.getActionMasked() == MotionEvent.ACTION_UP)?((Math.abs(event.getX() - x) > Math.abs(event.getY() - y) && (event.getX() - x) > 0)?3:(((Math.abs(event.getX() - x) > Math.abs(event.getY() - y)) && (event.getX() - x) <= 0)?2:((event.getY() - y) > 0?1:0))):ph.GlobDir);
	        return true;}
		class P {
			public int[] arI = new int[]{5,4,3,2};
			public ArrayList<Integer> arSnake = new ArrayList<Integer>();
			public Timer timer;
			public int posMeat, GlobDir = 1,scores;
			public Random r = new Random(System.currentTimeMillis());
			public P(final int w, final int h) {
				timer = new Timer();
				reset(w,h,0);
				timer.scheduleAtFixedRate(new TimerTask() {public void run() {
						final int res = next(GlobDir,posMeat,w,h);
						posMeat = (res==RESULT_INC)?nextPosMeat(w,h,-1,0,0,0):posMeat;
						scores += (res==RESULT_INC)?1:0;
						V.this.invalidateWrapper();
						if (res == RESULT_FINISH || res == RESULT_SELF || res == RESULT_WIN) reset(w,h,0);
					}}, 0, RATE);}
			public void reset(int width, int height,int i){
				for(GlobDir = 1,scores = 0,arSnake.clear(); i < arI.length;arSnake.add(arI[i]),i++){}
				posMeat = nextPosMeat(width, height,-1,0,0,0);}
			public void setDir(int dir){
				GlobDir = ((dir + GlobDir == 1) || (dir + GlobDir == 5))?GlobDir:dir;}
			public int nextPosMeat(int width, int height,int resI,int i,int count,int exit) {
		   		for (int r0 = r.nextInt(width*height - arSnake.size()) + 1; i < height*width && exit == 0;count+=(arSnake.contains(Integer.valueOf(i))?0:1),exit = ((count == r0)?1:0),resI = (exit==1?i:resI),i+=(exit == 1)?0:1){}
				return resI;}
			public int next(int d, int p, int width, int height){//0-up,1-down,2-left,3-right
			if (arSnake.size() >= width*height - 1) return RESULT_WIN;
			int y = arSnake.get(0) / width, x = arSnake.get(0) % width;
			if ((d == 0 && --y < 0) || (d == 1 && ++y >= height) || (d == 2 && --x < 0) || (d == 3 && ++x >= width) ) return RESULT_FINISH;
			if (arSnake.contains(Integer.valueOf(x + y * width))) return RESULT_SELF;
			arSnake.add(0,x + y * width);
			if (x + y * width == p) return RESULT_INC;
			arSnake.remove(arSnake.size() - 1);
			return RESULT_OK;}}}}