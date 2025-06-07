package com.sowisetech.advisor.request;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvProfessionalInfoRequestValidatorTest {

	@Autowired(required = true)
	AdvProfessionalInfoRequestValidator advProfessionalInfoRequestValidator;
	
	//Awards Test
	@Test
	public void validateAwardsTest() {
		
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq award=new AwardReq();
		award.setYear("MAY-1996");
		award.setTitle("Upper Crest Award");
		award.setIssuedBy("HDFC Mutual Funds");
		award.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		awards.add(award);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateAwards(awards,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	
	@Test
	public void validateAwardsTestForCheckYear() {
		
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq award=new AwardReq();
		award.setYear("08-1996");
		award.setTitle("Upper Crest Award");
		award.setIssuedBy("HDFC Mutual Funds");
		award.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		awards.add(award);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateAwards(awards,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : AWARDS", error);
		}
	}
	
	@Test
	public void validateAwardsTestForCheckTitle() {
		
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq award=new AwardReq();
		award.setYear("MAY-1996");
		award.setTitle("Upper Crest Awar567@#");
		award.setIssuedBy("HDFC Mutual Funds");
		award.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		awards.add(award);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateAwards(awards,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alphaNumeric characters : AWARDS", error);
		}
	}
	
	@Test
	public void validateAwardsTestForCheckIssuedBy() {
		
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq award=new AwardReq();
		award.setYear("MAY-1996");
		award.setTitle("Upper Crest Award");
		award.setIssuedBy("Life insure658");
		award.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		awards.add(award);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateAwards(awards,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Values must be in alphabets and multiple values separated by comma(,) only : AWARDS", error);
		}
	}
	
	//Certificate Test
	@Test
	public void validateCertificateTest() {
		
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificate=new CertificateReq();
		certificate.setYear("MAR-2008");
		certificate.setTitle("Upper Crest Award");
		certificate.setIssuedBy("HDFC Mutual Funds");
		certificate.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		certificates.add(certificate);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateCertificates(certificates,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	
	@Test
	public void validateCertificatesTestForCheckYear() {
		
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificate=new CertificateReq();
		certificate.setYear("03-2008");
		certificate.setTitle("Upper Crest Award");
		certificate.setIssuedBy("HDFC Mutual Funds");
		certificate.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		certificates.add(certificate);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateCertificates(certificates,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : CERTIFICATES", error);
		}
	}
	
	@Test
	public void validateCertificatesTestForCheckTitle() {
		
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificate=new CertificateReq();
		certificate.setYear("MAR-2008");
		certificate.setTitle("Upper Crest Award6778@#");
		certificate.setIssuedBy("HDFC Mutual Funds");
		certificate.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		certificates.add(certificate);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateCertificates(certificates,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alphaNumeric characters : CERTIFICATES", error);
		}
	}
	
	@Test
	public void validateCertificatesTestForCheckIssuedBy() {
		
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificate=new CertificateReq();
		certificate.setYear("MAR-2008");
		certificate.setTitle("Upper Crest Award");
		certificate.setIssuedBy("Life insure658,H54ealth insurance");
		certificate.setImagePath("C:\\Users\\Employee2\\Downloads\\download.jpg");
		certificates.add(certificate);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateCertificates(certificates,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Values must be in alphabets and multiple values separated by comma(,) only : CERTIFICATES", error);
		}
	}
	
	//Education Test
	@Test
	public void validateEducationsTest() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA");
		education.setField("Finance");
		education.setInstitution("NIIT");
		education.setFromYear("FEB-2001");
		education.setToYear("FEB-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	
	@Test
	public void validateEducationsTestForCheckDegree() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA.56");
		education.setField("Finance");
		education.setInstitution("NIIT");
		education.setFromYear("FEB-2001");
		education.setToYear("FEB-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Degree value must be a valid degree : EDUCATION", error);
		}
	}
	
	@Test
	public void validateEducationsTestForCheckField() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA");
		education.setField("Finance577");
		education.setInstitution("NIIT");
		education.setFromYear("FEB-2001");
		education.setToYear("FEB-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alpha characters : EDUCATION", error);
		}
	}
	
	@Test
	public void validateEducationsTestForCheckInstitution() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA");
		education.setField("Finance");
		education.setInstitution("NIIT4656#@");
		education.setFromYear("FEB-2001");
		education.setToYear("FEB-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alphaNumeric characters : EDUCATION", error);
		}
	}
	
	@Test
	public void validateEducationsTestForCheckFromYear() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA");
		education.setField("Finance");
		education.setInstitution("NIIT");
		education.setFromYear("02-2001");
		education.setToYear("FEB-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : EDUCATION", error);
		}
	}
	
	@Test
	public void validateEducationsTestForCheckToYear() {
		
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq education=new EducationReq();
		education.setDegree("MBA");
		education.setField("Finance");
		education.setInstitution("NIIT");
		education.setFromYear("FEB-2001");
		education.setToYear("02-2002");
		educations.add(education);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateEducations(educations,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : EDUCATION", error);
		}
	}
	
	//Experience Test
	@Test
	public void validateExperienceTest() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds");
		experience.setDesignation("Branch Manager");
		experience.setFromYear("FEB-2016");
		experience.setToYear("APR-2018");
		experience.setLocation("Bangalore");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	
	@Test
	public void validateExperienceTestForCheckCompany() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds56@#");
		experience.setDesignation("Branch Manager");
		experience.setFromYear("FEB-2016");
		experience.setToYear("APR-2018");
		experience.setLocation("Bangalore");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alphaNumeric characters : EXPERIENCE", error);
		}
	}
	
	@Test
	public void validateExperienceTestForCheckDesignation() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds");
		experience.setDesignation("Branch Manager56");
		experience.setFromYear("FEB-2016");
		experience.setToYear("APR-2018");
		experience.setLocation("Bangalore");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alpha characters : EXPERIENCE", error);
		}
	}
	
	@Test
	public void validateExperienceTestForCheckFromYear() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds");
		experience.setDesignation("Branch Manager");
		experience.setFromYear("02-2016");
		experience.setToYear("APR-2018");
		experience.setLocation("Bangalore");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : EXPERIENCE", error);
		}
	}
	
	@Test
	public void validateExperienceTestForCheckToYear() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds");
		experience.setDesignation("Branch Manager");
		experience.setFromYear("FEB-2016");
		experience.setToYear("04-2018");
		experience.setLocation("Bangalore");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("From date must be in a format(MMM-YYYY) : EXPERIENCE", error);
		}
	}
	
	@Test
	public void validateExperienceTestForCheckLocation() {
		
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experience=new ExperienceReq();
		experience.setCompany("Reliance mutual Funds");
		experience.setDesignation("Branch Manager");
		experience.setFromYear("FEB-2016");
		experience.setToYear("APR-2018");
		experience.setLocation("Bangalore4678");
		experiences.add(experience);
				
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProfessionalInfoRequestValidator.validateExperiences(experiences,"ADV000000000N");
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value contains non-alpha characters : EXPERIENCE", error);
		}
	}
	
}
	
