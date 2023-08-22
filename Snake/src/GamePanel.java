import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener{
    static final int screenWidth = 1000;
    static final int screenHeight = 1000;
    static final int unitSize = 50;
    static final int unitsTotal = (screenWidth*screenHeight)/unitSize;
    final int x[] = new int[unitsTotal];
    final int y[] = new int[unitsTotal];
    int score = 0;
    int highScore = -1;
    int bodyCount = 1;
    int pointGX;
    int pointGY;
    int pointSX;
    int pointSY;
    Direction direction = Direction.down;
    boolean running = false;
    boolean firstTime = true;
    Timer timer;
    Random random;
    boolean golden;
    boolean firstTime10=true;
    boolean firstTime20=true;
    boolean firstTime30=true;
    boolean firstTime40=true;
    ImageIcon background = new ImageIcon(getClass().getResource("grass.jpg"));
    ImageIcon growthPoint = new ImageIcon(getClass().getResource("mouse.png"));
    ImageIcon growthPointGolden = new ImageIcon(getClass().getResource("goldenMouse.png"));
    ImageIcon shrinkPoint = new ImageIcon(getClass().getResource("bottle.png"));
    ImageIcon bodyFragment = new ImageIcon(getClass().getResource("bodyFragment.png"));
    ImageIcon headUp = new ImageIcon(getClass().getResource("headUp.png"));
    ImageIcon headDown = new ImageIcon(getClass().getResource("headDown.png"));
    ImageIcon headLeft = new ImageIcon(getClass().getResource("headLeft.png"));
    ImageIcon headRight = new ImageIcon(getClass().getResource("headRight.png"));
    ImageIcon snake = new ImageIcon(getClass().getResource("snakeIcon.png"));
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start();
    }
    public void start(){
        if(highScore==-1){
            readHighScore();
        }
        if(firstTime==false){
            firstTime10=true;
            firstTime20=true;
            firstTime30=true;
            firstTime40=true;
            makeGrowthPoint();
            makeShrinkPoint();
            running = true;
            timer= new Timer(150,this::actionPerformed);
            timer.start();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            move();
            grow();
            shrink();
            collisions();
            repaint();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(firstTime==true&& running==false){
            Menu(g);
        }
        else if(firstTime==false&&running==true) {
            gameGoing(g);
        }
        else if(firstTime==false&&running==false){
            gameOver(g);
        }
    }
    public void Menu(Graphics g){
        background.paintIcon(this,g,0,0);
        snake.paintIcon(this,g,(screenWidth-512)/2,(screenHeight-512)/2);
        g.setFont(new Font("Arial",Font.PLAIN,100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Snake - The Game",(screenWidth-metrics.stringWidth("Snake - The Game"))/2,100);
        g.setFont(new Font("Arial",Font.PLAIN,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press ENTER to play",(screenWidth-metrics2.stringWidth("Press ENTER to play"))/2,800);
        g.setFont(new Font("Arial",Font.PLAIN,20));
        g.drawString("By Kacper Tonderys",800,970);
    }
    public void gameGoing(Graphics g){
        background.paintIcon(this,g,0,0);
        if(golden==true){
            growthPointGolden.paintIcon(this,g,pointGX,pointGY);
        }
        else {
            growthPoint.paintIcon(this, g, pointGX, pointGY);
        }
        if(score>0) {
            shrinkPoint.paintIcon(this, g, pointSX, pointSY);
        }
        for (int i = 0; i < bodyCount; i++) {
            if (i == 0) {
                switch(direction){
                    case up:{
                        headUp.paintIcon(this,g,x[i],y[i]);
                        break;
                    }
                    case down:{
                        headDown.paintIcon(this,g,x[i],y[i]);
                        break;
                    }
                    case left:{
                        headLeft.paintIcon(this,g,x[i],y[i]);
                        break;
                    }
                    case right:{
                        headRight.paintIcon(this,g,x[i],y[i]);
                        break;
                    }
                }

            } else {
                bodyFragment.paintIcon(this,g,x[i],y[i]);
            }
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,40));
        g.drawString("Score: "+score, 60, 40);
        g.drawString("High Score: "+highScore, 700, 40);
        g.setFont(new Font("Arial",Font.PLAIN,100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        if(score>10){
            firstTime10=false;
        }
        if(score>20){
            firstTime20=false;
        }
        if(score>30){
            firstTime30=false;
        }
        if(score>40){
            firstTime40=false;
        }
        if(score==10&&firstTime10==true){
            timer.setDelay(125);
            g.drawString("Speed Up!", (screenWidth-metrics.stringWidth("Speed Up!"))/2, (screenHeight+100)/2);
        }
        if(score==20&&firstTime20==true){
            timer.setDelay(100);
            g.drawString("Speed Up!", (screenWidth-metrics.stringWidth("Speed Up!"))/2, (screenHeight+100)/2);
        }
        if(score==30&&firstTime30==true){
            timer.setDelay(75);
            g.drawString("Speed Up!", (screenWidth-metrics.stringWidth("Speed Up!"))/2, (screenHeight+100)/2);
        }
        if(score==40&&firstTime40==true){
            timer.setDelay(50);
            g.drawString("Speed Up!", (screenWidth-metrics.stringWidth("Speed Up!"))/2, (screenHeight+100)/2);
        }
    }
    public void gameOver(Graphics g){
        background.paintIcon(this,g,0,0);
        snake.paintIcon(this,g,(screenWidth-512)/2,(screenHeight-512)/2);
        g.setFont(new Font("Arial",Font.PLAIN,100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth-metrics.stringWidth("Game Over"))/2, 100);
        g.drawString("Your score: "+score, (screenWidth-metrics.stringWidth("Your score: "+score))/2, 200);
        g.setFont(new Font("Arial",Font.PLAIN,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press ENTER to play again",(screenWidth-metrics2.stringWidth("Press ENTER to play again"))/2,900);
        if(highScore<score){
            highScore = score;
            saveHighScore();
            g.setFont(new Font("Arial",Font.PLAIN,50));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("New High Score!",(screenWidth-metrics3.stringWidth("New High Score!"))/2,300);
        }
    }
    public void move(){
        for(int i=bodyCount;i>0;i--){
            x[i]= x[i-1];
            y[i]= y[i-1];
        }

        switch(direction){
            case up:{
                y[0] = y[0] - unitSize;
                break;
            }
            case down:{
                y[0] = y[0] + unitSize;
                break;
            }
            case left:{
                x[0] = x[0] - unitSize;
                break;
            }
            case right:{
                x[0] = x[0] + unitSize;
                break;
            }
        }

    }
    public void grow(){
        if((x[0]==pointGX)&&(y[0]==pointGY)&&(golden==true)){
            bodyCount=bodyCount+2;
            score=score+2;
            makeGrowthPoint();
        }
        if((x[0]==pointGX)&&(y[0]==pointGY)){
            bodyCount++;
            score++;
            makeGrowthPoint();
        }
    }
    public void shrink(){
        if((x[0]==pointSX)&&(y[0]==pointSY)&&(score>0)){
            bodyCount--;
            score--;
            makeShrinkPoint();
        }
    }
    public void collisions(){
        for(int i=1;i<=bodyCount;i++)
            if((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }

        if(x[0]<0||x[0]>screenWidth||y[0]<0||y[0]>screenHeight){
            running=false;

        }
        if(running==false){
            timer.stop();
        }
    }
    public void makeGrowthPoint(){
        if(random.nextInt(10)==1){
            golden=true;
        }
        else{
            golden=false;
        }
        pointGX=random.nextInt((int)screenWidth/unitSize)*unitSize;
        pointGY=random.nextInt((int)screenHeight/unitSize)*unitSize;
        if((pointGX==pointSX)&&(pointGY==pointSY)){
            makeGrowthPoint();
        }
        for(int i=0;i<=bodyCount;i++)
            if((x[i]==pointGX)&&(y[i]==pointGY)){
                makeGrowthPoint();
            }
    }
    public void makeShrinkPoint(){
        pointSX=random.nextInt((int)screenWidth/unitSize)*unitSize;
        pointSY=random.nextInt((int)screenHeight/unitSize)*unitSize;
        if((pointGX==pointSX)&&(pointGY==pointSY)){
            makeShrinkPoint();
        }
        for(int i=0;i<=bodyCount;i++)
            if((x[i]==pointSX)&&(y[i]==pointSY)){
                makeGrowthPoint();
            }
    }



    public void readHighScore(){
        File file = new File("HighScore.txt");
        try{
            Scanner read = new Scanner(file);
            int score  = read.nextInt();
            read.close();
            highScore=score;
        }
        catch(FileNotFoundException e){
            highScore = 0;
        }
    }
    public void saveHighScore(){
        try{
            PrintWriter write = new PrintWriter("HighScore.txt");
            write.print(highScore);
            write.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP, KeyEvent.VK_W:
                    if(direction!=Direction.down){
                        direction=Direction.up;
                    }
                    break;

                case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                    if(direction!=Direction.up){
                        direction=Direction.down;
                    }
                    break;

                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    if(direction!=Direction.right){
                        direction=Direction.left;
                    }
                    break;

                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    if(direction!=Direction.left){
                        direction=Direction.right;
                    }
                    break;

                case KeyEvent.VK_ENTER:
                    firstTime=false;
                    if(running==false&&firstTime==false){
                        bodyCount=1;
                        score=0;
                        direction=direction.down;
                        for(int i=0;i<unitsTotal;i++){
                            x[i]=0;
                            y[i]=0;
                        }
                        start();
                    }
                    if(running==false&&firstTime==true){
                        start();
                    }
                    break;
            }
        }
    }
}
