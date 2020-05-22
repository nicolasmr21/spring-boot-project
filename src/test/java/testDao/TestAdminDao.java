package testDao;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.computacion.taller.model.TsscAdmin;
import com.computacion.taller.repository.AdminDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
@Rollback(false)
@TestInstance(Lifecycle.PER_CLASS)
public class TestAdminDao {
	
	@Autowired
	private AdminDao adminDao;
	
	private TsscAdmin admin;
	
	@BeforeEach
	public void setUp() {		
		admin = new TsscAdmin();
		admin.setUser("user");
		admin.setPassword("{noop}password");
		admin.setSuperAdmin("YES");
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTest() {
		assertNotNull(adminDao);
		TsscAdmin adminx = new TsscAdmin();
		adminx.setSuperAdmin("NO");
		adminx.setUser("Other");
		adminx.setPassword("{noop}admin");
		adminDao.save(adminx);
		assertNotNull(adminDao.findById(adminx.getId()));
		assertEquals(adminx, adminDao.findByUser(adminx.getUser()));
		adminDao.delete(adminx);
	}

	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTest() {
		assertNotNull(adminDao);
		adminDao.save(admin);
		admin.setSuperAdmin("NO");
		adminDao.update(admin);
		assertNotNull(adminDao.findById(admin.getId()).get());
		assertEquals(admin.getSuperAdmin(), adminDao.findById(admin.getId()).get().getSuperAdmin());
		admin.setSuperAdmin("YES");
		adminDao.update(admin);
		assertEquals(admin.getSuperAdmin(), adminDao.findById(admin.getId()).get().getSuperAdmin());
		adminDao.delete(admin);	
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTest() {
		assertNotNull(adminDao);
		adminDao.save(admin);
		adminDao.delete(admin);	
		assertFalse(adminDao.findById(admin.getId()).isPresent());
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByIdTest() {
		assertNotNull(adminDao);
		adminDao.save(admin);
		assertNotNull(adminDao.findById(admin.getId()));
		assertEquals(admin.getSuperAdmin(), adminDao.findById(admin.getId()).get().getSuperAdmin());
		adminDao.delete(admin);	
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findAllTest() {
		assertNotNull(adminDao);
		adminDao.save(admin);
		List<TsscAdmin> ad =  adminDao.findAll();
		assertEquals(1, ad.size());
		assertEquals(admin.getUser(), ad.get(0).getUser());
		adminDao.delete(admin);	
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByUserTest() {
		assertNotNull(adminDao);
		adminDao.save(admin);
		assertNotNull(adminDao.findByUser(admin.getUser()));
		assertEquals(admin.getSuperAdmin(), adminDao.findByUser(admin.getUser()).getSuperAdmin());
		adminDao.delete(admin);	
	}

}
