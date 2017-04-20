import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Items {
	int x;
	int y;
	int width = 20;
	int height = 40;
	int flag = 0;
	int color; 
	Image image;
	
	public Items(int locx, int locy, Image im) {
		x=locx; 
		y=locy;
		x+=100;
		y+=200;
		image = im;

	}
	public void eraseItem(){
		flag = 1;
	}
	public void render(GraphicsContext gc) {
		if(flag == 0){
			color = 0;
			gc.drawImage(image, x-Scroll.vleft, y, width, height);
		}
		else {
			color = 1;
			gc.setFill(Color.LIGHTBLUE);
			gc.fillRect(x-Scroll.vleft, y, width, height);	
		}		
		}
	
	public int locx()
	{
		return x;
	}
	public int locy()
	{
		return y;
	}
	public int width(){
		return width;
	}
	public int height(){
		return height; 
	}
	public int color(){
		if(color == 0){
			return 0;
		}
		else
			return 1;
	}
}
