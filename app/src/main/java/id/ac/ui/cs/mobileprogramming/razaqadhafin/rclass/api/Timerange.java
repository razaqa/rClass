package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "timerange", strict = false)
public class Timerange {

    @Attribute(name = "datetime", required = false)
    private String datetime;

    @Attribute(name = "h", required = false)
    private String h;

    @Path("value")
    @Text(required=false)
    private String value;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
