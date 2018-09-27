package com.city.test2.province;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "a")
@XmlAccessorType(XmlAccessType.FIELD)
public class A implements Serializable {

    @XmlAttribute(name = "href")
    private String href;

    @XmlValue
    private String content;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
