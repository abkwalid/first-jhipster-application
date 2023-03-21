package tech.digiwise.japp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DailyTaskLog.
 */
@Entity
@Table(name = "daily_task_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DailyTaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The daily task log details.
     */
    @Schema(description = "The daily task log details.", required = true)
    @NotNull
    @Size(min = 3)
    @Column(name = "employee", nullable = false)
    private String employee;

    @NotNull
    @Size(min = 3)
    @Column(name = "customer", nullable = false)
    private String customer;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "break_total_minutes")
    private Long breakTotalMinutes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DailyTaskLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployee() {
        return this.employee;
    }

    public DailyTaskLog employee(String employee) {
        this.setEmployee(employee);
        return this;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getCustomer() {
        return this.customer;
    }

    public DailyTaskLog customer(String customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public DailyTaskLog startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public DailyTaskLog endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getBreakTotalMinutes() {
        return this.breakTotalMinutes;
    }

    public DailyTaskLog breakTotalMinutes(Long breakTotalMinutes) {
        this.setBreakTotalMinutes(breakTotalMinutes);
        return this;
    }

    public void setBreakTotalMinutes(Long breakTotalMinutes) {
        this.breakTotalMinutes = breakTotalMinutes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyTaskLog)) {
            return false;
        }
        return id != null && id.equals(((DailyTaskLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTaskLog{" +
            "id=" + getId() +
            ", employee='" + getEmployee() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", breakTotalMinutes=" + getBreakTotalMinutes() +
            "}";
    }
}
