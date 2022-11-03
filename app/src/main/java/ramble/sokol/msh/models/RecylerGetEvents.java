package ramble.sokol.msh.models;

public class RecylerGetEvents {

    private String area, description, theme, date, time;

    public RecylerGetEvents(String area, String description, String theme, String date, String time) {
        this.area = area;
        this.description = description;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
