/*
 * ao-payments-payflowPro - Provider for the Payflow Pro XMLPay system.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2016, 2019, 2021, 2022, 2023  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-payments-payflowPro.
 *
 * ao-payments-payflowPro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-payments-payflowPro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-payments-payflowPro.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.aoapps.payments.payflowPro;

import com.aoapps.lang.security.acl.Group;
import com.aoapps.lang.util.PropertiesUtils;
import com.aoapps.payments.AuthorizationResult;
import com.aoapps.payments.CreditCard;
import com.aoapps.payments.CreditCardProcessor;
import com.aoapps.payments.PropertiesPersistenceMechanism;
import com.aoapps.payments.Transaction;
import com.aoapps.payments.TransactionRequest;
import com.aoapps.payments.TransactionResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests PayflowPro.
 *
 * @author  AO Industries, Inc.
 */
public class PayflowProTestTODO extends TestCase {

  private static Properties config;

  private static synchronized String getConfig(String name) throws IOException {
    if (config == null) {
      config = PropertiesUtils.loadFromResource(PayflowProTestTODO.class, "PayflowProTest.properties");
    }
    return config.getProperty(name);
  }

  private CreditCardProcessor processor;
  private Principal principal;
  private Group group;
  private List<CreditCard> testGoodCreditCards;

  public PayflowProTestTODO(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    processor = new CreditCardProcessor(
        new PayflowPro("PayflowProTest", getConfig("user"), getConfig("vendor"), getConfig("partner"), getConfig("password")),
        PropertiesPersistenceMechanism.getInstance(getConfig("persistencePath"))
    );

    principal = new Principal() {
      @Override
      public String getName() {
        return "TestPrincipal";
      }

      @Override
      public int hashCode() {
        return getName().hashCode();
      }

      @Override
      public String toString() {
        return getName();
      }

      @Override
      public boolean equals(Object obj) {
        if (obj == null) {
          return false;
        }
        if (getClass() != obj.getClass()) {
          return false;
        }
        return this == obj;
      }
    };
    group = new Group() {
      @Override
      public boolean addMember(Principal user) {
        throw new RuntimeException("Unimplemented");
      }

      @Override
      public String getName() {
        return "TestGroup";
      }

      @Override
      public boolean isMember(Principal member) {
        return "TestPrincipal".equals(member.getName());
      }

      @Override
      public Enumeration<? extends Principal> members() {
        throw new RuntimeException("Unimplemented");
      }

      @Override
      public boolean removeMember(Principal user) {
        throw new RuntimeException("Unimplemented");
      }
    };

    testGoodCreditCards = new ArrayList<>();
    testGoodCreditCards.add(
        new CreditCard(
            null,
            principal.getName(),
            group.getName(),
            null,
            null,
            "378282246310005",
            null,
            (byte) 11,
            (short) (new GregorianCalendar().get(Calendar.YEAR) + 1),
            "123",
            "First",
            "Last",
            "Company = Inc.",     // Contains = to test special characters in Payflow Pro protocol
            "signup@aoindustries.com",
            "(251)607-9556",
            "(251)382-1197",
            "AOINDUSTRIES",
            "123-45-6789",
            "816 Azalea & Rd",  // Contains & to test special characters in Payflow Pro protocol
            null,
            "Mobile",
            "AL",
            "36693",
            "US",
            "Test AmEx card"
        )
    );
    testGoodCreditCards.add(
        new CreditCard(
            null,
            principal.getName(),
            group.getName(),
            null,
            null,
            "6011111111111117",
            null,
            (byte) 7,
            (short) (new GregorianCalendar().get(Calendar.YEAR) + 1),
            null,
            "D First",
            "D Last",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "Test Discover card"
        )
    );
    testGoodCreditCards.add(
        new CreditCard(
            null,
            null,
            null,
            null,
            null,
            "5555555555554444",
            null,
            (byte) 4,
            (short) (new GregorianCalendar().get(Calendar.YEAR) + 3),
            "123",
            "First",
            "Last",
            "AO Inc",
            "accounting@aoindustries.com",
            "(251)607-9556",
            "(251)382-1197",
            null,
            null,
            "7262 Bull Pen Cir",
            null,
            "Mobile",
            "AL",
            "36695",
            "US",
            "Test MasterCard card"
        )
    );
    testGoodCreditCards.add(
        new CreditCard(
            null,
            null,
            null,
            null,
            null,
            "4111111111111111",
            null,
            (byte) 3,
            (short) (new GregorianCalendar().get(Calendar.YEAR) + 7),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    );
  }

  @Override
  protected void tearDown() throws Exception {
    processor = null;
    principal = null;
    group = null;
    testGoodCreditCards = null;
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(PayflowProTestTODO.class);
    return suite;
  }

  /**
   * Tests canStoreCreditCards.
   */
  public void testCanStoreCreditCards() throws IOException {
    // Test canStoreCreditCards, expecting false
    assertEquals(
        "Expecting to not be allowed to store credit cards",
        false,
        processor.canStoreCreditCards()
    );
  }

  /**
   * Test a sale with a new card.
   */
  public void testNewCardSaleApproved() throws IOException, SQLException {
    for (CreditCard testGoodCreditCard : testGoodCreditCards) {
      Transaction transaction = processor.sale(
          principal,
          group,
          new TransactionRequest(
              true,
              InetAddress.getLocalHost().getHostAddress(),
              120,
              "1",
              Currency.getInstance("USD"),
              new BigDecimal("1.00"),
              null,
              false,
              null,
              null,
              "Daniel",
              "Armstrong",
              "AO Industries, Inc.",
              "7262 Bull Pen Cir",
              null,
              "Mobile",
              "AL",
              "36695",
              "US",
              false,
              "accounting@aoindustries.com",
              null,
              null,
              "Test transaction"
          ),
          testGoodCreditCard
      );
      assertEquals(
          "Transaction authorization communication result should be SUCCESS",
          TransactionResult.CommunicationResult.SUCCESS,
          transaction.getAuthorizationResult().getCommunicationResult()
      );
      assertEquals(
          "Transaction capture communication result should be SUCCESS",
          TransactionResult.CommunicationResult.SUCCESS,
          transaction.getCaptureResult().getCommunicationResult()
      );
      assertEquals(
          "Transaction should be approved",
          AuthorizationResult.ApprovalResult.APPROVED,
          transaction.getAuthorizationResult().getApprovalResult()
      );
      assertEquals(
          "transaction.status should be CAPTURED",
          Transaction.Status.CAPTURED,
          transaction.getStatus()
      );
    }
  }

  /**
   * Test a sale for Japanese Yen.
   */
  public void testNewCardSaleJapaneseYen() throws IOException, SQLException {
    for (CreditCard testGoodCreditCard : testGoodCreditCards) {
      Transaction transaction = processor.sale(
          principal,
          group,
          new TransactionRequest(
              true,
              InetAddress.getLocalHost().getHostAddress(),
              120,
              "1",
              Currency.getInstance("JPY"),
              new BigDecimal("1000"),
              null,
              false,
              null,
              null,
              "Daniel",
              "Armstrong",
              "AO Industries, Inc.",
              "7262 Bull Pen Cir",
              null,
              "Mobile",
              "AL",
              "36695",
              "US",
              false,
              "accounting@aoindustries.com",
              null,
              null,
              "Test transaction"
          ),
          testGoodCreditCard
      );
      assertEquals(
          "Transaction authorization communication result should be SUCCESS",
          TransactionResult.CommunicationResult.SUCCESS,
          transaction.getAuthorizationResult().getCommunicationResult()
      );
      assertEquals(
          "Transaction capture communication result should be SUCCESS",
          TransactionResult.CommunicationResult.SUCCESS,
          transaction.getCaptureResult().getCommunicationResult()
      );
      assertEquals(
          "Transaction should be approved",
          AuthorizationResult.ApprovalResult.APPROVED,
          transaction.getAuthorizationResult().getApprovalResult()
      );
      assertEquals(
          "transaction.status should be CAPTURED",
          Transaction.Status.CAPTURED,
          transaction.getStatus()
      );
    }
  }
}
