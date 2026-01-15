package com.machidior.Loan_Management_Service.enums;

import com.machidior.Loan_Management_Service.exception.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum LoanApplicationStatus {
    DRAFTED,
    SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    RETURNED,
    REJECTED;

    private Set<LoanApplicationStatus> allowedTransitions;

    static {
        DRAFTED.allowedTransitions = EnumSet.of(SUBMITTED);
        SUBMITTED.allowedTransitions = EnumSet.of(UNDER_REVIEW, APPROVED, RETURNED, REJECTED);
        UNDER_REVIEW.allowedTransitions = EnumSet.of(APPROVED, RETURNED, REJECTED);
        RETURNED.allowedTransitions = EnumSet.of(DRAFTED, SUBMITTED);
    }

    public boolean canTransitionTo(LoanApplicationStatus next) {
        if (allowedTransitions == null) {
            return false;
        }
        return allowedTransitions.contains(next);
    }

    public void validateTransition (LoanApplicationStatus next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStatusTransitionException("Invalid Loan application transition: " + this + " -> " + next );
        }
    }
}
