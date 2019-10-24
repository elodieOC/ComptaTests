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
public class CompteComptableTest {

    @Test(expected = Test.None.class)
    public void toString1() {
        CompteComptable compteComptable = new CompteComptable();
        compteComptable.setNumero(3);
        compteComptable.setLibelle("Compte Exemple");
        Assert.assertEquals(compteComptable.toString(), "CompteComptable{numero=3, libelle='Compte Exemple'}");
        Assert.assertNotEquals(compteComptable.toString(), "CompteComptable{numero=154, libelle='XXXXXXX'}");
    }

    @Test(expected = Test.None.class)
    public void getByNumero() {
        List<CompteComptable> pListCompteComptable = new ArrayList<>();

        // mock list de trois objets CompteComptable
        for (int i = 0; i < 3; i++) {
            CompteComptable compteComptable = Mockito.mock(CompteComptable.class);
            Mockito.when(compteComptable.getNumero()).thenReturn(i);
            Mockito.when(compteComptable.getLibelle()).thenReturn("libelle" + i);
            pListCompteComptable.add(compteComptable);
        }
        // test du libelle d'un objet existant present dans la liste
        Assert.assertEquals("libelle2", CompteComptable.getByNumero(pListCompteComptable, 2).getLibelle());
        // test d'un objet non present dans la liste
        Assert.assertEquals(null, CompteComptable.getByNumero(pListCompteComptable, 5));
    }
}