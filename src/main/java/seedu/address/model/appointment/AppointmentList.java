package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.OverlappingAppointmentException;

/**
 * A list of appointments that enforces no overlapping between its elements and does not allow nulls.
 * Supports a minimal set of list operations.
 */
public class AppointmentList implements Iterable<Appointment> {
    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();
    private final ObservableList<Appointment> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an appointment overlapping wth the given argument.
     */
    public boolean overlaps(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::overlapsWith);
    }

    /**
     * Adds an appointment to the list.
     * The appointment must not already exist in the list.
     */
    public void add(Appointment toAdd) {
        requireNonNull(toAdd);
        if (overlaps(toAdd)) {
            throw new OverlappingAppointmentException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the appointment {@code target} in the list with {@code editedAppointment}.
     * {@code target} must exist in the appointment list.
     * {@code editedAppointment} must not overlap with another existing appointment in the list.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment) {
        requireAllNonNull(target, editedAppointment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentNotFoundException();
        }

        if (overlaps(editedAppointment)) {
            throw new OverlappingAppointmentException();
        }

        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the equivalent appointment from the list.
     * The appointment must exist in the list.
     */
    public void remove(Appointment toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new AppointmentNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code appointments}.
     */
    public void setAppointments(AppointmentList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code appointments}.
     * {@code appointments} must not contain overlapping appointments.
     */
    public void setAppointments(List<Appointment> appointments) {
        requireAllNonNull(appointments);
        if (!appointmentsDoNotOverlap(appointments)) {
            throw new OverlappingAppointmentException();
        }

        internalList.setAll(appointments);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Appointment> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    private boolean appointmentsDoNotOverlap(List<Appointment> appointments) {
        for (int i = 0; i < appointments.size() - 1; i++) {
            for (int j = i + 1; j < appointments.size(); j++) {
                if (appointments.get(i).overlapsWith(appointments.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentList)) {
            return false;
        }

        AppointmentList otherAppointmentList = (AppointmentList) other;
        return internalList.equals(otherAppointmentList.internalList);
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if the list contains an equivalent appointment as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    @Override
    public Iterator<Appointment> iterator() {
        return internalList.iterator();
    }
}
