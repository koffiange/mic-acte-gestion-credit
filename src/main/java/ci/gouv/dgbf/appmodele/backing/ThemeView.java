package ci.gouv.dgbf.appmodele.backing;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("themeView")
@SessionScoped
public class ThemeView implements Serializable {
    
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public void change(String color) {
        if(color.equals("green"))
            this.color = null;
        else
            this.color = "-" + color;
    }
    
    
}
