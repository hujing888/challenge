package com.finfunc;

import com.finfunc.enums.DayCountBasis;
import com.finfunc.enums.Frequency;
import com.finfunc.exception.FinancialFuncException;
import com.finfunc.functions.Bonds;
import com.finfunc.functions.Common;

import java.util.Date;

public class Financial {

    /**
     * <a target="_blank" href="https://support.microsoft.com/en-us/office/yield-function-f5f5ca43-c4bd-434f-8bd2-ed3c9727a4fe">YIELD function</a>
     * The yield on a security that pays periodic interest
     * @param settlement
     * @param maturity
     * @param rate
     * @param pr
     * @param redemption
     * @param frequency
     * @param basis
     * @return
     * @throws FinancialFuncException
     */
    public static double yield(Date settlement, Date maturity, double rate, double pr, double redemption, Frequency frequency, DayCountBasis basis) throws FinancialFuncException {
        Common.elesThrow("settlement must not be null", settlement != null);
        Common.elesThrow("maturity must not be null", maturity != null);

        Common.elesThrow("maturity must be after settlement", maturity.compareTo(settlement) > 0);
        Common.elesThrow("rate must not be negative", rate >= 0D);
        Common.elesThrow("pr must be more than 0", pr > 0D);
        Common.elesThrow("redemption must be more than 0", redemption > 0D);

        return Bonds.yieldFunc(settlement, maturity, rate, pr, redemption, frequency.getValue(), basis);
    }

    /**
     * <a target="_blank" href="https://support.microsoft.com/en-us/office/yieldmat-function-ba7d1809-0d33-4bcb-96c7-6c56ec62ef6f">YIELDMAT function</a>
     * The annual yield of a security that pays interest at maturity
     * @param settlement
     * @param maturity
     * @param issue
     * @param rate
     * @param pr
     * @param basis
     * @return
     * @throws FinancialFuncException
     */
    public static double yieldMat(Date settlement, Date maturity, Date issue, double rate, double pr, DayCountBasis basis) throws FinancialFuncException {
        Common.elesThrow("settlement must not be null", settlement != null);
        Common.elesThrow("maturity must not be null", maturity != null);
        Common.elesThrow("issue must not be null", issue != null);

        Common.elesThrow("maturity must be after settlement", maturity.compareTo(settlement) > 0);
        Common.elesThrow("maturity must be after issue", maturity.compareTo(issue) > 0);
        Common.elesThrow("settlement must be after issue", settlement.compareTo(issue) > 0);
        Common.elesThrow("rate must be more than 0", rate > 0D);
        Common.elesThrow("pr must be more than 0", pr > 0D);

        return Bonds.yieldMat(settlement, maturity, issue, rate, pr, basis);
    }

    /**
     * <a target="_blank" href="https://support.microsoft.com/en-us/office/duration-function-b254ea57-eadc-4602-a86a-c8e369334038">DURATION function</a>
     * The annual duration of a security with periodic interest payments
     * @param settlement
     * @param maturity
     * @param coupon
     * @param yld
     * @param frequency
     * @param basis
     * @return
     * @throws FinancialFuncException
     */
    public static double duration(Date settlement, Date maturity, double coupon, double yld, Frequency frequency, DayCountBasis basis) throws FinancialFuncException {
        Common.elesThrow("settlement must not be null", settlement != null);
        Common.elesThrow("maturity must not be null", maturity != null);

        Common.elesThrow("maturity must be after settlement", maturity.compareTo(settlement) > 0);
        Common.elesThrow("coupon must be more than 0", coupon >= 0D);
        Common.elesThrow("yld must be more than 0", yld >= 0);

        return Bonds.duration(settlement, maturity, coupon, yld, frequency,basis, false);
    }


    /**
     * <a target="_blank" href="https://support.microsoft.com/en-us/office/mduration-function-b3786a69-4f20-469a-94ad-33e5b90a763c">MDURATION function</a>
     * The Macauley modified duration for a security with an assumed par value of $100
     * @param settlement
     * @param maturity
     * @param coupon
     * @param yld
     * @param frequency
     * @param basis
     * @return
     * @throws FinancialFuncException
     */
    public static double mDuration(Date settlement, Date maturity, double coupon, double yld, Frequency frequency, DayCountBasis basis) throws FinancialFuncException {
        Common.elesThrow("settlement must not be null", settlement != null);
        Common.elesThrow("maturity must not be null", maturity != null);

        Common.elesThrow("maturity must be after settlement", maturity.compareTo(settlement) > 0);
        Common.elesThrow("coupon must be more than 0", coupon >= 0D);
        Common.elesThrow("yld must be more than 0", yld >= 0);

        return Bonds.duration(settlement, maturity, coupon, yld, frequency,basis, true);
    }
}
