package Model;

/**
 * The Reservation object represents one reservation. It is created, modified and managed solely by ReservationManager.
 */
class Reservation {

    /**
     * The time the reservation was made at in the following format: [DD-MM-YYYY hh:mm] brackets not included.
     * All values are positive (or 0) integers.
     */
    public String reservationTime;


    /**
     * The name the reservation is under. eg: "Sam Smith"
     */
    public String reservationOwner;

    /**
     * The table the restaurant has reserved for this reservation.
     */
    public int reservedTableNumber;
}
