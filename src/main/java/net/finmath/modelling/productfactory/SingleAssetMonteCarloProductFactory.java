/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 09.02.2018
 */

package net.finmath.modelling.productfactory;

import java.time.LocalDate;

import net.finmath.modelling.DescribedProduct;
import net.finmath.modelling.ProductDescriptor;
import net.finmath.modelling.ProductFactory;
import net.finmath.modelling.SingleAssetProductDescriptor;
import net.finmath.modelling.describedproducts.DigitalOptionMonteCarlo;
import net.finmath.modelling.describedproducts.EuropeanOptionMonteCarlo;
import net.finmath.modelling.descriptor.SingleAssetDigitalOptionProductDescriptor;
import net.finmath.modelling.descriptor.SingleAssetEuropeanOptionProductDescriptor;

/**
 * Product factory of single asset derivatives for use with a Monte-Carlo method based model.
 *
 * @author Christian Fries
 * @author Roland Bachl
 * @version 1.0
 */
public class SingleAssetMonteCarloProductFactory implements ProductFactory<SingleAssetProductDescriptor> {

	private final LocalDate referenceDate;

	/**
	 * Create the product factory.
	 *
	 * @param referenceDate To be used when converting absolute dates to relative dates in double.
	 */
	public SingleAssetMonteCarloProductFactory(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	@Override
	public DescribedProduct<? extends SingleAssetProductDescriptor> getProductFromDescriptor(ProductDescriptor descriptor) {

		if(descriptor instanceof SingleAssetEuropeanOptionProductDescriptor) {
			DescribedProduct<SingleAssetEuropeanOptionProductDescriptor> product = new EuropeanOptionMonteCarlo((SingleAssetEuropeanOptionProductDescriptor) descriptor, referenceDate);
			return product;
		}
		else if(descriptor instanceof SingleAssetDigitalOptionProductDescriptor) {
			DescribedProduct<SingleAssetDigitalOptionProductDescriptor> product = new DigitalOptionMonteCarlo((SingleAssetDigitalOptionProductDescriptor) descriptor, referenceDate);
			return product;
		}
		else {
			String name = descriptor.name();
			throw new IllegalArgumentException("Unsupported product type " + name);
		}
	}

}
