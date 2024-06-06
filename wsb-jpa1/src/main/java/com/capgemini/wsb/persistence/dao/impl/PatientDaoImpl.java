package com.capgemini.wsb.persistence.dao.impl;

import com.capgemini.wsb.persistence.dao.PatientDao;
import com.capgemini.wsb.persistence.entity.PatientEntity;
import com.capgemini.wsb.persistence.entity.VisitEntity;
import com.capgemini.wsb.persistence.enums.TreatmentType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {


    @Override
    public List<PatientEntity> findByDoctor(String firstName, String lastName) {
        Query query = entityManager.createQuery("select pe from PatientEntity pe " +
                        "join pe.visits vt " +
                        "where vt.doctor.firstName = :firstName and vt.doctor.lastName = :lastName") //
                .setParameter("firstName", firstName) //
                .setParameter("lastName", lastName);
        return query.getResultList();

    }

    @Override
    public List<PatientEntity> findPatientsHavingTreatmentType(TreatmentType treatmentType) {
        Query query = entityManager.createQuery("select distinct pe from PatientEntity pe " +
                "join pe.visits vt " +
                "join vt.medicalTreatments mt " +
                "where mt.type = :treatmentType")//
                .setParameter("treatmentType", treatmentType);
        return query.getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsSharingSameLocationWithDoc(String firstName, String lastName) {
        Query query = entityManager.createQuery("select  pe from PatientEntity pe " +
                        "join pe.addresses ad " +
                        "join ad.doctors dt " +
                        "where dt.firstName = :docFirstName and dt.lastName = :docLastName")//
                .setParameter("docFirstName", firstName)
                .setParameter("docLastName", lastName);
        return query.getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsWithoutLocation() {
        Query query = entityManager.createQuery("select  pe from PatientEntity pe where size(pe.addresses) = 0");
        return query.getResultList();
    }
}
