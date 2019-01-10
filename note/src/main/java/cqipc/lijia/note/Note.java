package cqipc.lijia.note;

public class Note {

    private String n_title;
    private String n_time;
    private String n_id;
    private int imgPathCounts;
    private int vedioPathCounts;
    private int recordPathCounts;
    private boolean isCheck;
  //  private String
    public String getN_id() {
        return n_id;
    }

    public void setN_id(String n_id) {
        this.n_id = n_id;
    }

    public String getN_time() {
        return n_time;
    }

    public void setN_time(String n_time) {
        this.n_time = n_time;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public int getRecordPathCounts() {
        return recordPathCounts;
    }

    public void setRecordPathCounts(int recordPathCounts) {
        this.recordPathCounts = recordPathCounts;
    }

    public int getVedioPathCounts() {
        return vedioPathCounts;
    }

    public void setVedioPathCounts(int vedioPathCounts) {
        this.vedioPathCounts = vedioPathCounts;
    }

    public int getImgPathCounts() {
        return imgPathCounts;
    }

    public void setImgPathCounts(int imgPathCounts) {
        this.imgPathCounts = imgPathCounts;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "toString="+getN_title();
    }
}

