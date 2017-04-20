import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class ScoreBoard {
	Font font;
	public static double count = 30.0; //time
	int flag = 0;
	
	public ScoreBoard(double acount){
		count = acount; 
	}
	
	public void render(GraphicsContext gc){
		font = Font.font("Calibri", FontWeight.BOLD, 24);
		gc.setFill(Color.BLACK);
		gc.setFont(font);
		gc.fillText("Time: "+count, Scroll.VWIDTH -6*Scroll.EDGE, 2*Scroll.EDGE);
		gc.fillText("Points: " + Scroll.points, Scroll.VWIDTH-6*Scroll.EDGE, Scroll.EDGE);
	}
	public void decreaseTime(){
		count= count - 0.05; 
	}
	public boolean checkTime(){
		if(flag == 0){
		if(Scroll.currlevel ==1){
			count = 30.0;
			flag = 1;
		}
		else if(Scroll.currlevel ==2){
			count = 25.0;
			flag = 1;
		}
		else if (Scroll.currlevel == 3){
			count = 20.0;
			flag = 1;
		}}

		if(count< 0.05 ){
			count = 0.0;
			return true;
		}
		else {
			decreaseTime();
			return false;
	}
	}
}

