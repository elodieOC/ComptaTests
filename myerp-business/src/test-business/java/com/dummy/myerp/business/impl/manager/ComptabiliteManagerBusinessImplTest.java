package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/bootstrapContext.xml")
public class ComptabiliteManagerBusinessImplTest extends BusinessTestCase {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable ecritureTest = new EcritureComptable();
    private SequenceEcritureComptable sequenceTest = new SequenceEcritureComptable();

    @Before
    public void setupBefore() throws Exception{
        // Set du jeu de données
        ecritureTest.setId(1);
        ecritureTest.setJournal(new JournalComptable("AC", "Achat"));
        ecritureTest.setDate(java.sql.Date.valueOf("1984-12-24"));
        ecritureTest.setLibelle("Trombone");
        ecritureTest.setReference("AC-1984/00001");
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123"),
                null));
        ecritureTest.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal("123")));

    }


    @Test
    public void testDeleteEcritureComptable() throws FunctionalException {

    }
    @Test
    public void testInsertEcritureComptable() throws FunctionalException {

    }
    @Test
    public void testUpdateEcritureComptable() throws FunctionalException {

    }
    @Test
    public void testDeleteSequenceEcritureComptable() throws FunctionalException {

    }
    @Test
    public void testInsertSequenceEcritureComptable() throws FunctionalException {

    }
    @Test
    public void testUpdateSequenceEcritureComptable() throws FunctionalException {

    }



}
