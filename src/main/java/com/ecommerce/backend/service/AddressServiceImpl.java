package com.ecommerce.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exception.AddressException;
import com.ecommerce.backend.exception.ProductException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repos.AddressRepo;
import com.ecommerce.backend.repos.ProductRepo;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private AddressRepo addressRepo;

	@Override
	public Address updateAddress(Long addressId, Address req) throws AddressException {
		Address address = findAddressById(addressId);
		
		
		 if (req.getStreetname() != null && !req.getStreetname().isEmpty()) {
		        address.setStreetname(req.getStreetname());
		    }
		    

		 if (req.getCity() != null && !req.getCity().isEmpty()) {
		        address.setCity(req.getCity());
		    }
		    

		 if (req.getState() != null && !req.getState().isEmpty()) {
		        address.setState(req.getState());
		    }
		    

		 if (req.getZipcode() != null && !req.getZipcode().isEmpty()) {
		        address.setZipcode(req.getZipcode());
		    }
		    
		return addressRepo.save(address);
	
	}

	@Override
	public Address findAddressById(Long id) throws AddressException{
			Optional<Address> opt = addressRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new AddressException("Address not found with id"+id);
	}

}
