package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class PlanRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(PlanRequest planRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (planRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (planRequest != null && planRequest.getName() != null) {
				error = validateName(planRequest.getName());
				if (error.isEmpty() == false) {
					allErrors.put("NAME", error);
				}
			}
			if (planRequest != null && planRequest.getAge() != null) {
				error = validateAge(planRequest.getAge());
				if (error.isEmpty() == false) {
					allErrors.put("AGE", error);
				}
			}
			if (planRequest != null && planRequest.getSelectedPlan().size() != 0) {
				for (String plan : planRequest.getSelectedPlan()) {
					error = validateSelectedPlan(plan);
					if (error.isEmpty() == false) {
						allErrors.put("SELECTED_PLAN", error);
					}
				}
			}
			if (planRequest != null && planRequest.getSpouse() != null) {
				error = validateSpouse(planRequest.getSpouse());
				if (error.isEmpty() == false) {
					allErrors.put("SPOUSE", error);
				}
			}
			if (planRequest != null && planRequest.getFather() != null) {
				error = validateParents(planRequest.getFather());
				if (error.isEmpty() == false) {
					allErrors.put("Father", error);
				}
			}
			if (planRequest != null && planRequest.getMother() != null) {
				error = validateParents(planRequest.getMother());
				if (error.isEmpty() == false) {
					allErrors.put("Mother", error);
				}
			}
			if (planRequest != null && planRequest.getChild1() != null) {
				error = validateChildren(planRequest.getChild1());
				if (error.isEmpty() == false) {
					allErrors.put("CHILD1", error);
				}
			}
			if (planRequest != null && planRequest.getChild2() != null) {
				error = validateChildren(planRequest.getChild2());
				if (error.isEmpty() == false) {
					allErrors.put("CHILD2", error);
				}
			}
			if (planRequest != null && planRequest.getChild3() != null) {
				error = validateChildren(planRequest.getChild3());
				if (error.isEmpty() == false) {
					allErrors.put("CHILD3", error);
				}
			}
			if (planRequest != null && planRequest.getInLaws() != null) {
				error = validateInLaws(planRequest.getInLaws());
				if (error.isEmpty() == false) {
					allErrors.put("IN-LAWS", error);
				}
			}
			if (planRequest != null && planRequest.getGrandParent() != null) {
				error = validateGrandParent(planRequest.getGrandParent());
				if (error.isEmpty() == false) {
					allErrors.put("GRAND_PARENT", error);
				}
			}
			if (planRequest != null && planRequest.getSibilings() != null) {
				error = validateSibilings(planRequest.getSibilings());
				if (error.isEmpty() == false) {
					allErrors.put("SIBILINGS", error);
				}
			}
			if (planRequest != null && planRequest.getOthers() != null) {
				error = validateOthers(planRequest.getOthers());
				if (error.isEmpty() == false) {
					allErrors.put("OTHERS", error);
				}
			}
		}
		return allErrors;

	}

	protected HashMap<String, String> validateOthers(String others) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.OTHERS;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(others, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(others, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(others, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateInLaws(String inLaws) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INLAWS;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(inLaws, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(inLaws, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(inLaws, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateSibilings(String sibilings) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.SIBILINGS;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(sibilings, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(sibilings, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(sibilings, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateGrandParent(String grandParent) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.GRAND_PARENT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(grandParent, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(grandParent, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(grandParent, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateChildren(String children) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.CHILDREN;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(children, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.checkYesOrNo(children, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("YES_OR_NO", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateParents(String parents) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PARENTS;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(parents, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(parents, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(parents, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateSpouse(String spouse) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.SPOUSE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(spouse, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(spouse, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		String error3 = common.checkYesOrNo(spouse, inputParamName);
		if (error3.isEmpty() == false) {
			errors.put("YES_OR_NO", error3);
		}
		return errors;
	}

	protected HashMap<String, String> validateSelectedPlan(String selectedPlan) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.SELECTED_PLAN;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(selectedPlan, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(selectedPlan, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateAge(String age) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.AGE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(age, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isNumericValues(age, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateName(String name) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.NAME;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(name, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(name, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}
}
