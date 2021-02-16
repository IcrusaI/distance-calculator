package model;

public enum CalculatorMethod {
    CROWFLIGHT("crowflight"),
    DISTANCE_MATRIX("distance_matrix"),
    ALL("all");

    private String method;

    CalculatorMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static CalculatorMethod getByType(String type) {
        for (CalculatorMethod method : values()) {
            if (method.getMethod().equals(type)) {
                return method;
            }
        }

        throw new IllegalArgumentException("No enum found with type: [" + type + "]");
    }
}
