package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/bootstrapContext.xml")
public class ComptabiliteManagerBusinessImplTest extends BusinessTestCase {
    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable ecritureComptableForTest;
    private SequenceEcritureComptable sequenceEcritureComptableTest;

    @Before
    public void setupBefore() throws Exception{
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
