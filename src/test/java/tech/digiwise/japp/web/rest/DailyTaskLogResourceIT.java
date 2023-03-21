package tech.digiwise.japp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.digiwise.japp.IntegrationTest;
import tech.digiwise.japp.domain.DailyTaskLog;
import tech.digiwise.japp.repository.DailyTaskLogRepository;

/**
 * Integration tests for the {@link DailyTaskLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DailyTaskLogResourceIT {

    private static final String DEFAULT_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_BREAK_TOTAL_MINUTES = 1L;
    private static final Long UPDATED_BREAK_TOTAL_MINUTES = 2L;

    private static final String ENTITY_API_URL = "/api/daily-task-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DailyTaskLogRepository dailyTaskLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyTaskLogMockMvc;

    private DailyTaskLog dailyTaskLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTaskLog createEntity(EntityManager em) {
        DailyTaskLog dailyTaskLog = new DailyTaskLog()
            .employee(DEFAULT_EMPLOYEE)
            .customer(DEFAULT_CUSTOMER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .breakTotalMinutes(DEFAULT_BREAK_TOTAL_MINUTES);
        return dailyTaskLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTaskLog createUpdatedEntity(EntityManager em) {
        DailyTaskLog dailyTaskLog = new DailyTaskLog()
            .employee(UPDATED_EMPLOYEE)
            .customer(UPDATED_CUSTOMER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .breakTotalMinutes(UPDATED_BREAK_TOTAL_MINUTES);
        return dailyTaskLog;
    }

    @BeforeEach
    public void initTest() {
        dailyTaskLog = createEntity(em);
    }

    @Test
    @Transactional
    void createDailyTaskLog() throws Exception {
        int databaseSizeBeforeCreate = dailyTaskLogRepository.findAll().size();
        // Create the DailyTaskLog
        restDailyTaskLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTaskLog)))
            .andExpect(status().isCreated());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeCreate + 1);
        DailyTaskLog testDailyTaskLog = dailyTaskLogList.get(dailyTaskLogList.size() - 1);
        assertThat(testDailyTaskLog.getEmployee()).isEqualTo(DEFAULT_EMPLOYEE);
        assertThat(testDailyTaskLog.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
        assertThat(testDailyTaskLog.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDailyTaskLog.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDailyTaskLog.getBreakTotalMinutes()).isEqualTo(DEFAULT_BREAK_TOTAL_MINUTES);
    }

    @Test
    @Transactional
    void createDailyTaskLogWithExistingId() throws Exception {
        // Create the DailyTaskLog with an existing ID
        dailyTaskLog.setId(1L);

        int databaseSizeBeforeCreate = dailyTaskLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyTaskLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTaskLog)))
            .andExpect(status().isBadRequest());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyTaskLogRepository.findAll().size();
        // set the field null
        dailyTaskLog.setEmployee(null);

        // Create the DailyTaskLog, which fails.

        restDailyTaskLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTaskLog)))
            .andExpect(status().isBadRequest());

        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyTaskLogRepository.findAll().size();
        // set the field null
        dailyTaskLog.setCustomer(null);

        // Create the DailyTaskLog, which fails.

        restDailyTaskLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTaskLog)))
            .andExpect(status().isBadRequest());

        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDailyTaskLogs() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        // Get all the dailyTaskLogList
        restDailyTaskLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTaskLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].employee").value(hasItem(DEFAULT_EMPLOYEE)))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].breakTotalMinutes").value(hasItem(DEFAULT_BREAK_TOTAL_MINUTES.intValue())));
    }

    @Test
    @Transactional
    void getDailyTaskLog() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        // Get the dailyTaskLog
        restDailyTaskLogMockMvc
            .perform(get(ENTITY_API_URL_ID, dailyTaskLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyTaskLog.getId().intValue()))
            .andExpect(jsonPath("$.employee").value(DEFAULT_EMPLOYEE))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.breakTotalMinutes").value(DEFAULT_BREAK_TOTAL_MINUTES.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDailyTaskLog() throws Exception {
        // Get the dailyTaskLog
        restDailyTaskLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDailyTaskLog() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();

        // Update the dailyTaskLog
        DailyTaskLog updatedDailyTaskLog = dailyTaskLogRepository.findById(dailyTaskLog.getId()).get();
        // Disconnect from session so that the updates on updatedDailyTaskLog are not directly saved in db
        em.detach(updatedDailyTaskLog);
        updatedDailyTaskLog
            .employee(UPDATED_EMPLOYEE)
            .customer(UPDATED_CUSTOMER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .breakTotalMinutes(UPDATED_BREAK_TOTAL_MINUTES);

        restDailyTaskLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDailyTaskLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDailyTaskLog))
            )
            .andExpect(status().isOk());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
        DailyTaskLog testDailyTaskLog = dailyTaskLogList.get(dailyTaskLogList.size() - 1);
        assertThat(testDailyTaskLog.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testDailyTaskLog.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
        assertThat(testDailyTaskLog.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDailyTaskLog.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDailyTaskLog.getBreakTotalMinutes()).isEqualTo(UPDATED_BREAK_TOTAL_MINUTES);
    }

    @Test
    @Transactional
    void putNonExistingDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyTaskLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTaskLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTaskLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTaskLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDailyTaskLogWithPatch() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();

        // Update the dailyTaskLog using partial update
        DailyTaskLog partialUpdatedDailyTaskLog = new DailyTaskLog();
        partialUpdatedDailyTaskLog.setId(dailyTaskLog.getId());

        partialUpdatedDailyTaskLog
            .employee(UPDATED_EMPLOYEE)
            .customer(UPDATED_CUSTOMER)
            .startDate(UPDATED_START_DATE)
            .breakTotalMinutes(UPDATED_BREAK_TOTAL_MINUTES);

        restDailyTaskLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTaskLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTaskLog))
            )
            .andExpect(status().isOk());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
        DailyTaskLog testDailyTaskLog = dailyTaskLogList.get(dailyTaskLogList.size() - 1);
        assertThat(testDailyTaskLog.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testDailyTaskLog.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
        assertThat(testDailyTaskLog.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDailyTaskLog.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDailyTaskLog.getBreakTotalMinutes()).isEqualTo(UPDATED_BREAK_TOTAL_MINUTES);
    }

    @Test
    @Transactional
    void fullUpdateDailyTaskLogWithPatch() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();

        // Update the dailyTaskLog using partial update
        DailyTaskLog partialUpdatedDailyTaskLog = new DailyTaskLog();
        partialUpdatedDailyTaskLog.setId(dailyTaskLog.getId());

        partialUpdatedDailyTaskLog
            .employee(UPDATED_EMPLOYEE)
            .customer(UPDATED_CUSTOMER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .breakTotalMinutes(UPDATED_BREAK_TOTAL_MINUTES);

        restDailyTaskLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTaskLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTaskLog))
            )
            .andExpect(status().isOk());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
        DailyTaskLog testDailyTaskLog = dailyTaskLogList.get(dailyTaskLogList.size() - 1);
        assertThat(testDailyTaskLog.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testDailyTaskLog.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
        assertThat(testDailyTaskLog.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDailyTaskLog.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDailyTaskLog.getBreakTotalMinutes()).isEqualTo(UPDATED_BREAK_TOTAL_MINUTES);
    }

    @Test
    @Transactional
    void patchNonExistingDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dailyTaskLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTaskLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTaskLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDailyTaskLog() throws Exception {
        int databaseSizeBeforeUpdate = dailyTaskLogRepository.findAll().size();
        dailyTaskLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTaskLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dailyTaskLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTaskLog in the database
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDailyTaskLog() throws Exception {
        // Initialize the database
        dailyTaskLogRepository.saveAndFlush(dailyTaskLog);

        int databaseSizeBeforeDelete = dailyTaskLogRepository.findAll().size();

        // Delete the dailyTaskLog
        restDailyTaskLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, dailyTaskLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyTaskLog> dailyTaskLogList = dailyTaskLogRepository.findAll();
        assertThat(dailyTaskLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
