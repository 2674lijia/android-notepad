package cqipc.lijia.note;

import java.util.ArrayList;

public class AlbumMessage {


    private int size; //大小
    private ArrayList<String> imgPathList;//所有img的路径
    private String name;//相册的名字 也就是文件夹名字
    private String fristImgPath; //相册展示的图片路径
    private String[] imgPath;
    private String parentFilePath;

    public String getFristImgPath() {
        return fristImgPath;
    }

    public void setFristImgPath(String fristImgPath) {
        this.fristImgPath = fristImgPath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImgPathList() {

        return imgPathList;
    }

    public void setImgPathList(ArrayList<String> imgPathList) {
        this.imgPathList = imgPathList;
    }


    public String[] getImgPath() {
        return imgPath;
    }

    public void setImgPath(String[] imgPath) {
        this.imgPath = imgPath;
    }

    public String getParentFilePath() {
        return parentFilePath;
    }

    public void setParentFilePath(String parentFilePath) {
        this.parentFilePath = parentFilePath;
    }
}
