package Sprites;

import Game.ImageUtil;

import java.awt.*;
import java.util.Hashtable;

public class Sprite {

    protected int x;
    protected int y;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    private String imagePath = "";
    private boolean visible_sprite;
    private Image image;
    protected Hashtable imageBuffer = new Hashtable<>();

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible_sprite = true;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void loadImage(String src) {
        if (imagePath == src) {
            return;
        } else if (imageBuffer.containsKey(src)) {
            image = (Image) imageBuffer.get(src);
        }
        else {
            image = ImageUtil.loadImage(src);
            imageBuffer.put(src, image);
        }
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        setImagePath(src);
    }

    public boolean isVisible() {
        return visible_sprite;
    }

    public void setVisible(Boolean visible) {
        visible_sprite = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
