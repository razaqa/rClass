package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "parameter", strict = false)
public class Parameter {

    @Attribute(name = "id")
    private String id;

    @ElementList(name = "timerange", inline = true, required = false)
    private List<Timerange> timerangeList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Timerange> getTimerangeList() {
        return timerangeList;
    }

    public void setTimerangeList(List<Timerange> timerangeList) {
        this.timerangeList = timerangeList;
    }
}
