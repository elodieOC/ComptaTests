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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    private LigneEcritureComptable ligneEcritureDebit;
    private LigneEcritureComptable ligneEcritureCredit;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.mockDaoProxy.getComptabiliteDao()).thenReturn(this.mockComptaDao);
        AbstractBusinessManager.configure(null, this.mockDaoProxy, this.mockTransactionManager);

        manager = new ComptabiliteManagerImpl();
        listeSequencesTest = new ArrayList<>();
        ecritureTest = new EcritureComptable();
        sequenceTest = new SequenceEcritureComptable("AC", 2018, 32);
        ligneEcritureDebit = new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123"),
                null);
        ligneEcritureCredit = new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal("123"));

        // Set du jeu de données
        ecritureTest.setId(1);
        ecritureTest.setJournal(new JournalComptable("AC", "Achat"));
        ecritureTest.setDate(java.sql.Date.valueOf("2018-10-04"));
        ecritureTest.setLibelle("Trombone");


    }

    @Test(expected = Test.None.class)
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00033");
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = Test.None.class)
    public void checkEcritureComptableRG2() throws Exception {
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnitRG2(ecritureTest);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG2NonEquilibree() throws Exception{
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal("1234")));
        manager.checkEcritureComptableUnitRG2(ecritureTest);
    }
    @Test (expected = FunctionalException.class)
    public void checkEcritureComptableRG3() throws Exception {
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        manager.checkEcritureComptableUnitRG3(ecritureTest);
    }


    @Test(expected = Test.None.class)
    public void checkEcritureComptableRG5PasDeSequenceExistante() throws NotFoundException {
        when(this.mockComptaDao.getListSequenceEcritureComptable(anyInt())).thenReturn(listeSequencesTest);
        manager.addReference(ecritureTest);
        Assert.assertEquals("AC-2018/00001",ecritureTest.getReference());
    }
    @Test(expected = Test.None.class)
    public void checkEcritureComptableRG5AvecSequenceExistante() throws NotFoundException {
        listeSequencesTest.add(sequenceTest);
        when(this.mockComptaDao.getListSequenceEcritureComptable(anyInt())).thenReturn(listeSequencesTest);
        manager.addReference(ecritureTest);
        Assert.assertEquals("AC-2018/00033",ecritureTest.getReference());
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG5AnneeRefDifferentDateEcriture() throws Exception{
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("AC-2019/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnitRG5(ecritureTest);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG5CodeJournalDifferentDateEcriture() throws Exception{
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("BB-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnitRG5(ecritureTest);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG6RefNonUnique() throws Exception{
        ecritureTest.setReference("AC-2018/00033");

        when(this.mockComptaDao.getEcritureComptableByRef(anyString())).thenReturn(ecritureTest);

        EcritureComptable vEcritureComptable = mock(EcritureComptable.class );
        when(vEcritureComptable.getId()).thenReturn(6);
        when(vEcritureComptable.getReference()).thenReturn("AC-2018/00033");

        manager.checkEcritureComptableContext(vEcritureComptable);
    }
    @Test(expected = Test.None.class)
    public void checkEcritureComptableTest() throws Exception{
        listeSequencesTest.add(sequenceTest);
        when(this.mockComptaDao.getEcritureComptableByRef(anyString())).thenReturn(ecritureTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptable(ecritureTest);
    }

    @Test(expected = Test.None.class)
    public void checkEcritureComptableTestNegatifRG4() throws Exception{
        listeSequencesTest.add(sequenceTest);
        when(this.mockComptaDao.getEcritureComptableByRef(anyString())).thenReturn(ecritureTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal("-123")));
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("-123"),
                null));
        manager.checkEcritureComptable(ecritureTest);
    }

    @Test(expected = Test.None.class)
    public void checkEcritureComptableRG7() throws Exception{
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(ligneEcritureDebit);
        ecritureTest.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnitRG7(ecritureTest);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG7Plus2ChiffresVirgules() throws Exception{
        listeSequencesTest.add(sequenceTest);
        ecritureTest.setReference("AC-2018/00033");
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal("123.256")));
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123.256"),
                null));
        manager.checkEcritureComptableUnitRG7(ecritureTest);
    }

    @Test(expected = FunctionalException.class)
    public void testInsertEcritureComptableAppelCheckEcritureComptable() throws Exception {
        manager.insertEcritureComptable(ecritureTest);

    }

}
