package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

    @Test(expected = Test.None.class)
    public void toString1() {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setAnnee(1998);
        sequenceEcritureComptable.setDerniereValeur(30);
        Assert.assertEquals( "SequenceEcritureComptable{journalCode=null, annee=1998, derniereValeur=30}",sequenceEcritureComptable.toString());
        Assert.assertNotEquals("SequenceEcritureComptable{annee=2019, derniereValeur=13}",sequenceEcritureComptable.toString());

    }
    @Test(expected = Test.None.class)
    public void getSetJournalCode() {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setJournalCode("TE");
        Assert.assertEquals("TE",sequenceEcritureComptable.getJournalCode());
        Assert.assertNotEquals("AC",sequenceEcritureComptable.getJournalCode());

    }
    @Test(expected = Test.None.class)
    public void getSetAnnee() {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setAnnee(1998);
        Assert.assertEquals( "1998",sequenceEcritureComptable.getAnnee().toString());
        Assert.assertNotEquals("2019",sequenceEcritureComptable.getAnnee().toString());

    }
    @Test(expected = Test.None.class)
    public void getSetDerniereValeur() {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setDerniereValeur(30);
        Assert.assertEquals( "30",sequenceEcritureComptable.getDerniereValeur().toString());
        Assert.assertNotEquals("13",sequenceEcritureComptable.getDerniereValeur().toString());

    }
}