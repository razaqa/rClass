package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "area", strict = false)
public class Area {

    @Attribute(name = "id")
    private String id;

    @Attribute(name = "description")
    private String description;

    @Attribute(name = "domain")
    private String domain;

    @ElementList(name = "parameter", inline = true, required = false)
    private List<Parameter> parameterList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
