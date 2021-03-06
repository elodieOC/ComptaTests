package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    @Override
    public List<SequenceEcritureComptable> getListSequenceEcritureComptable(Integer pAnnee) {
        return  getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable(pAnnee);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)*/

        //récupère l'année de l'écriture comptable
        Date ecritureDate = pEcritureComptable.getDate();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ecritureDate);
        int anneeEcriture = calendar.get(Calendar.YEAR);

        //prépare le formattage de la dernière valeur de la séquence du journal
        String valeurRefString = "";
        int valeurRefInt = 0;
        DecimalFormat decimalFormat = new DecimalFormat("00000");

        //récupère le code journal de l'écriture
        String codeJournalEcriture = pEcritureComptable.getJournal().getCode();

        // Récupère la liste de sequence du journal pour l'année de l'ecriture
        List<SequenceEcritureComptable> listSequenceEcritureComptable = this.getListSequenceEcritureComptable(anneeEcriture);

      /* 2.  * S'il n'y a aucun enregistrement pour le code_journal pour l'année concernée :
                1. Utiliser le numéro 1.
             * Sinon :
                1. Utiliser la dernière valeur + 1 */
        if (listSequenceEcritureComptable.size() == 0) {
            valeurRefInt = 1;
        } else {
            for (int i = 0; i < listSequenceEcritureComptable.size(); i++) {
                if (pEcritureComptable.getJournal().getCode().equals(listSequenceEcritureComptable.get(i).getJournalCode())) {
                    valeurRefInt = listSequenceEcritureComptable.get(i).getDerniereValeur() + 1;
                }
            }
        }
        valeurRefString = decimalFormat.format(valeurRefInt);

      //3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
        // Creation de la nouvelle reference
        String reference = pEcritureComptable.getJournal().getCode() + "-" + anneeEcriture + "/"+ valeurRefString;
        // Maj
        pEcritureComptable.setReference(reference);
        // Maj en BDD
        try {
            this.updateEcritureComptable(pEcritureComptable);
        } catch (FunctionalException e) {
            e.printStackTrace();
        }

      //4.  Enregistrer (insert/update) la valeur de la séquence en persitance (table sequence_ecriture_comptable)
        SequenceEcritureComptable newSeq = new SequenceEcritureComptable();
        newSeq.setJournalCode(pEcritureComptable.getJournal().getCode());
        newSeq.setAnnee(anneeEcriture);
        newSeq.setDerniereValeur(valeurRefInt);

        if (valeurRefInt == 1) {
            // Enregistrement avec insert
            insertSequenceEcritureComptable(newSeq);
        } else {
            // Enregistrement avec update (maj bdd)
            updateSequenceEcritureComptable(newSeq);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnitRG2(pEcritureComptable);
        this.checkEcritureComptableUnitRG3(pEcritureComptable);
        this.checkEcritureComptableUnitRG5(pEcritureComptable);
        this.checkEcritureComptableUnitRG7(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
        this.checkEcritureComptableUnit(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation: "+vViolations,
                            vViolations));
        }
    }

    /**
     * Vérifie que l'Ecriture comptable est équilibrée
     * @param pEcritureComptable
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnitRG2(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }
    }

    /**
     * Vérifie que l'Ecriture comptable a au moins 2 lignes d'écriture
     * @param pEcritureComptable
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnitRG3(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }

        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2 || vNbrCredit < 1 || vNbrDebit < 1) {
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
    }

    /**
     * Vérifie que des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule.
     * @param pEcritureComptable
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnitRG7(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_7 : Les montants des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule.
        int excep = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (vLigneEcritureComptable.getDebit() != null) {
                if (vLigneEcritureComptable.getDebit().scale() > 2) {excep++;}
            }
            if (vLigneEcritureComptable.getCredit() != null) {
                if (vLigneEcritureComptable.getCredit().scale() > 2) {excep++;}
            }
        }
        if(excep!=0) {
            throw new FunctionalException(
                    "Les montants des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule.");
        }
    }
    /**
     * Vérifie la validité de la référence de l'écriture comptable
     * @param pEcritureComptable
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnitRG5(EcritureComptable pEcritureComptable) throws FunctionalException {
      // ===== RG_Compta_5 : Format et contenu de la référence -> XX-AAAA/#####
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        String journalCode = pEcritureComptable.getJournal().getCode();
        String codeReference = pEcritureComptable.getReference().substring(0, 2);
        String anneeReference = pEcritureComptable.getReference().substring(3,7);
        String anneeFormat = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(anneeFormat);
        String anneeEcritureComptable = dateFormat.format(pEcritureComptable.getDate());
        // vérification code journal
        if (!journalCode.equals(codeReference)) {
            throw  new FunctionalException(
                    "Le journal code dans la référence doit être identique au journal code");
        }
        // vérification de la date sur la réference
        if (!anneeReference.equals(anneeEcritureComptable)) {
            throw  new FunctionalException(
                    "L'année dans la référence doit être identique à la date d'ecriture");
        }
    }
    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());
                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void deleteSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
