package com.utm;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        Simulation simulation = new Simulation();
        int timeStamp = 8;
        simulation.printInCages();

        while (true) {
            simulation.simulate(timeStamp);
            timeStamp++;
            if (timeStamp >= 21) {
                timeStamp = 8;
            }
        }
    }
}
