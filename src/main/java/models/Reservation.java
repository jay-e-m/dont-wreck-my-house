package models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private Host host;
    private BigDecimal total;

    public Reservation(String id, LocalDate startDate, LocalDate endDate, Guest guest, Host host, BigDecimal total) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.host = host;
        this.total = total;
    }

    public Reservation() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void calculateTotal() {
        this.total = getTotalCost();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    //Set this to getTotalCost()?

    public BigDecimal getTotalCost() {
        long duration = ChronoUnit.DAYS.between(startDate, endDate);
        long weekdays = duration - getWeekendDays();
        BigDecimal weekdayCost = host.getStandardRate().multiply(BigDecimal.valueOf(weekdays));
        BigDecimal weekendCost = host.getWeekendRate().multiply(BigDecimal.valueOf(getWeekendDays()));
        return weekdayCost.add(weekendCost);
    }

    private long getWeekendDays() {
        long weekendDays = 0;
        LocalDate date = this.startDate;
        while (!date.isAfter(this.endDate)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY) {
                weekendDays++;
            }
            date = date.plusDays(1);
        }
        return weekendDays;
    }

    public boolean isOverlapping(Reservation other) {
        return (this.startDate.isBefore(other.endDate) && this.endDate.isAfter(other.startDate))
                || (other.startDate.isBefore(this.endDate) && other.endDate.isAfter(this.startDate));
    }
    //returns true if dates overlap

    public boolean isInPast() {
        return this.endDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", guest=" + guest +
                ", host=" + host +
                ", total=" + total +
                '}';
    }
}
