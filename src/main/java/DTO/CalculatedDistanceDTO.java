package DTO;

import model.CalculatorMethod;

public class CalculatedDistanceDTO {
    private String from;
    private String to;
    private Double distance;
    private String method;

    public CalculatedDistanceDTO() {}

    public CalculatedDistanceDTO(String from, String to, Double distance, String method) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.method = method;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(CalculatorMethod method) {
        this.method = method.getMethod();
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
