package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompteComptableTest {

    @Test
    public void toString1() {
        CompteComptable compteComptable = new CompteComptable();
        compteComptable.setNumero(3);
        compteComptable.setLibelle("Compte Exemple");
        Assert.assertEquals(compteComptable.toString(), "CompteComptable{numero=3, libelle='Compte Exemple'}");
        Assert.assertNotEquals(compteComptable.toString(), "CompteComptable{numero=154, libelle='XXXXXXX'}");
    }

    @Test
    public void getByNumero() {

    }
}