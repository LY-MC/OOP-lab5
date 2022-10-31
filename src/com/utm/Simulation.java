package com.utm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import com.utm.clients.Adult;
import com.utm.clients.Child;
import com.utm.enums.AnimalType;
import com.utm.miscellaneous.Cage;
import com.utm.miscellaneous.CageWithLake;
import com.utm.zooworkers.Cashier;
import com.utm.zooworkers.SecurityGuard;
import com.utm.zooworkers.Veterinarian;
import com.utm.zooworkers.Zookeeper;

public class Simulation {
    // Create all instances
    Cage monkeyCage = new Cage(1, 300, 300);
    Cage elephantCage = new Cage(2, 500, 1000);
    Cage horseCage = new Cage(3, 850, 900);
    Cage lionCage = new Cage(4, 600, 940);
    CageWithLake duckCage = new CageWithLake(5, 400, 400, 200, 150);
    Adult adult = new Adult();
    Child child = new Child();
    Cashier cashier = new Cashier(400800200, "Vicki", "Richardson", 1500, 5);
    Veterinarian veterinarian = new Veterinarian(983467829, "Robyn", "Robson", 2500, 3);
    Zookeeper zookeeper = new Zookeeper(476389546, "Stefan", "Williamson", 1700, 4);
    SecurityGuard securityGuard = new SecurityGuard(285763839, "Andy", "Berg", 1000, 1);
    Random rand = new Random();

    void printInCages() throws InterruptedException, IOException {
        Properties prop = new Properties();
        FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/resources/config.properties");
        prop.load(ip);
        monkeyCage.populateCage(Integer.parseInt(prop.getProperty("numberOfMonkeys")), AnimalType.MONKEY);
        elephantCage.populateCage(Integer.parseInt(prop.getProperty("numberOfElephants")), AnimalType.ELEPHANT);
        horseCage.populateCage(Integer.parseInt(prop.getProperty("numberOfHorses")), AnimalType.HORSE);
        lionCage.populateCage(Integer.parseInt(prop.getProperty("numberOfLions")), AnimalType.LION);
        duckCage.populateCage(Integer.parseInt(prop.getProperty("numberOfDucks")), AnimalType.DUCK);
        System.out.println("In the cages we have:");
        elephantCage.printAnimalsInCage();
        horseCage.printAnimalsInCage();
        lionCage.printAnimalsInCage();
        monkeyCage.printAnimalsInCage();
        duckCage.printAnimalsInCage();
        Thread.sleep(5000);
        System.out.println();
    }

    void simulate(int timeStamp) throws InterruptedException, IOException {
        Properties prop = new Properties();
        FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/resources/config.properties");
        prop.load(ip);

        System.out.println("Time " + timeStamp + ":00");

        if (timeStamp == Integer.parseInt(prop.getProperty("timeForDiscount"))) {
            System.out.println("DISCOUNT FOR TICKETS!");
        }

        int appear = rand.nextInt(100);
        boolean wantsToEnter = appear % 2 == 0;
        boolean canEnter = appear % 20 != 0;
        int age = rand.nextInt(80);
        if (wantsToEnter) {
            if (age < Integer.parseInt(prop.getProperty("ageOfMajority"))) {
                child.setAge(age);
                child.buyTicket();
                child.setName(child.getClientName());
                child.setSurname(child.getClientSurname());
                child.setID(child.getRandomID());
            } else {
                adult.setAge(age);
                adult.buyTicket();
                adult.setName(adult.getClientName());
                adult.setSurname(adult.getClientSurname());
                adult.setID(adult.getRandomID());
            }

            if (timeStamp < Integer.parseInt(prop.getProperty("timeForDiscount"))) {
                cashier.setTicketPrice(Integer.parseInt(prop.getProperty("ticketPrice")));
            } else {
                cashier.setTicketPrice(Integer.parseInt(prop.getProperty("ticketPriceWithDiscount")));
            }
            cashier.sellTicket(canEnter, age);
            if (age < Integer.parseInt(prop.getProperty("ageOfMajority"))) {
                child.enterZoo(canEnter);
                if (appear % 18 == 0) {
                    System.out.println("Cashier was rude");
                    child.setHappiness(75);
                } else {
                    child.setHappiness(100);
                }
            } else {
                adult.enterZoo(canEnter);
                if (appear % 18 == 0) {
                    System.out.println("Cashier was rude");
                    adult.setHappiness(75);
                } else {
                    adult.setHappiness(100);
                }
            }

            if (canEnter) {
                System.out.println("Animals:");
                elephantCage.getAnimalList().get(0).makeSound();
                for (Cage cage : Arrays.asList(horseCage, lionCage, monkeyCage)) {
                    cage.getAnimalList().get(0).makeSound();
                }
                duckCage.getAnimalList().get(0).makeSound();
            }
        }

        int sleeping = rand.nextInt(100);
        securityGuard.setSleeping(sleeping % 2 == 0);
        if (securityGuard.isSleeping()) {
            securityGuard.setHoursSlept(securityGuard.getHoursSlept() + 1);
        }
        if (securityGuard.getHoursSlept() > 2) {
            securityGuard.setSleeping(false);
            securityGuard.setHoursSlept(0);
        }
        if (securityGuard.isSleeping() && !canEnter) {
            System.out.println("Security guard is sleeping");
            System.out.println("Client sneaks in the zoo");
            System.out.println("Animals:");
            elephantCage.getAnimalList().get(0).makeSound();
            for (Cage cage : Arrays.asList(horseCage, lionCage, monkeyCage)) {
                cage.getAnimalList().get(0).makeSound();
            }
            duckCage.getAnimalList().get(0).makeSound();
        } else if (securityGuard.isSleeping()) {
            System.out.println("Security guard is sleeping");
        } else {
            System.out.println("Security guard is not sleeping");
        }

        int counterHungryDucks = duckCage.countHungryAnimals(0);
        int counterHungryMonkeys = monkeyCage.countHungryAnimals(0);
        int counterHungryElephants = elephantCage.countHungryAnimals(0);
        int counterHungryHorses = horseCage.countHungryAnimals(0);
        int counterHungryLions = lionCage.countHungryAnimals(0);
        zookeeper.setFeeding(true);
        int wrongFood = rand.nextInt(100);
        if (timeStamp == 8 || timeStamp == 15 || timeStamp == 20) {
            System.out.println("Zookeeper is cleaning cages");
            zookeeper.setCleaning(true);
        } else {
            zookeeper.setCleaning(false);
            if (timeStamp == 9 || timeStamp == 14) {
                duckCage.feedingAnimals(zookeeper.getSpeed(), wrongFood);
            } else if (timeStamp == 10 || timeStamp == 16) {
                monkeyCage.feedingAnimals(zookeeper.getSpeed(), wrongFood);
            } else if (timeStamp == 11 || timeStamp == 17) {
                elephantCage.feedingAnimals(zookeeper.getSpeed(), wrongFood);
            } else if (timeStamp == 12 || timeStamp == 18) {
                horseCage.feedingAnimals(zookeeper.getSpeed(), wrongFood);
            } else if (timeStamp == 13 || timeStamp == 19) {
                lionCage.feedingAnimals(zookeeper.getSpeed(), wrongFood);
            }
            System.out.println("Zookeeper needs to feed " + counterHungryDucks + " ducks, " +
                    counterHungryMonkeys + " monkeys, " + counterHungryElephants + " elephants, " +
                    counterHungryHorses + " horses, " + counterHungryLions + " lions");
        }

        elephantCage.becomeHungry();
        horseCage.becomeHungry();
        lionCage.becomeHungry();
        monkeyCage.becomeHungry();
        duckCage.becomeHungry();

        int counterIllAnimals = duckCage.countIllAnimals(0) + lionCage.countIllAnimals(0) +
                monkeyCage.countIllAnimals(0) + elephantCage.countIllAnimals(0) + horseCage.countIllAnimals(0);
        if (wantsToEnter && age < Integer.parseInt(prop.getProperty("ageOfMajority")) && canEnter) {
            child.setHappiness(child.getHappiness() - Integer.parseInt(prop.getProperty("childHappinessCoefficient")) * counterIllAnimals);
        } else if (wantsToEnter && canEnter) {
            adult.setHappiness(adult.getHappiness() - Integer.parseInt(prop.getProperty("adultHappinessCoefficient")) * counterIllAnimals);
        }

        if (counterIllAnimals != 0) {
            veterinarian.setTreating(true);
            if (wrongFood % 10 == 0 && !zookeeper.isCleaning()) {
                System.out.println("Veterinarian is treating animals because of wrong food.");
                if (timeStamp == 9 || timeStamp == 14) {
                    duckCage.treatingAnimals();
                } else if (timeStamp == 10 || timeStamp == 16) {
                    monkeyCage.treatingAnimals();
                } else if (timeStamp == 11 || timeStamp == 17) {
                    elephantCage.treatingAnimals();
                } else if (timeStamp == 12 || timeStamp == 18) {
                    horseCage.treatingAnimals();
                } else if (timeStamp == 13 || timeStamp == 19) {
                    lionCage.treatingAnimals();
                }
            } else {
                int animalType = rand.nextInt(5) + 1;
                switch (animalType) {
                    case 1:
                        duckCage.treatingAnimal();
                        System.out.println("Veterinarian is treating a duck");
                        break;
                    case 2:
                        monkeyCage.treatingAnimal();
                        System.out.println("Veterinarian is treating a monkey");
                        break;
                    case 3:
                        elephantCage.treatingAnimal();
                        System.out.println("Veterinarian is treating an elephant");
                        break;
                    case 4:
                        horseCage.treatingAnimal();
                        System.out.println("Veterinarian is treating a horse");
                        break;
                    case 5:
                        lionCage.treatingAnimal();
                        System.out.println("Veterinarian is treating a lion");
                        break;
                }
            }

        }

        int noElephant = rand.nextInt(Integer.parseInt(prop.getProperty("numberOfElephants")));
        int noHorse = rand.nextInt(Integer.parseInt(prop.getProperty("numberOfHorses")));
        int noLion = rand.nextInt(Integer.parseInt(prop.getProperty("numberOfLions")));
        int noMonkey = rand.nextInt(Integer.parseInt(prop.getProperty("numberOfMonkeys")));
        int noDuck = rand.nextInt(Integer.parseInt(prop.getProperty("numberOfDucks")));

        if (wantsToEnter && age < Integer.parseInt(prop.getProperty("ageOfMajority")) && canEnter) {
            child.wantsRideHorse(age);
            if (age >= Integer.parseInt(prop.getProperty("acceptableAge"))) {
                if (horseCage.getAnimalList().get(noHorse).rideAnimal() == 1) {
                    child.setHappiness(child.getHappiness() + Integer.parseInt(prop.getProperty("childHappinessCoefficient")));
                }
            }
        }

        if (elephantCage.getAnimalList().get(noElephant).getAge() > Integer.parseInt(prop.getProperty("elephantOldAge"))) {
            elephantCage.getAnimalList().get(noElephant).setIll(true);
        }
        if (horseCage.getAnimalList().get(noHorse).getAge() > Integer.parseInt(prop.getProperty("horseOldAge"))) {
            horseCage.getAnimalList().get(noHorse).setIll(true);
        }
        if (lionCage.getAnimalList().get(noLion).getAge() > Integer.parseInt(prop.getProperty("lionOldAge"))) {
            lionCage.getAnimalList().get(noLion).setIll(true);
        }
        if (monkeyCage.getAnimalList().get(noMonkey).getAge() > Integer.parseInt(prop.getProperty("monkeyOldAge"))) {
            monkeyCage.getAnimalList().get(noMonkey).setIll(true);
        }
        if (duckCage.getAnimalList().get(noDuck).getAge() > Integer.parseInt(prop.getProperty("duckOldAge"))) {
            duckCage.getAnimalList().get(noDuck).setIll(true);
        }

        if (wantsToEnter && canEnter && adult.getHappiness() >= Integer.parseInt(prop.getProperty("tipHappinessRate"))) {
            int tips = rand.nextInt(Integer.parseInt(prop.getProperty("maxTipValue"))) + 1;
            cashier.setTips(tips);
            System.out.println("Cashier gets " + tips + " dollars tips because client was happy");
        } else if ((wantsToEnter && canEnter && adult.getHappiness() < Integer.parseInt(prop.getProperty("returnAdultHappinessRate"))) ||
                (wantsToEnter && canEnter && child.getHappiness() < Integer.parseInt(prop.getProperty("returnChildHappinessRate")))) {
            System.out.println("Client gets " + cashier.getTicketPrice() / 2 + " dollars back because he was unhappy");
        }

        System.out.println();
        Thread.sleep(3000);
    }
}
