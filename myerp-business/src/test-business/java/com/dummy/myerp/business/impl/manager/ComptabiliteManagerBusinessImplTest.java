package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/bootstrapContext.xml")
public class ComptabiliteManagerBusinessImplTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable ecritureTest = new EcritureComptable();
    private SequenceEcritureComptable sequenceTest = new SequenceEcritureComptable();
    private List<LigneEcritureComptable> listLigneEcritureTest = new ArrayList<>();

    @Before
    public void setupBefore() throws Exception{
        // Set du jeu de données
        ecritureTest.setId(1);
        ecritureTest.setJournal(new JournalComptable("AC", "Achat"));
        ecritureTest.setDate(java.sql.Date.valueOf("1984-12-24"));
        ecritureTest.setLibelle("Trombone");
        ecritureTest.setReference("AC-1984/00001");
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal("123"),
                null));
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal("123")));

        sequenceTest = new SequenceEcritureComptable("AC", 1984, 24);

    }

    @Test(expected = Test.None.class)
    public void testInsertEcritureComptable() throws FunctionalException {
        manager.insertEcritureComptable(ecritureTest);
        manager.deleteEcritureComptable(ecritureTest.getId());
    }
    @Test(expected = Test.None.class)
    public void testUpdateEcritureComptable() throws FunctionalException {
        manager.insertEcritureComptable(ecritureTest);
        ecritureTest.setLibelle("Gomme");
        manager.updateEcritureComptable(ecritureTest);
        manager.deleteEcritureComptable(ecritureTest.getId());
    }


    @Test(expected = Test.None.class)
    public void testDeleteEcritureComptable() throws FunctionalException {
        manager.insertEcritureComptable(ecritureTest);
        manager.deleteEcritureComptable(ecritureTest.getId());
    }
    @Test(expected = Test.None.class)
    public void testInsertSequenceEcritureComptable() throws FunctionalException {
        manager.insertSequenceEcritureComptable(sequenceTest);
        manager.deleteSequenceEcritureComptable(sequenceTest);
    }
    @Test(expected = Test.None.class)
    public void testUpdateSequenceEcritureComptable() throws FunctionalException {
        manager.insertSequenceEcritureComptable(sequenceTest);
        sequenceTest.setDerniereValeur(25);
        manager.updateSequenceEcritureComptable(sequenceTest);
        manager.deleteSequenceEcritureComptable(sequenceTest);
    }

    @Test(expected = Test.None.class)
    public void testDeleteSequenceEcritureComptable() throws FunctionalException {
        manager.insertSequenceEcritureComptable(sequenceTest);
        manager.deleteSequenceEcritureComptable(sequenceTest);
    }



}
