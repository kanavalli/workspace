package com.tenant.resource;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tenant.exception.TenantNotFoundException;
import com.tenant.model.RentReceipt;
import com.tenant.model.Tenant;
import com.tenant.repository.RentReceiptRepository;
import com.tenant.repository.TenantRepository;

@RestController
@RequestMapping("/console")
public class TenantResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantResource.class);

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private RentReceiptRepository rentReceiptRepository;

	@GetMapping("/tenants")
	public List<Tenant> retrieveAllTenants() {
		return tenantRepository.findAll();
	}

	@GetMapping("/tenants/{id}")
	public Tenant retrieveTenant(@PathVariable long id) {
		Optional<Tenant> tenant = tenantRepository.findById(id);

		if (!tenant.isPresent()) {
			LOGGER.error("Tenant not found for the given ID!");
			throw new TenantNotFoundException("Tenant not found for the given Id: " + id,
					" May be try to insert the resource first. ", " ");
		}

		return tenant.get();
	}

	@DeleteMapping("/tenants/{id}")
	public void deleteTenant(@PathVariable long id) {
		tenantRepository.deleteById(id);
	}

	@PostMapping("/tenants")
	public ResponseEntity<Object> createTenant(@Valid @RequestBody Tenant Tenant) {

		if (Tenant.getPaidToDate() == null) {
			Tenant.setPaidToDate(LocalDateTime.now());

		}

		Tenant savedTenant = tenantRepository.save(Tenant);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedTenant.getId()).toUri();

		return ResponseEntity.created(location).build();

	}

	@PutMapping("/tenants/{id}")
	public ResponseEntity<Object> updateTenant(@RequestBody Tenant tenant, @PathVariable long id) {

		Optional<Tenant> tenantOptional = tenantRepository.findById(id);

		if (!tenantOptional.isPresent())
			return ResponseEntity.notFound().build();

		tenant.setId(id);

		tenantRepository.save(tenant);

		return ResponseEntity.noContent().build();
	}

	/**
	 * API's for Receipts
	 * 
	 */

	@GetMapping("/receipts")
	public List<RentReceipt> retrieveAllReceipts() {
		return rentReceiptRepository.findAll();
	}

	@GetMapping("/receipts/{id}")
	public RentReceipt retrieveReceipt(@PathVariable Long id) {
		Optional<RentReceipt> receipt = rentReceiptRepository.findById(id);

		if (!receipt.isPresent()) {
			LOGGER.error("Tenant not found for the given ID!");
			throw new TenantNotFoundException("Rent Receipt not found for the given Id: " + id,
					" May be try to insert the resource first. ", " ");
		}

		return receipt.get();
	}

	@GetMapping("/receipts/tenant/{id}")
	public List<RentReceipt> retrieveReceiptByTenant(@PathVariable Integer id) {

		return rentReceiptRepository.findByTenant(id);

	}

	@GetMapping("/receipts/tenant/time/{hour}")
	public List<RentReceipt> retrieveTenantByTime(@PathVariable Integer hour) {

		LocalDateTime date1 = LocalDateTime.now();
		LocalDateTime date2 = date1.minusHours(hour);

		return rentReceiptRepository.findTenantByTime(date2, date1);

	}

	@DeleteMapping("/receipts/{id}")
	public void deleteReceipt(@PathVariable long id) {
		rentReceiptRepository.deleteById(id);
	}

	@PostMapping("/receipts")
	public ResponseEntity<Object> createReceipt(@RequestBody RentReceipt receipt) {

		if (receipt.getCreatedDate() == null) {
			receipt.setCreatedDate(LocalDateTime.now());
		}

		RentReceipt savedReceipt = rentReceiptRepository.save(receipt);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedReceipt.getId()).toUri();

		/**
		 * 1. Based on the rent receipt get the tenant id 2. based on the tenant id get
		 * he tenant bean. 3. make the calculation changes and update the date 4. update
		 * the tenant bean in the db.
		 */
		Optional<Tenant> optTenant = tenantRepository.findById(savedReceipt.getTenant());

		Tenant tenant = optTenant.get();
		tenant.setCredit((int) (savedReceipt.getAmount() - tenant.getWeeklyRent() + tenant.getCredit()));
		tenant.setPaidToDate(LocalDateTime.now());
		tenantRepository.save(tenant);

		return ResponseEntity.created(location).build();

	}

	@PutMapping("/receipts/{id}")
	public ResponseEntity<Object> updateRentReceipt(@RequestBody RentReceipt rentReceipt, @PathVariable long id) {

		Optional<RentReceipt> tenantOptional = rentReceiptRepository.findById(id);

		if (!tenantOptional.isPresent())
			return ResponseEntity.notFound().build();

		if (rentReceipt.getCreatedDate() == null) {
			rentReceipt.setCreatedDate(LocalDateTime.now());
		}

		rentReceipt.setId(id);

		rentReceiptRepository.save(rentReceipt);

		return ResponseEntity.noContent().build();
	}
}
