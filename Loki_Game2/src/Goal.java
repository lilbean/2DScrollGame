import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Goal {
	int x;
	int y; 
	int width= 150; 
	int height = 150; 
	Image image;
	public Goal(int locx, int locy, Image im){
		x=locx- (width/2);
		y=locy - (height/2);
		x+=100;
		image = im;
	}
	public void render(GraphicsContext gc){
		gc.drawImage(image, x-Scroll.vleft, y, width, height);
	}
	
	public int locx(){
		return x;
	}
	public int locy(){
		return y; 
	}
}
