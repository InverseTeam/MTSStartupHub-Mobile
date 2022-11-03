package ramble.sokol.msh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostEvents {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("goals")
    @Expose
    private String goals;
    @SerializedName("equipment")
    @Expose
    private String equipment;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
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

    public String getAdd_information() {
        return add_information;
    }

    public void setAdd_information(String add_information) {
        this.add_information = add_information;
    }

    @SerializedName("add_information")
    @Expose
    private String add_information;

}
