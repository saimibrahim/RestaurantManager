package Model;

import java.util.ArrayList;
import java.util.HashMap;

class ReservationManager {

    /**
     * A HashMap containing all reservations made. The key is a date and time: "DD-MM-YYY hh-mm".
     * The value is an array of reservations corresponding at that time.
     */
    private static HashMap<String, ArrayList<Reservation>> allReservations = new HashMap<>();

    /**
     * The number of tables in the restaurant.
     */
    private static int numberOfTables = 0;


    /**
     * Sets the numberOfTables this manager should handle. This clears all current reservations.
     *
     * @param numberOfTables The number of tables to have.
     */
    public static void setNumberOfTables(int numberOfTables) {
        if (numberOfTables < 0) {
            ReservationManager.numberOfTables = 0;

        } else {
            ReservationManager.numberOfTables = numberOfTables;
        }

        allReservations = new HashMap<>();
    }

    /**
     * Creates a reservation with the given details if possible. This method does not check for time constraints.
     * It is YOUR responsibility to make sure your table will be available at the given time for new customers.
     *
     * @param name The name to save the reservation under.
     * @param time The time in the correct format, for the reservation: [DD-MM-YYY hh:mm]
     * @return -1 if the reservation could not be made due to no tables available. Otherwise the reserved table number.
     *          0 is a table number.
     */
    public static int makeReservation(String name, String time) {
        ArrayList<Reservation> reservationsAtTime = allReservations.get(time);

        // Check if it is possible to make a reservation
        if (reservationsAtTime.size() < numberOfTables) {
            Reservation newReservation = new Reservation();
            newReservation.reservationOwner = name;
            newReservation.reservationTime = time;

            int tableNumber = getFreeTable(time);
            if (tableNumber == -1) {
                return -1;
            }

            newReservation.reservedTableNumber = tableNumber;

            reservationsAtTime.add(newReservation);
            allReservations.put(time, reservationsAtTime);

            return newReservation.reservedTableNumber;

        } else {
            return -1;
        }
    }

    /**
     * Cancels a reservation for the given table number at the given time.
     *
     * @param time The time in the correct format, for the reservation: [DD-MM-YYY hh:mm]
     * @param tableNumber A valid table number.
     * @return True if the reservation was found and removed. False otherwise.
     */
    public static boolean cancelReservation(String time, int tableNumber) {
        ArrayList<Reservation> reservationsAtTime = allReservations.get(time);

        Reservation reservationToDelete = null;
        for (Reservation reservation: reservationsAtTime) {
            if (reservation.reservedTableNumber == tableNumber) {
                reservationToDelete = reservation;
                break;
            }
        }

        if (reservationToDelete == null) {
            return false;

        } else {
            reservationsAtTime.remove(reservationToDelete);
            allReservations.put(time, reservationsAtTime);

            return true;
        }
    }

    /**
     * Cancels a reservation for the given name.
     *
     * @param name The name of the reservation owner.
     * @return True if the reservation was found and removed. False otherwise.
     */
    public static boolean cancelReservation(String name) {
        for (String time: allReservations.keySet()) {
            ArrayList<Reservation> reservationsAtTime = allReservations.get(time);

            Reservation reservationToDelete = null;
            for (Reservation reservation : reservationsAtTime) {
                if (reservation.reservationOwner.equals(name)) {
                    reservationToDelete = reservation;
                    break;
                }
            }

            if (reservationToDelete != null) {
                reservationsAtTime.remove(reservationToDelete);
                allReservations.put(time, reservationsAtTime);

                return true;
            }
        }

        return false;
    }

    /**
     * Returns a table number which is free at the given time.
     *
     * @param time The time to check for a free table.
     * @return -1 if no table was available. Otherwise the table number to be used in a reservation.
     */
    private static int getFreeTable(String time) {
        ArrayList<Reservation> reservationsAtTime = allReservations.get(time);

        ArrayList<Integer> possibleTableNumbers = new ArrayList<>();
        for (int i=0; i < numberOfTables; i++) {
            possibleTableNumbers.add(i);
        }

        for (Reservation reservation: reservationsAtTime) {
            possibleTableNumbers.remove(reservation.reservedTableNumber);
        }

        if (possibleTableNumbers.size() > 0) {
            return possibleTableNumbers.get(0);

        } else {
            return -1;
        }
    }
}
