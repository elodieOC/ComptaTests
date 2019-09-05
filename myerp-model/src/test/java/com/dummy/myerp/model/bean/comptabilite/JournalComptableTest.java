package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JournalComptableTest {

    @Test
    public void toString1() {
        JournalComptable journalComptable = new JournalComptable();
        journalComptable.setCode("EX");
        journalComptable.setLibelle("Exemple");
        System.out.println(journalComptable.toString());
        Assert.assertEquals(journalComptable.toString(), "JournalComptable{code='EX', libelle='Exemple'}");
        Assert.assertNotEquals(journalComptable.toString(), "JournalComptable{code='CTRX', libelle='Contre Exemple'}");
    }

    @Test
    public void getByCode() {
    }
}