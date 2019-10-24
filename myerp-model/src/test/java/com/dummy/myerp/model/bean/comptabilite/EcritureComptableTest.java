package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test (expected = Test.None.class)
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test (expected = Test.None.class)
    public void getTotalDebit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals(vEcriture.getTotalDebit().toString(), "301.00");
        Assert.assertNotEquals(vEcriture.getTotalDebit().toString(), null);
    }

    @Test (expected = Test.None.class)
    public void getTotalCredit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals(vEcriture.getTotalCredit().toString(), "33");
        Assert.assertNotEquals(vEcriture.getTotalCredit().toString(), null);
    }

    @Test (expected = Test.None.class)
    public void toString1() {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(-1);
        ecritureComptable.setLibelle("Câble ethernet");
        Assert.assertEquals(ecritureComptable.toString(), "EcritureComptable{id=-1, journal=null, reference='null', " +
                "date=null, libelle='Câble ethernet', totalDebit=0, totalCredit=0, listLigneEcriture=[\n" +
                "\n" +
                "]}");
        Assert.assertNotEquals(ecritureComptable.toString(), "EcritureComptable{id=-1, journal=null, reference='null', " +
                "date=null, libelle='tapis de souris', totalDebit=0, totalCredit=0, listLigneEcriture=[\n" +
                "\n" +
                "]}");
    }
}
