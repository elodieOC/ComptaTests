package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class JournalComptableTest {

    @Test(expected = Test.None.class)
    public void toString1() {
        JournalComptable journalComptable = new JournalComptable();
        journalComptable.setCode("EX");
        journalComptable.setLibelle("Exemple");
        System.out.println(journalComptable.toString());
        Assert.assertEquals(journalComptable.toString(), "JournalComptable{code='EX', libelle='Exemple'}");
        Assert.assertNotEquals(journalComptable.toString(), "JournalComptable{code='CTRX', libelle='Contre Exemple'}");
    }

    @Test(expected = Test.None.class)
    public void getByCode() {
        List<JournalComptable> pListJournalComptable = new ArrayList<>();

        // mock liste de journalComptable
        for (int i = 0; i < 3; i++) {
            JournalComptable journalComptable = Mockito.mock(JournalComptable.class);
            Mockito.when(journalComptable.getCode()).thenReturn("AC");
            Mockito.when(journalComptable.getLibelle()).thenReturn("Achat");
            pListJournalComptable.add(journalComptable);
        }

        // test du libelle quand l'objet est present dans la liste
        Assert.assertEquals( "Achat", JournalComptable.getByCode(pListJournalComptable, "AC").getLibelle());
        // test quand l'objet n'est pas present dans la liste
        Assert.assertEquals(null, JournalComptable.getByCode(pListJournalComptable,"XYZ"));
    }
}