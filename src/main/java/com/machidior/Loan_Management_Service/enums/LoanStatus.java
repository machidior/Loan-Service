package com.machidior.Loan_Management_Service.enums;


import com.machidior.Loan_Management_Service.exception.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum LoanStatus {
    PENDING,
    DISBURSED,
    ACTIVE,
    FAILED,
    TERMINATED,
    CLOSED;

    private Set<LoanStatus> allowedTransitions;

    public boolean canTransitionTo(LoanStatus next) {
        if (allowedTransitions == null) {
            return false;
        }
        return allowedTransitions.contains(next);
    }

    public void validateTransition(LoanStatus next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStatusTransitionException("Invalid Loan status transition: " + this + " -> " + next);
        }
    }

    static {
        PENDING.allowedTransitions = EnumSet.of(DISBURSED, FAILED, TERMINATED, CLOSED);
        DISBURSED.allowedTransitions = EnumSet.of(ACTIVE, TERMINATED, CLOSED, FAILED);
        ACTIVE.allowedTransitions = EnumSet.of(TERMINATED, CLOSED, FAILED);
        FAILED.allowedTransitions = EnumSet.of(DISBURSED, ACTIVE, TERMINATED, CLOSED);
    }
}
