package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    @Mock
    private DaoProxy mockDaoProxy;
    @Mock
    private TransactionManager mockTransactionManager;
    @Mock
    private ComptabiliteDao mockComptaDao;

    /** Jeu de données */
    private SequenceEcritureComptable sequenceTest;
    private EcritureComptable ecritureTest;
    private List<SequenceEcritureComptable> listeSequencesTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.mockDaoProxy.getComptabiliteDao()).thenReturn(this.mockComptaDao);
        AbstractBusinessManager.configure(null, this.mockDaoProxy, this.mockTransactionManager);

        manager = new ComptabiliteManagerImpl();
        sequenceTest = new SequenceEcritureComptable();
        listeSequencesTest = new ArrayList<>();
        ecritureTest = new EcritureComptable();

        // Set du jeu de données
        ecritureTest.setId(1);
        ecritureTest.setJournal(new JournalComptable("AC", "Achat"));
        ecritureTest.setDate(java.sql.Date.valueOf("2018-10-04"));
        ecritureTest.setLibelle("Trombone");

    }

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00033");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG5PasDeSequenceExistante() throws NotFoundException {
        when(this.mockComptaDao.getListSequenceEcritureComptable(anyInt())).thenReturn(listeSequencesTest);
        when(this.mockComptaDao.getEcritureComptableByRef(anyString())).thenReturn(ecritureTest);
        manager.addReference(ecritureTest);
        Assert.assertEquals("AC-2018/00001",ecritureTest.getReference());
    }
    @Test
    public void checkEcritureComptableUnitRG5AvecSequenceExistante() throws NotFoundException {
        listeSequencesTest.add(new SequenceEcritureComptable("AC", 2018, 32));
        when(this.mockComptaDao.getEcritureComptableByRef(anyString())).thenReturn(ecritureTest);
        when(this.mockComptaDao.getListSequenceEcritureComptable(anyInt())).thenReturn(listeSequencesTest);
        manager.addReference(ecritureTest);
        Assert.assertEquals("AC-2018/00033",ecritureTest.getReference());
    }
}
