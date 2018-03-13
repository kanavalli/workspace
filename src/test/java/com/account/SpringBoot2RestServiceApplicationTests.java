package com.account;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tenant.model.RentReceipt;
import com.tenant.model.Tenant;
import com.tenant.repository.RentReceiptRepository;
import com.tenant.repository.TenantRepository;
import com.tenant.resource.TenantResource;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class SpringBoot2RestServiceApplicationTests {

	
	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private RentReceiptRepository rentReceiptRepository;
	
	@Autowired
	private TenantResource tenantResource;
	
	@Test
	public void contextLoads() {
	}
	
	/**
	 * This test case assumes that the initial data has been loaded.
	 * 
	 */
	
	@Test
	public void testh2DataBaseFetching() {
		
		Optional<Tenant> optTenant = tenantRepository.findById(1l);
		Tenant tenant= optTenant.get();
		Assert.assertEquals(tenant.getName(), "John");
		Assert.assertEquals(tenant.getId(), 1);
		
	}
	
	@Test
	public void testh2DataBaseInserts() {
		
		Tenant tenant = new Tenant();
		
		tenant.setId(11);
		tenant.setCredit(50);
		tenant.setName("Raj");
		tenant.setWeeklyRent(300);
		tenantRepository.save(tenant);
		
		Optional<Tenant> optTenant = tenantRepository.findById(11l);
		Tenant tenantRetrieved= optTenant.get();
		Assert.assertEquals(tenantRetrieved.getName(),"Raj");
		Assert.assertEquals(tenantRetrieved.getId(),11);
		
	}
	
	@Test
	public void testCredit() {
		
		Tenant tenant = new Tenant();
		
		tenant.setId(12);
		tenant.setCredit(50);
		tenant.setName("Kana");
		tenant.setWeeklyRent(300);
		tenantRepository.save(tenant);
		
		RentReceipt receipt= new RentReceipt();
		receipt.setTenant(12l);
		receipt.setAmount(250l);
		
		tenantResource.createReceipt(receipt);
		
		Optional<Tenant> optTenant = tenantRepository.findById(12l);
		Tenant tenantRetrieved= optTenant.get();
		Assert.assertEquals(tenantRetrieved.getName(),"Kana");
		
		Assert.assertEquals(tenantRetrieved.getCredit(),0);
		
	}
	
}
