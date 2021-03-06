package net.finmath.modelling.describedproducts;

import java.time.LocalDate;
import java.util.Arrays;

import net.finmath.modelling.DescribedProduct;
import net.finmath.modelling.descriptor.InterestRateSwapLegProductDescriptor;
import net.finmath.montecarlo.interestrate.products.SwapLeg;
import net.finmath.montecarlo.interestrate.products.components.Notional;
import net.finmath.montecarlo.interestrate.products.indices.AbstractIndex;
import net.finmath.montecarlo.interestrate.products.indices.LIBORIndex;
import net.finmath.time.ScheduleInterface;

/**
 * Monte-Carlo method based implementation of a interest rate swap leg from a product descriptor.
 *
 * @author Christian Fries
 * @author Roland Bachl
 */
public class SwapLegMonteCarlo extends SwapLeg implements DescribedProduct<InterestRateSwapLegProductDescriptor> {

	private static final boolean						couponFlow = true;

	private final InterestRateSwapLegProductDescriptor descriptor;

	/**
	 * Create product from descriptor.
	 * 
	 * @param descriptor The descriptor of the product.
	 * @param referenceDate The reference date of the data for the valuation, used to convert absolute date to relative dates in double representation.
	 */
	public SwapLegMonteCarlo(InterestRateSwapLegProductDescriptor descriptor, LocalDate referenceDate) {
		super(descriptor.getLegScheduleDescriptor().getSchedule(referenceDate),
				Arrays.stream(descriptor.getNotionals()).mapToObj(x -> new Notional(x)).toArray(Notional[]::new),
				constructLiborIndex(descriptor.getForwardCurveName(), descriptor.getLegScheduleDescriptor().getSchedule(referenceDate)),
				descriptor.getSpreads(),
				couponFlow,
				descriptor.isNotionalExchanged());

		this.descriptor = descriptor;
	}



	/**
	 * Construct a Libor index for a given curve and schedule.
	 *
	 * @param forwardCurveName
	 * @param schedule
	 * @return The Libor index or null, if forwardCurveName is null.
	 */
	private static AbstractIndex constructLiborIndex(String forwardCurveName, ScheduleInterface schedule) {

		if(forwardCurveName != null) {

			//determine average fixing offset and period length
			double fixingOffset = 0;
			double periodLength = 0;

			for(int i = 0; i < schedule.getNumberOfPeriods(); i++) {
				fixingOffset *= ((double) i) / (i+1);
				fixingOffset += (schedule.getPeriodStart(i) - schedule.getFixing(i)) / (i+1);

				periodLength *= ((double) i) / (i+1);
				periodLength += schedule.getPeriodLength(i) / (i+1);
			}

			return new LIBORIndex(forwardCurveName, fixingOffset, periodLength);
		} else {
			return null;
		}
	}



	@Override
	public InterestRateSwapLegProductDescriptor getDescriptor() {
		return descriptor;
	}
}
