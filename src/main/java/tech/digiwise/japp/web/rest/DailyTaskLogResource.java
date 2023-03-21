package tech.digiwise.japp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.digiwise.japp.domain.DailyTaskLog;
import tech.digiwise.japp.repository.DailyTaskLogRepository;
import tech.digiwise.japp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link tech.digiwise.japp.domain.DailyTaskLog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DailyTaskLogResource {

    private final Logger log = LoggerFactory.getLogger(DailyTaskLogResource.class);

    private static final String ENTITY_NAME = "dailyTaskLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DailyTaskLogRepository dailyTaskLogRepository;

    public DailyTaskLogResource(DailyTaskLogRepository dailyTaskLogRepository) {
        this.dailyTaskLogRepository = dailyTaskLogRepository;
    }

    /**
     * {@code POST  /daily-task-logs} : Create a new dailyTaskLog.
     *
     * @param dailyTaskLog the dailyTaskLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dailyTaskLog, or with status {@code 400 (Bad Request)} if the dailyTaskLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daily-task-logs")
    public ResponseEntity<DailyTaskLog> createDailyTaskLog(@Valid @RequestBody DailyTaskLog dailyTaskLog) throws URISyntaxException {
        log.debug("REST request to save DailyTaskLog : {}", dailyTaskLog);
        if (dailyTaskLog.getId() != null) {
            throw new BadRequestAlertException("A new dailyTaskLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyTaskLog result = dailyTaskLogRepository.save(dailyTaskLog);
        return ResponseEntity
            .created(new URI("/api/daily-task-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /daily-task-logs/:id} : Updates an existing dailyTaskLog.
     *
     * @param id the id of the dailyTaskLog to save.
     * @param dailyTaskLog the dailyTaskLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyTaskLog,
     * or with status {@code 400 (Bad Request)} if the dailyTaskLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dailyTaskLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daily-task-logs/{id}")
    public ResponseEntity<DailyTaskLog> updateDailyTaskLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DailyTaskLog dailyTaskLog
    ) throws URISyntaxException {
        log.debug("REST request to update DailyTaskLog : {}, {}", id, dailyTaskLog);
        if (dailyTaskLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyTaskLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyTaskLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DailyTaskLog result = dailyTaskLogRepository.save(dailyTaskLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dailyTaskLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /daily-task-logs/:id} : Partial updates given fields of an existing dailyTaskLog, field will ignore if it is null
     *
     * @param id the id of the dailyTaskLog to save.
     * @param dailyTaskLog the dailyTaskLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyTaskLog,
     * or with status {@code 400 (Bad Request)} if the dailyTaskLog is not valid,
     * or with status {@code 404 (Not Found)} if the dailyTaskLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the dailyTaskLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/daily-task-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DailyTaskLog> partialUpdateDailyTaskLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DailyTaskLog dailyTaskLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update DailyTaskLog partially : {}, {}", id, dailyTaskLog);
        if (dailyTaskLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyTaskLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyTaskLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DailyTaskLog> result = dailyTaskLogRepository
            .findById(dailyTaskLog.getId())
            .map(existingDailyTaskLog -> {
                if (dailyTaskLog.getEmployee() != null) {
                    existingDailyTaskLog.setEmployee(dailyTaskLog.getEmployee());
                }
                if (dailyTaskLog.getCustomer() != null) {
                    existingDailyTaskLog.setCustomer(dailyTaskLog.getCustomer());
                }
                if (dailyTaskLog.getStartDate() != null) {
                    existingDailyTaskLog.setStartDate(dailyTaskLog.getStartDate());
                }
                if (dailyTaskLog.getEndDate() != null) {
                    existingDailyTaskLog.setEndDate(dailyTaskLog.getEndDate());
                }
                if (dailyTaskLog.getBreakTotalMinutes() != null) {
                    existingDailyTaskLog.setBreakTotalMinutes(dailyTaskLog.getBreakTotalMinutes());
                }

                return existingDailyTaskLog;
            })
            .map(dailyTaskLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dailyTaskLog.getId().toString())
        );
    }

    /**
     * {@code GET  /daily-task-logs} : get all the dailyTaskLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dailyTaskLogs in body.
     */
    @GetMapping("/daily-task-logs")
    public List<DailyTaskLog> getAllDailyTaskLogs() {
        log.debug("REST request to get all DailyTaskLogs");
        return dailyTaskLogRepository.findAll();
    }

    /**
     * {@code GET  /daily-task-logs/:id} : get the "id" dailyTaskLog.
     *
     * @param id the id of the dailyTaskLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dailyTaskLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daily-task-logs/{id}")
    public ResponseEntity<DailyTaskLog> getDailyTaskLog(@PathVariable Long id) {
        log.debug("REST request to get DailyTaskLog : {}", id);
        Optional<DailyTaskLog> dailyTaskLog = dailyTaskLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dailyTaskLog);
    }

    /**
     * {@code DELETE  /daily-task-logs/:id} : delete the "id" dailyTaskLog.
     *
     * @param id the id of the dailyTaskLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daily-task-logs/{id}")
    public ResponseEntity<Void> deleteDailyTaskLog(@PathVariable Long id) {
        log.debug("REST request to delete DailyTaskLog : {}", id);
        dailyTaskLogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
