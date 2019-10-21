package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

    @Test
    public void toString1() {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setAnnee(1998);
        sequenceEcritureComptable.setDerniereValeur(30);
        Assert.assertEquals(sequenceEcritureComptable.toString(), "SequenceEcritureComptable{journalCode=null, annee=1998, derniereValeur=30}");
        Assert.assertNotEquals(sequenceEcritureComptable.toString(), "SequenceEcritureComptable{annee=2019, derniereValeur=13}");

    }
}