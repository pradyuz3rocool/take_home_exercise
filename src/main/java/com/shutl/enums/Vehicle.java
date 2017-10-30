package com.shutl.enums;

public enum Vehicle {

    bicycle(0.1),
    motorbike(0.15),
    parcel_car(0.2),
    small_van(0.3),
    large_van(0.4);

    private Double markupRatio;

    Vehicle(Double markupRatio) {
        this.markupRatio = markupRatio;
    }

    public Double getMarkupRatio() {
        return markupRatio;
    }

    public Long getMarkupAmount(Long preMarkupPrice) {
        Double retVal = (preMarkupPrice.doubleValue() * (this.getMarkupRatio() + 1));
        return retVal.longValue();
    }
}
