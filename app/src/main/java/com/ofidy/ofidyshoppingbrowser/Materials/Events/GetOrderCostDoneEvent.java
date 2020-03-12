package com.ofidy.ofidyshoppingbrowser.Materials.Events;

import java.util.List;

public class GetOrderCostDoneEvent {

    public final List<String> values;
    public final List<Double> valuesD;
    public final boolean pod;
    public final boolean simplepay;

    public GetOrderCostDoneEvent(List<String> dest, List<Double> valuesD, boolean pod, boolean simplepay) {
        this.values = dest;
        this.valuesD = valuesD;
        this.pod = pod;
        this.simplepay = simplepay;
    }

}
