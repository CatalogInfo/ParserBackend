package com.example.backend_parser.models;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class BidsAsks {
    ArrayList<Order> bids = new ArrayList<>();
    ArrayList<Order> asks = new ArrayList<>();

    public void addOrderToBids(Order order) {
        bids.add(order);
    }

    public void addOrderToAsks(Order order) {
        asks.add(order);
    }
}
