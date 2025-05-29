package project.reservations;

public enum ReservationStatus {
    PENDING,     // creată, dar neconfirmată (ex: în așteptarea plății)
    ACTIVE,      // confirmată, dar check-in-ul nu e făcut
    CHECKED_IN,  // clientul a scanat codul QR și a sosit
    CANCELLED,   // rezervare anulată
    COMPLETED    // check-out făcut / rezervarea s-a încheiat
}

