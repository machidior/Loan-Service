package com.machidior.Loan_Management_Service.evaluator;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequirementEvaluationService {

    private final Map<RequirementType, RequirementEvaluator<?, ?>> evaluators;

    public RequirementEvaluationService(
            List<RequirementEvaluator<?, ?>> list
    ) {
        this.evaluators = list.stream()
                .collect(Collectors.toMap(
                        RequirementEvaluator::supports,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    public <C, D> RequirementEvaluationResult evaluate(
            RequirementType type,
            D submission,
            C config
    ) {
        RequirementEvaluator<C, D> evaluator =
                (RequirementEvaluator<C, D>) evaluators.get(type);

        if (evaluator == null) {
            return RequirementEvaluationResult.passed(type);
        }

        return evaluator.evaluate(submission, config);
    }
}
