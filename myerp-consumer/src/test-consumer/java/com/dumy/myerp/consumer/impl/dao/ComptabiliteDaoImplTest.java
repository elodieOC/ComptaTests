package com.dumy.myerp.consumer.impl.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testconsumer.consumer.ConsumerTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/bootstrapContext.xml")
public class ComptabiliteDaoImplTest extends ConsumerTestCase {
    private ComptabiliteDao daoProxy = getDaoProxy().getComptabiliteDao();
    private EcritureComptable ecritureTest = new EcritureComptable();
    private SequenceEcritureComptable sequenceTest = new SequenceEcritureComptable();
    private List<LigneEcritureComptable> listLigneEcritureTest = new ArrayList<>();


    @Before
    public void setupBefore() throws Exception{
        // Set du jeu de données
        ecritureTest.setJournal(new JournalComptable("AC", "Achat"));
        ecritureTest.setDate(java.sql.Date.valueOf("1984-12-24"));
        ecritureTest.setLibelle("Trombone");
        ecritureTest.setReference("AC-1984/00001");
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Fournisseurs", new BigDecimal("123"),
                null));
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Fournisseurs", null,
                new BigDecimal("123")));

        sequenceTest = new SequenceEcritureComptable("AC", 1984, 24);

    }

    @Test(expected=Test.None.class)
    public void testGetListeCompteComptable() {
        List<CompteComptable> listCompteComptable = daoProxy.getListCompteComptable();
        assertTrue(!listCompteComptable.isEmpty());
    }

    @Test(expected=Test.None.class)
    public void testGetListejournalComptable() {
        List<JournalComptable> journalComptableList = daoProxy.getListJournalComptable();
        assertTrue(!journalComptableList.isEmpty());
    }
    @Test(expected=Test.None.class)
    public void testGetListeEcritureComptable() {
        List<EcritureComptable> listeEcrituresComptable = daoProxy.getListEcritureComptable();
        assertTrue(!listeEcrituresComptable.isEmpty());
    }

    @Test(expected=Test.None.class)
    public void testGetListeSequenceEcritureComptable() throws NotFoundException{
        daoProxy.insertSequenceEcritureComptable(sequenceTest);
        List<SequenceEcritureComptable> sequenceEcritureComptablesList = daoProxy.getListSequenceEcritureComptable(1984);
        assertTrue(!sequenceEcritureComptablesList.isEmpty());
        daoProxy.deleteSequenceEcritureComptable(sequenceTest);
    }

    @Test(expected=Test.None.class)
    public void testGetEcritureComptable() throws NotFoundException{
        EcritureComptable ecritureComptable = daoProxy.getEcritureComptable(-1);
        assertTrue(ecritureComptable != null);
    }

    @Test(expected=Test.None.class)
    public void testGetEcritureComptableByRef() throws NotFoundException{
        EcritureComptable ecritureComptable = daoProxy.getEcritureComptableByRef("AC-2016/00001");
        assertTrue(ecritureComptable != null);
    }

    @Test(expected=NotFoundException.class)
    public void testGetEcritureComptableNotFound() throws NotFoundException{
        EcritureComptable ecritureComptable = daoProxy.getEcritureComptable(0);

    }

    @Test(expected=NotFoundException.class)
    public void testGetEcritureComptableByRefNotFound() throws NotFoundException{
        EcritureComptable ecritureComptable = daoProxy.getEcritureComptableByRef("XX-1984/00024");
    }

    //TODO à compléter
    @Test
    public void testloadListLigneEcriture(){
    }

    @Test(expected = Test.None.class)
    public void testInsertEcritureComptable() throws FunctionalException, NotFoundException {
        List<EcritureComptable> ecritureComptableList = daoProxy.getListEcritureComptable();
        daoProxy.insertEcritureComptable(ecritureTest);
        Assert.assertEquals(ecritureComptableList.size()+1, daoProxy.getListEcritureComptable().size());
        EcritureComptable toDelete = daoProxy.getEcritureComptableByRef("AC-1984/00001");
        daoProxy.deleteEcritureComptable(toDelete.getId());
    }

    @Test(expected = Test.None.class)
    public void testUpdateEcritureComptable() throws FunctionalException, NotFoundException {
        daoProxy.insertEcritureComptable(ecritureTest);
        EcritureComptable toUpdate = daoProxy.getEcritureComptableByRef("AC-1984/00001");
        toUpdate.setLibelle("Gomme");
        daoProxy.updateEcritureComptable(toUpdate);
        Assert.assertEquals("Gomme", daoProxy.getEcritureComptableByRef("AC-1984/00001").getLibelle());
        EcritureComptable toDelete = daoProxy.getEcritureComptableByRef("AC-1984/00001");
        daoProxy.deleteEcritureComptable(toDelete.getId());
    }


    @Test(expected = Test.None.class)
    public void testUpdateSequenceEcritureComptable() throws FunctionalException, NotFoundException {
        daoProxy.insertSequenceEcritureComptable(sequenceTest);
        sequenceTest.setDerniereValeur(56);
        daoProxy.updateSequenceEcritureComptable(sequenceTest);
        SequenceEcritureComptable updated = new SequenceEcritureComptable();
        for(SequenceEcritureComptable seq : daoProxy.getListSequenceEcritureComptable(1984)){
            updated = seq;
        }
        Assert.assertTrue(updated.getDerniereValeur() == 56);
        daoProxy.deleteSequenceEcritureComptable(sequenceTest);
    }




}
