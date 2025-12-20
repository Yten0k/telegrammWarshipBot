package tutorial;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawField {
    private final int size = 900;
    private final String path =  System.getProperty("user.dir") + "\\src";//"C:\\Users\\Yten0chek\\IdeaProjects\\warship\\src";
    private String fileName;
    private final File PathFile = new File(path);

    private BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D g2d = img.createGraphics();

    private final int sizePoint = 100;

    private String[] LetterNaumber = {"A","B","C","D","E","F","G","H"};

    private void BuildName(PlayerMutex mutex){
        fileName = Integer.toString(mutex.GetID()) + "_field.png";
    }

    public void DrawPlayerField(PlayerMutex mutex, boolean[][] field){
        BuildName(mutex);
        g2d.setFont(new Font("TimesRoman",2,100));
        for(int y = 0; y<9;y++){
            for(int x = 0; x<9;x++){
                g2d.setColor(Color.white);
                g2d.fillRect(y*sizePoint,x*sizePoint, sizePoint, sizePoint);
                if(x == 0 && y!=0){
                    g2d.setColor(Color.black);
                    g2d.drawRect(y*sizePoint, 0, sizePoint,sizePoint);
                    g2d.drawString(LetterNaumber[y-1],y*sizePoint+15,85);
                }
                if(y == 0 && x!=0){
                    g2d.setColor(Color.black);
                    g2d.drawRect(0, x*sizePoint, sizePoint,sizePoint);
                    g2d.drawString(Integer.toString(x),15,x*sizePoint+85);

                }
            }
        }
        for(int y = 1; y<9;y++){
            for(int x = 1; x<9;x++){
                if(field[x-1][y-1]){
                    g2d.setColor(Color.black);
                    g2d.fillRect(y*sizePoint,x*sizePoint, sizePoint, sizePoint);
                }

                g2d.setColor(Color.black);
                g2d.drawRect(y*sizePoint, x*sizePoint, sizePoint,sizePoint);
            }
        }
        DrawToFile();
    }

    public void DrawPlayerChangedField(PlayerMutex mutex, int[] coordinate, boolean Final){
        BuildName(mutex);

        try {
            img = ImageIO.read(new File(path + "\\" + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        g2d = img.createGraphics();

        int coordinateX = coordinate[0]+1;
        int coordinateY = coordinate[1]+1;

        Color color = Final ? Color.red : Color.gray;
        g2d.setColor(color);
        g2d.fillRect(coordinateY*sizePoint, coordinateX*sizePoint, sizePoint, sizePoint);

        g2d.setColor(Color.black);
        g2d.drawRect(coordinateY*sizePoint, coordinateX*sizePoint, sizePoint,sizePoint);
        DrawToFile();
    }

    private void DrawToFile(){
        System.out.println("Field Drowned");
        try {
            ImageIO.write(img, "PNG", new File(PathFile, fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
