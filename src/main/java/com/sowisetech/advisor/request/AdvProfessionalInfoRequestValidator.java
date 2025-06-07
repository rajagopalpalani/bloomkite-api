package com.sowisetech.advisor.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.request.AwardReq;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class AdvProfessionalInfoRequestValidator implements IValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	@Autowired
	AdvisorService advisorService;

	public HashMap<String, HashMap<String, String>> validate(AdvProfessionalInfoRequest advisorProfInfoRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (advisorProfInfoRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (advisorProfInfoRequest != null && advisorProfInfoRequest.getAwards() != null) {
				error = validateAwards(advisorProfInfoRequest.getAwards(), advisorProfInfoRequest.getAdvId());
				if (error.isEmpty() == false) {
					allErrors.put("AWARDS", error);
				}
			}
			if (advisorProfInfoRequest != null && advisorProfInfoRequest.getCertificates() != null) {
				error = validateCertificates(advisorProfInfoRequest.getCertificates(),
						advisorProfInfoRequest.getAdvId());
				if (error.isEmpty() == false) {
					allErrors.put("CERTIFICATES", error);
				}
			}
			if (advisorProfInfoRequest != null && advisorProfInfoRequest.getEducations() != null) {
				error = validateEducations(advisorProfInfoRequest.getEducations(), advisorProfInfoRequest.getAdvId());
				if (error.isEmpty() == false) {
					allErrors.put("EDUCATION", error);
				}
			}
			if (advisorProfInfoRequest != null && advisorProfInfoRequest.getExperiences() != null) {
				error = validateExperiences(advisorProfInfoRequest.getExperiences(), advisorProfInfoRequest.getAdvId());
				if (error.isEmpty() == false) {
					allErrors.put("EXPERIENCE", error);
				}
			}

		}

		// for (String error : allErrors) {
		// if (StringUtils.isEmpty(error) == false) {
		// validErrors.add(error);
		// }
		//
		// }
		// return validErrors;
		return allErrors;

	}

	public HashMap<String, String> validateAwards(List<AwardReq> awards, String advId) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.AWARDS;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (AwardReq a : awards) {
			if (a.getAwardId() != 0) {
				if (advisorService.fetchAdvAwardByAdvIdAndAwardId(a.getAwardId(), advId) == null) {
					errors.put("ID", appMessages.getAward_not_found());
				}
			}
			if (a.getTitle() != null) {
				String error = common.isAlphaNumericSpace(a.getTitle(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("TITLE", error);
				}
			}
			if (a.getIssuedBy() != null) {
				String error = common.allowMultipleText(a.getIssuedBy(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("ISSUEDBY", error);
				}
			}
			if (a.getYear() != null) {
				String error = common.fromDateCheck(a.getYear(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("YEAR", error);
				}
			}
			// if (a.getImagePath() != null) {
			// if(Common.isImage(a.getImagePath(), inputParamName).isEmpty()==false) {
			// errors.put("IMAGE_PATH", Common.isImage(a.getImagePath(), inputParamName));}
			// }
		}
		return errors;
	}

	public HashMap<String, String> validateCertificates(List<CertificateReq> certificates, String advId) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.CERTIFICATES;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (CertificateReq cert : certificates) {
			if (cert.getCertificateId() != 0) {
				if (advisorService.fetchAdvCertificateByAdvIdAndCertificateId(cert.getCertificateId(), advId) == null) {
					errors.put("ID", appMessages.getCertificate_not_found());
				}
			}
			if (cert.getTitle() != null) {
				String error = common.isAlphaNumericSpace(cert.getTitle(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("TITLE", error);
				}
			}
			if (cert.getIssuedBy() != null) {
				String error = common.allowMultipleText(cert.getIssuedBy(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("ISSUEDBY", error);
				}
			}
			if (cert.getYear() != null) {
				String error = common.fromDateCheck(cert.getYear(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("YEAR", error);
				}
			}
			// if (cert.getImagePath() != null) {
			// if(Common.isImage(cert.getImagePath(), inputParamName).isEmpty()==false) {
			// errors.put("IMAGE_PATH", Common.isImage(cert.getImagePath(),
			// inputParamName));}
			// }
		}
		return errors;
	}

	protected HashMap<String, String> validateEducations(List<EducationReq> educations, String advId) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.EDUCATION;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (EducationReq e : educations) {
			if (e.getEduId() != 0) {
				if (advisorService.fetchAdvEducationByAdvIdAndEduId(e.getEduId(), advId) == null) {
					errors.put("ID", appMessages.getEducation_not_found());//
				}
			}
			if (e.getInstitution() != null) {
				String error = common.isAlphaNumericSpace(e.getInstitution(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("INSTITUTION", error);
				}
			}
			if (e.getDegree() != null) {
				String error = common.degreeCheck(e.getDegree(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("DEGREE", error);
				}
			}
			if (e.getField() != null) {
				String error = common.isAlpha(e.getField(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("FIELD", error);
				}
			}
			if (e.getFromYear() != null) {
				String error = common.fromDateCheck(e.getFromYear(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("FROMYEAR", error);
				}
			}
			if (!e.isTillDate()) {
				if (e.getToYear() != null) {
					String error = common.fromDateCheck(e.getToYear(), inputParamName);
					if (error.isEmpty() == false) {
						errors.put("TOYEAR", error);
					}
				}
			}

		}
		return errors;
	}

	protected HashMap<String, String> validateExperiences(List<ExperienceReq> experiences, String advId) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.EXPERIENCE;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (ExperienceReq e : experiences) {
			if (e.getExpId() != 0) {
				if (advisorService.fetchAdvExperienceByAdvIdAndExpId(e.getExpId(), advId) == null) {
					errors.put("ID", appMessages.getExperience_not_found());//
				}
			}
			if (e.getCompany() != null) {
				String error = common.isAlphaNumericSpace(e.getCompany(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("COMPANY", error);
				}
			}
			if (e.getDesignation() != null) {
				String error = common.isAlpha(e.getDesignation(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("DESIGNATION", error);
				}
			}
			if (e.getLocation() != null) {
				String error = common.isAlpha(e.getLocation(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("LOCATION", error);
				}
			}
			if (e.getFromYear() != null) {
				String error = common.fromDateCheck(e.getFromYear(), inputParamName);
				if (error.isEmpty() == false) {
					errors.put("FROMYEAR", error);
				}
			}
			if (!e.isTillDate()) {
				if (e.getToYear() != null) {
					String error = common.fromDateCheck(e.getToYear(), inputParamName);
					if (error.isEmpty() == false) {
						errors.put("TOYEAR", error);
					}
				}
			}
		}
		return errors;
	}
}
