package no.nav.kiv.confluence.labs.rest.model;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class JiraSearchResponse {

    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "status")
    private String status;

    public JiraSearchResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public JiraSearchResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}