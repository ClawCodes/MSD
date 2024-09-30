import java.awt.*;

public class Cat implements Animal{
    int foodLevel = 0;
    Point location_ = new Point(0,0);
    @Override
    public void eat(){
        foodLevel++;
    }
    @Override
    public boolean isHungry(){
        return foodLevel < 3;
    }
    @Override
    public void makeNoise(){
        System.out.println("meow meow");
    }
    @Override
    public void move(int x, int y){
        location_.x = x;
        location_.y = y;
    }
}
