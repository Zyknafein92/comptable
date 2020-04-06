package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;


public class EcritureComptableTest {

    private EcritureComptable vEcriture;

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

    @Before
    public void setUp() {
        vEcriture = new EcritureComptable();
    }

    @Test
    public void isEquilibree() {
        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.06", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.06", "33.12"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "300"));
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

    /**
     getTotalDebit()
     **/

    @Test
    public void EmptyOrNoIncomeDebit_ReturnZero () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null,null));
        assertEquals(BigDecimal.valueOf(0), vEcriture.getTotalDebit());
    }

    @Test
    public void OneDebit_ReturnDebit () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"10.04",null));
        assertEquals(BigDecimal.valueOf(10.04), vEcriture.getTotalDebit());
    }

    @Test
    public void MultipleDebit_ReturnDebit () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"10",null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"100",null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"200",null));
        assertEquals(BigDecimal.valueOf(310), vEcriture.getTotalDebit());
    }

    /**
     getTotalCredit()
     **/

    @Test
    public void EmptyOrNoIncomeCredit_ReturnZero () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null,null));
        assertEquals(BigDecimal.valueOf(0), vEcriture.getTotalCredit());
        vEcriture.getListLigneEcriture().add(this.createLigne(1,"0",null));
        assertEquals(BigDecimal.valueOf(0), vEcriture.getTotalCredit());
    }

    @Test
    public void OneCredit_ReturnCrebit () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null ,"20"));
        assertEquals(BigDecimal.valueOf(20), vEcriture.getTotalCredit());
    }

    @Test
    public void MultipleCredit_ReturnCrebit () {
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null,"30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null,"100.11"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1,null,"70"));
        assertEquals(BigDecimal.valueOf(200.11), vEcriture.getTotalCredit());
    }
}
