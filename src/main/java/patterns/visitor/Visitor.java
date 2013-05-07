package patterns.visitor;

import java.util.Arrays;

/**
 * Visitor Pattern Demo.
 */
public class Visitor {

    public static void main(String[] args){
        Iterable<Room> rooms = Arrays.asList(
                new Room("Guest Room"),
                new Room("Kitchen"),
                new Room("Bathroom"));

        House house = new House(rooms);

        house.accept(new LeaveForWorkVisitor());
    }

    /**
     * The Visitor Interface.
     */
    interface IVisitor {
        void visit(House house);
        void visit(Room room);
    }

    /**
     * An Interface for anything able to accept a Visitor.
     */
    interface Visitable {
        void accept(IVisitor visitor);
    }

    /**
     * A simple Visitor to lock the doors and turn off the lights
     * before heading out to work for the day.
     */
    static class LeaveForWorkVisitor implements IVisitor {
        @Override
        public void visit(House house) {
            house.lockDoors();
        }

        @Override
        public void visit(Room room) {
            room.setLight(false);
        }
    }

    static class House implements Visitable {
        private Iterable<Room> rooms;

        House(Iterable<Room> rooms){
            super();
            this.rooms = rooms;
        }

        @Override
        public void accept(IVisitor visitor) {
            for(Visitable room : rooms){
                room.accept(visitor);
            }
            visitor.visit(this);
        }

        void lockDoors(){
            System.out.println("Locking the front door.");
        }
    }

    static class Room implements Visitable {

        private String name;

        Room(String name){
            super();
            this.name = name;
        }

        @Override
        public void accept(IVisitor visitor) {
            visitor.visit(this);
        }

        void setLight(boolean on){
            System.out.println("Turning Room " + this.name + " light " + ((on) ? "on" : "off") + ".");
        }
    }
}
