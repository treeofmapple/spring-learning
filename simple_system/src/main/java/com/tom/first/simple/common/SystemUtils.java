package com.tom.first.simple.common;

import java.util.List;

import com.tom.first.simple.dto.UserRequest;
import com.tom.first.simple.dto.evaluation.EvaluationUpdate;
import com.tom.first.simple.dto.user.UserGradeResponse;
import com.tom.first.simple.mapper.UserMapper;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.model.User;

public class SystemUtils {

	protected double mathAverageGrade(List<Evaluation> evaluations) {
		double sumGrades = evaluations.stream().mapToDouble(Evaluation::getGrade).sum();
		return sumGrades / evaluations.size();
	}

	protected UserGradeResponse mathAverageGrade(User user, UserMapper mapper) {
		List<Evaluation> evaluations = user.getEvaluations();
		double averageGrade = 0.0;
		if (!evaluations.isEmpty()) {
			averageGrade = mathAverageGrade(evaluations);
		}
		return mapper.fromUserGrade(user.getName(), averageGrade);
	}

	protected void mergeUser(User user, UserRequest request) {
		user.setName(request.name());
		user.setEmail(request.email());
	}

	protected void mergeEvaluation(Evaluation evaluation, EvaluationUpdate request, User user) {
		evaluation.setSubject(request.subject());
		evaluation.setDescription(request.description());
		evaluation.setGrade(request.grade());
		evaluation.setUser(user);
	}
	
}
