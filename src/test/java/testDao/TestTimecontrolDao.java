package testDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalTime;
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
import com.computacion.taller.model.TsscTimecontrol;
import com.computacion.taller.repository.TimecontrolDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
@Rollback(false)
@TestInstance(Lifecycle.PER_METHOD)
public class TestTimecontrolDao {
	
	@Autowired
	private TimecontrolDao timecontrolDao;
	
	private TsscTimecontrol tc;
	
	@BeforeEach
	public void setUp() {
		tc = new TsscTimecontrol();
		tc.setName("Timecontrol");
		tc.setIntervalRunning(BigDecimal.ONE);
		tc.setLastPlayTime(LocalTime.NOON);
		tc.setAutostart("YES");
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTest() {
		assertNotNull(timecontrolDao);
		TsscTimecontrol tcx = new TsscTimecontrol();
		tcx.setName("Other Timecontrol");
		tcx.setIntervalRunning(BigDecimal.TEN);
		tcx.setLastPlayTime(LocalTime.NOON);
		tcx.setAutostart("YES");
		timecontrolDao.save(tcx);
		assertNotNull(timecontrolDao.findById(tcx.getId()));
		assertEquals(tcx, timecontrolDao.findById(tcx.getId()).get());
		timecontrolDao.delete(tcx);
	}

	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTest() {
		assertNotNull(timecontrolDao);
		timecontrolDao.save(tc);
		tc.setName("Name updated");;
		timecontrolDao.update(tc);
		assertNotNull(timecontrolDao.findById(tc.getId()).get());
		assertEquals(tc.getName(), timecontrolDao.findById(tc.getId()).get().getName());
		tc.setName("Timecontrol");
		timecontrolDao.update(tc);
		assertEquals(tc.getName(), timecontrolDao.findById(tc.getId()).get().getName());
		timecontrolDao.delete(tc);	
	}
	
	@Test
	@Transactional(readOnly=false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteTest() {
		assertNotNull(timecontrolDao);
		timecontrolDao.save(tc);
		timecontrolDao.delete(tc);	
		assertFalse(timecontrolDao.findById(tc.getId()).isPresent());
		timecontrolDao.delete(tc);	
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findByIdTest() {
		assertNotNull(timecontrolDao);
		timecontrolDao.save(tc);
		assertNotNull(timecontrolDao.findById(tc.getId()));
		assertEquals(tc.getName(), timecontrolDao.findById(tc.getId()).get().getName());
		timecontrolDao.delete(tc);	
	}
	
	@Test
	@Transactional(readOnly=true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void findAllTest() {
		assertNotNull(timecontrolDao);
		timecontrolDao.save(tc);
		List<TsscTimecontrol> ad =  timecontrolDao.findAll();
		assertEquals(1, ad.size());
		assertEquals(tc.getName(), ad.get(0).getName());
		timecontrolDao.delete(tc);	
	}
	

}
